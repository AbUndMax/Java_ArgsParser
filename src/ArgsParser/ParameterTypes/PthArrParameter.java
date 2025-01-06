package ArgsParser.ParameterTypes;

import ArgsParser.ArgsExceptions.NotExistingPathArgsException;
import ArgsParser.Parameter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Represents a parameter that holds an array of file system paths.
 *
 * This class extends {@link Parameter} to manage parameters of type {@link Path}-array.
 * It provides constructors for defining the parameter with flags, descriptions, mandatory status,
 * default values, and the option to validate the existence of each path.
 *
 * <p>
 * The {@code pathCheck} field determines whether the existence of the specified paths
 * should be validated during parsing.
 * </p>
 */
public class PthArrParameter extends Parameter<Path[]> {

    private final boolean pathCheck;

    /**
     * Constructs a new {@link Parameter} of type {@link Path}-array instance with the specified flags, description, and mandatory status.
     * <p>
     * The constructor validates and formats the provided flag names.
     * </p>
     *
     * <h2>Behavior:</h2>
     * <ul>
     *     <li>Validates that the full and short flags are correctly formatted and non-empty.</li>
     *     <li>Stores the description and mandatory status of the parameter.</li>
     *     <li>Initializes internal fields for argument management and default values.</li>
     * </ul>
     *
     * <h2>Flag Validation Rules:</h2>
     * <ul>
     *     <li><b>Full Flag:</b> Full words recommended (e.g., example), two dashes `--` will automatically be added.</li>
     *     <li><b>Short Flag:</b> Abbreviations of the fullFlag are recommended (e.g., e), one dash `-`will automatically be added.</li>
     *     <li><b>Reserved Flags:</b> The flags `--help` and `-h` cannot be used.</li>
     *     <li><b>Uniqueness:</b> Full and short flags must be unique and must not already be defined.</li>
     * </ul>
     *
     * @param fullFlag    The full version of the flag (e.g., `--example`).
     * @param shortFlag   The short version of the flag (e.g., `-e`).
     * @param description A brief description of what the parameter represents.
     * @param isMandatory Indicates if this parameter is mandatory.
     * @throws IllegalArgumentException If the flag names are invalid, empty, or reserved.
     */
    public PthArrParameter(String fullFlag, String shortFlag, String description, boolean isMandatory, boolean pathCheck) {
        super(fullFlag, shortFlag, description, isMandatory, Path[].class);
        this.pathCheck = pathCheck;
    }

    /**
     * Constructs a new {@link Parameter} of type {@link Path}-array instance with the specified flags, description, and a default value.
     * <p>
     * The constructor validates and formats the provided flag names.
     * </p>
     *
     * <h2>Behavior:</h2>
     * <ul>
     *     <li>Validates that the full and short flags are correctly formatted and non-empty.</li>
     *     <li>Stores the description and mandatory status of the parameter.</li>
     *     <li>Initializes internal fields for argument management and default values.</li>
     * </ul>
     *
     * <h2>Flag Validation Rules:</h2>
     * <ul>
     *     <li><b>Full Flag:</b> Full words recommended (e.g., example), two dashes `--` will automatically be added.</li>
     *     <li><b>Short Flag:</b> Abbreviations of the fullFlag are recommended (e.g., e), one dash `-`will automatically be added.</li>
     *     <li><b>Reserved Flags:</b> The flags `--help` and `-h` cannot be used.</li>
     *     <li><b>Uniqueness:</b> Full and short flags must be unique and must not already be defined.</li>
     * </ul>
     *
     * @param defaultValue Sets a default value for this Parameter & makes it not mandatory.
     * @param fullFlag     The full version of the flag (e.g., `--example`).
     * @param shortFlag    The short version of the flag (e.g., `-e`).
     * @param description  A brief description of what the parameter represents.
     * @throws IllegalArgumentException If the flag names are invalid, empty, or reserved.
     */
    public PthArrParameter(Path[] defaultValue, String fullFlag, String shortFlag, String description, boolean pathCheck) {
        super(defaultValue, fullFlag, shortFlag, description, Path[].class);
        this.pathCheck = pathCheck;
    }

    /**
     * Casts the argument to type T
     *
     * @param argument to be cast
     * @return the argument as type T
     * @throws NotExistingPathArgsException if a PthParameter with pathCheck was handed a non-existing path
     */
    @Override
    protected Path[] castArgument(String argument) throws NotExistingPathArgsException {
        Path[] array = super.readArgument();
        array = array == null ? new Path[1] : Arrays.copyOf(array, array.length + 1);
        Path pathToAdd = Path.of(argument);
        if (pathCheck && !Files.exists(pathToAdd)) throw new NotExistingPathArgsException(pathToAdd);
        array[array.length - 1] = pathToAdd;
        return array;
    }
}
