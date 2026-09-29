package nia.tasks;

public class Todo extends Task{
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTag() {
        return "T";
    }

    @Override
    public String toString() {
        return "[%s]".formatted(this.getTag()) + super.toString();
    }
}