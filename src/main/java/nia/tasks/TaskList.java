package nia.tasks;

import java.util.ArrayList;

/**
 * Owns the list of tasks and the operations on it, so command methods in Nia.java
 * don't have to juggle a Task[] array and a separate int count across method calls.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public int getSize() {
        return this.tasks.size();
    }

    public void add(Task task) {
        this.tasks.add(task);
    }

    public Task getTask(int oneIndexed) {
        if (isNotValidIndex(oneIndexed)) {
            return null;
        }
        return this.tasks.get(oneIndexed - 1);
    }

    /** Removes and returns the task at oneIndexed, or null if the index doesn't refer to an existing task. */
    public Task delete(int oneIndexed) {
        if (isNotValidIndex(oneIndexed)) {
            return null;
        }
        return this.tasks.remove(oneIndexed - 1);
    }

    /** True if oneIndexed refers to an existing task. */
    public boolean isNotValidIndex(int oneIndexed) {
        return oneIndexed < 1 || oneIndexed > this.tasks.size();
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }
}
