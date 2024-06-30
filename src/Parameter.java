import java.util.Objects;

/**
 * class to keep the argument attributes together
 */
class Parameter {
    private final String flagName;
    private final boolean isMandatory;
    private String shortName;
    private String description;
    private String argument;

    public Parameter(String flagName, boolean isMandatory) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
    }

    public Parameter(String flagName, String shortName, boolean isMandatory) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
        this.shortName = shortName;
    }

    public Parameter(String flagName, String shortName, String description, boolean isMandatory) {
        this.flagName = flagName;
        this.isMandatory = isMandatory;
        this.shortName = shortName;
        this.description = description.trim();
    }

    /**
     * getter method for the flagName
     * @return flagName
     */
    public String getFlagName() {
        return flagName;
    }

    /**
     * getter method for the isMandatory attribute
     * @return true if the parameter is mandatory, false otherwise
     */
    public boolean isMandatory() {
        return isMandatory;
    }

    /**
     * getter method for the shortName attribute
     * @return shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * getter method for the description attribute
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter method for the argument attribute
     * @return argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * setter method for the argument attribute
     * @param argument argument
     */
    public void setArgument(String argument) {
        this.argument = argument;
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
