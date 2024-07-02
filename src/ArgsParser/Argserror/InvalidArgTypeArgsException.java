package ArgsParser.Argserror;

public class InvalidArgTypeArgsException extends ArgsException {
    public InvalidArgTypeArgsException(String flagName) {
        super("Invalid argument type provided to: " + flagName);
    }
}
