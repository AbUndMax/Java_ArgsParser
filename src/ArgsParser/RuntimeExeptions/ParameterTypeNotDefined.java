package ArgsParser.RuntimeExeptions;

/**
 * Exception thrown when the type of a parameter is not defined but a non-String type argument is tried to access.
 */
public class ParameterTypeNotDefined extends RuntimeException {
    public ParameterTypeNotDefined(String flagName) {
        super("Type for parameter: " + flagName + " not defined!");
    }
}
