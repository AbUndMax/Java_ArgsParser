package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

/**
 * Exception to be thrown if a flag is provided more than once in the arguments.
 *
 * This exception indicates that the flag was already supplied and
 * highlights the redundancy.
 */
public class FlagAlreadyProvidedArgsException extends ArgsException {
    public FlagAlreadyProvidedArgsException(String fullFlag, String shortFlag) {
        super("Redundant specification of arguments to: " + fullFlag + "/" + shortFlag, true);
    }
}
