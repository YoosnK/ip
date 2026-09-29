package nia.tasks;

import java.time.LocalDateTime;
import nia.parser.Parser;

public class Event extends Task{

    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String getTag() {
        return "E";
    }

    @Override
    public String toString() {
        return "[%s]".formatted(this.getTag()) + super.toString()
                + " (from: %s; to: %s)".formatted(Parser.formatDisplay(this.from), Parser.formatDisplay(this.to));
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + SAVE_DELIMITER + Parser.formatCanonical(this.from)
                + SAVE_DELIMITER + Parser.formatCanonical(this.to);
    }
}
