/** Thrown when "mark"/"unmark" isn't given exactly one argument. */
public class IncorrectMarkArgumentCountException extends NiaProcessorException {
    IncorrectMarkArgumentCountException() {
        super(
                "[Debug] mark/unmark takes exactly one argument, a task number, e.g. \"mark 2\"",
                "Can you at least give me a real order?");
    }
}
