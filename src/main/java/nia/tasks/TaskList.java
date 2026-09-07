package nia.tasks;

/**
 * Owns the list of tasks and the operations on it, so command methods in Nia.java
 * don't have to juggle a Task[] array and a separate int count across method calls.
 */
public class TaskList {
    private final Task[] tasks;
    private int count;

    public TaskList(int capacity) {
        this.tasks = new Task[capacity];
        this.count = 0;
    }

    public int getSize() {
        return this.count;
    }

    public boolean isFull() {
        return (this.count == this.tasks.length);
    }

    /** Returns false (and adds nothing) if the list is already full. */
    public boolean add(Task task) {
        if (this.count < this.tasks.length) {
            this.tasks[count++] = task;
            return true;
        }
        return false;
    }

    public Task getTask(int oneIndexed) {
        if (isNotValidIndex(oneIndexed)) {
            return null;
        }
        return this.tasks[oneIndexed - 1];
    }

    /** True if oneIndexed refers to an existing task. */
    public boolean isNotValidIndex(int oneIndexed) {
        if (oneIndexed < 1 || oneIndexed > this.tasks.length) {
            return true;
        }
        return this.tasks[oneIndexed - 1] == null;
    }

    public boolean isEmpty() {
        return (this.tasks.length == 0 || this.getSize() == 0);
    }
}