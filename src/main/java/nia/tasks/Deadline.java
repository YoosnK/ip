package nia.tasks;

public class Deadline extends Task{

    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTag() {
        return "D";
    }

    @Override
    public String toString() {
        return "[%s]".formatted(this.getTag()) + super.toString() + " (by: %s)".formatted(this.by);
    }
}