/**
 * Base type for everything Parser can throw when raw input can't be turned
 * into a valid command. Each subclass corresponds to one specific parse
 * failure and hardcodes its own pair of messages: a technical detail (passed
 * to Exception's own message, for stderr/debugging) and Nia's in-character
 * voiceline (for the user to actually read). Keeping them on the exception
 * itself - rather than passed in as strings at every throw site - means each
 * failure's wording lives in exactly one place.
 */
public abstract class NiaParseException extends Exception {
    private final String voiceline;

    NiaParseException(String debugMessage, String voiceline) {
        super(debugMessage);
        this.voiceline = voiceline;
    }

    String getVoiceline() {
        return voiceline;
    }
}
