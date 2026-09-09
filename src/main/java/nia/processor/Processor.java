package nia.processor;

import nia.exceptions.IncorrectTaskIndexArgumentCountException;
import nia.exceptions.InvalidTaskIndexException;
import nia.exceptions.NiaProcessorException;
import nia.exceptions.NonNumericTaskIndexException;
import nia.exceptions.UnknownCommandException;
import nia.tasks.Deadline;
import nia.tasks.Event;
import nia.tasks.Task;
import nia.tasks.TaskList;
import nia.tasks.Todo;
import nia.ui.Printer;

/**
 * Executes commands against a TaskList using the already-canonical, already-sanitized
 * words Parser produces. No input sanitization or alias resolution happens here -
 * by the time `process` sees `words`, words[0] is guaranteed to be a canonical command.
 */
public class Processor {

    /** Dispatches on words[0]. Returns false when the program should stop running. */
    public static boolean process(String[] words, TaskList taskList) throws NiaProcessorException {
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
            case "delete":
                handleDelete(words, taskList);
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
        for (int i = 1; i <= taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
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
        int taskToChange = parseAndValidateTaskIndex(words, taskList);

        if (markAsDone) {
            taskList.getTask(taskToChange).markAsDone();
            Printer.printIndent(String.format("Marked %d as done", taskToChange));
        } else {
            taskList.getTask(taskToChange).markAsNotDone();
            Printer.printIndent(String.format("Marked %d as not done", taskToChange));
        }
    }

    /**
     * Deletes the task at `words` = ["delete", "<number>"]. Prints the removed task
     * and the resulting list size, matching the tone of the "add" confirmation.
     */
    private static void handleDelete(String[] words, TaskList taskList) throws NiaProcessorException {
        int taskToRemove = parseAndValidateTaskIndex(words, taskList);
        Task removedTask = taskList.delete(taskToRemove);

        int remaining = taskList.getSize();
        Printer.printIndent(String.format(
                "Ugh, fine, I've thrown out this task:\n    %s\nNow you have %d task%s left in your list.",
                removedTask, remaining, remaining == 1 ? "" : "s"));
    }

    /**
     * Parses and validates `words` (expects exactly [command, "<number>"]) into a
     * one-indexed task number. Shared by mark/unmark/delete, whose argument shape
     * is identical - only what happens to the referenced task differs.
     */
    private static int parseAndValidateTaskIndex(String[] words, TaskList taskList) throws NiaProcessorException {
        if (words.length != 2) {
            throw new IncorrectTaskIndexArgumentCountException(words[0]);
        }

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(words[1]);
        } catch (NumberFormatException e) {
            throw new NonNumericTaskIndexException(words[1]);
        }

        if (taskList.isNotValidIndex(taskIndex)) {
            throw new InvalidTaskIndexException(taskIndex, taskList.getSize());
        }
        return taskIndex;
    }

    /**
     * Adds a Todo from `words` = ["todo", description]. Parser guarantees
     * description is non-blank before words ever reaches here.
     */
    private static void handleAddTodo(String[] words, TaskList taskList) {
        addTask(new Todo(words[1]), taskList);
    }

    /**
     * Adds a Deadline from `words` = ["deadline", description, by]. Parser
     * guarantees both fields are non-blank before words ever reaches here.
     */
    private static void handleAddDeadline(String[] words, TaskList taskList) {
        addTask(new Deadline(words[1], words[2]), taskList);
    }

    /**
     * Adds an Event from `words` = ["event", description, from, to]. Parser
     * guarantees all three fields are non-blank before words ever reaches here.
     */
    private static void handleAddEvent(String[] words, TaskList taskList) {
        addTask(new Event(words[1], words[2], words[3]), taskList);
    }

    /** Adds `task` to taskList. */
    private static void addTask(Task task, TaskList taskList) {
        taskList.add(task);
        Printer.printIndent("I've added the task \"" + task + "\" to your task list");
    }
}