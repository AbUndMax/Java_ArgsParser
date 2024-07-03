package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

/**
 * InvalidArgTypeArgsException is thrown when an invalid argument type is provided to a flag.
 */
public class InvalidArgTypeArgsException extends ArgsException {
    public InvalidArgTypeArgsException(String flagName) {
        super("Invalid argument type provided to: " + flagName);
    }
}
