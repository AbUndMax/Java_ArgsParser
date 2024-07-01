package ArgsParser;

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
 * <p> available at: <a href="https://github.com/AbUndMax/Java_ArgsParser">GitHub</a></p>
 */
public class ArgsParser {

    private final String[] args;
    private final Map<String, Parameter> parameterMap = new HashMap<>();
    private final Set<Parameter> mandatoryParameters = new HashSet<>();
    private boolean parsedSuccessfully = false;
    private int longestFlagSize = 0;
    private int longestShortFlag = 0;
    private final int consoleWidth = 80;

    /**
     * Constructor demands the args array of the main method to be passed.
     * <p><strong>If args is null or args has no elements, the program ist terminated with Code 1</strong></p>
     * @param args String array of the main method
     */
    public ArgsParser(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("No arguments provided!");
            System.exit(1);
        }

        this.args = args;
    }

    /**
     * adds given parameter to argumentList, sets longestFlagSize and adds mandatory parameters to the mandatoryList
     * @param parameter parameter to be processed
     */
    private void prepareParameter(Parameter parameter) {
        parameterMap.put(parameter.getFlagName(), parameter);
        if (parameter.getShortName() != null) parameterMap.put(parameter.getShortName(), parameter);
        int nameSize = parameter.getFlagName().length();
        if (parameter.getShortName() != null) {
            int shortSize = parameter.getShortName().length();
            if (longestShortFlag < shortSize) longestShortFlag = shortSize;
        }
        if (longestFlagSize < nameSize) longestFlagSize = nameSize;
        if (parameter.isMandatory()) mandatoryParameters.add(parameter);
    }

    /**
     * Creates a flag from the given flagName, if the flagName is already in the correct format, it will be returned as is.
     * If not, it will add a leading or -- to the flagName
     * @param flagName name of the flag
     * @param isShortName true if the flag is a short flag
     * @return flag in the correct format (e.g. --flagName or -f)
     */
    private String makeFlag(String flagName, boolean isShortName) {
        int i = 0;
        while (flagName.charAt(i) == '-') {
            i++;
        }

        if (i == 2 && !isShortName) return flagName;
        else if (i == 1 && isShortName) return flagName;
        else {
            String newFlag = isShortName ? "-" : "--";
            return newFlag + flagName.substring(i);
        }
    }

    /**
     * adds a new parameter that will be checked in args
     * @param flagName name of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     */
    public Parameter addParameter(String flagName, boolean isMandatory) {
        Parameter parameter = new Parameter(makeFlag(flagName, false), isMandatory);
        prepareParameter(parameter);
        return parameter;
    }

    /**
     * adds a new parameter with a short version of the flag that will be checked in args
     * @param flagName name of the parameter
     * @param shortName short version of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     */
    public Parameter addParameter(String flagName, String shortName, boolean isMandatory) {
        Parameter parameter = new Parameter(makeFlag(flagName, false), makeFlag(shortName, true), isMandatory);
        prepareParameter(parameter);
        return parameter;
    }

    /**
     * adds a new parameter with a short version of the flag that will be checked in args as well as a description
     * @param flagName name of the parameter
     * @param shortName short version of the parameter
     * @param description description of the parameter
     * @param isMandatory true if parameter is mandatory, false if optional
     */
    public Parameter addParameter(String flagName, String shortName, String description, boolean isMandatory) {
        Parameter parameter = new Parameter(makeFlag(flagName, false), makeFlag(shortName, true), description, isMandatory);
        prepareParameter(parameter);
        return parameter;
    }

    /**
     * <ul>checks if --help or -h was called on the program or a specific parameter, printing out teh corresponding help Strings. <strong>Exits program after printout</strong></ul>
     * <ul>goes through the args given to the ArgsParser and assigns each parameter its argument, making it callable via flags</ul>
     * <ul>checks if all mandatory parameters were given in the args, <strong>if not, exits the program</strong></ul>
     *
     */
    public void parseArgs() {
        checkForHelpCall();
        Set<Parameter> givenParameters = parseArguments();
        checkMandatoryArguments(givenParameters);

        parsedSuccessfully = true;
    }

    /**
     * <ul>checks if --help or -h was called on the program, printing out help Strings for all parameters</ul>
     * <ul>checks if --help or -h was called for a specific parameter, printing out this parameters help string</ul>
     * <p><strong>Exits the program after the help information were printed!</strong></p>
     */
    private void checkForHelpCall() {
        boolean oneArgProvided = args.length == 1;
        boolean twoArgsProvided = args.length == 2;
        boolean firstArgumentIsParameter = parameterMap.get(args[0]) != null;

        if (oneArgProvided) {
            if (args[0].equals("--help") || args[0].equals("-h")) { // if --help or -h was called, the help is printed
                printHelp(true);
                System.exit(0);

            } else if (firstArgumentIsParameter) { // if the first argument is a parameter but --help was not called, the program notifies the user of a missing argument
                System.out.println("Missing argument for parameter: " + args[0]);
                System.exit(0);

            } else { // if the first argument is not a parameter and --help was not called, the program notifies the user of an unknown parameter input
                System.out.println("# Parameter flag " + args[0] + " is unknown!");
                System.exit(1);
            }

        } else if (twoArgsProvided && (args[1].equals("--help") || args[1].equals("-h"))) {
            if (firstArgumentIsParameter) { // if the first argument is a parameter and --help follows,
                // help is printed for this parameter
                printHelp(false);
                System.exit(0);

            } else { // if the first argument is not a parameter but --help was called,
                // the program notifies the user of an unknown parameter input
                System.out.println("# Parameter flag " + args[0] + " is unknown!");
                System.exit(1);
            }
        }
    }

    /**
     * goes through all entries in args and creates a Parameter instance for each found flag.
     * @return a set of all Parameter instances created based on args
     */
    private Set<Parameter> parseArguments() {
        Set<Parameter> givenParameters = new HashSet<>();
        Parameter currentParameter;

        // check if arg is odd and if so,
        // check if the last arg is a parameter thus reporting missing argument for this one
        if (args.length % 2 != 0) {
            if (parameterMap.get(args[args.length - 1]) != null) {
                System.out.println("Missing argument for parameter: " + args[args.length - 1]);
                System.exit(1);
            } else {
                System.out.println("Provided two arguments to: " + args[args.length - 2]);
                System.exit(1);
            }
        }

        // goes through all args and assigns the arguments to the corresponding parameters
        for (int i = 0; i <= args.length - 2; i += 2) {
            currentParameter = parameterMap.get(args[i]);
            boolean currentPositionIsParameter = currentParameter != null;
            boolean nextPositionIsArgument = parameterMap.get(args[i + 1]) == null;

            if (currentPositionIsParameter && nextPositionIsArgument) {
                currentParameter.setArgument(args[i + 1]);
                givenParameters.add(currentParameter);

            } else if (currentPositionIsParameter) {
                System.out.println("Missing argument for parameter: " + parameterMap.get(args[i]).getFlagName());
                System.exit(1);

            }  else {
                System.out.println("Provided two arguments to: " + parameterMap.get(args[i - 2]).getFlagName());
                System.exit(1);
            }
        }

        return givenParameters;
    }

    /**
     * checks if all mandatory parameters were given in args!
     * <p><strong>Exits the program if not all mandatory parameters were given!</strong></p>
     * @param givenParameters a set of all Parameter instances created based on args
     */
    private void checkMandatoryArguments(Set<Parameter> givenParameters) {
        if (!givenParameters.containsAll(mandatoryParameters)) {
            mandatoryParameters.removeAll(givenParameters);
            System.out.println("Mandatory parameters are missing: ");
            for (Parameter param : mandatoryParameters) {
                System.out.println("# " + param.getFlagName());
            }
            System.exit(1);
        }
    }

    /**
     * prints all available Parameters found in argumentsList to the console
     */
    private void printHelp(boolean printAll) {
        String headTitle = " HELP ";
        int spaceForHeadTitle = headTitle.length();
        int numberOfHashes = consoleWidth / 2 - spaceForHeadTitle / 2;
        String header = "#".repeat(numberOfHashes) + headTitle + "#".repeat(numberOfHashes);
        System.out.println(header);

        System.out.println(centerString("(!) = mandatory parameter | (+) = optional parameter"));
        System.out.println("#");

        if (printAll) {
            System.out.println(centerString("Available Parameters:"));
        }
        System.out.println("#");

        Set<Parameter> parameters = new HashSet<>(parameterMap.values());
        for (Parameter param : parameters) {
            String helpString = parameterHelpString(param);
            System.out.println(helpString);
            System.out.println("#");
        }

        System.out.println("#".repeat(consoleWidth));
    }

    /**
     * centers a given string in the help Box
     * @param stringToCenter String to be centered
     * @return centered String with a leading #
     */
    private String centerString(String stringToCenter) {
        int freeSpace = (consoleWidth - stringToCenter.length()) / 2 - 1;
        return "#" + " ".repeat(freeSpace) + stringToCenter;
    }

    /**
     * generates the help String (flagName, shortFlag, description) for a single Parameter
     * @param parameter parameter Instance of which the help String should be generated
     * @return String with all information for the given Parameter
     */
    private String parameterHelpString(Parameter parameter) {
        String name = parameter.getFlagName();
        String shortName = parameter.getShortName() == null ? "/" : parameter.getShortName();
        String description = parameter.getDescription() == null ? "No description provided!" : parameter.getDescription();
        String isMandatory = parameter.isMandatory() ? "(!)" : "(+)";
        StringBuilder helpString = new StringBuilder("###  ");

        // align the parameter names nicely
        int nameWhiteSpaceSize = longestFlagSize - name.length();
        name = name + " ".repeat(nameWhiteSpaceSize);
        int shortWhiteSpaceSize = longestShortFlag == 0 ? 0 : longestShortFlag - shortName.length();
        shortName = shortName + " ".repeat(shortWhiteSpaceSize);

        helpString.append(name).append("  ").append(shortName).append("  ").append(isMandatory).append("  ");

        // The description String gets checked if it fits inside the info box.
        // If not, a new line will be added and the rest of the description will be aligned.
        String[] descriptionRows = description.split(" \\n| \\n |\\n ", -1);
        int whiteSpace = helpString.length();
        if (descriptionRows.length == 1) {
            return helpString.append(concatDescriptionLines(description, whiteSpace)).toString();

        } else {
            for (int i = 0; i < descriptionRows.length; i++) {
                String row = descriptionRows[i];
                helpString.append(concatDescriptionLines(row, whiteSpace));
                if (i != descriptionRows.length - 1) helpString.append("\n#").append(" ".repeat(whiteSpace - 1));
            }
            return helpString.toString();
        }
    }

    /**
     * helper function to do correct new lines if the Description is too long to fit into the help box
     * @param restDescription part of the description that doesn't fit
     * @param whiteSpace int that specifies how many white space before the description should be placed
     * @return the concatenated lines
     */
    private String concatDescriptionLines(String restDescription, int whiteSpace) {
        int restLength = restDescription.length();
        StringBuilder lineBuilder = new StringBuilder();

        while (whiteSpace + restLength > consoleWidth) {
            int i = consoleWidth - whiteSpace;
            char currentChar = restDescription.charAt(i);
            while (currentChar != ' ') {
                currentChar = restDescription.charAt(--i);
            }

            lineBuilder.append(restDescription, 0, i).append("\n#").append(" ".repeat(whiteSpace - 1));
            restDescription = restDescription.substring(++i);
            restLength = restDescription.length();
        }

        return lineBuilder.append(restDescription).toString();
    }

}
