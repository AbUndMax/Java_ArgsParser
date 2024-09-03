package ArgsParser;

/*
COPYRIGHT © 2024 Niklas Max G.
This work is licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
More details at: https://github.com/AbUndMax/Java_ArgsParser/blob/main/LICENSE.md
For a quick overview, visit https://creativecommons.org/licenses/by-nc/4.0/
 */

import ArgsParser.ArgsExceptions.InvalidArgTypeArgsException;

import java.util.*;
import java.util.function.Function;

/**
 * Parameter class with fields for each attribute of the Parameter including the argument.
 */
public class Parameter<T> {
    private final String fullFlag;
    private final String shortFlag;
    private final String description;
    private final boolean isMandatory;
    private final Class<T> type;
    private T defaultValue = null;
    private boolean hasDefault = false;
    private T argument = null;
    private boolean hasArgument = false;

    /**
     * converter map for converting a string argument to T used in {@link #setArgument(String)}
     */
    private static final Map<Class<?>, Function<String, ?>> converters = new HashMap<>();
    static {
        converters.put(Integer.class, Integer::valueOf);
        converters.put(Double.class, Double::valueOf);
        converters.put(Boolean.class, Boolean::valueOf);
        converters.put(Character.class, s -> {
            if (s.length() != 1) {
                throw new IllegalArgumentException("Argument must be a single character!");
            }
            return s.charAt(0);
        });
    }

    /**
     * Constructor for the Parameter class with type definition
     * @param fullFlag name of the parameter
     * @param shortFlag short name of the parameter
     * @param description description of the parameter
     * @param type type of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     */
    protected Parameter(String fullFlag, String shortFlag, String description, Class<T> type, boolean isMandatory) {
        this.fullFlag = fullFlag;
        this.shortFlag = shortFlag;
        this.description = description;
        this.isMandatory = isMandatory;
        this.type = type;
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
        return hasDefault;
    }

    /**
     * getter method for the defaultValue attribute
     * @return defaultValue
     */
    protected T getDefaultValue() {
        return this.defaultValue;
    }

    protected boolean hasArgument() {
        return hasArgument;
    }

    /**
     * getter method for the argument attribute
     * @return argument as String
     * @throws IllegalStateException if {@link ArgsParser#parse(String[])} was not called before trying to access this argument
     */
    public T getArgument() throws IllegalStateException {
        if (!ArgsParser.parseArgsWasCalled()) throw new IllegalStateException("parseArgs() was not called before trying to access the argument!");
        if (!hasArgument && !hasDefault) return null;
        return argument;
    }

    /**
     * setter method for the argument attribute, sets parsed status of this parameter instance to true
     * @param argument argument
     */
    protected void setArgument(String argument) throws InvalidArgTypeArgsException {
        if (type.equals(String.class)) {
            this.argument = type.cast(argument);
        } else {
            Function<String, ?> converter = converters.get(type);
            if (converter != null) {
                try {
                    this.argument = type.cast(converter.apply(argument));
                } catch (Exception e) {
                    throw new InvalidArgTypeArgsException(this.fullFlag, type.getSimpleName(), e.getMessage());
                }
            } else {
                throw new InvalidArgTypeArgsException(this.fullFlag, type.getSimpleName(), "Unsupported type!");
            }
        }
        this.hasArgument = true;
    }

    /**
     * Sets the default value for the argument and assigns it to the corresponding type-specific field.
     * The value is also converted to a string and stored in the 'argument' field.
     *
     * @param defaultValue the default value to be set, which can be of type Integer, Double, Character, or Boolean
     * @throws IllegalArgumentException if the type of defaultValue is unsupported
     */
    protected void setDefault(T defaultValue) {
        this.defaultValue = this.argument = defaultValue;
        hasDefault = true;
    }

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

}
