package ArgsParser;

/*
COPYRIGHT © 2024 Niklas Max G.
This work is licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
More details at: https://github.com/AbUndMax/Java_ArgsParser/blob/main/LICENSE.md
For a quick overview, visit https://creativecommons.org/licenses/by-nc/4.0/
 */

import ArgsParser.ArgsExceptions.InvalidArgTypeArgsException;
import ArgsParser.ArgsExceptions.NotExistingPathArgsException;

import java.util.Objects;

/**
 * Abstract base class for defining command-line parameters.
 *
 * <p>
 * The {@code Parameter<T>} class serves as a foundation for creating specific types of command-line parameters
 * within the {@link ArgsParser} framework. To create a functional parameter, developers must extend this class
 * and implement the abstract {@code castArgument} method to handle the conversion of string inputs to the desired type.
 * </p>
 *
 * <h2>Extending Parameter&lt;T&gt;</h2>
 *
 * <p>
 * To create a custom parameter, follow these steps:
 * </p>
 *
 * <ol>
 *     <li>
 *         <strong>Define the Parameter Type:</strong>
 *         Determine the type {@code T} that the parameter will handle (e.g., {@link Integer}, {@link String}, etc.).
 *     </li>
 *     <li>
 *         <strong>Create the Subclass:</strong>
 *         Extend the {@code Parameter<T>} class, specifying the appropriate type.
 *         <pre>{@code
 * public class IntegerParameter extends Parameter<Integer> {
 *     // Implementation details
 * }
 * }</pre>
 *     </li>
 *     <li>
 *         <strong>Implement Constructors:</strong>
 *         Provide constructors that call the superclass constructors, passing necessary parameters such as flags, description,
 *         mandatory status, and default values if applicable.
 *         <pre>{@code
 * public IntegerParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
 *     super(fullFlag, shortFlag, description, isMandatory, Integer.class);
 * }
 *
 * public IntegerParameter(Integer defaultValue, String fullFlag, String shortFlag, String description) {
 *     super(defaultValue, fullFlag, shortFlag, description, Integer.class);
 * }
 * }</pre>
 *     </li>
 *     <li>
 *         <strong>Override castArgument:</strong>
 *         Implement the {@code castArgument} method to convert the input string to the desired type {@code T}.
 *         Handle any necessary validation and exception throwing within this method.
 *         <pre>{@code
 * @Override
 * protected Integer castArgument(String argument) throws InvalidArgTypeArgsException {
 *     try {
 *         return Integer.parseInt(argument);
 *     } catch (NumberFormatException e) {
 *         throw new InvalidArgTypeArgsException(getFullFlag(), "Integer", "Invalid integer value: " + argument);
 *     }
 * }
 * }</pre>
 *     </li>
 * </ol>
 *
 * <h2>Key Components</h2>
 *
 * <ul>
 *     <li><strong>Flags:</strong> Define both full (e.g., {@code --verbose}) and short (e.g., {@code -v}) flags to identify the parameter in command-line arguments.</li>
 *     <li><strong>Description:</strong> Provide a clear description of what the parameter represents, aiding users in understanding its purpose.</li>
 *     <li><strong>Mandatory Status:</strong> Indicate whether the parameter is required for the application to run.</li>
 *     <li><strong>Default Values:</strong> Optionally set a default value that will be used if the parameter is not explicitly provided.</li>
 * </ul>
 *
 * <h2>Exception Handling</h2>
 *
 * <p>
 * When implementing {@code castArgument}, ensure that any invalid input is properly handled by throwing relevant exceptions,
 * such as {@link InvalidArgTypeArgsException} or custom exceptions as needed.
 * </p>
 *
 * <h2>Example Implementation</h2>
 *
 * <pre>{@code
 * public class StringParameter extends Parameter<Integer> {
 *
 *     public StringParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
 *         super(fullFlag, shortFlag, description, isMandatory, String.class);
 *     }
 *
 *     public StringParameter(Integer defaultValue, String fullFlag, String shortFlag, String description) {
 *         super(defaultValue, fullFlag, shortFlag, description, String.class);
 *     }
 *
 *     @Override
 *     protected String castArgument(String argument) {
 *         if (argument == null || argument.isEmpty()) {
 *             throw new InvalidArgTypeArgsException(getFullFlag(), "String", "Argument cannot be null or empty.");
 *         }
 *         return Integer.parseInt(argument);
 *     }
 * }
 * }</pre>
 *
 * <h2>Integration with ArgsParser</h2>
 *
 * <p>
 * After creating a subclass, register the parameter with an instance of {@link ArgsParser}. Ensure that flags are unique
 * and adhere to the validation rules defined in {@link Parameter}.
 * </p>
 *
 * @param <T> The type of the parameter value (e.g., {@link Integer}, {@link String}, {@link Boolean}).
 * @see ArgsParser
 * @see InvalidArgTypeArgsException
 * @see NotExistingPathArgsException
 */
