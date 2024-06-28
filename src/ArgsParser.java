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

    private final String[] args;
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
        argumentsList.add(parameter);
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
        argumentsList.add(parameter);
        if (isMandatory) mandatoryParameters.add(parameter);
    }

    /**
     * adds a new parameter with a short version of the flag that will be checked in args as well as a description
     * @param flagName name of the parameter
     * @param shortName short version of the parameter
     * @param description description of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     */
    public void addParameter(String flagName, String shortName, String description, boolean isMandatory) {
        Parameter parameter = new Parameter(flagName, shortName, description);
        argumentsList.add(parameter);
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
        return getArgumentFromParameter(flagName);
    }

    /**
     * returns the argument of the given flag as a String
     * @param flagName name of the flag
     * @return argument of the flag as a String
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public String getArgumentAsString(String flagName) throws IllegalStateException, IllegalArgumentException{
        return getArgumentFromParameter(flagName);
    }

    /**
     * returns the argument of the given flag as an Integer
     * @param flagName name of the flag
     * @return argument of the flag as an Integer
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public int getArgumentAsInteger(String flagName) throws IllegalStateException, IllegalArgumentException {
        return Integer.parseInt(getArgumentFromParameter(flagName));
    }

    /**
     * returns the argument of the given flag as a Double
     * @param flagName name of the flag
     * @return argument of the flag as a Double
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public double getArgumentAsDouble(String flagName) throws IllegalStateException, IllegalArgumentException {
        return Double.parseDouble(getArgumentFromParameter(flagName));
    }

    /**
     * returns the argument of the given flag as a Boolean
     * @param flagName name of the flag
     * @return argument of the flag as a Boolean
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public boolean getArgumentAsBoolean(String flagName) throws IllegalStateException, IllegalArgumentException {
        return Boolean.parseBoolean(getArgumentFromParameter(flagName));
    }

    /**
     * returns the argument of the given flag as a char
     * @param flagName name of the flag
     * @return argument of the flag as a char - if input is longer than one character, only the first character is returned
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    public char getArgumentAsChar(String flagName) throws IllegalStateException, IllegalArgumentException{
        return getArgumentFromParameter(flagName).charAt(0);
    }

    /**
     * returns the Parameter Object of the given flagName
     * @param flagName name of the flag
     * @return Parameter Object of the given flagName
     * @throws IllegalStateException if parseArgs() was not called before
     * @throws IllegalArgumentException if a wrong or not existent flagName was given
     */
    private String getArgumentFromParameter(String flagName) throws IllegalArgumentException, IllegalStateException {
        if (!parsedSuccessfully) throw new IllegalStateException();
        Parameter param;
        if ((param = argumentsList.getParameterFromList(flagName)) == null) throw new IllegalArgumentException();
        return param.argument;
    }

    /**
     * searches the args Array (user Input) for a specific flag and returns the argument of the flag
     * @param flagName name of the flag
     * @return null if no matching flag was found, argument if flag was found
     */
    private String findArgumentInArgs(String flagName) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(flagName)) {
                return args[i + 1];
            }
        }
        return null;
    }

    /**
     * searches the args Array (user Input) for a specific flag based on long version and short version of the flag
     * and returns the argument of the flag
     * @param flagName long version of the flag
     * @param shortName short version of the flag
     * @return null if no matching flag was found, argument if flag was found
     */
    private String findArgumentInArgs(String flagName, String shortName) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(flagName) || args[i].equals(shortName)) {
                return args[i + 1];
            }
        }
        return null;
    }

    /**
     * uses the flagName to parse the argument from the args array
     * @param flagName name of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    private String parse(String flagName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = findArgumentInArgs(flagName);
        if (argument == null && isMandatory) throw new MandatoryArgumentNotProvided("Mandatory argument " + flagName + " not provided");
        return argument;
    }

    /**
     * uses the flagName and shortName to parse the argument from the args array
     * @param flagName name of the flag
     * @param shortName short version of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    private String parse(String flagName, String shortName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = findArgumentInArgs(flagName, shortName);
        if (argument == null && isMandatory) throw new MandatoryArgumentNotProvided("Mandatory argument " + flagName + " not provided");
        return argument;
    }

    /**
     * Returns the argument provided under the given flag as String.
     * If the given flag is not found in the args array,
     * null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public String parseParameter(String flagName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        return parse(flagName, isMandatory);
    }

    /**
     * Returns the argument provided under the given full flag or short flag as String.
     * If the given flag is not found in the args array, null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param shortName short version of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public String parseParameter(String flagName, String shortName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        return parse(flagName, shortName, isMandatory);
    }

    /**
     * Returns the argument provided under the given flag as Integer Object.
     * If the given flag is not found in the args array,
     * null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public Integer parseParameterAsInt(String flagName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = parse(flagName, isMandatory);
        if (argument == null) return null;
        return Integer.parseInt(argument);
    }

    /**
     * Returns the argument provided under the given full flag or short flag as Integer Object.
     * If the given flag is not found in the args array, null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param shortName short version of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public Integer parseParameterAsInt(String flagName, String shortName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = parse(flagName, shortName, isMandatory);
        if (argument == null) return null;
        return Integer.parseInt(argument);
    }

    /**
     * Returns the argument provided under the given flag as Double Object.
     * If the given flag is not found in the args array,
     * null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public Double parseParameterAsDouble(String flagName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = parse(flagName, isMandatory);
        if (argument == null) return null;
        return Double.parseDouble(argument);
    }

    /**
     * Returns the argument provided under the given full flag or short flag as Double Object.
     * If the given flag is not found in the args array, null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param shortName short version of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public Double parseParameterAsDouble(String flagName, String shortName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = parse(flagName, shortName, isMandatory);
        if (argument == null) return null;
        return Double.parseDouble(argument);
    }

    /**
     * Returns the argument provided under the given flag as Boolean Object.
     * If the given flag is not found in the args array,
     * null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public Boolean parseParameterAsBoolean(String flagName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = parse(flagName, isMandatory);
        if (argument == null) return null;
        return Boolean.parseBoolean(argument);
    }

    /**
     * Returns the argument provided under the given full flag or short flag as Boolean Object.
     * If the given flag is not found in the args array, null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param shortName short version of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public Boolean parseParameterAsBoolean(String flagName, String shortName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = parse(flagName, shortName, isMandatory);
        if (argument == null) return null;
        return Boolean.parseBoolean(argument);
    }

    /**
     * Returns the argument provided under the given flag as Character Object.
     * If the given flag is not found in the args array,
     * null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public Character parseParameterAsChar(String flagName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = parse(flagName, isMandatory);
        if (argument == null) return null;
        return argument.charAt(0);
    }

    /**
     * Returns the argument provided under the given full flag or short flag as Character Object.
     * If the given flag is not found in the args array, null is returned
     * or a MandatoryArgumentNotProvided exception is thrown if the argument was set as mandatory
     *
     * <p>The argument is not saved in the ArgsParser Instance thus has to be assigned to a variable for further usage!</p>
     * @param flagName name of the flag
     * @param shortName short version of the flag
     * @param isMandatory true if the argument is mandatory
     * @return argument of the flag as String or null if flag not in args
     * @throws MandatoryArgumentNotProvided if the argument is mandatory and not provided
     */
    public Character parseParameterAsChar(String flagName, String shortName, boolean isMandatory) throws MandatoryArgumentNotProvided {
        String argument = parse(flagName, shortName, isMandatory);
        if (argument == null) return null;
        return argument.charAt(0);
    }


    /**
     * class to keep the argument attributes together
     */
    private class Parameter {
        private final String flagName;
        private String shortName;
        private String description;
        private String argument;

        public Parameter(String flagName) {
            this.flagName = flagName;
        }

        public Parameter(String flagName, String shortName) {
            this.flagName = flagName;
            this.shortName = shortName;
        }

        public Parameter(String flagName, String shortName, String description) {
            this.flagName = flagName;
            this.shortName = shortName;
            this.description = description;
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


    /**
     * Exception to be thrown if a mandatory argument is not provided
     */
    public static class MandatoryArgumentNotProvided extends RuntimeException {
        public MandatoryArgumentNotProvided(String message) {
            super(message);
        }
    }

}
