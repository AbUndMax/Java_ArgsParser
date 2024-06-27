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
 */
public class ArgsParser {

    private String[] args;
    private Set<String> argsSet;
    private final Map<String, String> argumentsMap = new HashMap<>();
    private final Map<String, String> shortFlagMap = new HashMap<>();
    private final Set<String> allFlags = new HashSet<>();
    private final Set<String> mandatoryFlags = new HashSet<>();
    private boolean parsedSuccessfully = false;

    public ArgsParser(String[] args) {
        this.args = args;
        this.argsSet = new HashSet<>(Arrays.asList(args));
    }

    /**
     * adds a new parameter that will be checked in args
     * @param flagName name of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     */
    public void addParameter(String flagName, boolean isMandatory) {
        argumentsMap.put(flagName, null);
        allFlags.add(flagName);

        if(isMandatory) mandatoryFlags.add(flagName);
    }

    /**
     * adds a new parameter with a short version of the flag that will be checked in args
     * @param flagName name of the parameter
     * @param shortName short version of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     */
    public void addParameter(String flagName, String shortName, boolean isMandatory) {
        addParameter(flagName, isMandatory);
        shortFlagMap.put(shortName, flagName);
    }

    public void parseArgs() {
        checkMandatoryParameters();

        for (int i = 0; i < args.length; i++) {
            if (argumentsMap.containsKey(args[i])) {
                try {
                    argumentsMap.put(args[i], args[i + 1]);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error while parsing argument: " + args[i] + " " + args[i + 1]);
                    System.exit(1);
                }
            }
        }

        parsedSuccessfully = true;
    }

    /**
     * checks if all mandatory parameters are set with an argument
     * reports missing flags and exits program if not all mandatory parameters are set
     */
    private void checkMandatoryParameters() {
        //find all fullName parameters in the given args:
        Set<String> givenFlags = allFlags;
        givenFlags.retainAll(argsSet);

        //find all nicknames and convert them to fullNames and add them to the nameSet:
        Set<String> givenShortFlags = shortFlagMap.keySet();
        givenShortFlags.retainAll(argsSet);

        for (String shortFlag : givenShortFlags) {
            givenFlags.add(shortFlagMap.get(shortFlag));
        }

        //check if all mandatory parameters are set:
        if (!givenFlags.containsAll(mandatoryFlags)) {
            mandatoryFlags.removeAll(givenFlags);
            System.out.println("Mandatory parameters are missing: " + mandatoryFlags);
            System.exit(1);
        }
    }

    /**
     * returns the argument of the given flag
     * @param flagName name of the flag
     * @return argument of the flag
     * @throws IllegalStateException if parseArgs() was not called before
     */
    public String getArgument(String flagName) {
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgument()!");
        return argumentsMap.get(flagName);
    }

    /**
     * returns the argument of the given flag as a String
     * @param flagName name of the flag
     * @return argument of the flag as a String
     * @throws IllegalStateException if parseArgs() was not called before
     */
    public String getArgumentAsString(String flagName) {
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsString()!");
        return argumentsMap.get(flagName);
    }

    /**
     * returns the argument of the given flag as an Integer
     * @param flagName name of the flag
     * @return argument of the flag as an Integer
     * @throws IllegalStateException if parseArgs() was not called before
     */
public int getArgumentAsInteger(String flagName) throws IllegalStateException {
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsInteger()!");
        return Integer.parseInt(argumentsMap.get(flagName));
    }

    /**
     * returns the argument of the given flag as a Double
     * @param flagName name of the flag
     * @return argument of the flag as a Double
     * @throws IllegalStateException if parseArgs() was not called before
     */
    public double getArgumentAsDouble(String flagName) throws IllegalStateException {
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsDouble()!");
        return Double.parseDouble(argumentsMap.get(flagName));
    }

    /**
     * returns the argument of the given flag as a Boolean
     * @param flagName name of the flag
     * @return argument of the flag as a Boolean
     * @throws IllegalStateException if parseArgs() was not called before
     */
    public boolean getArgumentAsBoolean(String flagName) throws IllegalStateException{
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsBoolean()!");
        return Boolean.parseBoolean(argumentsMap.get(flagName));
    }

    /**
     * returns the argument of the given flag as a char
     * @param flagName name of the flag
     * @return argument of the flag as a char - if input is longer than 1 character, only the first character is returned
     * @throws IllegalStateException if parseArgs() was not called before
     */
    public char getArgumentAsChar(String flagName) throws IllegalStateException{
        if (!parsedSuccessfully) throw new IllegalStateException("parseArgs() has to be called before getArgumentAsChar()!");
        return argumentsMap.get(flagName).charAt(0);
    }
}
