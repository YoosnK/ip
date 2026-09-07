/** Thrown when a "deadline" command is missing its description and/or its "/by". */
public class MissingDeadlineFieldsException extends NiaParseException {
    MissingDeadlineFieldsException() {
        super(
                "[Debug] Deadline needs a description and a /by",
                "I need a description and a /by, or I've got nothing to remember.");
    }
}
