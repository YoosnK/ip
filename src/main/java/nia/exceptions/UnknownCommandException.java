package nia.exceptions;

/** Thrown when the command word isn't one Processor recognizes. */
public class UnknownCommandException extends NiaProcessorException {
    public UnknownCommandException(String commandWord) {
        super(
                String.format(
                        "[Debug] Unrecognized command \"%s\". Valid commands: bye, list, mark, unmark, todo, deadline, event",
                        commandWord),
                "Hm? I don't know what that means.");
    }
}