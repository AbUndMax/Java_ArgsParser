package ArgsParser.Argserror;

/**
 * Exception to be thrown if the user asks for help
 */
public class CalledForHelpArgsException extends ArgsException{
    public CalledForHelpArgsException(String helpMessage) {
        super(helpMessage);
    }
}
