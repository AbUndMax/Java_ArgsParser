package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

/**
 * Exception used as Notification that --help was used and after printing the help message the program should exit
 */
public class CalledForHelpNotification extends ArgsException {
    public CalledForHelpNotification(String helpMessage) {
        super(helpMessage);
    }
}
