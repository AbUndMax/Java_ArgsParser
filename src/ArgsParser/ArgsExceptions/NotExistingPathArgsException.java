package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;

import java.nio.file.Path;

public class NotExistingPathArgsException extends ArgsException {
    public NotExistingPathArgsException(Path path) {
        super(path + " Does not exist!", false);
    }
}
