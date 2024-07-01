package ArgsParser.Argserror;

/**
 * Exception to be thrown if too many arguments are provided for a parameter
 */
public class TooManyArgumentsArgserror extends Exception{
    public TooManyArgumentsArgserror(String flagName) {
        super("Too many arguments provided for parameter: " + flagName);
    }
}
