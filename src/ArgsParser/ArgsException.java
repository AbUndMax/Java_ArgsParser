package ArgsParser;

/**
 * Exception to be thrown if the user provides wrong arguments
 * Parent class for all exceptions that can be thrown based on wrong arguments provided in String[] args
 *
 * <p>It is recommended to call {@link System#exit(int status)}  with status = 1, after outputting the message.</p>
 */
public class ArgsException extends Exception {
    public ArgsException(String message) {
        super("\n<!> " + message + "\n\n> Use --help for more information.\n");
    }
}
