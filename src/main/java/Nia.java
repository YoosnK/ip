public class Nia {
    static void printBar() {
        System.out.println("--------------------------------------------------");
    }

    // a function that given an array of strings, return one of them (each have equal probability)
    static String randomLine(String[] lines) {
        return lines[0];
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
        // randomly prints one of the line, not yet implemented
        System.out.println();
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
        // prints one of the starting lines, using the random line function
        System.out.println(startingLines[0]);
        printBar();
        printEnding();
    }
}
