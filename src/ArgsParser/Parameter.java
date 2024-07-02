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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Parameter class with fields for each attribute of the Parameter including the argument.
 */
public class Parameter {
    private final String flagName;
    private final boolean isMandatory;
    private final ArgsParser parser;
    private boolean hasArgument = false;
    private String shortName = null;
    private String description = null;
    private String argument = null;
    private Integer argumentAsInteger = null;
    private Double argumentAsDouble = null;
    private Boolean argumentAsBoolean = null;
    private Character argumentAsChar = null;

    protected Parameter(String flagName, boolean isMandatory, ArgsParser parserInstance) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
        this.parser = parserInstance;
    }

    protected Parameter(String flagName, String shortName, boolean isMandatory, ArgsParser parserInstance) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
        this.shortName = shortName;
        this.parser = parserInstance;
    }

    protected Parameter(String flagName, String shortName, String description, boolean isMandatory, ArgsParser parserInstance) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
        this.shortName = shortName;
        this.description = description.trim();
        this.parser = parserInstance;
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
     * getter method for the argument attribute
     * @return argument
     * @throws IllegalStateException if parseArgs() was not called before trying to access this argument
     */
    @SuppressWarnings("unchecked")
    public <T> T getArgument() throws IllegalStateException{
        if (!parser.parseArgsWasCalled) throw new IllegalStateException();
        if (!hasArgument) return null;
        List<Supplier<Object>> conversionFunctions = Arrays.asList(
                () -> argument,
                this::getArgumentAsInteger,
                this::getArgumentAsDouble,
                this::getArgumentAsBoolean,
                this::getArgumentAsChar
        );

        for (Supplier<Object> function : conversionFunctions) {
            try {
                return (T) function.get();
            } catch (Exception e) {
                // Ignorieren und zum nächsten Versuch übergehen
            }
        }
        return null;
    }

    /**
     * getter method for the argument attribute as an integer
     * @return argument as an integer
     */
    private Integer getArgumentAsInteger() {
        if (this.argumentAsInteger == null) this.argumentAsInteger = Integer.parseInt(argument);
        return this.argumentAsInteger;
    }

    /**
     * getter method for the argument attribute as a double
     * @return argument as a double
     */
    private Double getArgumentAsDouble() {
        if (this.argumentAsDouble == null) this.argumentAsDouble = Double.parseDouble(argument);
        return this.argumentAsDouble;
    }

    /**
     * getter method for the argument attribute as a boolean
     * @return argument as a boolean
     */
    private Boolean getArgumentAsBoolean() {
        if (this.argumentAsBoolean == null) this.argumentAsBoolean = Boolean.parseBoolean(argument);
        return this.argumentAsBoolean;
    }
    /**
     * getter method for the argument attribute as a char
     * @return argument as a char
     */
    private Character getArgumentAsChar() {
        if (this.argumentAsChar == null) this.argumentAsChar = argument.charAt(0);
        return this.argumentAsChar;
    }

    /**
     * setter method for the argument attribute, sets parsed status of this parameter to true
     * @param argument argument
     */
    protected void setArgument(String argument) {
        this.argument = argument;
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
