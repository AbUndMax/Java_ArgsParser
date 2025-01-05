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
 * Parameter class with fields for each attribute of the Parameter including the argument.
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
     * Sets the type of this parameter
     * @param type type T of this parameter
     */
    protected void setType(Class<T> type) {
        this.type = type;
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
            throw new InvalidArgTypeArgsException(fullFlag, type.getSimpleName(), "Provided argument does not match the parameters type!");
        } catch (NotExistingPathArgsException nepae) {
            throw nepae;
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
