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

import java.util.*;

/**
 * Parameter class with fields for each attribute of the Parameter including the argument.
 */
public class Parameter<T> {
    private final String flagName;
    private final String shortName;
    private final String description;
    private final boolean isMandatory;
    private final ArgsParser parser;
    private final Class<T> type;
    private T defaultValue = null;
    private boolean hasDefault = false;
    private T argument = null;
    private boolean hasArgument = false;

    /**
     * Constructor for the Parameter class with type definition
     * @param flagName name of the parameter
     * @param shortName short name of the parameter
     * @param description description of the parameter
     * @param type type of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @param parserInstance instance of the ArgsParser class
     */
    protected Parameter(String flagName, String shortName, String description, Class<T> type, boolean isMandatory, ArgsParser parserInstance) {
        this.flagName = flagName;
        this.shortName = shortName;
        this.description = description;
        this.parser = parserInstance;
        this.isMandatory = isMandatory;
        this.type = type;
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
     * @throws IllegalStateException if {@link ArgsParser#parseArgs()} was not called before trying to access this argument
     */
    public T getArgument() throws IllegalStateException {
        if (!parser.parseArgsWasCalled) throw new IllegalStateException("parseArgs() was not called before trying to access the argument!");
        if (!hasArgument && !hasDefault) return null;
        return argument;
    }

    /**
     * setter method for the argument attribute, sets parsed status of this parameter instance to true
     * @param argument argument
     */
    protected void setArgument(String argument) throws InvalidArgTypeArgsException {
        if (type.equals(String.class)) {
            this.argument = (T) argument;
        } else {
            try {
                switch (type.getSimpleName()) {
                    case "Integer" -> this.argument = (T) Integer.valueOf(argument);
                    case "Double" -> this.argument = (T) Double.valueOf(argument);
                    case "Boolean" -> this.argument = (T) Boolean.valueOf(argument);
                    case "Character" -> {
                        if (argument.length() == 1) {
                            this.argument = (T) Character.valueOf(argument.charAt(0));
                        } else {
                            throw new InvalidArgTypeArgsException(this.flagName, type.getSimpleName(), "Argument must be a single character!");
                        }
                    }
                    default -> throw new InvalidArgTypeArgsException(this.flagName, type.getSimpleName(), "Unsupported type!");
                }
            } catch (Exception e) {
                throw new InvalidArgTypeArgsException(this.flagName, type.getSimpleName(), e.getMessage());
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
        Parameter parameter = (Parameter) o;
        return Objects.equals(flagName, parameter.flagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flagName);
    }

}
