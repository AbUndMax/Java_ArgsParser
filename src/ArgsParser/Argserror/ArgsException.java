package ArgsParser.Argserror;

/**
 * Exception to be thrown if the user provides wrong arguments
 * Parent class for all exceptions that can be thrown based on wrong arguments provided in String[] args
 */
public class ArgsException extends Exception {
    public ArgsException(String message) {
        super(message);
    }
}
