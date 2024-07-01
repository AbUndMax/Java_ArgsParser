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
    private boolean isParsed = false;
    private String shortName = null;
    private String description = null;
    private String argument = null;
    private Integer argumentAsInteger = null;
    private Double argumentAsDouble = null;
    private Boolean argumentAsBoolean = null;
    private Character argumentAsChar = null;

    protected Parameter(String flagName, boolean isMandatory) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
    }

    protected Parameter(String flagName, String shortName, boolean isMandatory) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
        this.shortName = shortName;
    }

    protected Parameter(String flagName, String shortName, String description, boolean isMandatory) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
        this.shortName = shortName;
        this.description = description.trim();
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
     */
    @SuppressWarnings("unchecked")
    public <T> T getArgument() {
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
        if (!isParsed) throw new IllegalStateException("Arguments not parsed yet. Call parseArgs() first.");
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
        isParsed = true;
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