public abstract class Parameter<T> {
    private final String fullFlag;
    private final String shortFlag;
    private final String description;
    private final boolean isMandatory;
    private T argument = null;
    private T defaultValue = null;
    private ArgsParser argsParser;
    private Class<T> type;
    private boolean isProvided = false;

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
     * @throws IllegalArgumentException If the flag names are invalid, empty, or reserved.
     */
    protected Parameter(String fullFlag, String shortFlag, String description, boolean isMandatory, Class<T> type) {
        this.fullFlag = ArgsParser.makeFlag(fullFlag, false);
        this.shortFlag = ArgsParser.makeFlag(shortFlag, true);
        this.description = description;
        this.isMandatory = isMandatory;
        this.type = type;
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
     * @param defaultValue Sets a default value for this Parameter & makes it not mandatory.
     * @param fullFlag     The full version of the flag (e.g., `--example`).
     * @param shortFlag    The short version of the flag (e.g., `-e`).
     * @param description  A brief description of what the parameter represents.
     * @throws IllegalArgumentException If the flag names are invalid, empty, or reserved.
     */
    protected Parameter(T defaultValue, String fullFlag, String shortFlag, String description, Class<T> type) {
        this(fullFlag, shortFlag, description, false, type);
        this.defaultValue = defaultValue;
    }

    /**
     * add the ArgsParser instance on which this Parameter was added.
     * @param argsParser parser instance on which this parameter was added.
     */
    protected void setParser(ArgsParser argsParser) {
        this.argsParser = argsParser;
    }

    /**
     * returns the argument as is
     * @return argument or null if no argument is set
     */
    protected T readArgument() {
        return argument;
    }

    /**
     * Checks if the parameter type represents an array.
     * @return true if the parameter type is an array, false otherwise.
     */
    protected boolean isArray() {
        return type.isArray();
    }

    /**
     * getter method for the fullFlag
     * @return fullFlag
     */
    protected String getFullFlag() {
        return fullFlag;
    }

    /**
     * getter method for the isMandatory attribute
     * @return true if the parameter is mandatory, false otherwise
     */
    protected boolean isMandatory() {
        return isMandatory;
    }

    /**
     * getter method for the shortFlag attribute
     * @return shortFlag
     */
    protected String getShortFlag() {
        return shortFlag;
    }

    /**
     * getter method for the description attribute
     * @return description
     */
    protected String getDescription() {
        return description;
    }

    /**
     * getter method for the type attribute
     * @return type
     */
    protected String getType() {
        return type.getSimpleName();
    }

    /**
     * getter method for the hasArgument attribute
     * @return true if this Parameter was created with a default value, false otherwise
     */
    protected boolean hasDefault() {
        return defaultValue != null;
    }

    /**
     * getter method for the defaultValue attribute
     * @return defaultValue
     */
    protected T getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Checks if the parameter has an (actual) Argument that is not null!.
     *
     * @return true if the parameter has an argument, false otherwise
     */
    public boolean hasArgument() {
        return argument != null;
    }

    /**
     * Marks the parameter as provided.
     * This method sets the internal flag indicating
     * that this parameter has been supplied with a value.
     */
    protected void setProvided() {
        isProvided = true;
    }

    /**
     * Checks if the parameter was provided with a value.
     *
     * @return true if the parameter was provided, false otherwise
     */
    public boolean isProvided() {
        return isProvided;
    }

    /**
     * getter method for the argument attribute
     * @return argument as String
     * @throws IllegalStateException if {@link ArgsParser#parse(String[] args)} was not called before trying to access this argument
     * or if this Parameter was not added to any {@link ArgsParser}!
     */
    public T getArgument() throws IllegalStateException {
        if (argsParser == null) throw new IllegalStateException("Parameter: " + this + " is not assigned to any parser instance!");
        if (!argsParser.parseArgsWasCalled()) throw new IllegalStateException("parse() was not called before trying to access the argument!");
        if (hasArgument()) return argument;
        else if (hasDefault()) return defaultValue;
        else return null;
    }

    /**
     * setter method for the argument attribute, sets parsed status of this parameter instance to true
     * @param argument argument
     * @throws InvalidArgTypeArgsException if the given Argument is not of the target type
     * @throws NotExistingPathArgsException if a PthParameter with pathCheck was handed a non-existing path
     */
    protected void setArgument(String argument) throws InvalidArgTypeArgsException, NotExistingPathArgsException {
        try {
            this.argument = castArgument(argument);
        } catch (NumberFormatException nfe) {
            throw new InvalidArgTypeArgsException(fullFlag, type.getSimpleName(), "Provided argument does not match the parameter type!");
        } catch (NotExistingPathArgsException argsExcep) {
            throw argsExcep;
        } catch (Exception e) {
            throw new InvalidArgTypeArgsException(fullFlag, type.getSimpleName(), e.getMessage());
        }
    }

    /**
     * Casts the argument to type T
     * @param argument to be cast
     * @return the argument as type T
     * @throws NotExistingPathArgsException if a PthParameter with pathCheck was handed a non-existing path
     */
    protected abstract T castArgument(String argument) throws NotExistingPathArgsException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter<?> parameter = (Parameter<?>) o;
        return Objects.equals(fullFlag, parameter.fullFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullFlag);
    }

    @Override
    public String toString() {
        return "[" + fullFlag + " / " + shortFlag + "]";
    }

}
