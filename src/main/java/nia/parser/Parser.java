package nia.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import nia.exceptions.EmptyTodoDescriptionException;
import nia.exceptions.IncorrectTaskIndexArgumentCountException;
import nia.exceptions.InvalidDateTimeException;
import nia.exceptions.InvalidTaskIndexException;
import nia.exceptions.MissingDeadlineFieldsException;
import nia.exceptions.MissingEventFieldsException;
import nia.exceptions.NiaParserException;
import nia.exceptions.NonNumericTaskIndexException;
import nia.tasks.TaskList;

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
            "dl", "deadline",
            "ls", "list",
            "d", "delete"
    );

    /** The one format Nia's save file uses - unambiguous, always includes a time. */
    private static final DateTimeFormatter CANONICAL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /** Friendly display format for task listings, e.g. "Oct 15 2019, 6:00pm". */
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    /** Time assumed when the user gives a date with no time component. */
    private static final LocalTime DEFAULT_TIME = LocalTime.of(23, 59);

    /** Date-time formats accepted from user input: "-" or "/" date separator, "HHmm" or "HH:mm" time. */
    private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HHmm"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    );

    /** Date-only formats accepted from user input; time defaults to DEFAULT_TIME. */
    private static final List<DateTimeFormatter> DATE_ONLY_FORMATS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
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
    public static String[] parse(String rawInput) throws NiaParserException {
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

    /**
     * Parses and validates `words` (expects exactly [command, "<number>"]) into a
     * one-indexed task number. Shared by mark/unmark/delete, whose argument shape
     * is identical - only what happens to the referenced task differs.
     */
    public static int parseAndValidateTaskIndex(String[] words, TaskList taskList) throws NiaParserException {
        if (words.length != 2) {
            throw new IncorrectTaskIndexArgumentCountException(words[0]);
        }

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(words[1]);
        } catch (NumberFormatException e) {
            throw new NonNumericTaskIndexException(words[1]);
        }

        if (taskList.isNotValidIndex(taskIndex)) {
            throw new InvalidTaskIndexException(taskIndex, taskList.getSize());
        }
        return taskIndex;
    }

    /**
     * Parses `raw` (already trimmed) against every accepted date-time format in
     * turn - "-" or "/" as the date separator, "HHmm" or "HH:mm" as the time.
     * A date given with no time component defaults to 23:59 that day. Shared by
     * parseDeadline/parseEvent, whose "/by"/"/from"/"/to" fields all accept the
     * same formats.
     */
    public static LocalDateTime parseDateTime(String raw) throws InvalidDateTimeException {
        for (DateTimeFormatter format : DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(raw, format);
            } catch (DateTimeParseException ignored) {
                // Not this format - try the next one.
            }
        }
        for (DateTimeFormatter format : DATE_ONLY_FORMATS) {
            try {
                return LocalDate.parse(raw, format).atTime(DEFAULT_TIME);
            } catch (DateTimeParseException ignored) {
                // Not this format - try the next one.
            }
        }
        throw new InvalidDateTimeException(raw);
    }

    /** Formats `dateTime` the way Nia's save file persists it - lossless, machine-parseable. */
    public static String formatCanonical(LocalDateTime dateTime) {
        return dateTime.format(CANONICAL_FORMATTER);
    }

    /** Parses a string that's already in the save file's canonical format back into a LocalDateTime. */
    public static LocalDateTime parseCanonical(String canonical) {
        return LocalDateTime.parse(canonical, CANONICAL_FORMATTER);
    }

    /** Formats `dateTime` for the user to read, e.g. "Oct 15 2019, 6:00pm". */
    public static String formatDisplay(LocalDateTime dateTime) {
        // DateTimeFormatter's "a" pattern renders AM/PM in uppercase regardless of
        // case in the pattern string itself, so lowercase it afterward to match style.
        String formatted = dateTime.format(DISPLAY_FORMATTER);
        return formatted.replace("AM", "am").replace("PM", "pm");
    }
}
