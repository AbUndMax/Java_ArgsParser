package ArgsParser;

/*
COPYRIGHT © 2024 Niklas Max G.
This work is licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
More details at: https://github.com/AbUndMax/Java_ArgsParser/blob/main/LICENSE.md
For a quick overview, visit https://creativecommons.org/licenses/by-nc/4.0/
 */

import ArgsParser.ArgsExceptions.InvalidArgTypeArgsException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * Parameter class with fields for each attribute of the Parameter including the argument.
 */
public class Parameter<T> {
    private final ArgsParser argsParser;
    private final String fullFlag;
    private final String shortFlag;
    private final String description;
    private final boolean isMandatory;
    private final Class<T> type;
    private T defaultValue = null;
    private boolean hasDefault = false;
    private T argument = null;
    private boolean hasArgument = false;
    private boolean isProvided = false;

    /**
     * Constructor for the Parameter class with type definition
     * @param fullFlag name of the parameter
     * @param shortFlag short name of the parameter
     * @param description description of the parameter
     * @param type type of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     */
    protected Parameter(String fullFlag, String shortFlag, String description, Class<T> type, boolean isMandatory, ArgsParser argsParser) {
        this.fullFlag = fullFlag;
        this.shortFlag = shortFlag;
        this.description = description;
        this.isMandatory = isMandatory;
        this.type = type;
        this.argsParser = argsParser;
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

    /**
     * Checks if the parameter has an (actual) Argument that is not null!.
     *
     * @return true if the parameter has an argument, false otherwise
     */
    public boolean hasArgument() {
        return hasArgument;
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
     * @throws IllegalStateException if {@link ArgsParser#parse()} was not called before trying to access this argument
     */
    public T getArgument() throws IllegalStateException {
        if (!argsParser.parseArgsWasCalled()) throw new IllegalStateException("parse() was not called before trying to access the argument!");
        if (!hasArgument && !hasDefault) return null;
        return argument;
    }

    /**
     * setter method for the argument attribute, sets parsed status of this parameter instance to true
     * @param argument argument
     * @throws InvalidArgTypeArgsException if the given Argument is not of the target type
     */
    protected void setArgument(String argument) throws InvalidArgTypeArgsException {
        try {
            ArgumentConverter argumentConverter = ArgumentConverter.fromClass(type);
            this.argument = type.cast(argumentConverter.convert(argument));
            this.hasArgument = true;
        } catch (IllegalArgumentException e) {
            throw new InvalidArgTypeArgsException(this.fullFlag, type.getSimpleName(), "Unsupported type!");
        } catch (Exception e) {
            throw new InvalidArgTypeArgsException(this.fullFlag, type.getSimpleName(), e.getMessage());
        }
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

    /**
     * The ArgumentConverter enum provides a mapping between argument types and their
     * corresponding conversion logic. It supports both array and non-array types for
     * several common data types such as Integer, String, Boolean, Double, and Character.
     */
    private enum ArgumentConverter {
        INTEGER_ARRAY(Integer[].class, Integer::parseInt),
        INTEGER(Integer.class, Integer::parseInt),
        STRING_ARRAY(String[].class, Function.identity()),
        STRING(String.class, Function.identity()),
        BOOLEAN_ARRAY(Boolean[].class, Boolean::parseBoolean),
        BOOLEAN(Boolean.class, Boolean::parseBoolean),
        DOUBLE_ARRAY(Double[].class, Double::parseDouble),
        DOUBLE(Double.class, Double::parseDouble),
        CHARACTER_ARRAY(Character[].class, s -> s.charAt(0)),
        CHARACTER(Character.class, s -> s.charAt(0));

        private final Class<?> typeClass;
        private final Function<String, ?> mapper;

        /**
         * Constructs an ArgumentConverter with the specified type class and mapping function.
         *
         * @param typeClass the class type this ArgumentConverter will handle. Can be an array class or a single class.
         * @param mapper a function that converts a String argument to an instance of the specified class type.
         */
        ArgumentConverter(Class<?> typeClass, Function<String, ?> mapper) {
            this.typeClass = typeClass;
            this.mapper = mapper;
        }

        /**
         * Converts the provided argument string into an Object of the appropriate type,
         * handling both array and non-array types.
         *
         * @param argument the argument string to be converted
         * @return the converted object, either as a single instance or an array
         */
        private Object convert(String argument) {
            if (typeClass.isArray()) {
                String[] parts = argument.split("===");
                Object array = Array.newInstance(typeClass.getComponentType(), parts.length);
                for (int i = 0; i < parts.length; i++) {
                    Array.set(array, i, mapper.apply(parts[i]));
                }
                return array;
            } else {
                return mapper.apply(argument);
            }
        }

        /**
         * Returns an ArgumentConverter that matches the provided class type.
         *
         * @param cls the class type to match against the available ArgumentConverters
         * @return the ArgumentConverter corresponding to the provided class type
         * @throws IllegalArgumentException if the provided class type is not supported
         */
        private static ArgumentConverter fromClass(Class<?> cls) {
            for (ArgumentConverter argumentConverter : values()) {
                if (argumentConverter.typeClass.equals(cls)) {
                    return argumentConverter;
                }
            }
            throw new IllegalArgumentException("Unsupported type: " + cls.getSimpleName());
        }
    }

}
