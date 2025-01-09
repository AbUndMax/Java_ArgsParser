package ArgsParser;

import java.nio.file.Path;
import java.util.*;

/**
 * Exception used as Notification that --help was used.
 * It will print all available flags to the console if only --help was provided in args
 * It will print the help message for a single flag if --help was provided behind a flag
 *
 * <p>It is recommended to call {@link System#exit(int status)}  with status = 0, after outputting the message.</p>
 */
public class CalledForHelpNotification extends Exception {

    private static final int consoleWidth = 100;
    private static int longestUsedTypeSize = 1;
    private static final Map<Class<?>, String> shortTypes = new HashMap<>(){{
        put(String.class, "s");
        put(Path.class, "p");
        put(Integer.class, "i");
        put(Float.class, "f");
        put(Double.class, "d");
        put(Boolean.class, "b");
        put(Character.class, "c");
        put(String[].class, "s+");
        put(Path[].class, "p+");
        put(Integer[].class, "i+");
        put(Float[].class, "f+");
        put(Double[].class, "d+");
        put(Boolean[].class, "b+");
        put(Character[].class, "c+");
    }};

    public CalledForHelpNotification(Map<String, Parameter<?>> parameterMap, List<String> flagsInDefinitionOrder,
                                     Map<String, Command> commandMap, List<String> commandsInDefinitionOrder,
                                     int longestFullFlagSize, int longestShortFlagSizeSize) {
        super(helpMessage(parameterMap, flagsInDefinitionOrder,
                                  commandMap, commandsInDefinitionOrder,
                                  longestFullFlagSize, longestShortFlagSizeSize));
    }

    private static String helpMessage(Map<String, Parameter<?>> parameterMap, List<String> flagsInDefinitionOrder,
                                              Map<String, Command> commandMap, List<String> commandsInDefinitionOrder,
                                              int longestFullFlagSize, int longestShortFlagSize) {
        StringBuilder helpMessage = new StringBuilder();

        // Header
        helpMessage.append(generateHead(parameterMap));

        // Parameters
        for (String flag : flagsInDefinitionOrder ) {
            Parameter<?> parameter = parameterMap.get(flag);
            helpMessage.append(generateSingleHelpString(parameter.getFullFlag(), longestFullFlagSize,
                                     parameter.getShortFlag(), longestShortFlagSize,
                                     shortTypes.get(parameter.getType()), formatMandatory(parameter.isMandatory()),
                                     parameter.getDescription(), parameter.hasDefault(), "default:  ",
                                     parameter.getDefaultAsString()));
        }


        // Commands
        for (String cmd : commandsInDefinitionOrder ) {
            Command command = commandMap.get(cmd);
            helpMessage.append(generateSingleHelpString(command.getFullCommandName(), longestFullFlagSize,
                                                        command.getShortCommandName(), longestShortFlagSize,
                                                        " ".repeat(longestUsedTypeSize + 2), "   ",
                                                        command.getDescription(), command.isPartOfToggle(),
                                                        "cannot be combined with:  ",
                                                        command.cannotBeCombinedWith()));
        }


        return helpMessage.toString();
    }

