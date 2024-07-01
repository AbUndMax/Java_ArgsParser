package ArgsParser.Argserror;

/**
 * if an argument is missing for a flag
 */
public class MissingArgArgserror extends Exception{
    public MissingArgArgserror(String flagName){
        super("Missing argument for parameter: " + flagName);
    }
}
