/** Thrown when the command word isn't one Processor recognizes. */
public class UnknownCommandException extends NiaProcessorException {
    UnknownCommandException() {
        super(
                "[Debug] Unrecognized command",
                "Hm? I don't know what that means.");
    }
}
