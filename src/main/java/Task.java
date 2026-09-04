/**
 * Represents a single task: its description and whether it has been completed.
 * New tasks start as not done.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    /** Returns "X" if the task is done, or a blank space otherwise (for display, e.g. "[X]"/"[ ]"). */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public String getTag() {
        return " ";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }
}