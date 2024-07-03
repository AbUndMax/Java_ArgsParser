package ArgsParser;

/*
MIT License

Copyright (c) 2024 Niklas Max G.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import ArgsParser.ArgsExceptions.InvalidArgTypeArgsException;
import ArgsParser.RuntimeExeptions.ParameterTypeNotDefined;

import java.util.*;

/**
 * Parameter class with fields for each attribute of the Parameter including the argument.
 */
public class Parameter {
    private final String flagName;
    private final boolean isMandatory;
    private final ArgsParser parser;
    private Class<?> type;
    private boolean typeWasSet = false;
    private boolean hasArgument = false;
    private String defaultValue = null;
    private String shortName = null;
    private String description = null;
    private String argument = null;
    private Integer argumentAsInteger = null;
    private Double argumentAsDouble = null;
    private Boolean argumentAsBoolean = null;
    private Character argumentAsChar = null;

    // Simple Constructors

    /**
     * Constructor for the Parameter class
     * @param flagName name of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, boolean isMandatory, ArgsParser parserInstance) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
        this.parser = parserInstance;
        this.type = String.class;
    }

    /**
     * Constructor for the Parameter class
     * @param flagName name of the parameter
     * @param shortName short name of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, String shortName, boolean isMandatory, ArgsParser parserInstance) {
        this(flagName, isMandatory, parserInstance);
        this.shortName = shortName;
    }

    /**
     * Constructor for the Parameter class
     * @param flagName name of the parameter
     * @param shortName short name of the parameter
     * @param description description of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, String shortName, String description, boolean isMandatory, ArgsParser parserInstance) {
        this(flagName, shortName, isMandatory, parserInstance);
        this.description = description;
    }

    // Constructors with type definition

    /**
     * Constructor for the Parameter class with type definition
     * @param flagName name of the parameter
     * @param type type of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, Class<?> type, boolean isMandatory, ArgsParser parserInstance) {
        this(flagName, isMandatory, parserInstance);
        this.type = type;
        this.typeWasSet = true;
    }

    /**
     * Constructor for the Parameter class with type definition
     * @param flagName name of the parameter
     * @param shortName short name of the parameter
     * @param type type of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, String shortName, Class<?> type, boolean isMandatory, ArgsParser parserInstance) {
        this(flagName, type, isMandatory, parserInstance);
        this.shortName = shortName;
    }

    /**
     * Constructor for the Parameter class with type definition
     * @param flagName name of the parameter
     * @param shortName short name of the parameter
     * @param description description of the parameter
     * @param type type of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, String shortName, String description, Class<?> type, boolean isMandatory, ArgsParser parserInstance) {
        this(flagName, shortName, type, isMandatory, parserInstance);
        this.description = description;
    }

    // Constructors with default value
    // (they automatically set the type of the parameter based on the type of the provided default value)

    /**
     * Constructor for the Parameter class with default value
     * (sets type of Parameter based on the type of the default value)
     * @param flagName name of the parameter
     * @param defaultValue default value of the parameter
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, Object defaultValue, ArgsParser parserInstance) {
        this.flagName = flagName;
        this.isMandatory = false;
        this.parser = parserInstance;
        this.defaultValue = defaultValue.toString();
        setDefault(defaultValue);
        type = defaultValue.getClass();
        typeWasSet = true;
    }

    /**
     * Constructor for the Parameter class with default value
     * (sets type of Parameter based on the type of the default value)
     * @param flagName name of the parameter
     * @param shortName short name of the parameter
     * @param defaultValue default value of the parameter
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, Object defaultValue, String shortName, ArgsParser parserInstance) {
        this(flagName, defaultValue, parserInstance);
        this.shortName = shortName;
    }

    /**
     * Constructor for the Parameter class with default value
     * (sets type of Parameter based on the type of the default value)
     * @param flagName name of the parameter
     * @param shortName short name of the parameter
     * @param description description of the parameter
     * @param defaultValue default value of the parameter
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, Object defaultValue, String shortName, String description, ArgsParser parserInstance) {
        this(flagName, defaultValue, shortName, parserInstance);
        this.description = description;
    }

    /**
     * getter method for the flagName
     * @return flagName
     */
    protected String getFlagName() {
        return flagName;
    }

