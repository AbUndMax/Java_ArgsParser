package ArgsParser;

/*
COPYRIGHT © 2024 Niklas Max G.
This work is licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
More details at: https://github.com/AbUndMax/Java_ArgsParser/blob/main/LICENSE.md
For a quick overview, visit https://creativecommons.org/licenses/by-nc/4.0/
 */

import ArgsParser.ArgsExceptions.*;
import java.util.*;

/**
 * Class, to parse arguments given in the command line, the tool checks for several conditions:
 * <ul>
 *     <li>if no arguments were provided</li>
 *     <li>if an unknown flag was provided</li>
 *     <li>if a flag was provided without an argument</li>
 *     <li>if more than one argument was provided to a single flag</li>
 *     <li>if not all mandatory parameters were given</li>
 * </ul>
 *
 * <p>The Parser functions as follows:</p>
 *
 * <ol>
 *     <li>Define as many Parameters on a ArgsParser instance as needed by using any of the four "addParameter" Methods
 *     of the type the argument of the Parameter should be (Parameters can be of type String, Integer, Double, Boolean or Character):
 *     <ul>
 *         <li>{@link ArgsParser#addMandatoryStringParameter(String, String, String)}</li>
 *         <li>{@link ArgsParser#addOptionalStringParameter(String, String, String)}</li>
 *         <li>{@link ArgsParser#addDefaultIntegerParameter(String, String, String, Integer)}</li>
 *         <li>{@link ArgsParser#addStringArrayParameter(String, String, String, boolean)}</li>
 *     </ul>
 *     Each of this adder methods take several arguments:
       <ul>
 *         <li>Parameters have a full flag name</li>
 *         <li>Parameters have a short version flags</li>
 *         <li>Parameters can have a description (hand "" or null if not needed)</li>
 *         <li>addDefaultParameters take a default argument</li>
 *         <li>addArrayParameters can be specified mandatory or optional</li>
 *     </ul>
 *     <li>After all parameters are added, the {@link ArgsParser#parse()} method has to be called! (this is mandatory!)</li>
 *     <li>Then the arguments can be accessed by using {@link Parameter#getArgument()} on the specific Parameter variable
 *          which will return the parsed argument of that parameter as the specified type </li>
 * </ol>
 * available at: <a href="https://github.com/AbUndMax/Java_ArgsParser">GitHub</a>
 * @author Niklas Max G. 2024
 */
public class ArgsParser {

    private final String[] args;
    private final Map<String, Parameter<?>> parameterMap = new HashMap<>();
    private final Set<Parameter<?>> mandatoryParameters = new HashSet<>();
    private final Set<String> arrayParameters = new HashSet<>();
    private final LinkedList<String> fullFlags = new LinkedList<>();
    private final LinkedList<String> shortFlags = new LinkedList<>();
    protected boolean parseArgsWasCalled = false;
    private int longestFlagSize = 0;
    private int longestShortFlag = 0;

    /**
     * Constructor
     * @param args arguments given to the program
     * @throws IllegalArgumentException if args is null
     */
    public ArgsParser(String[] args) throws IllegalArgumentException {
        if (args == null) throw new IllegalArgumentException("Args cannot be null!");
        this.args = args;
    }

    /**
     * checks if the parse method was called
     * @return true if the parse method was called, false otherwise
     */
    protected boolean parseArgsWasCalled() {
        return parseArgsWasCalled;
    }

    /**
     * creates a new Parameter instance and sets it accordingly with the given fields.
     * Adds given parameter to argumentList, sets longestFlagSize and adds mandatory parameters to the mandatoryList
     * @param fullFlag name of the parameter
     * @param shortFlag short name of the parameter
     * @param description description of the parameter
     * @param type type of the parameter
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance
     * @param <T> type of the parameter
     */
    private <T> Parameter<T> createParameter(String fullFlag,
                                             String shortFlag,
                                             String description,
                                             Class<T> type,
                                             boolean isMandatory,
                                             T defaultValue) {
        fullFlag = makeFlag(fullFlag, false);
        shortFlag = makeFlag(shortFlag, true);

        // check if the flag names are already used
        if (fullFlags.contains(fullFlag)) throw new IllegalArgumentException("Flag already exists: " + fullFlag);
        if (shortFlags.contains(shortFlag)) throw new IllegalArgumentException("Flag already exists: " + shortFlag);

        // create new parameter instance
        Parameter<T> parameter = new Parameter<T>(fullFlag, shortFlag, description, type, isMandatory, this);

        if (defaultValue != null) {
            parameter.setDefault(defaultValue);
        }

        // add parameter to the map
        parameterMap.put(parameter.getFullFlag(), parameter);
        parameterMap.put(parameter.getShortFlag(), parameter);

        // check for the lengths of the name
        int nameSize = parameter.getFullFlag().length();
        if (longestFlagSize < nameSize) longestFlagSize = nameSize;

        int shortSize = parameter.getShortFlag().length();
        if (longestShortFlag < shortSize) longestShortFlag = shortSize;

        // add names to the name sets
        fullFlags.add(fullFlag);
        shortFlags.add(shortFlag);

        // add to mandatory parameters if parameter is mandatory
        if (parameter.isMandatory()) mandatoryParameters.add(parameter);

        return parameter;
    }

