public class Nia {
    static final java.util.Random RNG = new java.util.Random();

    static void printBar() {
        System.out.println("--------------------------------------------------");
    }

    // a function that given an array of strings, return one of them (each have equal probability)
    static String pickRandomLine(String[] possibleLines) {
        return possibleLines[RNG.nextInt(possibleLines.length)];
    }

    // triggers when program ends
    static void printEnding() {
        System.out.println("Bye, see you soon.");
    }

    // after user does an action, has a chance to trigger this line
    static void printAfterRequest() {
        String[] possibleLines = {
                "Anything else?",
                "I'm not much of a helper... Can I just slack off?",
                "Fine, fine... I'll help."
        };
        System.out.println(pickRandomLine(possibleLines));
    }

    public static void main(String[] args) {
        String banner = " _   _   ___      _    \n"
                + "| \\ | | |_ _|    / \\   \n"
                + "|  \\| |  | |    / _ \\  \n"
                + "| |\\  |  | |   / ___ \\ \n"
                + "|_| \\_| |___| /_/   \\_\\\n";
        String[] startingLines = {
                "How can I help you?",
                "What do you need me to do?"
        };
        printBar();
        System.out.println(banner);
        System.out.println("Hi, my name's Nia.");
        System.out.println(pickRandomLine(startingLines));
        printBar();
        printEnding();
    }
}
