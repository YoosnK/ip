package nia.tasks;

/**
 * Represents a single task: its description and whether it has been completed.
 * New tasks start as not done.
 */
public abstract class Task {
    /** Delimiter between fields in the on-disk save format, e.g. "T|~|0|~|description". */
    public static final String SAVE_DELIMITER = "|~|";

    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDone() {
        return this.isDone;
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

    /** Serializes this task to one line of the save file, e.g. "T|~|0|~|description". */
    public String toSaveFormat() {
        return getTag() + SAVE_DELIMITER + (isDone ? "1" : "0") + SAVE_DELIMITER + description;
    }
}