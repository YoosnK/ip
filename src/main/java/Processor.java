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
            case "todo":
                handleAddTodo(words, taskList);
                return true;
            case "deadline":
                handleAddDeadline(words, taskList);
                return true;
            case "event":
                handleAddEvent(words, taskList);
                return true;
            default:
                handleUnknownCommand();
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
            Printer.printIndent(String.format("%d. %s", i, task));
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
            Printer.printIndentedError("[Debug] Incorrect number of arguments, expected 1 argument"); // Debug message
            Printer.printIndent("Can you at least give me a real order?"); // Nia's voiceline placeholder
            return;
        }

        int taskToChange;
        try {
            taskToChange = Integer.parseInt(words[1]);
        } catch (NumberFormatException e) {
            Printer.printIndentedError("[Debug] Expected a number in argument 1"); // Debug message
            Printer.printIndent("Just so you know, I only identify tasks with numbers."); // Nia's voiceline placeholder
            return;
        }

        if (taskList.isNotValidIndex(taskToChange)) {
            Printer.printIndentedError(String.format(
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
     * Adds a Todo from `words` = ["todo", description]. Rejects a blank description
     * rather than silently creating an empty task.
     */
    private static void handleAddTodo(String[] words, TaskList taskList) {
        String description = words[1];
        if (description.isEmpty()) {
            Printer.printIndentedError("[Debug] Todo description is empty"); // Debug message
            Printer.printIndent("A todo with nothing in it? There's nothing for me to do."); // Nia's voiceline placeholder
            return;
        }
        addTask(new Todo(description), taskList);
    }

    /**
     * Adds a Deadline from `words` = ["deadline", description, by]. Rejects a
     * missing description or missing "/by" rather than silently dropping the field.
     */
    private static void handleAddDeadline(String[] words, TaskList taskList) {
        String description = words[1];
        String by = words[2];
        if (description.isEmpty() || by.isEmpty()) {
            Printer.printIndentedError("[Debug] Deadline needs a description and a /by"); // Debug message
            Printer.printIndent("I need a description and a /by, or I've got nothing to remember."); // Nia's voiceline placeholder
            return;
        }
        addTask(new Deadline(description, by), taskList);
    }

    /**
     * Adds an Event from `words` = ["event", description, from, to]. Rejects a
     * missing description, "/from", or "/to" rather than silently dropping a field.
     */
    private static void handleAddEvent(String[] words, TaskList taskList) {
        String description = words[1];
        String from = words[2];
        String to = words[3];
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            Printer.printIndentedError("[Debug] Event needs a description, a /from, and a /to"); // Debug message
            Printer.printIndent("Description, /from, /to. All three, or don't bother."); // Nia's voiceline placeholder
            return;
        }
        addTask(new Event(description, from, to), taskList);
    }

    /** Adds `task` to taskList, or prints an error if taskList is full. */
    private static void addTask(Task task, TaskList taskList) {
        if (taskList.isFull()) {
            Printer.printIndent("Task list is full");
            return;
        }
        taskList.add(task);
        Printer.printIndent("I've added the task [" + task + "] to your task list");
    }

    /** Prints an error for a command word that isn't recognized - no task is created. */
    private static void handleUnknownCommand() {
        Printer.printIndentedError("[Debug] Unrecognized command"); // Debug message
        Printer.printIndent("Hm? I don't know what that means."); // Nia's voiceline placeholder
    }
}