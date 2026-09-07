package nia.exceptions;

/** Thrown when "mark"/"unmark"'s argument isn't a number. */
public class NonNumericTaskIndexException extends NiaProcessorException {
    public NonNumericTaskIndexException(String badArgument) {
        super(
                String.format(
                        "[Debug] Expected a numeric task index but got \"%s\". Use a task number, e.g. \"mark 2\"",
                        badArgument),
                "Just so you know, I only identify tasks with numbers.");
    }
}