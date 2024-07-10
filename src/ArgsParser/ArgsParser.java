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

import ArgsParser.ArgsExceptions.*;
import java.util.*;

/**
 * Class to parse arguments given in the command line, the tool checks for several conditions:
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
 *     <li>The ArgsParser constructor {@link #ArgsParser(String[])} is called and the args array of the main method provided.</li>
 *     <li>Now we can specify the parameters we want to have for the program by using {@link #addStringParameter(String, String, String, boolean)}</li>
       <ul>
 *         <li>Parameters can be mandatory or optional</li>
 *         <li>Parameters can have a short version flags</li>
 *         <li>Parameters can have a description</li>
 *         <li>Parameters can be of type String, Integer, Double, Boolean or Character</li>
 *     </ul>
 *     <li>After all parameters are added, the {@link #parseArgs()} method has to be called! (this is mandatory!)</li>
 *     <li>Then the arguments can be accessed by using {@link Parameter#getArgument()} on the specific Parameter variable
 *          and type specific arguments can be accessed if a type i.e. Integer.Class was provided in {@link #addStringParameter(String, String, String, boolean)} </li>
 * </ol>
 * available at: <a href="https://github.com/AbUndMax/Java_ArgsParser">GitHub</a>
 * @author Niklas Max G. 2024
 */
public class ArgsParser {

    private final String[] args;
    private final Map<String, Parameter<?>> parameterMap = new HashMap<>();
    private final Set<Parameter<?>> mandatoryParameters = new HashSet<>();
    private final Set<String> fullFlags = new HashSet<>();
    private final Set<String> shortFlags = new HashSet<>();
    protected boolean parseArgsWasCalled = false;
    private int longestFlagSize = 0;
    private int longestShortFlag = 0;

    /**
     * Constructor demands the args array of the main method to be passed.
     * @param args args array of type String[] from the main method
     */
    public ArgsParser(String[] args) {
        this.args = args;
    }

