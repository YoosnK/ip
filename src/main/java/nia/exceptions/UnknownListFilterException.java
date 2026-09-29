package nia.exceptions;

/** Thrown when "list" is given a filter token that isn't t/task, d/dl, d/deadline, or e/event. */
public class UnknownListFilterException extends NiaParserException {
    public UnknownListFilterException(String filterToken) {
        super(
                String.format(
                        "[Debug] Unrecognized list filter \"%s\". Valid filters: t/task, d/dl, d/deadline, e/event",
                        filterToken),
                "Filter by what now? I only know t/task, d/dl (or d/deadline), and e/event.");
    }
}
