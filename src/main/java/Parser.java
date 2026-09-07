/**
 * Turns raw input text into a clean, canonical array of words for Processor.
 * Owns all input sanitization: trimming, blank-input guarding, word-splitting,
 * command-alias resolution (e.g. "close"/"exit"/"quit" all mean "bye"), and the
 * command-specific shape of the "add task" commands (todo/deadline/event).
 * Processor should never see a non-canonical command word, and should be able
 * to trust the array shape for each command without re-splitting anything.
 */
public class Parser {
    /**
     * Maps alternate spellings of a command to its canonical form.
     * This is the single place to register new aliases (e.g. "dl" -> "deadline").
     */
    private static final java.util.Map<String, String> ALIASES = java.util.Map.of(
            "close", "bye",
            "exit", "bye",
            "quit", "bye",
            "td", "todo",
            "dl", "deadline"
    );

    /**
     * Splits `rawInput` into a canonical command word plus command-specific
     * fields, e.g.:
     *   "td buy milk"                        -> ["todo", "buy milk"]
     *   "dl return book /by Sunday"          -> ["deadline", "return book", "Sunday"]
     *   "event exam /from Mon /to Tue"       -> ["event", "exam", "Mon", "Tue"]
     * Other commands (bye/list/mark/unmark, and unrecognized words) fall back to a
     * plain whitespace split with only the first word alias-resolved.
     * Returns an empty array if `rawInput` is blank - callers should treat that
     * as "nothing to process" and skip it. A todo/deadline/event missing a
     * required field (description, "/by", "/from", or "/to") throws the
     * matching NiaParserException rather than coming back with a blank field
     * for Processor to catch later.
     */
    static String[] parse(String rawInput) throws NiaParserException {
        String trimmed = rawInput.trim();
        if (trimmed.isEmpty()) {
            return new String[0];
        }

        int firstSpace = trimmed.indexOf(' ');
        String commandWord = firstSpace == -1 ? trimmed : trimmed.substring(0, firstSpace);
        String rest = firstSpace == -1 ? "" : trimmed.substring(firstSpace + 1).trim();
        String command = ALIASES.getOrDefault(commandWord, commandWord);

        switch (command) {
            case "todo":
                return parseTodo(command, rest);
            case "deadline":
                return parseDeadline(command, rest);
            case "event":
                return parseEvent(command, rest);
            default:
                String[] words = trimmed.split("\\s+");
                words[0] = command;
                return words;
        }
    }

    /** Rejects a blank description rather than letting an empty todo through. */
    private static String[] parseTodo(String command, String rest) throws EmptyTodoDescriptionException {
        if (rest.isEmpty()) {
            throw new EmptyTodoDescriptionException();
        }
        return new String[]{command, rest};
    }

    /** Splits `rest` on "/by" into description and by-fields; throws if either comes back blank. */
    private static String[] parseDeadline(String command, String rest) throws MissingDeadlineFieldsException {
        int byIndex = rest.indexOf("/by");
        String description = byIndex == -1 ? rest.trim() : rest.substring(0, byIndex).trim();
        String by = byIndex == -1 ? "" : rest.substring(byIndex + "/by".length()).trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new MissingDeadlineFieldsException();
        }
        return new String[]{command, description, by};
    }

    /** Splits `rest` on "/from" and "/to" into description, from, and to; throws if any comes back blank. */
    private static String[] parseEvent(String command, String rest) throws MissingEventFieldsException {
        int fromIndex = rest.indexOf("/from");
        int toIndex = rest.indexOf("/to");

        String description = fromIndex == -1 ? rest.trim() : rest.substring(0, fromIndex).trim();

        String from = "";
        if (fromIndex != -1) {
            int fromEnd = (toIndex != -1 && toIndex > fromIndex) ? toIndex : rest.length();
            from = rest.substring(fromIndex + "/from".length(), fromEnd).trim();
        }

        String to = toIndex == -1 ? "" : rest.substring(toIndex + "/to".length()).trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new MissingEventFieldsException();
        }
        return new String[]{command, description, from, to};
    }
}
