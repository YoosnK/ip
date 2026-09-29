package nia.tasks;

import java.time.LocalDateTime;
import nia.parser.Parser;

public class Deadline extends Task{

    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String getTag() {
        return "D";
    }

    @Override
    public String toString() {
        return "[%s]".formatted(this.getTag()) + super.toString()
                + " (by: %s)".formatted(Parser.formatDisplay(this.by));
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + SAVE_DELIMITER + Parser.formatCanonical(this.by);
    }
}
