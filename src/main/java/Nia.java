public class Nia {
    static final java.util.Random RNG = new java.util.Random();

    static void printBar() {
        final int barLength = 50;
        System.out.println("-".repeat(barLength));
    }

    // prints a bar indented, used to visually set a reply apart from the user's input line
    static void printIndentedBar() {
        System.out.print("    ");
        printBar();
    }

    // a function that given an array of strings, return one of them (each have equal probability)
    static String pickRandomLine(String[] possibleLines) {
        return possibleLines[RNG.nextInt(possibleLines.length)];
    }

    // triggers when program ends
    static void printEnding() {
        String[] endingLines = {
                "Mm, I'm off. Try not to miss me too much.",
                "Later~ Don't work yourself too hard, okay?",
                "Guess that's enough for today. Go rest a bit."
        };
        printIndentedBar();
        System.out.print("    ");
        System.out.println(pickRandomLine(endingLines));
        printIndentedBar();
    }

    // after user does an action, has a chance to trigger this line
    static void printAfterRequest() {
        String[] postActionLines = {
                "Anything else?",
                "I'm not much of a helper... Can I just slack off?",
                "Fine, fine... I'll help.",
                "Done. Was that so hard? ...for me, I mean.",
                "Hah, easy. What's next?",
                "Mm, noted. Don't expect this energy every time.",
                "There, sorted. Now let me go back to doing nothing.",
                "You know, most people would've just given up there."
        };
        System.out.println(pickRandomLine(postActionLines));
    }

    public static void main(String[] args) {
        String banner = "     _   _   ___      _    \n"
                + "    | \\ | | |_ _|    / \\   \n"
                + "    |  \\| |  | |    / _ \\  \n"
                + "    | |\\  |  | |   / ___ \\ \n"
                + "    |_| \\_| |___| /_/   \\_\\\n";
        String[] startingLines = {
                "How can I help you?",
                "What do you need me to do?",
                "Mm... give me something to do, I guess.",
                "Alright, alright, I'm here. What's up?",
                "Don't mind me, just say what you need.",
                "Surprised to see me? Haha, relax. I told you we'd meet again.",
        };
        printIndentedBar();
        System.out.println(banner);
        System.out.println("    Hi, my name's Nia.");
        System.out.println("    " + pickRandomLine(startingLines));
        printIndentedBar();

        String[] taskList = new String[100];
        int taskCount = 0;

        // Actions Loop
        java.util.Set<String> exitCommands = java.util.Set.of("bye", "close", "exit", "quit");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String command;
        while (true) {
            command = scanner.nextLine();

            if (command.isEmpty()) {
                continue;
            }

            if (exitCommands.contains(command)) {
                break;
            }

            switch (command) {
                case "list":
                    printIndentedBar();
                    for (int i = 0; i < taskList.length; i++) {
                        if (taskList[i] == null || taskList[i].isEmpty()) {
                            // Check if there is no commands logged yet
                            if (i == 0) {
                                System.out.println("    Nothing here..."); // Change to fit Nia's Personality
                            }
                            break;
                        }
                        System.out.printf("     %d. %s%n", i + 1, taskList[i]);
                    }
                    printIndentedBar();
                    break;
                default:
                    printIndentedBar();
                    // Prevents adding over 100 tasks
                    if (taskCount < taskList.length) {
                        taskList[taskCount++] = command;
                        System.out.println("    added: " + command); // Change to fit Nia's Personality if is an improvement
                    } else {
                        System.out.println("    I can't remember all of that"); // Maybe change this line, or add some variant
                    }
                    printIndentedBar();
                    break;
            }
        }
        printEnding();
    }
}
