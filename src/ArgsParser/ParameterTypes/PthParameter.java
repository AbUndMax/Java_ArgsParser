package ArgsParser.ParameterTypes;

import ArgsParser.ArgsExceptions.NotExistingPathArgsException;
import ArgsParser.Parameter;

import java.nio.file.Files;
import java.nio.file.Path;

public class PthParameter extends Parameter<Path> {

    private final boolean pathCheck;

    /**
     * Constructs a new {@link Parameter} instance with the specified flags, description, and mandatory status.
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
     * Constructs a new {@link Parameter} instance with the specified flags, description, and a default value.
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
     * @param fullFlag     The full version of the flag (e.g., `--example`).
     * @param shortFlag    The short version of the flag (e.g., `-e`).
     * @param description  A brief description of what the parameter represents.
     * @param defaultValue Sets a default value for this Parameter & makes it not mandatory.
     * @throws IllegalArgumentException If the flag names are invalid, empty, or reserved.
     */
    public PthParameter(String fullFlag, String shortFlag, String description, Path defaultValue, boolean pathCheck) {
        super(defaultValue, fullFlag, shortFlag, description, Path.class);
        this.pathCheck = pathCheck;
    }

    public boolean pathExists() {
        return Files.exists(super.getArgument());
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
