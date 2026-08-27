/**
 * Handles all of Nia's user-facing output: banners, bars, and her personality lines.
 * Keeping this separate from the command logic means Nia.java only has to decide
 * *what* happened, not *how* to display it.
 */
public class Ui {
    static final java.util.Random RNG = new java.util.Random();

    static String pickRandomLine(String[] possibleLines) {
        if (possibleLines.length > 0) {
            return possibleLines[RNG.nextInt(possibleLines.length)];
        }
        return null;
    }

    static void printStarter() {
        final String BANNER = " _   _   ___      _    \n"
                + "| \\ | | |_ _|    / \\   \n"
                + "|  \\| |  | |    / _ \\  \n"
                + "| |\\  |  | |   / ___ \\ \n"
                + "|_| \\_| |___| /_/   \\_\\\n";
        final String[] STARTING_LINES = {
                "How can I help you?",
                "What do you need me to do?",
                "Mm... give me something to do, I guess.",
                "Alright, alright, I'm here. What's up?",
                "Don't mind me, just say what you need.",
                "Surprised to see me? Haha, relax. I told you we'd meet again.",
        };
        printIndent(BANNER);
        printIndent("Hi, my name's Nia.");
        printIndent(pickRandomLine(STARTING_LINES));
    }

    static void printAfterRequest() {
        String[] postActionLines = {
                "Anything else?",
                "I'm not much of a helper... Can I just slack off?",
                "Fine, fine... I'll help.",
                "Done. Was that so hard? ...for me, I mean.",
                "Hah, easy. What's next?",
                "Mm, noted. Don't expect this energy every time.",
                "There, sorted. Now let me go back to doing nothing.",
                "You know, most people would've just given up there.",
                "Working is so tiring, why don't you take a break?",
        };
        printIndent(pickRandomLine(postActionLines));
    }

    static void printEnding() {
        String[] endingLines = {
                "Mm, I'm off. Try not to miss me too much.",
                "Later~ Don't work yourself too hard, okay?",
                "Guess that's enough for today. Go rest a bit."
        };
        printIndent(pickRandomLine(endingLines));
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