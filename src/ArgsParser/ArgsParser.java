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
 *     <li>Define as many Parameters on a ArgsParser instance as needed by using {@link ArgsParser#addParameter(Parameter)}.
 *     This method takes a instance of any Parameter<?> type. There are several usage-ready child classes for the most
 *     common types used. There are also Array type Parameters for each equivalent:
 *
 *     <ul>
 *          <li><b>BolParameter / BolArrParameter</b> Parameter with Boolean type arguments.</li>
 *          <li><b>ChrParameter / ChrArrParameter</b> Parameter with Character type arguments</li>
 *          <li><b>DbParameter / DblArrParameter</b> Parameter with Double type arguments</li>
 *          <li><b>FltParameter / FltArrParameter</b> Parameter with Float type arguments</li>
 *          <li><b>IntParameter / IntArrParameter</b> Parameter with Integer type arguments</li>
 *          <li><b>PathParameter / PthArrParameter</b> Parameter with Path type arguments</li>
 *          <li><b>StrParameter / StrArrParameter</b> Parameter with String type arguments</li>
 *     </ul>
 *
 *     Each Parameter has several specified fields:
 *     <ul>
 *         <li>Parameters have a full flag name</li>
 *         <li>Parameters have a short version flag</li>
 *         <li>Parameters can have a description (hand "" or null if not needed)</li>
 *         <li>Parameters can be specified mandatory</li>
 *         <li>Parameters can have default values (which makes them automatically optional)</li>
 *     </ul>
 *     There are also Commands available to be defined which are just checked if they are provided or not:
 *     <ul>
 *         <li>{@link ArgsParser#addCommand(Command)}</li>
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
    private final LinkedList<Command[]> toggleList = new LinkedList<>();
    private final LinkedList<String> commandsInDefinitionOrder = new LinkedList<>();
    private final LinkedList<String> flagsInDefinitionOrder = new LinkedList<>();
    protected boolean parseArgsWasCalled = false;
    private int longestFullFlagSize = 0;
    private int longestShortFlagSize = 0;

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
     * Formats a flag string to ensure it conforms to the required syntax for command-line arguments.
     * <p>
     * This method removes any leading dashes (`-`) from the input flag and appends the appropriate prefix:
     * <ul>
     *     <li>For full flags (e.g., `--example`), it ensures two leading dashes (`--`).</li>
     *     <li>For short flags (e.g., `-e`), it ensures one leading dash (`-`).</li>
     * </ul>
     * <p>
     * If the input flag is already correctly formatted, it will be returned unchanged.
     * </p>
     *
     * <h3>Examples:</h3>
     * <pre>
     * makeFlag("example", false)  → "--example"
     * makeFlag("--example", false) → "--example"
     * makeFlag("e", true) → "-e"
     * makeFlag("-e", true) → "-e"
     * </pre>
     *
     * @param flag of a Parameter
     * @param isShortName true if the flag is a shortFlag (false if fullFlag)
     * @return flag in the correct format (e.g. --flag or -f)
     */
    protected static String makeFlag(String flag, boolean isShortName) {
        flag = flag.replaceFirst("^-+", "");
        return isShortName ? "-" + flag : "--" + flag;
    }

    /**
     * Validates whether the provided flag names are valid, unique, and not reserved.
     * <p>
     * This method performs multiple checks to ensure that the given full and short flag names:
     * </p>
     * <ul>
     *     <li><b>Are not empty:</b> Both full and short flag names must be non-empty strings.</li>
     *     <li><b>Are unique:</b> Neither flag name must already exist in the {@code parameterMap}.</li>
     *     <li><b>Are not reserved:</b> Reserved flag names such as `--help`, `--h`, `-h`, and `-help` cannot be used.</li>
     * </ul>
     * <p>
     * If any of these conditions are violated, an {@link IllegalArgumentException} will be thrown.
     * </p>
     *
     * <h3>Examples:</h3>
     * <pre>
     * checkReservedFlags("--example", "-e"); // Valid
     * checkReservedFlags("--help", "-h");    // Throws IllegalArgumentException
     * </pre>
     *
     * @param fullVersion The full version of the flag (e.g., "--example").
     * @param shortVersion The short version of the flag (e.g., "-e").
     * @throws IllegalArgumentException if:
     *         <ul>
     *             <li>Either flag name is empty.</li>
     *             <li>Either flag name already exists in {@code parameterMap}.</li>
     *             <li>Either flag name matches a reserved flag (`--help`, `--h`, `-h`, `-help`).</li>
     *         </ul>
     */
    protected void checkReservedFlags(String fullVersion, String shortVersion) {
        if (fullVersion.isEmpty() || shortVersion.isEmpty()) {
            throw new IllegalArgumentException("No empty strings allowed for flags!");
        }
        if (parameterMap.containsKey(fullVersion)) {
            throw new IllegalArgumentException("Flag already exists: " + fullVersion);
        }
        if (parameterMap.containsKey(shortVersion)) {
            throw new IllegalArgumentException("Flag already exists: " + shortVersion);
        }
        if (fullVersion.equals("--help") || fullVersion.equals("--h") ||
                shortVersion.equals("-h") || shortVersion.equals("-help")) {
            throw new IllegalArgumentException("--help/-h is reserved!");
        }
    }

    /**
     * Updates the maximum lengths of the full and short flag names for consistent formatting in help text.
     * <p>
     * This method compares the lengths of the provided full and short flag names with the current
     * maximum values (`longestFlagSize` and `longestShortFlag`) and updates them if the new flag names
     * are longer. This ensures proper alignment when displaying flags in help messages or documentation.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Compares the length of the provided full flag name with the current maximum length (`longestFlagSize`).</li>
     *     <li>Updates `longestFlagSize` if the new full flag is longer.</li>
     *     <li>Compares the length of the provided short flag name with the current maximum length (`longestShortFlag`).</li>
     *     <li>Updates `longestShortFlag` if the new short flag is longer.</li>
     * </ul>
     *
     * @param fullVersion The full flag name (e.g., "--example").
     * @param shortVersion The short flag name (e.g., "-e").
     */
    protected void setNameSizes(String fullVersion, String shortVersion) {
        int nameSize = fullVersion.length();
        if (longestFullFlagSize < nameSize) longestFullFlagSize = nameSize;

        int shortSize = shortVersion.length();
        if (longestShortFlagSize < shortSize) longestShortFlagSize = shortSize;
    }


    /**
     * Adds a parameter to the parser after validating its flags, uniqueness, and reservation status.
     * <p>
     * This method ensures that the provided parameter has valid flag names, is not already registered,
     * and doesn't conflict with reserved flags such as `--help` or `-h`.
     * </p>
     *
     * <h2>Behavior:</h2>
     * <ul>
     *     <li>Validates that the parameter's full and short flags are non-empty and unique.</li>
     *     <li>Ensures reserved flags (`--help`, `-h`) are not being reused.</li>
     *     <li>Adds the parameter to internal maps for retrieval and validation.</li>
     *     <li>Keeps track of the flag's length for consistent help text formatting.</li>
     *     <li>Registers the parameter in mandatory or array-specific sets if applicable.</li>
     * </ul>
     *
     * @param <T> The type of the parameter being added, extending {@link Parameter}.
     * @param parameter The parameter object to be added to the parser.
     * @return The same parameter instance that was added.
     * @throws IllegalArgumentException If the flag names are empty, duplicate, or reserved.
     */
    public <T extends Parameter<?>> T addParameter(T parameter) {

        // check if the flag names are already used / empty or reserved
        checkReservedFlags(parameter.getFullFlag(), parameter.getShortFlag());

        // set name sizes
        setNameSizes(parameter.getFullFlag(), parameter.getShortFlag());

        // add parameter to parameterMap
        parameterMap.put(parameter.getFullFlag(), parameter);
        parameterMap.put(parameter.getShortFlag(), parameter);

        // add fullFlag to definition order
        flagsInDefinitionOrder.add(parameter.getFullFlag());

        // add this parser to the parameter for .parseWasCalled() check
        parameter.setParser(this);

        // add parameter to arrayParameters if isArray
        if (parameter.isArray()) {
            arrayParameters.add(parameter.getFullFlag());
            arrayParameters.add(parameter.getShortFlag());
        }

        // add to mandatory parameters if parameter is mandatory
        if (parameter.isMandatory()) mandatoryParameters.add(parameter);

        return parameter;
    }

    // Command

    /**
     * Adds a new command with its full name, short name, and description to the existing command set.
     *
     * @param command the {@link Command} to add to this parser.
     * @return The added {@link Command} object.
     * @throws IllegalArgumentException If the fullCommandName or shortCommandName are already defined, empty, or reserved (--help/-h)
     */
    public Command addCommand(Command command) {

        // check for reserved flags
        checkReservedFlags(command.getFullCommandName(), command.getShortCommandName());

        // set name sizes
        setNameSizes(command.getFullCommandName(), command.getShortCommandName());

        // add to commandMap
        commandMap.put(command.getFullCommandName(), command);
        commandMap.put(command.getShortCommandName(), command);

        // add to definition order
        commandsInDefinitionOrder.add(command.getFullCommandName());

        // add this parser to the command for .parseWasCalled() check
        command.setArgsParser(this);

        return command;
    }

    /**
     * When parse is called, each toggle gets checked so that only ONE Command
     * inside the toggle was provided.
     * This means that this method prevents the combination of given commands!
     * @param commands commands that cannot be combined
     */
    public void toggle(Command... commands) {
        toggleList.add(commands);
        for (Command command : commands) {
            command.setToggle(commands);
        }
    }


    // parsing functions


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
     * @throws NotExistingPathArgsException if a PthParameter with pathCheck was handed a non-existing path
     */
    public void parseUnchecked(String[] args) throws NoArgumentsProvidedArgsException, UnknownFlagArgsException,
            TooManyArgumentsArgsException, MissingArgArgsException, MandatoryArgNotProvidedArgsException,
            CalledForHelpNotification, InvalidArgTypeArgsException, IllegalStateException,
            FlagAlreadyProvidedArgsException, HelpAtWrongPositionArgsException,
            IllegalArgumentException, ToggleArgsException, NotExistingPathArgsException {

        if (args == null) throw new IllegalArgumentException("Args cannot be null!");
        if(parseArgsWasCalled) throw new IllegalStateException(".parse() was already called!");

        parseArgsWasCalled = true;

        checkIfAnyArgumentsProvided(args);
        if (args.length > 0) {
            checkForHelpCall(args);
            Set<Parameter<?>> givenParameters = parseArguments(args);
            checkMandatoryArguments(givenParameters);
            checkToggles();
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
                                                longestFullFlagSize, longestShortFlagSize);

        } else if (twoArgsProvided && (args[1].equals("--help") || args[1].equals("-h"))) {
            if (firstArgumentIsParameter) { // if the first argument is a parameter and --help follows,
                throw new CalledForHelpNotification(parameterMap, Collections.singletonList(args[0]),
                                                    commandMap, new LinkedList<>(),
                                                    longestFullFlagSize, longestShortFlagSize);

            } else if (firstArgumentIsCommand) { // if the first argument is a command and --help follows
                throw new CalledForHelpNotification(parameterMap, new LinkedList<>(),
                                                    commandMap, Collections.singletonList(args[0]),
                                                    longestFullFlagSize, longestShortFlagSize);

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
     * @throws NotExistingPathArgsException if a PthParameter with pathCheck was handed a non-existing path
     */
    private Set<Parameter<?>> parseArguments(String[] args) throws UnknownFlagArgsException, TooManyArgumentsArgsException,
            MissingArgArgsException, InvalidArgTypeArgsException, FlagAlreadyProvidedArgsException,
            HelpAtWrongPositionArgsException, NotExistingPathArgsException {

        Set<Parameter<?>> givenParameters = new HashSet<>();

        //check if the first argument provided is actually a flag or command
        if (args.length > 0 && parameterMap.get(args[0]) == null && commandMap.get(args[0]) == null &&
                !(args[0].equals("--help") || args[0].equals("-h"))) {
            throw new UnknownFlagArgsException(args[0], parameterMap.keySet(), commandMap.keySet(), true);
        }

        Parameter<?> currentParameter = null;
        boolean longFlagUsed = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            boolean currentPositionIsFlag = arg.startsWith("-");
            if (currentPositionIsFlag) {
                currentParameter = parameterMap.get(arg);
                longFlagUsed = arg.startsWith("--");
            }
            boolean currentPositionIsCommand = commandMap.get(arg) != null;
            boolean flagExists = parameterMap.get(arg) != null;
            boolean isLastEntry = i == args.length - 1;
            boolean currentParameterNotNull = currentParameter != null;
            boolean argumentSet = currentParameterNotNull && currentParameter.readArgument() != null;
            boolean lastPositionWasFlag = i >= 1 && args[i - 1].startsWith("-");
            boolean flagAlreadyProvided = false;
            if (flagExists) flagAlreadyProvided = givenParameters.contains(currentParameter);
            boolean isHelpCall = ("--help".equals(arg) || "-h".equals(arg));
            boolean helpCallInWrongPosition = isHelpCall && (i > 1 || (i == 0 && args.length == 2));

            if (helpCallInWrongPosition) {
                throw new HelpAtWrongPositionArgsException();

            } else if (currentPositionIsFlag && !flagExists) { // if flag is unknown
                throw new UnknownFlagArgsException(arg, parameterMap.keySet(), commandMap.keySet(), false);

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
                throw new MissingArgArgsException(arg);

            } else if (currentPositionIsCommand) { // if current position is a command
                commandMap.get(arg).setCommand(); // set the command to true

            } else if (lastPositionWasFlag && currentParameterNotNull) { // if the current position is an argument

                boolean isArrayParam = arrayParameters.contains(args[i - 1]);
                if (isArrayParam) { // "collect" all following arguments after an array parameter in a StringBuilder
                    currentParameter.setArgument(args[i]);
                    while(i + 1 < args.length && !args[i + 1].startsWith("-") && !commandMap.containsKey(args[i + 1])) { // loop through all arguments
                        currentParameter.setArgument(args[++i]);
                    }

                } else {
                    currentParameter.setArgument(arg);

                }
                currentParameter.setProvided();
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

    private void checkToggles() throws ToggleArgsException{
        for (Command[] toggle : toggleList) {

            // if more than two commands in one toggle are provided, a ToggleArgsException is thrown
            int numberOfProvidedCommands = 0;
            for (Command command : toggle) {
                if (command.isProvided()) numberOfProvidedCommands++;
                if (numberOfProvidedCommands > 1) throw new ToggleArgsException(toggle);
            }
        }
    }


    // external getters


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
