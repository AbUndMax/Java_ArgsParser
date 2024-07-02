package ArgsParser.Argserror;

/**
 * NoArgumentsProvidedArgsException is thrown when no arguments at all are provided (args.size() == 0)
 */
public class NoArgumentsProvidedArgsException extends ArgsException {
    public NoArgumentsProvidedArgsException() {
        super("No arguments provided");
    }
}