    private static String generateHead(Map<String, Parameter<?>> parameterMap) {
        StringBuilder header = new StringBuilder();
        String headTitle = " HELP ";
        int numberOfHashes = consoleWidth / 2 - headTitle.length() / 2;
        String head = "#".repeat(numberOfHashes) + headTitle + "#".repeat(numberOfHashes);
        header.append(head).append("\n");

        // generate a list of the used types:
        HashSet<Class<?>> usedTypes = new HashSet<>();
        for (Parameter<?> parameter : parameterMap.values()) {
            usedTypes.add(parameter.getType());
        }

        // information about the abbreviation of types used:
        StringBuilder currentLine = new StringBuilder();
        boolean arrayParamUsed = false;
        for (Class<?> type : usedTypes) {
            if (type.isArray()) arrayParamUsed = true;
            String typeInformation = "[" + getShortType(type) + "]=" +
                    type.getSimpleName().replaceFirst("\\[\\]", "");
            longestUsedTypeSize = Math.max(longestUsedTypeSize, typeInformation.length());
            if (currentLine.length() + typeInformation.length() > consoleWidth - 11) {
                header.append(centerString(currentLine.toString())).append("\n");
                currentLine.setLength(0);
                currentLine.append(typeInformation);

            } else if (currentLine.isEmpty()){
                currentLine.append(typeInformation);

            } else {
                currentLine.append(" | ").append(typeInformation);
            }
        }
        header.append(centerString(currentLine.toString())).append("\n");

        if (arrayParamUsed) {
            header.append(centerString(
                    "('+' marks a flag that takes several arguments " +
                            "of the same type whitespace separated)")).append("\n");
        }

        header.append(centerString("(!)=mandatory | ( )=optional | (/)=command")).append("\n");
        header.append("#\n");
        return header.toString();
    }

    /**
     * centers a given string in the help Box
     * @param stringToCenter String to be centered
     * @return centered String with a leading #
     */
    private static String centerString(String stringToCenter) {
        int freeSpace = (consoleWidth - stringToCenter.length()) / 2 - 1;
        return "#" + " ".repeat(freeSpace) + stringToCenter;
    }

    /**
     * calculates the short version of the given type:
     * tries to use single character abbreviation. If this already exists, it takes two chars and so on...
     * reserved: see "shortTypes" Map
     * @param type
     * @return abbreviation of the type
     */
    private static String getShortType(Class<?> type) {
        String simpleName = type.getSimpleName();
        String shortType = shortTypes.get(type);
        if (shortType != null) return shortType;

        for (int i = 1; i < simpleName.length(); i++) {
            shortType = type.isArray() ? simpleName.substring(0, i) + "+" : simpleName.substring(0, i);
            if (!shortTypes.containsValue(shortType)) {
                shortTypes.put(type, shortType);
                return shortType;
            }
        }

        return simpleName.replaceFirst("\\[\\]", "");
    }

    private static String formatMandatory(boolean isMandatory) {
        if (isMandatory) return "(!)";
        else return "( )";
    }

    private static String generateSingleHelpString(String fullName, int longestFullFlagSize, String shortName,
                                                   int longestShortFlagSize, String type, String mandatory,
                                                   String description, boolean hasDefaultOrToggle, String defaultOrToggle, String value) {
        StringBuilder helpMessage = new StringBuilder();
        String informationLine = (generateInformationLine(fullName, longestFullFlagSize,
                                                          shortName, longestShortFlagSize,
                                                          type, mandatory));
        helpMessage.append(formatLastPartInLine(informationLine, description));
        if (hasDefaultOrToggle) helpMessage.append(formatLastPartInLine(defaultOrToggle, value));

        return helpMessage.toString();
    }

    private static String generateInformationLine(String fullName, int longestFullFlagSize,
                                                  String shortName, int LongestShortFlagSize,
                                                  String type, String mandatory) {
        StringBuilder informationLine = new StringBuilder();

        return informationLine.toString();
    }

    private static String formatLastPartInLine(String informationLine, String lastPart) {
        StringBuilder fullPart = new StringBuilder(informationLine);

        return fullPart.toString();
    }






















    /**
     * prints all available Parameters found in argumentsList to the console
     */
    private static String generateHelpMessage(Map<String, Parameter<?>> parameterMap, List<String> flagsInDefinitionOrder,
                                              Map<String, Command> commandMap, List<String> commandsInDefinitionOrder,
                                              int longestFullFlagSize, int longestShortFlagSize) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("\n");

