package ArgsParser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * class to keep the argument attributes together
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
    
        if (defaultValue instanceof Integer) {
            argumentAsInteger = (Integer) defaultValue;
        } else if (defaultValue instanceof Double) {
            argumentAsDouble = (Double) defaultValue;
        } else if (defaultValue instanceof Character) {
            argumentAsChar = (Character) defaultValue;
        } else if (defaultValue instanceof Boolean) {
            argumentAsBoolean = (Boolean) defaultValue;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + defaultValue.getClass().getName());
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