    /**
     * getter method for the isMandatory attribute
     * @return true if the parameter is mandatory, false otherwise
     */
    protected boolean isMandatory() {
        return isMandatory;
    }

    /**
     * getter method for the shortName attribute
     * @return shortName
     */
    protected String getShortName() {
        return shortName;
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
        return this.defaultValue != null;
    }

    /**
     * getter method for the defaultValue attribute
     * @return defaultValue
     */
    protected String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * getter method for the argument attribute
     * @return argument as String
     * @throws IllegalStateException if {@link ArgsParser#parseArgs()} was not called before trying to access this argument
     */
    public String getArgument() throws IllegalStateException {
        if (!parser.parseArgsWasCalled) throw new IllegalStateException("parseArgs() was not called before trying to access the argument!");
        if (!hasArgument) return null;
        return argument;
    }

    /**
     * getter method for the argument attribute
     * @return argument
     * @throws IllegalStateException if {@link ArgsParser#parseArgs()} was not called before trying to access this argument
     * @throws IllegalArgumentException if the argument is not of the expected type
     * @throws ParameterTypeNotDefined if the type of the parameter was not defined when using {@link ArgsParser#addParameter(String, boolean)} without a specified type
     */
    @SuppressWarnings("unchecked")
    public <T> T getCastedArgument() throws IllegalStateException, IllegalArgumentException, ParameterTypeNotDefined{
        if (!typeWasSet) throw new ParameterTypeNotDefined(this.flagName);
        if (!parser.parseArgsWasCalled) throw new IllegalStateException("parseArgs() was not called before trying to access the argument!");
        if (!hasArgument) return null;

        switch (type.getSimpleName()) {
            case "Integer" -> { return (T) argumentAsInteger; }
            case "Double" -> { return (T) argumentAsDouble; }
            case "Boolean" -> { return (T) argumentAsBoolean; }
            case "Character" -> { return (T) argumentAsChar; }
            default -> throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }

    }

    /**
     * setter method for the argument attribute, sets parsed status of this parameter instance to true
     * @param argument argument
     */
    protected void setArgument(String argument) throws InvalidArgTypeArgsException {
        this.argument = argument;

        if (type != String.class) {
            try {
                switch (type.getSimpleName()) {
                    case "Integer" -> this.argumentAsInteger = Integer.parseInt(argument);
                    case "Double" -> this.argumentAsDouble = Double.parseDouble(argument);
                    case "Boolean" -> this.argumentAsBoolean = Boolean.parseBoolean(argument);
                    case "Character" -> this.argumentAsChar = argument.charAt(0);
                }

            } catch (Exception e) {
                throw new InvalidArgTypeArgsException(this.flagName);
            }
        }

        this.hasArgument = true;
    }

    /**
     * Sets the default value for the argument and assigns it to the corresponding type-specific field.
     * The value is also converted to a string and stored in the 'argument' field.
     *
     * @param <T> the type of the default value
     * @param defaultValue the default value to be set, which can be of type Integer, Double, Character, or Boolean
     * @throws IllegalArgumentException if the type of defaultValue is unsupported
     */
    protected <T> void setDefault(T defaultValue) {
        this.argument = defaultValue.toString();

        switch (defaultValue) {
            case String s -> argument = s;
            case Integer i -> argumentAsInteger = i;
            case Double v -> argumentAsDouble = v;
            case Character c -> argumentAsChar = c;
            case Boolean b -> argumentAsBoolean = b;
            default -> throw new IllegalArgumentException("Unsupported type: " + defaultValue.getClass().getName());
        }
        this.hasArgument = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(flagName, parameter.flagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flagName);
    }

}
