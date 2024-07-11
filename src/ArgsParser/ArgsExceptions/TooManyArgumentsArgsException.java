package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

/**
 * Exception to be thrown if too many arguments are provided for a parameter
 */
public class TooManyArgumentsArgsException extends ArgsException {
    public TooManyArgumentsArgsException(String flagName) {
        super("Too many arguments provided to flag: " + flagName);
    }
}
