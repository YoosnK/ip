/**
 * Executes commands against a TaskList using the already-canonical, already-sanitized
 * words Parser produces. No input sanitization or alias resolution happens here -
 * by the time `process` sees `words`, words[0] is guaranteed to be a canonical command.
 */
public class Processor {

    /** Dispatches on words[0]. Returns false when the program should stop running. */
    static boolean process(String[] words, TaskList taskList) {
        String command = words[0];

        switch (command) {
            case "bye":
                return false;
            case "list":
                handleList(taskList);
                return true;
            case "mark":
            case "unmark":
                handleMarkOrUnmark(words, taskList, command.equals("mark"));
                return true;
            default:
                handleAdd(words, taskList);
                return true;
        }
    }

    /**
     * Prints every task in taskList, or a placeholder message if it's empty.
     */
    private static void handleList(TaskList taskList) {
        if (taskList.isEmpty()) {
            Printer.printIndent("Nothing here...");
            return;
        }
        for (int i = 1; i <= taskList.size(); i++) {
            Task task = taskList.get(i);
            Printer.printIndent(String.format("%d. [%s] %s", i, task.getStatusIcon(), task.getDescription()));
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
            Printer.printIndent("[Debug] Incorrect number of arguments, expected 1 argument"); // Debug message
            Printer.printIndent("Can you at least give me a real order?"); // Nia's voiceline placeholder
            return;
        }

        int taskToChange;
        try {
            taskToChange = Integer.parseInt(words[1]);
        } catch (NumberFormatException e) {
            Printer.printIndent("[Debug] Expected a number in argument 1"); // Debug message
            Printer.printIndent("Just so you know, I only identify tasks with numbers."); // Nia's voiceline placeholder
            return;
        }

        if (taskList.isNotValidIndex(taskToChange)) {
            Printer.printIndent(String.format(
                    "[Debug] Task number %d is out of range (1-%d) or does not exist"
                    , taskToChange, taskList.size())); // Debug message
            Printer.printIndent("That task doesn't exist... did you make it up?"); // Nia's voiceline placeholder
            return;
        }

        if (markAsDone) {
            taskList.get(taskToChange).markAsDone();
            Printer.printIndent(String.format("Marked %d as done", taskToChange));
        } else {
            taskList.get(taskToChange).markAsNotDone();
            Printer.printIndent(String.format("Marked %d as not done", taskToChange));
        }
    }

    /**
     * Adds a new task built from the raw command words, or prints an error if taskList is full.
     */
    private static void handleAdd(String[] words, TaskList taskList) {
        if (taskList.isFull()) {
            Printer.printIndent("Task list is full");
            return;
        }
        String description = String.join(" ", words);
        Task t = new Task(description);
        taskList.add(t);
        Printer.printIndent("I've added the task [" + description + "] to your task list");
    }
}
