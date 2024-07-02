package ArgsParser.Argserror;

public class NoArgumentsProvidedArgsException extends ArgsException {
    public NoArgumentsProvidedArgsException() {
        super("No arguments provided");
    }
}
