/** Thrown when adding a task would exceed the TaskList's fixed capacity. */
public class TaskListFullException extends NiaProcessorException {
    TaskListFullException() {
        super(
                "[Debug] Task list is full",
                "Task list is full");
    }
}
