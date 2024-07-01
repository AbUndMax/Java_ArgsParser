package ArgsParser.Argserror;

/**
 * Exception to be thrown if a mandatory argument is not provided
 */
public class MandatoryArgNotProvidedArgserror extends Exception {
    public MandatoryArgNotProvidedArgserror(String message) {
        super(message);
    }
}
