package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

/**
 * if an argument is missing for a flag
 */
public class MissingArgArgsException extends ArgsException {
    public MissingArgArgsException(String flagName){
        super("Missing argument for flag: " + flagName, true);
    }
}
