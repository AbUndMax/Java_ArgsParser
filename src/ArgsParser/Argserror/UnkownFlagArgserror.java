package ArgsParser.Argserror;

/**
 * Exception to be thrown if an unknown flag is provided
 */
public class UnkownFlagArgserror extends Exception{
    public UnkownFlagArgserror(String flagName) {
        super("unknown parameter: " + flagName);
    }
}
