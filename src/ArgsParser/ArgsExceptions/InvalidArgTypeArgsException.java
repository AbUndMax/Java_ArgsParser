package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

/**
 * InvalidArgTypeArgsException is thrown when an invalid argument type is provided to a flag.
 */
public class InvalidArgTypeArgsException extends ArgsException {
    public InvalidArgTypeArgsException(String flagName, String typeName, String message) {
        super("Failed to set argument for " + flagName + " of type " + typeName + ": " + message);
    }
}
