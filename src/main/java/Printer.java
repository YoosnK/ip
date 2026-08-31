/**
 * Handles all of Nia's generic terminal output: prompts, bars, and indented text.
 * Has no knowledge of Nia's personality - that lives in VoicelineManager, which
 * calls back into this class to actually print.
 */
public class Printer {
    /** Prints the input prompt without a trailing newline. */
    static void printPrompt() {
        System.out.print("❯ ");
    }

    /**
     * Prints `message` indented, one indent per line so multiline strings
     * (e.g. the task list) stay aligned under the bar.
     */
    static void printIndent(String message) {
        final int INDENT_LEVEL = 4;
        String indentedMessage = " ".repeat(INDENT_LEVEL) + message;
        indentedMessage = indentedMessage.replace("\n", "\n" + " ".repeat(INDENT_LEVEL));

        System.out.println(indentedMessage);
    }

    static void printBar() {
        final int BAR_LENGTH = 50;
        System.out.println("-".repeat(BAR_LENGTH));
    }
}
