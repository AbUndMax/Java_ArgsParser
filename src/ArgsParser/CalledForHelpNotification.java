package ArgsParser;

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
    private static final Map<String, String> shortFlagTypes = new HashMap<>(){{
        put("String", "s");
        put("Integer", "i");
        put("Double", "d");
        put("Boolean", "b");
        put("Character", "c");
        put("String[]", "s+");
        put("Integer[]", "i+");
        put("Double[]", "d+");
        put("Boolean[]", "b+");
        put("Character[]", "c+");
    }};

    public CalledForHelpNotification(Map<String, Parameter<?>> parameterMap, List<String> flagsInDefinitionOrder,
                                     Map<String, Command> commandMap, List<String> commandsInDefinitionOrder,
                                     int longestFlagSize, int longestShortFlag) {
        super(generateHelpMessage(parameterMap, flagsInDefinitionOrder, 
                                  commandMap, commandsInDefinitionOrder,
                                  longestFlagSize, longestShortFlag));
    }

    /**
     * prints all available Parameters found in argumentsList to the console
     */
    private static String generateHelpMessage(Map<String, Parameter<?>> parameterMap, List<String> flagsInDefinitionOrder,
                                              Map<String, Command> commandMap, List<String> commandsInDefinitionOrder,
                                              int longestFlagSize, int longestShortFlag) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("\n");

        String headTitle = " HELP ";
        int spaceForHeadTitle = headTitle.length();
        int numberOfHashes = consoleWidth / 2 - spaceForHeadTitle / 2;
        String header = "#".repeat(numberOfHashes) + headTitle + "#".repeat(numberOfHashes);
        helpMessage.append(header).append("\n");
        helpMessage.append(centerString("[s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double")).append("\n");
        helpMessage.append(centerString(" ('+' marks a flag that takes several arguments of the same type whitespace separated)")).append("\n");
        helpMessage.append(centerString("(!)=mandatory | (?)=optional")).append("\n");
        helpMessage.append("#").append("\n");

        if (flagsInDefinitionOrder.size() > 1) {
            helpMessage.append(centerString("Available Parameters:")).append("\n");
            helpMessage.append("#").append("\n");
        }

        for (String flag : flagsInDefinitionOrder) {
            String helpString = parameterHelpString(parameterMap.get(flag), longestFlagSize, longestShortFlag);
            helpMessage.append(helpString).append("\n");
            helpMessage.append("#").append("\n");
        }
        
        if (commandsInDefinitionOrder.size() > 1) {
            helpMessage.append(centerString("Available Commands:")).append("\n");
            helpMessage.append("#").append("\n");
        }
        
        for (String command : commandsInDefinitionOrder) {
            String helpString = commandHelpString(commandMap.get(command), longestFlagSize, longestShortFlag);
            helpMessage.append(helpString).append("\n");
            helpMessage.append("#").append("\n");
        }

        return helpMessage.append("#".repeat(consoleWidth)).toString();
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
    
    private static String commandHelpString(Command command, int longestFlagSize, int longestShortFlag) {
        String fullCommandName = command.getFullCommandName();
        String shortCommandName = command.getShortCommandName();
        String description = command.getDescription();
        String isMandatory = "   ";
        StringBuilder helpString = new StringBuilder("###  ");

        // check if a description is available:
        if (description == null || description.isEmpty()) {
            description = "No description available!";
        } else {
            description = description.trim();
        }

        // align the parameter names nicely
        int nameWhiteSpaceSize = longestFlagSize - fullCommandName.length();
        fullCommandName = fullCommandName + " ".repeat(nameWhiteSpaceSize);
        int shortWhiteSpaceSize = longestShortFlag == 0 ? 0 : longestShortFlag - shortCommandName.length();
        shortCommandName = shortCommandName + " ".repeat(shortWhiteSpaceSize);

        helpString.append(fullCommandName).append("  ").append(shortCommandName).append("            ");

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
    private static String parameterHelpString(Parameter<?> parameter, int longestFlagSize, int longestShortFlag) {
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
        int nameWhiteSpaceSize = longestFlagSize - name.length();
        name = name + " ".repeat(nameWhiteSpaceSize);
        int shortWhiteSpaceSize = longestShortFlag == 0 ? 0 : longestShortFlag - shortFlag.length();
        shortFlag = shortFlag + " ".repeat(shortWhiteSpaceSize);

        // get type
        String type = shortFlagTypes.get(parameter.getType());

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
