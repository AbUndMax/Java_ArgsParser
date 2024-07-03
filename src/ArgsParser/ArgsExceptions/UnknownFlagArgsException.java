package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

/**
 * Exception to be thrown if an unknown flag is provided
 */
public class UnknownFlagArgsException extends ArgsException {
    public UnknownFlagArgsException(String flagName) {
        super("unknown parameter: " + flagName);
    }
}
