/** Thrown when "mark"/"unmark"'s argument isn't a number. */
public class NonNumericTaskIndexException extends NiaProcessorException {
    NonNumericTaskIndexException() {
        super(
                "[Debug] Expected a number in argument 1",
                "Just so you know, I only identify tasks with numbers.");
    }
}