    /**
     * Creates a flag from the given fullFlag, if the fullFlag is already in the correct format, it will be returned as is.
     * If not, it will add a leading or -- to the fullFlag
     * @param fullFlag name of the flag
     * @param isShortName true if the flag is a short flag
     * @return flag in the correct format (e.g. --fullFlag or -f)
     */
    private String makeFlag(String fullFlag, boolean isShortName) {
        int i = 0;
        while (fullFlag.charAt(i) == '-') {
            i++;
        }

        if (i == 2 && !isShortName) return fullFlag;
        else if (i == 1 && isShortName) return fullFlag;
        else {
            String newFlag = isShortName ? "-" : "--";
            return newFlag + fullFlag.substring(i);
        }
    }


    // String Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type String
     */
    public Parameter<String> addMandatoryStringParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, String.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type String
     */
    public Parameter<String> addDefaultStringParameter(String fullFlag, String shortFlag, String description, String defaultValue) {
        return createParameter(fullFlag, shortFlag, description, String.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameterhand empty string "" or null if not needed
     * @return the created Parameter instance of type String
     */
    public Parameter<String> addOptionalStringParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, String.class, false, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type String[]
     */
    public Parameter<String[]> addStringArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, String[].class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type String[]
     */
    public Parameter<String[]> addDefaultStringArrayParameter(String fullFlag, String shortFlag, String description, String[] defaultValue) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, String[].class, false, defaultValue);
    }


    // Integer Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Integer
     */
    public Parameter<Integer> addMandatoryIntegerParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Integer.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Integer
     */
    public Parameter<Integer> addDefaultIntegerParameter(String fullFlag, String shortFlag, String description, Integer defaultValue) {
        return createParameter(fullFlag, shortFlag, description, Integer.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Integer
     */
    public Parameter<Integer> addOptionalIntegerParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Integer.class, false, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type int[]
     */
    public Parameter<Integer[]> addIntegerArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Integer[].class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type int[]
     */
    public Parameter<Integer[]> addDefaultIntegerArrayParameter(String fullFlag, String shortFlag, String description, Integer[] defaultValue) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Integer[].class, false, defaultValue);
    }


    // Double Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Double
     */
    public Parameter<Double> addMandatoryDoubleParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Double.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Double
     */
    public Parameter<Double> addDefaultDoubleParameter(String fullFlag, String shortFlag, String description, Double defaultValue) {
        return createParameter(fullFlag, shortFlag, description, Double.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Double
     */
    public Parameter<Double> addOptionalDoubleParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Double.class, false, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type double[]
     */
    public Parameter<Double[]> addDoubleArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Double[].class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type double[]
     */
    public Parameter<Double[]> addDefaultDoubleArrayParameter(String fullFlag, String shortFlag, String description, Double[] defaultValue) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Double[].class, false, defaultValue);
    }


    // Boolean Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Boolean
     */
    public Parameter<Boolean> addMandatoryBooleanParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Boolean.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Boolean
     */
    public Parameter<Boolean> addDefaultBooleanParameter(String fullFlag, String shortFlag, String description, Boolean defaultValue) {
        return createParameter(fullFlag, shortFlag, description, Boolean.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Boolean
     */
    public Parameter<Boolean> addOptionalBooleanParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Boolean.class, false, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type boolean[]
     */
    public Parameter<Boolean[]> addBooleanArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Boolean[].class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type boolean[]
     */
    public Parameter<Boolean[]> addDefaultBooleanArrayParameter(String fullFlag, String shortFlag, String description, Boolean[] defaultValue) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Boolean[].class, false, defaultValue);
    }


