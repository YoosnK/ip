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

    static void printStarter() {
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
                "You know, most people would've just given up there.",
                "Working is so tiring, why don't you take a break?",
        };
        System.out.println(pickRandomLine(postActionLines));
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

    public static void main(String[] args) {
        printStarter();

        Task[] taskList = new Task[100];
        int taskCount = 0;

        // Actions Loop
        java.util.Set<String> exitCommands = java.util.Set.of("bye", "close", "exit", "quit");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String command;
        while (true) {
            // .trim() up front so exit-checking and word-splitting agree on what counts as the command
            command = scanner.nextLine().trim();
            // Skip empty lines of input
            if (command.isEmpty()) {
                continue;
            }
            // User quits
            if (exitCommands.contains(command)) {
                break;
            }

            // \\s+ handles multiple spaces between words
            String[] words = command.split("\\s+");
            String firstWord = words.length > 0 ? words[0] : "";

            // User types a non-empty command that is not quitting
            switch (firstWord) {
                case "list":
                    printIndentedBar();
                    if (taskCount == 0) {
                        System.out.println("    Nothing here..."); // Change to fit Nia's Personality
                    }
                    for (int i = 0; i < taskCount; i++) {
                        System.out.printf("     %d. [%s] %s%n", i + 1, taskList[i].getStatusIcon(), taskList[i].getDescription());
                    }
                    printIndentedBar();
                    break;
                case "mark":
                case "unmark":
                    if (words.length != 2) {
                        printIndentedBar();
                        System.out.println("    [Debug] Incorrect number of arguments, expected 1 argument"); // Debug message
                        System.out.println("    Can you at least give me a real order?"); // Nia's voiceline placeholder
                        printIndentedBar();
                        break;
                    }
                    int taskToChange;
                    try {
                        taskToChange = Integer.parseInt(words[1]);
                    } catch (NumberFormatException e) {
                        printIndentedBar();
                        System.out.println("    [Debug] Expected a number in argument 1"); // Debug message
                        System.out.println("    Just so you know, I only identify tasks with numbers."); // Nia's voiceline placeholder
                        printIndentedBar();
                        break;
                    }
                    // Bounds check: task numbers are 1-indexed and must refer to an existing task
                    if (taskToChange < 1 || taskToChange > taskCount) {
                        printIndentedBar();
                        System.out.printf("    [Debug] Task number %d is out of range (1-%d)%n", taskToChange, taskCount); // Debug message
                        System.out.println("    That task doesn't exist... did you make it up?"); // Nia's voiceline placeholder
                        printIndentedBar();
                        break;
                    }

                    // Slightly annoying: you can mark an already done task and unmark an unmarked task
                    printIndentedBar();
                    if (firstWord.equals("mark")) {
                        taskList[taskToChange - 1].markAsDone();
                        System.out.printf("    Marked %d as done%n", taskToChange);
                    } else {
                        taskList[taskToChange - 1].markAsNotDone();
                        System.out.printf("    Marked %d as not done%n", taskToChange);
                    }
                    printIndentedBar();
                    break;
                default:
                    printIndentedBar();
                    // Prevents adding over 100 tasks
                    if (taskCount < taskList.length) {
                        Task t = new Task(command);
                        taskList[taskCount++] = t;
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
