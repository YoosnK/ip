package nia.exceptions;

/**
 * Thrown by Parser when raw input can't be turned into valid, canonical
 * words - e.g. a todo/deadline/event missing a required field. Each concrete
 * subclass corresponds to one specific parse failure.
 */
public abstract class NiaParserException extends NiaException {
    NiaParserException(String debugMessage, String voiceline) {
        super(debugMessage, voiceline);
    }
}