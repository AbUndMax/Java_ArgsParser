package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

public class HelpAtWrongPositionArgsException extends ArgsException {
    public HelpAtWrongPositionArgsException() {
        super("use --help/-h alone or directly behind a flag!", false);
    }
}
