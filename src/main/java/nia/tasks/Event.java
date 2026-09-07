package nia.tasks;

public class Event extends Task{

    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTag() {
        return "E";
    }

    @Override
    public String toString() {
        return "[%s]".formatted(this.getTag()) + super.toString() + " (FROM: %s; TO: %s)".formatted(this.from, this.to);
    }
}