        String headTitle = " HELP ";
        int spaceForHeadTitle = headTitle.length();
        int numberOfHashes = consoleWidth / 2 - spaceForHeadTitle / 2;
        String header = "#".repeat(numberOfHashes) + headTitle + "#".repeat(numberOfHashes);
        helpMessage.append(header).append("\n");
        helpMessage.append(centerString("[s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double")).append("\n");
        helpMessage.append(centerString(" ('+' marks a flag that takes several arguments of the same type whitespace separated)")).append("\n");
        helpMessage.append(centerString("(!)=mandatory | (?)=optional | (/)=command")).append("\n");
        helpMessage.append("#").append("\n");

        boolean parametersAvailable = !flagsInDefinitionOrder.isEmpty();
        boolean commandsAvailable = !commandsInDefinitionOrder.isEmpty();
        boolean printEverything = flagsInDefinitionOrder.size() + commandsInDefinitionOrder.size() > 1;

        if (printEverything && parametersAvailable) {
            helpMessage.append(centerString("Available Parameters:")).append("\n");
            helpMessage.append("#").append("\n");
        }

        for (String flag : flagsInDefinitionOrder) {
            String helpString = parameterHelpString(parameterMap.get(flag), longestFullFlagSize, longestShortFlagSize);
            helpMessage.append(helpString).append("\n");
            helpMessage.append("#").append("\n");
        }
        
        if (printEverything && commandsAvailable) {
            helpMessage.append(centerString("Available Commands:")).append("\n");
            helpMessage.append("#").append("\n");
        }
        
        for (String command : commandsInDefinitionOrder) {
            String helpString = commandHelpString(commandMap.get(command), longestFullFlagSize, longestShortFlagSize);
            helpMessage.append(helpString).append("\n");
            helpMessage.append("#").append("\n");
        }

