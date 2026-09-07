package nia.exceptions;

/** Thrown when adding a task would exceed the TaskList's fixed capacity. */
public class TaskListFullException extends NiaProcessorException {
    public TaskListFullException() {
        super(
                "[Debug] Task list is full; no more tasks can be added (there is currently no way to remove one)",
                "Task list is full");
    }
}