    // Character Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Character
     */
    public Parameter<Character> addMandatoryCharacterParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Character.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Character
     */
    public Parameter<Character> addDefaultCharacterParameter(String fullFlag, String shortFlag, String description, Character defaultValue) {
        return createParameter(fullFlag, shortFlag, description, Character.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Character
     */
    public Parameter<Character> addOptionalCharacterParameter(String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Character.class, false, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type char[]
     */
    public Parameter<Character[]> addCharacterArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Character[].class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * Array Parameters can take multiple arguments behind their flag
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type char[]
     */
    public Parameter<Character[]> addDefaultCharacterArrayParameter(String fullFlag, String shortFlag, String description, Character[] defaultValue) {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Character[].class, false, defaultValue);
    }


    //


    /**
     * <ul>
     *     <li>checks if args is Empty</li>
     *     <li>checks if --help or -h was called on the program</li>
     *     <li>goes through the args given to the ArgsParser and assigns each parameter its argument, making it callable via flags</li>
     *     <li>checks if all mandatory parameters were given in the args
     * </ul>
     * <p>Directly handles any ArgsException by printing the message to the console than <strong>exiting the program!</strong></p>
     */
    public void parse() {

        try {
        parseUnchecked();

        } catch (CalledForHelpNotification help) {
            System.out.println(help.getMessage());
            System.exit(0);

        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

    /**
     * <ul>
     *     <li>checks if args is Empty</li>
     *     <li>checks if --help or -h was called on the program</li>
     *     <li>goes through the args given to the ArgsParser and assigns each parameter its argument, making it callable via flags</li>
     *     <li>checks if all mandatory parameters were given in the args
     * </ul>
     * @throws NoArgumentsProvidedArgsException if no arguments were provided in args and mandatory params were defined
     * @throws UnknownFlagArgsException if an unknown flag was provided in args
     * @throws TooManyArgumentsArgsException if more than one argument was provided to a single flag
     * @throws MissingArgArgsException if a flag was provided without an argument
     * @throws MandatoryArgNotProvidedArgsException if not all mandatory parameters were given in args
     * @throws CalledForHelpNotification if --help or -h was called
     * @throws InvalidArgTypeArgsException if the argument provided to a flag is not of the correct type
     */
    public void parseUnchecked() throws NoArgumentsProvidedArgsException, UnknownFlagArgsException,
            TooManyArgumentsArgsException, MissingArgArgsException, MandatoryArgNotProvidedArgsException,
            CalledForHelpNotification, InvalidArgTypeArgsException, IllegalStateException, FlagAlreadyProvidedArgsException {

        if(parseArgsWasCalled) throw new IllegalStateException(".parse() was already called!");

        parseArgsWasCalled = true;

        checkIfAnyArgumentsProvided();
        if (args.length > 0) {
            checkForHelpCall();
            Set<Parameter<?>> givenParameters = parseArguments();
            checkMandatoryArguments(givenParameters);
        }
    }

    /**
     * Checks if any arguments were provided to the program.
     * It allows the script
     * to run without any arguments as long as no mandatory parameters were defined on this ArgsParser.
     * @throws NoArgumentsProvidedArgsException if no arguments were provided in args
     */
    private void checkIfAnyArgumentsProvided() throws NoArgumentsProvidedArgsException {
        if (args.length == 0 & !mandatoryParameters.isEmpty()) {
            throw new NoArgumentsProvidedArgsException();
        }
    }

    /**
     * <ul>checks if --help or -h was called on the program, printing out help Strings for all parameters</ul>
     * <ul>checks if --help or -h was called for a specific parameter, printing out this parameters help string</ul>
     * @throws UnknownFlagArgsException if an unknown flag was provided in args
     * @throws CalledForHelpNotification if --help or -h was called
     */
    private void checkForHelpCall() throws UnknownFlagArgsException, CalledForHelpNotification {
        boolean oneArgProvided = args.length == 1;
        boolean twoArgsProvided = args.length == 2;
        boolean firstArgumentIsParameter = parameterMap.get(args[0]) != null;

        if (oneArgProvided && (args[0].equals("--help") || args[0].equals("-h"))) { // if --help or -h was called, the help is printed
            throw new CalledForHelpNotification(parameterMap, fullFlags, longestFlagSize, longestShortFlag);

        } else if (twoArgsProvided && (args[1].equals("--help") || args[1].equals("-h"))) {
            if (firstArgumentIsParameter) { // if the first argument is a parameter and --help follows,
                throw new CalledForHelpNotification(parameterMap, new LinkedList<>(Collections.singletonList(args[0])), longestFlagSize, longestShortFlag);

            } else { // if the first argument is not a parameter but --help was called,
                // the program notifies the user of an unknown parameter input
                throw new UnknownFlagArgsException(args[0], fullFlags, shortFlags);
            }
        }
    }

    /**
     * goes through all entries in args and creates a Parameter instance for each found flag.
     * @return a set of all Parameter instances created based on args
     * @throws UnknownFlagArgsException if an unknown flag was provided in args
     * @throws TooManyArgumentsArgsException if more than one argument was provided to a single flag
     * @throws MissingArgArgsException if a flag was provided without an argument
     * @throws InvalidArgTypeArgsException if the argument provided to a flag is not of the correct type
     */
    private Set<Parameter<?>> parseArguments() throws UnknownFlagArgsException, TooManyArgumentsArgsException,
            MissingArgArgsException, InvalidArgTypeArgsException, FlagAlreadyProvidedArgsException {
        Set<Parameter<?>> givenParameters = new HashSet<>();

        Parameter<?> currentParameter = null;
        boolean longFlagUsed = false;
        for (int i = 0; i < args.length; i++) {

            boolean currentPositionIsFlag = args[i].startsWith("-");
            if (currentPositionIsFlag) {
                currentParameter = parameterMap.get(args[i]);
                longFlagUsed = args[i].startsWith("--");
            }
            boolean flagExists = parameterMap.get(args[i]) != null;
            boolean isLastEntry = i == args.length - 1;
            boolean currentParameterNotNull = currentParameter != null;
            boolean argumentSet = currentParameterNotNull && currentParameter.hasArgument();
            boolean lastPositionWasFlag = i >= 1 && args[i - 1].startsWith("-");
            boolean flagAlreadyProvided = false;
            if (flagExists) flagAlreadyProvided = givenParameters.contains(currentParameter);

            if (currentPositionIsFlag && !flagExists) { // if flag is unknown
                throw new UnknownFlagArgsException(args[i], fullFlags, shortFlags);

            } else if (currentPositionIsFlag && flagAlreadyProvided) { // if the flag already was set
                throw new FlagAlreadyProvidedArgsException(currentParameter.getFullFlag(), currentParameter.getShortFlag());

            } else if (argumentSet && !currentPositionIsFlag) { // if two arguments are provided to a single flag
                throw new TooManyArgumentsArgsException(longFlagUsed ? currentParameter.getFullFlag() : currentParameter.getShortFlag());

            } else if (currentPositionIsFlag && lastPositionWasFlag) { // if a flag follows another flag
                throw new MissingArgArgsException(args[i - 1]);

            } else if (isLastEntry && currentPositionIsFlag) { //if last Flag has no argument
                throw new MissingArgArgsException(args[i]);

            }  else if (lastPositionWasFlag && currentParameterNotNull) { // if the current position is an argument

                boolean isArrayParam = arrayParameters.contains(args[i - 1]);
                if (isArrayParam) { // we "collect" all following arguments after an array parameter in a StringBuilder
                    StringBuilder arguments = new StringBuilder();
                    arguments.append(args[i]).append("==="); // every entry in the array gets seperated by ===
                    while(i + 1 < args.length && !args[i + 1].startsWith("-")) { // loop through all arguments
                        arguments.append(args[++i]).append("===");
                    }
                    currentParameter.setArgument(arguments.toString());

                } else {
                currentParameter.setArgument(args[i]);

                }
                givenParameters.add(currentParameter); // add parameter to the given Parameter Set
            }
        }

        return givenParameters;
    }

    /**
     * checks if all mandatory parameters were given in args!
     * @param givenParameters a set of all Parameter instances created based on args
     * @throws MandatoryArgNotProvidedArgsException if not all mandatory parameters were given in args
     */
    private void checkMandatoryArguments(Set<Parameter<?>> givenParameters) throws MandatoryArgNotProvidedArgsException {
        if (!givenParameters.containsAll(mandatoryParameters)) {
            mandatoryParameters.removeAll(givenParameters);
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Mandatory parameters are missing:");
            for (Parameter<?> param : mandatoryParameters) {
                errorMessage.append("\n").append(param.getFullFlag());
            }
            throw new MandatoryArgNotProvidedArgsException(errorMessage.toString());
        }
    }

    /**
     * getter method for the argument of a specific parameter
     * <p>Make sure T is of same type as given fullFlag parameter type! </p>
     * @param fullFlag name of the parameter
     * @param <T> type of the parameter
     * @return the argument of the parameter
     * @throws ClassCastException if the argument is not of the correct type
     */
    @SuppressWarnings("unchecked")
    public <T> T getArgumentOf(String fullFlag) throws ClassCastException {
        return (T) parameterMap.get(makeFlag(fullFlag, false)).getArgument();
    }
}