        return helpMessage.append("#".repeat(consoleWidth)).toString();
    }



    //TODO abstract commandHelpString and ParameterHelpString
    private static String commandHelpString(Command command, int longestFullFlagSize, int longestShortFlagSize) {
        String fullCommandName = command.getFullCommandName();
        String shortCommandName = command.getShortCommandName();
        String description = command.getDescription();
        StringBuilder helpString = new StringBuilder("###  ");

        // check if a description is available:
        if (description == null || description.isEmpty()) {
            description = "No description available!";
        } else {
            description = description.trim();
        }

        // align the parameter names nicely
        int nameWhiteSpaceSize = longestFullFlagSize - fullCommandName.length();
        fullCommandName = fullCommandName + " ".repeat(nameWhiteSpaceSize);
        int shortWhiteSpaceSize = longestShortFlagSize == 0 ? 0 : longestShortFlagSize - shortCommandName.length();
        shortCommandName = shortCommandName + " ".repeat(shortWhiteSpaceSize);

        helpString.append(fullCommandName).append("  ").append(shortCommandName).append("       (/)  ");

        int whiteSpace = helpString.length();

        // The description String gets checked if it fits inside the info box.
        // If not, a new line will be added and the rest of the description will be aligned.
        String[] descriptionRows = description.split(" \\n| \\n |\\n ", -1);
        if (descriptionRows.length == 1) {
            helpString.append(lineBreakAtWhitespace(description, whiteSpace));

        } else {
            for (int i = 0; i < descriptionRows.length; i++) {
                String row = descriptionRows[i];
                helpString.append(lineBreakAtWhitespace(row, whiteSpace));
                if (i != descriptionRows.length - 1) helpString.append("\n#").append(" ".repeat(whiteSpace - 1));
            }
        }

        return helpString.toString();
    }

    /**
     * generates the help String (fullFlag, shortFlag, description) for a single Parameter
     * @param parameter parameter Instance of which the help String should be generated
     * @return String with all information for the given Parameter
     */
    private static String parameterHelpString(Parameter<?> parameter, int longestFullFlagSize, int longestShortFlagSize) {
        String name = parameter.getFullFlag();
        String shortFlag = parameter.getShortFlag();
        String description = parameter.getDescription();
        String isMandatory = parameter.isMandatory() ? "(!)" : "(?)";
        StringBuilder helpString = new StringBuilder("###  ");

        // check if a description is available:
        if (description == null || description.isEmpty()) {
            description = "No description available!";
        } else {
            description = description.trim();
        }

        // align the parameter names nicely
        int nameWhiteSpaceSize = longestFullFlagSize - name.length();
        name = name + " ".repeat(nameWhiteSpaceSize);
        int shortWhiteSpaceSize = longestShortFlagSize == 0 ? 0 : longestShortFlagSize - shortFlag.length();
        shortFlag = shortFlag + " ".repeat(shortWhiteSpaceSize);

        // get type
        String type = shortTypes.get(parameter.getType());

        helpString.append(name).append("  ").append(shortFlag).append("  [").append(type);
        if (type.contains("+")) helpString.append("] ");
        else helpString.append("]  ");
        helpString.append(isMandatory).append("  ");

        int whiteSpace = helpString.length();

        // The description String gets checked if it fits inside the info box.
        // If not, a new line will be added and the rest of the description will be aligned.
        String[] descriptionRows = description.split(" \\n| \\n |\\n ", -1);
        if (descriptionRows.length == 1) {
            helpString.append(lineBreakAtWhitespace(description, whiteSpace));

        } else {
            for (int i = 0; i < descriptionRows.length; i++) {
                String row = descriptionRows[i];
                helpString.append(lineBreakAtWhitespace(row, whiteSpace));
                if (i != descriptionRows.length - 1) helpString.append("\n#").append(" ".repeat(whiteSpace - 1));
            }
        }

        // print default value if available
        if (parameter.hasDefault()) {
            String defaultTitle = "default:  ";
            String defaultValue = parameter.getDefaultValue().getClass().isArray() ?
                    Arrays.toString((Object[]) parameter.getDefaultValue()) : // here we convert the Array to a readable String
                    parameter.getDefaultValue().toString();
            helpString.append("\n").append("#").append(" ".repeat(whiteSpace - defaultTitle.length() - 1)).append(defaultTitle);

            if (whiteSpace + defaultValue.length() > consoleWidth) { // if the default is as large as the consoleWindow split default
                helpString.append(lineBreak(defaultValue, whiteSpace));

            } else {
                helpString.append(defaultValue);
            }
        }

        return helpString.toString();
    }

    /**
     * helper function to do correct new lines if the Description is too long to fit into the help box
     * @param description description that doesn't fit
     * @param whiteSpace int that specifies how many white space before the description should be placed
     * @return the concatenated lines
     */
    private static String lineBreakAtWhitespace(String description, int whiteSpace) {
        int restLength = description.length();
        StringBuilder lineBuilder = new StringBuilder();


        while (whiteSpace + restLength > consoleWidth) {
            int i = consoleWidth - whiteSpace;
            char currentChar = description.charAt(i);
            while (currentChar != ' ') {
                currentChar = description.charAt(--i);
            }

            lineBuilder.append(description, 0, i).append("\n#").append(" ".repeat(whiteSpace - 1));
            description = description.substring(++i);
            restLength = description.length();
        }

        return lineBuilder.append(description).toString();
    }

    /**
     * helper function to do correct new lines if the defaultValue is too long to fit into the help box
     * @param string defaultValue that doesn't fit
     * @param whiteSpace int that specifies how many white space before the defaultValue should be placed
     * @return the concatenated lines
     */
    private static String lineBreak(String string, int whiteSpace) {
        StringBuilder helpString = new StringBuilder();

        int spaceForValue;
        int staticFreeSpace = spaceForValue = consoleWidth - whiteSpace;
        String substring = string.substring(0, staticFreeSpace);
        helpString.append(substring);

        for (int i = 1; i < Math.ceil(string.length() / (double) staticFreeSpace); i++) { // print default line by line
            if (spaceForValue + staticFreeSpace >= string.length())
                substring = string.substring(spaceForValue);
            else substring = string.substring(spaceForValue, spaceForValue += (staticFreeSpace - 1));

            helpString.append("\n").append("#").append(" ".repeat(whiteSpace - 1)).append(substring);
        }

        return helpString.toString();
    }
}
