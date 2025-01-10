package ArgsParser.ParameterTypes;

import ArgsParser.ArgsExceptions.NotExistingPathArgsException;
import ArgsParser.Parameter;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents a parameter that holds a file system path.
 *
 * This class extends {@link Parameter} to manage parameters of type {@link Path}.
 * It provides constructors for defining the parameter with flags, descriptions,
 * mandatory status, default values, and the option to check if the path exists.
 *
 * <p>
 * The {@code pathCheck} field determines whether the existence of the specified path
 * should be validated during parsing.
 * </p>
 */
public class PthParameter extends Parameter<Path> {

    private final boolean pathCheck;

    /**
     * Constructs a new {@link Parameter} of type {@link Path} instance with the specified flags, description, and mandatory status.
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
     * @param pathCheck   Specify whether to automatically check if the path exists or to not check while parsing.
     * @throws IllegalArgumentException If the flag names are invalid, empty, or reserved.
     */
    public PthParameter(String fullFlag, String shortFlag, String description, boolean isMandatory, boolean pathCheck) {
        super(fullFlag, shortFlag, description, isMandatory, Path.class);
        this.pathCheck = pathCheck;
    }

    /**
     * Constructs a new {@link Parameter} of type {@link Path} instance with the specified flags, description, and a default value.
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
    public PthParameter(Path defaultValue, String fullFlag, String shortFlag, String description, boolean pathCheck) {
        super(defaultValue, fullFlag, shortFlag, description, Path.class);
        this.pathCheck = pathCheck;
    }

    /**
     * Checks if the path specified by the current argument exists in the file system.
     * <p>
     * This method retrieves the argument value associated with this parameter,
     * interprets it as a file system path, and verifies whether it exists.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Returns {@code true} if the path exists, otherwise {@code false}.</li>
     *     <li>If the argument is invalid or null, this method may throw a {@link NullPointerException}.</li>
     * </ul>
     *
     * @return {@code true} if the path exists, otherwise {@code false}.
     */
    public boolean pathExists() {
        return Files.exists(super.getArgument());
    }

    /**
     * Casts the default Argument of type T to String
     *
     * @param defaultValue the default to be cast to String
     * @return the defaultValue as String
     */
    @Override
    protected String castDefaultToString(Path defaultValue) {
        return defaultValue.toString();
    }

    /**
     * Casts the argument to type T
     *
     * @param argument to be cast
     * @return the argument as type T
     * @throws NotExistingPathArgsException if a PthParameter with pathCheck was handed a non-existing path
     */
    @Override
    protected Path castArgument(String argument) throws NotExistingPathArgsException {
        Path path = Path.of(argument);
        if (pathCheck && !Files.exists(path)) throw new NotExistingPathArgsException(path);
        return path;
    }
}
