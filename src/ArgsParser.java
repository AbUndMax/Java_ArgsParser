import java.util.*;

/**
 * Class to parse arguments given in the command line,
 * Arguments are given in the form of flagName argument,
 * Flags can be mandatory or optional,
 * Flags can have a short version,
 * Arguments returned by the getter methods can be of type String, Integer, Double, Boolean or char
 * <ol>
 * <li>The argsParser is called and the args array has to be passed to the constructor</li>
 * <li>then the parameters have to be added with the addParameter method</li>
 * <li>after all parameters are added, the parseArgs method has to be called! (this is mandatory!)</li>
 * <li>then the arguments can be accessed with the getArgument methods</li>
 * </ol>
 * <p> Made by Niklas Max G. 2024 </p>
 */
public class ArgsParser {

    private String[] args;
    private final ParameterList argumentsList = new ParameterList();
    private final Set<Parameter> mandatoryParameters = new HashSet<>();
    private boolean parsedSuccessfully = false;

    public ArgsParser(String[] args) {
        this.args = args;
    }

    /**
     * adds a new parameter that will be checked in args
     * @param flagName name of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     */
    public void addParameter(String flagName, boolean isMandatory) {
        Parameter parameter = new Parameter(flagName);
        argumentsList.addParameterToList(parameter);
        if (isMandatory) mandatoryParameters.add(parameter);
    }

    /**
     * adds a new parameter with a short version of the flag that will be checked in args
     * @param flagName name of the parameter
     * @param shortName short version of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     */
    public void addParameter(String flagName, String shortName, boolean isMandatory) {
        Parameter parameter = new Parameter(flagName, shortName);
        argumentsList.addParameterToList(parameter);
        if (isMandatory) mandatoryParameters.add(parameter);
    }

    /**
     * checks the given args Array and connects the argument of the user to the correct flag
     */
    public void parseArgs() {

        Set<Parameter> givenParameters = new HashSet<>();
        Parameter currentParameter;

        for (int i = 0; i < args.length; i++) {
            if ((currentParameter = argumentsList.getParameterFromList(args[i])) != null) {
                currentParameter.argument = args[i + 1];
                givenParameters.add(currentParameter);
            }
        }

        if (!givenParameters.containsAll(mandatoryParameters)) {
            mandatoryParameters.removeAll(givenParameters);
            System.out.println("Mandatory parameters are missing: " + mandatoryParameters);
            System.exit(1);
        }

        parsedSuccessfully = true;
    }

    /**
     * returns the argument of the given flag (duplicate of getArgumentAsString)
     * @param flagName name of the flag
     * @return argument of the flag
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public String getArgument(String flagName) throws IllegalStateException, IllegalArgumentException {
        return getArgumentAsString(flagName);
    }

    /**
     * returns the argument of the given flag as a String
     * @param flagName name of the flag
     * @return argument of the flag as a String
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public String getArgumentAsString(String flagName) throws IllegalStateException, IllegalArgumentException{
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgument()!");
        Parameter param;
        if ((param = argumentsList.getParameterFromList(flagName)) == null) throw new IllegalArgumentException();
        return param.argument;
    }

    /**
     * returns the argument of the given flag as an Integer
     * @param flagName name of the flag
     * @return argument of the flag as an Integer
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public int getArgumentAsInteger(String flagName) throws IllegalStateException, IllegalArgumentException {
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsInteger()!");
        Parameter param;
        if ((param = argumentsList.getParameterFromList(flagName)) == null) throw new IllegalArgumentException();
        return Integer.parseInt(param.argument);
    }

    /**
     * returns the argument of the given flag as a Double
     * @param flagName name of the flag
     * @return argument of the flag as a Double
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public double getArgumentAsDouble(String flagName) throws IllegalStateException, IllegalArgumentException {
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsDouble()!");
        Parameter param;
        if ((param = argumentsList.getParameterFromList(flagName)) == null) throw new IllegalArgumentException();
        return Double.parseDouble(param.argument);
    }

    /**
     * returns the argument of the given flag as a Boolean
     * @param flagName name of the flag
     * @return argument of the flag as a Boolean
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public boolean getArgumentAsBoolean(String flagName) throws IllegalStateException, IllegalArgumentException {
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsBoolean()!");
        Parameter param;
        if ((param = argumentsList.getParameterFromList(flagName)) == null) throw new IllegalArgumentException();
        return Boolean.parseBoolean(param.argument);
    }

    /**
     * returns the argument of the given flag as a char
     * @param flagName name of the flag
     * @return argument of the flag as a char - if input is longer than one character, only the first character is returned
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public char getArgumentAsChar(String flagName) throws IllegalStateException{
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsChar()!");
        Parameter param;
        if ((param = argumentsList.getParameterFromList(flagName)) == null) throw new IllegalArgumentException();
        return param.argument.charAt(0);
    }


    /**
     * class to keep the argument attributes together
     */
    private class Parameter {
        private String flagName;
        private String shortName;
        private String argument;

        public Parameter(String flagName) {
            this.flagName = flagName;
        }

        public Parameter(String flagName, String shortName) {
            this.flagName = flagName;
            this.shortName = shortName;
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


    /**
     * List to store the Parameters and a method to retrieve them
     */
    private class ParameterList extends LinkedList<Parameter> {

        public void addParameterToList(Parameter parameter) {
            add(parameter);
        }

        /**
         * returns the parameter with the given flagName
         * @param flag flagName (long or short version) of the parameter
         * @return parameter with the given flagName
         */
        public Parameter getParameterFromList(String flag) {
            for (Parameter p : this) {
                if (p.flagName.equals(flag) || (p.shortName != null && p.shortName.equals(flag))) {
                    return p;
                }
            }
            return null;
        }

    }

}
