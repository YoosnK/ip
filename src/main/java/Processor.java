/**
 * Executes commands against a TaskList using the already-canonical, already-sanitized
 * words Parser produces. No input sanitization or alias resolution happens here -
 * by the time `process` sees `words`, words[0] is guaranteed to be a canonical command.
 */
public class Processor {

    /** Dispatches on words[0]. Returns false when the program should stop running. */
    static boolean process(String[] words, TaskList taskList) throws NiaProcessorException {
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
                throw new UnknownCommandException(command);
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
     * Guard clauses: throw early on wrong arg count, non-numeric arg, or out-of-range index —
     * this is what keeps this method flat instead of nesting ifs inside ifs.
     */
    private static void handleMarkOrUnmark(String[] words, TaskList taskList, boolean markAsDone)
            throws NiaProcessorException {
        if (words.length != 2) {
            throw new IncorrectMarkArgumentCountException();
        }

        int taskToChange;
        try {
            taskToChange = Integer.parseInt(words[1]);
        } catch (NumberFormatException e) {
            throw new NonNumericTaskIndexException(words[1]);
        }

        if (taskList.isNotValidIndex(taskToChange)) {
            throw new InvalidTaskIndexException(taskToChange, taskList.size());
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
     * Adds a Todo from `words` = ["todo", description]. Parser guarantees
     * description is non-blank before words ever reaches here.
     */
    private static void handleAddTodo(String[] words, TaskList taskList) throws TaskListFullException {
        addTask(new Todo(words[1]), taskList);
    }

    /**
     * Adds a Deadline from `words` = ["deadline", description, by]. Parser
     * guarantees both fields are non-blank before words ever reaches here.
     */
    private static void handleAddDeadline(String[] words, TaskList taskList) throws TaskListFullException {
        addTask(new Deadline(words[1], words[2]), taskList);
    }

    /**
     * Adds an Event from `words` = ["event", description, from, to]. Parser
     * guarantees all three fields are non-blank before words ever reaches here.
     */
    private static void handleAddEvent(String[] words, TaskList taskList) throws TaskListFullException {
        addTask(new Event(words[1], words[2], words[3]), taskList);
    }

    /** Adds `task` to taskList, or throws if taskList is full. */
    private static void addTask(Task task, TaskList taskList) throws TaskListFullException {
        if (taskList.isFull()) {
            throw new TaskListFullException();
        }
        taskList.add(task);
        Printer.printIndent("I've added the task [" + task + "] to your task list");
    }
}