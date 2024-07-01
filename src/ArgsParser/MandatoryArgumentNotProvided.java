package ArgsParser;

/**
 * Exception to be thrown if a mandatory argument is not provided
 */
public class MandatoryArgumentNotProvided extends RuntimeException {
    public MandatoryArgumentNotProvided(String message) {
        super(message);
    }
}