    /**
     * creates a new Parameter instance and sets it accordingly with the given fields
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

        // check if the flag names are already used
        if (fullFlags.contains(fullFlag)) throw new IllegalArgumentException("Flag already exists: " + fullFlag);
        if (shortFlags.contains(shortFlag)) throw new IllegalArgumentException("Flag already exists: " + shortFlag);

        // create new parameter instance
        Parameter<T> parameter = new Parameter<T>(makeFlag(fullFlag, false),
                                                  makeFlag(shortFlag, true),
                                                  description, type, isMandatory, this);

        if (defaultValue != null) {
            parameter.setDefault(defaultValue);
        }

        // add parameter to the map
        parameterMap.put(parameter.getFullFlag(), parameter);
        if (parameter.getShortFlag() != null) parameterMap.put(parameter.getShortFlag(), parameter);

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
     * @param description description of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type String
     */
    public Parameter<String> addStringParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, description, String.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type String
     */
    public Parameter<String> addStringParameter(String fullFlag, String shortFlag, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, null, String.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type String
     */
    public Parameter<String> addStringParameter(String fullFlag, String shortFlag, String description, String defaultValue) {
        return createParameter(fullFlag, shortFlag, description, String.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type String
     */
    public Parameter<String> addStringParameter(String fullFlag, String shortFlag, String defaultValue) {
        return createParameter(fullFlag, shortFlag, null, String.class, false, defaultValue);
    }


    // Integer Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type Integer
     */
    public Parameter<Integer> addIntegerParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, description, Integer.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type Integer
     */
    public Parameter<Integer> addIntegerParameter(String fullFlag, String shortFlag, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, null, Integer.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Integer
     */
    public Parameter<Integer> addIntegerParameter(String fullFlag, String shortFlag, String description, Integer defaultValue) {
        return createParameter(fullFlag, shortFlag, description, Integer.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Integer
     */
    public Parameter<Integer> addIntegerParameter(String fullFlag, String shortFlag, Integer defaultValue) {
        return createParameter(fullFlag, shortFlag, null, Integer.class, false, defaultValue);
    }


    // Double Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type Double
     */
    public Parameter<Double> addDoubleParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, description, Double.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type Double
     */
    public Parameter<Double> addDoubleParameter(String fullFlag, String shortFlag, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, null, Double.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Double
     */
    public Parameter<Double> addDoubleParameter(String fullFlag, String shortFlag, String description, Double defaultValue) {
        return createParameter(fullFlag, shortFlag, description, Double.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Double
     */
    public Parameter<Double> addDoubleParameter(String fullFlag, String shortFlag, Double defaultValue) {
        return createParameter(fullFlag, shortFlag, null, Double.class, false, defaultValue);
    }


    // Boolean Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type Boolean
     */
    public Parameter<Boolean> addBooleanParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, description, Boolean.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type Boolean
     */
    public Parameter<Boolean> addBooleanParameter(String fullFlag, String shortFlag, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, null, Boolean.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Boolean
     */
    public Parameter<Boolean> addBooleanParameter(Boolean defaultValue, String fullFlag, String shortFlag, String description) {
        return createParameter(fullFlag, shortFlag, description, Boolean.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Boolean
     */
    public Parameter<Boolean> addBooleanParameter(Boolean defaultValue, String fullFlag, String shortFlag) {
        return createParameter(fullFlag, shortFlag, null, Boolean.class, false, defaultValue);
    }


    // Character Parameter constructors


    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type Character
     */
    public Parameter<Character> addCharacterParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, description, Character.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param isMandatory true if parameter is mandatory, false if optional
     * @return the created Parameter instance of type Character
     */
    public Parameter<Character> addCharacterParameter(String fullFlag, String shortFlag, boolean isMandatory) {
        return createParameter(fullFlag, shortFlag, null, Character.class, isMandatory, null);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param description description of the parameter
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Character
     */
    public Parameter<Character> addCharacterParameter(String fullFlag, String shortFlag, String description, Character defaultValue) {
        return createParameter(fullFlag, shortFlag, description, Character.class, false, defaultValue);
    }

    /**
     * Adds a new parameter that will be checked in args and assigned to the Parameter instance
     * @param fullFlag name of the parameter (-- will automatically be added)
     * @param shortFlag short version of the parameter (- will automatically be added)
     * @param defaultValue default value of the parameter
     * @return the created Parameter instance of type Character
     */
    public Parameter<Character> addCharacterParameter(String fullFlag, String shortFlag, Character defaultValue) {
        return createParameter(fullFlag, shortFlag, null, Character.class, false, defaultValue);
    }

    /**
     * <ul>
     *     <li>checks if args is Empty</li>
     *     <li>checks if --help or -h was called on the program</li>
     *     <li>goes through the args given to the ArgsParser and assigns each parameter its argument, making it callable via flags</li>
     *     <li>checks if all mandatory parameters were given in the args
     * </ul>
     * @throws NoArgumentsProvidedArgsException if no arguments were provided in args
     * @throws UnknownFlagArgsException if an unknown flag was provided in args
     * @throws TooManyArgumentsArgsException if more than one argument was provided to a single flag
     * @throws MissingArgArgsException if a flag was provided without an argument
     * @throws MandatoryArgNotProvidedArgsException if not all mandatory parameters were given in args
     * @throws CalledForHelpNotification if --help or -h was called
     * @throws InvalidArgTypeArgsException if the argument provided to a flag is not of the correct type
     */
    public void parseArgs() throws NoArgumentsProvidedArgsException, UnknownFlagArgsException,
            TooManyArgumentsArgsException, MissingArgArgsException, MandatoryArgNotProvidedArgsException,
            CalledForHelpNotification, InvalidArgTypeArgsException {

        parseArgsWasCalled = true;

        checkIfAnyArgumentsProvided();
        checkForHelpCall();
        Set<Parameter<?>> givenParameters = parseArguments();
        checkMandatoryArguments(givenParameters);

    }

    /**
     * checks if any arguments were provided to the program
     * @throws NoArgumentsProvidedArgsException if no arguments were provided in args
     */
    private void checkIfAnyArgumentsProvided() throws NoArgumentsProvidedArgsException{
        if (args == null || args.length == 0) {
            throw new NoArgumentsProvidedArgsException();
        }
    }

    /**
     * <ul>checks if --help or -h was called on the program, printing out help Strings for all parameters</ul>
     * <ul>checks if --help or -h was called for a specific parameter, printing out this parameters help string</ul>
     * @throws UnknownFlagArgsException if an unknown flag was provided in args
     * @throws MissingArgArgsException if a flag was provided without an argument
     * @throws CalledForHelpNotification if --help or -h was called
     */
    private void checkForHelpCall() throws UnknownFlagArgsException, MissingArgArgsException, CalledForHelpNotification {
        boolean oneArgProvided = args.length == 1;
        boolean twoArgsProvided = args.length == 2;
        boolean firstArgumentIsParameter = parameterMap.get(args[0]) != null;

        if (oneArgProvided) {
            if (args[0].equals("--help") || args[0].equals("-h")) { // if --help or -h was called, the help is printed
                throw new CalledForHelpNotification(new HashSet<>(parameterMap.values()), longestFlagSize, longestShortFlag);

            } else if (firstArgumentIsParameter) { // if the first argument is a parameter but --help was not called, the program notifies the user of a missing argument
                throw new MissingArgArgsException(args[0]);

            } else { // if the first argument is not a parameter and --help was not called, the program notifies the user of an unknown parameter input
                throw new UnknownFlagArgsException(args[0]);
            }

        } else if (twoArgsProvided && (args[1].equals("--help") || args[1].equals("-h"))) {
            if (firstArgumentIsParameter) { // if the first argument is a parameter and --help follows,
                throw new CalledForHelpNotification(new HashSet<>(Collections.singletonList(parameterMap.get(args[0]))), longestFlagSize, longestShortFlag);

            } else { // if the first argument is not a parameter but --help was called,
                // the program notifies the user of an unknown parameter input
                throw new UnknownFlagArgsException(args[0]);
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
            MissingArgArgsException, InvalidArgTypeArgsException {
        Set<Parameter<?>> givenParameters = new HashSet<>();

        Parameter<?> currentParameter = null;
        for (int i = 0; i < args.length; i++) {

            boolean currentPositionIsFlag = args[i].startsWith("-");
            if (currentPositionIsFlag) currentParameter = parameterMap.get(args[i]);
            boolean flagExists = parameterMap.get(args[i]) != null;
            boolean isLastEntry = i == args.length - 1;
            boolean currentParameterNotNull = currentParameter != null;
            boolean argumentSet = currentParameterNotNull && currentParameter.hasArgument();
            boolean lastPositionWasFlag = i >= 1 && args[i - 1].startsWith("-");

            if (currentPositionIsFlag && !flagExists) { // if flag is unknown
                throw new UnknownFlagArgsException(args[i]);

            } else if (argumentSet && !currentPositionIsFlag) { // if two arguments are provided to a single flag
                throw new TooManyArgumentsArgsException(currentParameter.getFullFlag());

            } else if (currentPositionIsFlag && lastPositionWasFlag) { // if a flag follows another flag
                throw new MissingArgArgsException(args[i - 1]);

            } else if (isLastEntry && currentPositionIsFlag) { //if last Flag has no argument
                throw new MissingArgArgsException(args[i]);

            }  else if (lastPositionWasFlag && currentParameterNotNull) { // if the current position is an argument
                currentParameter.setArgument(args[i]);
                givenParameters.add(currentParameter);

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
            errorMessage.append("Mandatory parameters are missing:\n");
            for (Parameter<?> param : mandatoryParameters) {
                errorMessage.append(param.getFullFlag()).append("\n");
            }
            throw new MandatoryArgNotProvidedArgsException(errorMessage.toString());
        }
    }

}
