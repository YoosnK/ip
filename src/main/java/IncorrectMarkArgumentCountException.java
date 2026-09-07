/** Thrown when "mark"/"unmark" isn't given exactly one argument. */
public class IncorrectMarkArgumentCountException extends NiaProcessorException {
    IncorrectMarkArgumentCountException() {
        super(
                "[Debug] Incorrect number of arguments, expected 1 argument",
                "Can you at least give me a real order?");
    }
}
