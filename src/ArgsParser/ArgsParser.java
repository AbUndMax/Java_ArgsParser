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
 *     There are also Commands available to be defined which are just checked if they are provided or not:
 *     <ul>
 *         <li>{@link ArgsParser#addCommand(String, String, String)}</li>
 *     </ul>
 *
 *     <li>After all parameters are added, the {@link ArgsParser#parse(String[] args)} method has to be called! (this is mandatory!)</li>
 *     <li>Then the arguments can be accessed by using {@link Parameter#getArgument()} on the specific Parameter variable
 *          which will return the parsed argument of that parameter as the specified type or for commands by using the
 *          {@link Command#isProvided()} at the respective command instance to check rather the command was provided or not</li>
 * </ol>
 * available at: <a href="https://github.com/AbUndMax/Java_ArgsParser">GitHub</a>
 * @author Niklas Max G. 2024
 */
public class ArgsParser {

    private final Map<String, Parameter<?>> parameterMap = new HashMap<>();
    private final Map<String, Command> commandMap = new HashMap<>();
    private final Set<Parameter<?>> mandatoryParameters = new HashSet<>();
    private final Set<String> arrayParameters = new HashSet<>();
    private final LinkedList<String> commandsInDefinitionOrder = new LinkedList<>();
    private final LinkedList<String> flagsInDefinitionOrder = new LinkedList<>();
    protected boolean parseArgsWasCalled = false;
    private int longestFlagSize = 0;
    private int longestShortFlag = 0;

    /**
     * Constructor
     * @throws IllegalArgumentException if args is null
     */
    public ArgsParser() throws IllegalArgumentException {
    }

    /**
     * checks if the parse method was called
     * @return true if the parse method was called, false otherwise
     */
    protected boolean parseArgsWasCalled() {
        return parseArgsWasCalled;
    }

    /**
     * Creates a new Parameter object with the given attributes and adds it to the parameter map.
     *
     * @param fullFlag The full version of the flag (e.g., --example).
     * @param shortFlag The short version of the flag (e.g., -e).
     * @param description A brief description of what the parameter represents.
     * @param type The data type of the parameter's value.
     * @param isMandatory Indicates if the parameter is mandatory.
     * @param defaultValue The default value for the parameter, hand null for no default.
     * @return The newly created Parameter object.
     * @throws IllegalArgumentException If the flag names are already used or reserved (--help/-h).
     */
    private <T> Parameter<T> createParameter(String fullFlag,
                                             String shortFlag,
                                             String description,
                                             Class<T> type,
                                             boolean isMandatory,
                                             T defaultValue) throws IllegalArgumentException {
        fullFlag = makeFlag(fullFlag, false);
        shortFlag = makeFlag(shortFlag, true);

        // check if the flag names are already used / empty or reserved
        if (fullFlag.isEmpty() || shortFlag.isEmpty()) {
            throw new IllegalArgumentException("No empty strings allowed for flags!");
        }
        if (parameterMap.containsKey(fullFlag)) {
            throw new IllegalArgumentException("Flag already exists: " + fullFlag);
        }
        if (parameterMap.containsKey(shortFlag)) {
            throw new IllegalArgumentException("Flag already exists: " + shortFlag);
        }
        if (fullFlag.equals("--help") || fullFlag.equals("--h") ||
                shortFlag.equals("-h") || shortFlag.equals("-help")) {
            throw new IllegalArgumentException("--help/-h is reserved!");
        }

        // create new parameter instance
        Parameter<T> parameter = new Parameter<T>(fullFlag, shortFlag, description, type, isMandatory, this);

        if (defaultValue != null) {
            parameter.setDefault(defaultValue);
        }

        // add parameter to the map
        parameterMap.put(parameter.getFullFlag(), parameter);
        parameterMap.put(parameter.getShortFlag(), parameter);

        flagsInDefinitionOrder.add(fullFlag);

        // check for the lengths of the name
        int nameSize = parameter.getFullFlag().length();
        if (longestFlagSize < nameSize) longestFlagSize = nameSize;

        int shortSize = parameter.getShortFlag().length();
        if (longestShortFlag < shortSize) longestShortFlag = shortSize;

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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<String> addMandatoryStringParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, String.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type String
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<String> addDefaultStringParameter(String fullFlag, String shortFlag,
                                                       String description, String defaultValue)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, String.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameterhand empty string "" or null if not needed
     * @return the created Parameter instance of type String
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<String> addOptionalStringParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<String[]> addStringArrayParameter(String fullFlag, String shortFlag,
                                                       String description, boolean isMandatory)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<String[]> addDefaultStringArrayParameter(String fullFlag, String shortFlag,
                                                              String description, String[] defaultValue)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Integer> addMandatoryIntegerParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, Integer.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Integer
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Integer> addDefaultIntegerParameter(String fullFlag, String shortFlag,
                                                         String description, Integer defaultValue)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, Integer.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Integer
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Integer> addOptionalIntegerParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Integer[]> addIntegerArrayParameter(String fullFlag, String shortFlag,
                                                         String description, boolean isMandatory)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Integer[]> addDefaultIntegerArrayParameter(String fullFlag, String shortFlag,
                                                                String description, Integer[] defaultValue)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Double> addMandatoryDoubleParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, Double.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Double
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Double> addDefaultDoubleParameter(String fullFlag, String shortFlag,
                                                       String description, Double defaultValue)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, Double.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Double
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Double> addOptionalDoubleParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Double[]> addDoubleArrayParameter(String fullFlag, String shortFlag,
                                                       String description, boolean isMandatory)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Double[]> addDefaultDoubleArrayParameter(String fullFlag, String shortFlag,
                                                              String description, Double[] defaultValue)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Boolean> addMandatoryBooleanParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, Boolean.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Boolean
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Boolean> addDefaultBooleanParameter(String fullFlag, String shortFlag,
                                                         String description, Boolean defaultValue)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, Boolean.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Boolean
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Boolean> addOptionalBooleanParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Boolean[]> addBooleanArrayParameter(String fullFlag, String shortFlag,
                                                         String description, boolean isMandatory)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Boolean[]> addDefaultBooleanArrayParameter(String fullFlag, String shortFlag,
                                                                String description, Boolean[] defaultValue)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Character> addMandatoryCharacterParameter(String fullFlag, String shortFlag,
                                                               String description) throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, Character.class, true, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Character
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Character> addDefaultCharacterParameter(String fullFlag, String shortFlag,
                                                             String description, Character defaultValue)
            throws IllegalArgumentException {
        return createParameter(fullFlag, shortFlag, description, Character.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter, hand empty string "" or null if not needed
     * @return the created Parameter instance of type Character
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Character> addOptionalCharacterParameter(String fullFlag, String shortFlag, String description)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Character[]> addCharacterArrayParameter(String fullFlag, String shortFlag,
                                                             String description, boolean isMandatory)
            throws IllegalArgumentException {
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
     * @throws IllegalArgumentException if the flag names are already defined, empty, or reserved (--help/-h)
     */
    public Parameter<Character[]> addDefaultCharacterArrayParameter(String fullFlag, String shortFlag,
                                                                    String description, Character[] defaultValue)
            throws IllegalArgumentException {
        arrayParameters.add(makeFlag(fullFlag, false));
        arrayParameters.add(makeFlag(shortFlag, true));
        return createParameter(fullFlag, shortFlag, description, Character[].class, false, defaultValue);
    }


    // Command


    /**
     * Adds a new command with its full name, short name, and description to the existing command set.
     *
     * @param fullCommandName The full name of the command, used as the primary identifier.
     * @param shortCommandName The abbreviated name of the command, used as a shorthand identifier.
     * @param description A brief description of what the command does.
     * @return The newly created and added Command object.
     * @throws IllegalArgumentException If the fullCommandName or shortCommandName are already defined, empty, or reserved (--help/-h)
     */
    public Command addCommand(String fullCommandName, String shortCommandName, String description) {

        if (fullCommandName.isEmpty() || shortCommandName.isEmpty()) {
            throw new IllegalArgumentException("No empty strings allowed for flags!");
        }
        if (commandMap.containsKey(fullCommandName)) {
            throw new IllegalArgumentException("Command name already exists: " + fullCommandName);
        }
        if (commandMap.containsKey(shortCommandName)) {
            throw new IllegalArgumentException("Command name already exists: " + shortCommandName);
        }
        if (fullCommandName.equals("--help") || fullCommandName.equals("-h") ||
                shortCommandName.equals("--help") || shortCommandName.equals("-h")) {
            throw new IllegalArgumentException("--help/-h is reserved!");
        }

        Command command = new Command(fullCommandName, shortCommandName, description, this);

        int nameSize = fullCommandName.length();
        if (longestFlagSize < nameSize) longestFlagSize = nameSize;

        int shortSize = shortCommandName.length();
        if (longestShortFlag < shortSize) longestShortFlag = shortSize;

        commandMap.put(fullCommandName, command);
        commandMap.put(shortCommandName, command);

        commandsInDefinitionOrder.add(fullCommandName);

        return command;
    }


    /**
     * Parses the provided input by invoking the parseUnchecked method, and
     * handles Help calls or ArgsExceptions that may occur during the parsing process.
     *
     * If a CalledForHelpNotification exception is thrown, its message is
     * printed to the standard output and the application exits with a
     * status code of 0.
     *
     * If an ArgsException is thrown, its message is printed to the
     * standard output and the application exits with a status code of 1.
     * @throws IllegalArgumentException if args is null
     */
    public void parse(String[] args) throws IllegalArgumentException {

        try {
            parseUnchecked(args);

        } catch (CalledForHelpNotification help) {
            System.out.println(help.getMessage());
            System.exit(0);

        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

    /**
     * Parses command-line arguments and performs various checks to ensure
     * correctness and validity of the provided arguments. If any issues are
     * detected during the parsing process, specific exceptions are thrown
     * to indicate what went wrong.
     *
     * @throws NoArgumentsProvidedArgsException if no command-line arguments are provided.
     * @throws UnknownFlagArgsException if an unknown flag is encountered in the arguments.
     * @throws TooManyArgumentsArgsException if too many arguments are provided.
     * @throws MissingArgArgsException if an expected argument is missing.
     * @throws MandatoryArgNotProvidedArgsException if a mandatory argument is not provided.
     * @throws CalledForHelpNotification if the argument for help (-help or --help) is included.
     * @throws InvalidArgTypeArgsException if an argument is of invalid type.
     * @throws IllegalStateException if the .parse() method is called more than once.
     * @throws FlagAlreadyProvidedArgsException if a flag is provided more than once.
     * @throws HelpAtWrongPositionArgsException if the help argument is positioned incorrectly.
     * @throws IllegalArgumentException if args is null
     */
    public void parseUnchecked(String[] args) throws NoArgumentsProvidedArgsException, UnknownFlagArgsException,
            TooManyArgumentsArgsException, MissingArgArgsException, MandatoryArgNotProvidedArgsException,
            CalledForHelpNotification, InvalidArgTypeArgsException, IllegalStateException,
            FlagAlreadyProvidedArgsException, HelpAtWrongPositionArgsException, IllegalArgumentException {

        if (args == null) throw new IllegalArgumentException("Args cannot be null!");
        if(parseArgsWasCalled) throw new IllegalStateException(".parse() was already called!");

        parseArgsWasCalled = true;

        checkIfAnyArgumentsProvided(args);
        if (args.length > 0) {
            checkForHelpCall(args);
            Set<Parameter<?>> givenParameters = parseArguments(args);
            checkMandatoryArguments(givenParameters);
        }
    }

    /**
     * Checks if any arguments were provided to the program.
     * It allows the script
     * to run without any arguments as long as no mandatory parameters were defined on this ArgsParser.
     * @throws NoArgumentsProvidedArgsException if no arguments were provided in args
     */
    private void checkIfAnyArgumentsProvided(String[] args) throws NoArgumentsProvidedArgsException {
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
    private void checkForHelpCall(String[] args) throws UnknownFlagArgsException, CalledForHelpNotification {
        boolean oneArgProvided = args.length == 1;
        boolean twoArgsProvided = args.length == 2;
        boolean firstArgumentIsParameter = parameterMap.get(args[0]) != null;
        boolean firstArgumentIsCommand = commandMap.get(args[0]) != null;

        if (oneArgProvided && (args[0].equals("--help") || args[0].equals("-h"))) { // if --help or -h was called, the help is printed
            throw new CalledForHelpNotification(parameterMap, flagsInDefinitionOrder,
                                                commandMap, commandsInDefinitionOrder,
                                                longestFlagSize, longestShortFlag);

        } else if (twoArgsProvided && (args[1].equals("--help") || args[1].equals("-h"))) {
            if (firstArgumentIsParameter) { // if the first argument is a parameter and --help follows,
                throw new CalledForHelpNotification(parameterMap, Collections.singletonList(args[0]),
                                                    commandMap, new LinkedList<>(),
                                                    longestFlagSize, longestShortFlag);

            } else if (firstArgumentIsCommand) { // if the first argument is a command and --help follows
                throw new CalledForHelpNotification(parameterMap, new LinkedList<>(),
                                                    commandMap, Collections.singletonList(args[0]),
                                                    longestFlagSize, longestShortFlag);

            } else { // if the first argument is not a parameter but --help was called,
                // the program notifies the user of an unknown parameter input
                throw new UnknownFlagArgsException(args[0], parameterMap.keySet(), commandMap.keySet(), false);

            }
        }
    }

    /**
     * Parses the command-line arguments and returns a set of parameters that were provided.
     * The method checks for various conditions such as unknown flags, duplicate flags,
     * missing arguments, and invalid argument types, and validates the correct placement of help flags.
     *
     * @return A set of {@code Parameter<?>} objects representing the parsed arguments.
     * @throws UnknownFlagArgsException If an unrecognized flag is encountered.
     * @throws TooManyArgumentsArgsException If a flag receives more than one argument.
     * @throws MissingArgArgsException If a flag is missing its expected argument.
     * @throws InvalidArgTypeArgsException If an argument type is invalid.
     * @throws FlagAlreadyProvidedArgsException If a flag is provided more than once.
     * @throws HelpAtWrongPositionArgsException If the help flag is not in the correct position.
     */
    private Set<Parameter<?>> parseArguments(String[] args) throws UnknownFlagArgsException, TooManyArgumentsArgsException,
            MissingArgArgsException, InvalidArgTypeArgsException, FlagAlreadyProvidedArgsException,
            HelpAtWrongPositionArgsException {

        Set<Parameter<?>> givenParameters = new HashSet<>();

        //check if the first argument provided is actually a flag or command
        if (args.length > 0 && parameterMap.get(args[0]) == null && commandMap.get(args[0]) == null &&
                !(args[0].equals("--help") || args[0].equals("-h"))) {
            throw new UnknownFlagArgsException(args[0], parameterMap.keySet(), commandMap.keySet(), true);
        }

        Parameter<?> currentParameter = null;
        boolean longFlagUsed = false;
        for (int i = 0; i < args.length; i++) {

            boolean currentPositionIsFlag = args[i].startsWith("-");
            if (currentPositionIsFlag) {
                currentParameter = parameterMap.get(args[i]);
                longFlagUsed = args[i].startsWith("--");
            }
            boolean currentPositionIsCommand = commandMap.get(args[i]) != null;
            boolean flagExists = parameterMap.get(args[i]) != null;
            boolean isLastEntry = i == args.length - 1;
            boolean currentParameterNotNull = currentParameter != null;
            boolean argumentSet = currentParameterNotNull && currentParameter.hasArgument();
            boolean lastPositionWasFlag = i >= 1 && args[i - 1].startsWith("-");
            boolean flagAlreadyProvided = false;
            if (flagExists) flagAlreadyProvided = givenParameters.contains(currentParameter);
            boolean isHelpCall = ("--help".equals(args[i]) || "-h".equals(args[i]));
            boolean helpCallInWrongPosition = isHelpCall && (i > 1 || (i == 0 && args.length == 2));

            if (helpCallInWrongPosition) {
                throw new HelpAtWrongPositionArgsException();

            } else if (currentPositionIsFlag && !flagExists) { // if flag is unknown
                throw new UnknownFlagArgsException(args[i], parameterMap.keySet(), commandMap.keySet(), false);

            } else if (currentPositionIsFlag && flagAlreadyProvided) { // if the flag already was set
                throw new FlagAlreadyProvidedArgsException(currentParameter.getFullFlag(),
                                                           currentParameter.getShortFlag());

            } else if (argumentSet && !currentPositionIsFlag && !currentPositionIsCommand) { // if two arguments are provided to a single flag
                throw new TooManyArgumentsArgsException(longFlagUsed ?
                                                                currentParameter.getFullFlag() :
                                                                currentParameter.getShortFlag());

            } else if (currentPositionIsFlag && lastPositionWasFlag) { // if a flag follows another flag
                throw new MissingArgArgsException(args[i - 1]);

            } else if (isLastEntry && currentPositionIsFlag) { //if last Flag has no argument
                throw new MissingArgArgsException(args[i]);

            } else if (currentPositionIsCommand) { // if current position is a command
                commandMap.get(args[i]).setCommand(); // set the command to true

            } else if (lastPositionWasFlag && currentParameterNotNull) { // if the current position is an argument

                boolean isArrayParam = arrayParameters.contains(args[i - 1]);
                if (isArrayParam) { // we "collect" all following arguments after an array parameter in a StringBuilder
                    StringBuilder arguments = new StringBuilder();
                    arguments.append(args[i]).append("==="); // every entry in the array gets seperated by ===
                    while(i + 1 < args.length && !args[i + 1].startsWith("-") && !commandMap.containsKey(args[i + 1])) { // loop through all arguments
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
     * @throws IllegalArgumentException if the provided flag is not defined on this parser instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getArgumentOf(String fullFlag) throws ClassCastException, IllegalArgumentException {
        if (parameterMap.get(fullFlag) == null) {
            throw new IllegalArgumentException("Parameter '" + fullFlag + "' not defined");
        }
        return (T) parameterMap.get(makeFlag(fullFlag, false)).getArgument();
    }

    /**
     * Checks if the specified command is provided.
     *
     * @param fullCommandName the full name of the command to be checked
     * @return true if the command is provided, false otherwise
     * @throws IllegalArgumentException if the command is not defined on this parser instance
     */
    public boolean checkIfCommandIsProvided(String fullCommandName) {
        if (commandMap.get(fullCommandName) == null) {
            throw new IllegalArgumentException("Command '" + fullCommandName + "' not defined");
        }
        return commandMap.get(fullCommandName).isProvided();
    }
}
