package ArgsParser.ParameterTypes;

import ArgsParser.Parameter;

/**
 * Represents a parameter that holds a boolean value.
 *
 * This class extends {@link Parameter} to manage parameters of type {@link Boolean}.
 * It provides constructors for defining the parameter with flags, descriptions,
 * mandatory status, and default values.
 *
 * <p>
 * The {@code castArgument} method is overridden to parse string arguments into {@link Boolean} values.
 * </p>
 */
public class BolParameter extends Parameter<Boolean> {
    /**
     * Constructs a new {@link Parameter} of type {@link Boolean} instance with the specified flags, description, and mandatory status.
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
    public BolParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        super(fullFlag, shortFlag, description, isMandatory, Boolean.class);
    }

    /**
     * Constructs a new {@link Parameter} of type {@link Boolean} instance with the specified flags, description, and a default value.
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
    public BolParameter(Boolean defaultValue, String fullFlag, String shortFlag, String description) {
        super(defaultValue, fullFlag, shortFlag, description, Boolean.class);
    }

    /**
     * Casts the default Argument of type T to String
     *
     * @param defaultValue the default to be cast to String
     * @return the defaultValue as String
     */
    @Override
    protected String castDefaultToString(Boolean defaultValue) {
        return defaultValue.toString();
    }

    /**
     * Casts the argument to type T
     *
     * @param argument to be cast
     * @return the argument as type T
     */
    @Override
    protected Boolean castArgument(String argument) {
        return Boolean.parseBoolean(argument);
    }


}
