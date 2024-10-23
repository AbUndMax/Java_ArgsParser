package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

/**
 * Exception to be thrown if a mandatory argument is not provided
 */
public class MandatoryArgNotProvidedArgsException extends ArgsException {
    public MandatoryArgNotProvidedArgsException(String message) {
        super(message, true);
    }
}
