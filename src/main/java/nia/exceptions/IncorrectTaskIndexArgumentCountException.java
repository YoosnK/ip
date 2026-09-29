package nia.exceptions;

/** Thrown when a command expecting exactly one task-number argument (mark/unmark/delete) isn't given one. */
public class IncorrectTaskIndexArgumentCountException extends NiaProcessorException {
    public IncorrectTaskIndexArgumentCountException(String command) {
        super(
                String.format(
                        "[Debug] \"%s\" takes exactly one argument, a task number, e.g. \"%s 2\"",
                        command, command),
                "Can you at least give me a real order?");
    }
}
