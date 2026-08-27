/**
 * Entry point for Nia, a CLI task-tracking assistant.
 * Runs a read-parse-dispatch loop: each line of input is split into a command word
 * and arguments, then routed to a handleXxx method that validates and executes it.
 */
public class Nia {

    /** Reads commands from stdin in a loop until an exit command is entered. */
    public static void main(String[] args) {
        Ui.printStarter();

        TaskList taskList = new TaskList(100);

        java.util.Set<String> exitCommands = java.util.Set.of("bye", "close", "exit", "quit");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String command;
        while (true) {
            Ui.printBar();
            System.out.print("❯ ");
            command = scanner.nextLine().trim();
            Ui.printBar();
            // Ignore empty commands
            if (command.isEmpty()) {
                continue;
            }

            // Exits program
            if (exitCommands.contains(command)) {
                break;
            }

            String[] words = command.split("\\s+");
            String firstWord = words.length > 0 ? words[0] : "";

            switch (firstWord) {
                case "list":
                    handleList(taskList);
                    break;
                case "mark":
                case "unmark":
                    handleMarkOrUnmark(words, taskList, firstWord.equals("mark"));
                    break;
                default:
                    handleAdd(command, taskList);
                    break;
            }
        }
        Ui.printEnding();
    }

    /**
     * Prints every task in taskList, or a placeholder message if it's empty.
     */
    private static void handleList(TaskList taskList) {
        if (taskList.isEmpty()) {
            Ui.printIndent("Nothing here...");
            return;
        }
        for (int i = 1; i <= taskList.size(); i++) {
            Task task = taskList.get(i);
            Ui.printIndent(String.format("%d. [%s] %s", i, task.getStatusIcon(), task.getDescription()));
        }
    }

    /**
     * Parses and validates `words` (expects exactly ["mark"/"unmark", "<number>"]),
     * then marks/unmarks the referenced task.
     * Guard clauses: return early on wrong arg count, non-numeric arg, or out-of-range index —
     * this is what keeps this method flat instead of nesting ifs inside ifs.
     */
    private static void handleMarkOrUnmark(String[] words, TaskList taskList, boolean markAsDone) {
        if (words.length != 2) {
            Ui.printIndent("[Debug] Incorrect number of arguments, expected 1 argument"); // Debug message
            Ui.printIndent("Can you at least give me a real order?"); // Nia's voiceline placeholder
            return;
        }

        int taskToChange;
        try {
            taskToChange = Integer.parseInt(words[1]);
        } catch (NumberFormatException e) {
            Ui.printIndent("[Debug] Expected a number in argument 1"); // Debug message
            Ui.printIndent("Just so you know, I only identify tasks with numbers."); // Nia's voiceline placeholder
            return;
        }

        if (taskList.isNotValidIndex(taskToChange)) {
            Ui.printIndent(String.format(
                    "[Debug] Task number %d is out of range (1-%d) or does not exist"
                    , taskToChange, taskList.size())); // Debug message
            Ui.printIndent("That task doesn't exist... did you make it up?"); // Nia's voiceline placeholder
            return;
        }

        if (markAsDone) {
            taskList.get(taskToChange).markAsDone();
            Ui.printIndent(String.format("Marked %d as done", taskToChange));
        } else {
            taskList.get(taskToChange).markAsNotDone();
            Ui.printIndent(String.format("Marked %d as not done", taskToChange));
        }
    }

    /**
     * Adds a new task built from the raw command text, or prints an error if taskList is full.
     */
    private static void handleAdd(String command, TaskList taskList) {
        if (taskList.isFull()) {
            Ui.printIndent("Task list is full");
            return;
        }
        Task t = new Task(command);
        taskList.add(t);
        Ui.printIndent("I've added the task [" + command + "] to your task list");
    }
}