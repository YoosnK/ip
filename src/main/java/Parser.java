/**
 * Turns raw input text into a clean, canonical array of words for Processor.
 * Owns all input sanitization: trimming, blank-input guarding, word-splitting,
 * and command-alias resolution (e.g. "close"/"exit"/"quit" all mean "bye").
 * Processor should never see a non-canonical command word.
 */
public class Parser {
    /**
     * Maps alternate spellings of a command to its canonical form.
     * This is the single place to register new aliases (e.g. "dl" -> "deadline").
     */
    private static final java.util.Map<String, String> ALIASES = java.util.Map.of(
            "close", "bye",
            "exit", "bye",
            "quit", "bye"
    );

    /**
     * Trims and splits `rawInput` into words, resolving the first word through
     * ALIASES. Returns an empty array if `rawInput` is blank - callers should
     * treat that as "nothing to process" and skip it.
     */
    static String[] parse(String rawInput) {
        String trimmed = rawInput.trim();
        if (trimmed.isEmpty()) {
            return new String[0];
        }

        String[] words = trimmed.split("\\s+");
        words[0] = ALIASES.getOrDefault(words[0], words[0]);
        return words;
    }
}
