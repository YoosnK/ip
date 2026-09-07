/** Thrown when a "todo" command has no description. */
public class EmptyTodoDescriptionException extends NiaParseException {
    EmptyTodoDescriptionException() {
        super(
                "[Debug] Todo description is empty",
                "A todo with nothing in it? There's nothing for me to do.");
    }
}
