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
    private static int longestFullSize;
    private static int longestShortSize;
    private static final Map<Class<?>, String> shortTypes = new HashMap<>(){{
        put(String.class, "[s]");
        put(Path.class, "[p]");
        put(Integer.class, "[i]");
        put(Float.class, "[f]");
        put(Double.class, "[d]");
        put(Boolean.class, "[b]");
        put(Character.class, "[c]");
        put(String[].class, "[s+]");
        put(Path[].class, "[p+]");
        put(Integer[].class, "[i+]");
        put(Float[].class, "[f+]");
        put(Double[].class, "[d+]");
        put(Boolean[].class, "[b+]");
        put(Character[].class, "[c+]");
    }};

    public CalledForHelpNotification(Map<String, Parameter<?>> parameterMap, List<String> flagsInDefinitionOrder,
                                     Map<String, Command> commandMap, List<String> commandsInDefinitionOrder,
                                     int longestFullFlagSize, int longestShortFlagSize) {
        super(helpMessage(parameterMap, flagsInDefinitionOrder, commandMap, commandsInDefinitionOrder,
                          longestFullFlagSize, longestShortFlagSize));
    }

    /**
     * Generates the full help message for all parameters and commands.
     *
     * @param parameterMap the map containing all parameters and their metadata
     * @param flagsInDefinitionOrder the list of flags in their definition order
     * @param commandMap the map containing all commands and their metadata
     * @param commandsInDefinitionOrder the list of commands in their definition order
     * @param longestFullFlagSize the length of the longest full flag
     * @param longestShortFlagSize the length of the longest short flag
     * @return the generated help message as a string
     */
    private static String helpMessage(Map<String, Parameter<?>> parameterMap, List<String> flagsInDefinitionOrder,
                                      Map<String, Command> commandMap, List<String> commandsInDefinitionOrder,
                                      int longestFullFlagSize, int longestShortFlagSize) {
        longestFullSize = longestFullFlagSize;
        longestShortSize = longestShortFlagSize;
        StringBuilder helpMessage = new StringBuilder();
        boolean printSingleParameter = parameterMap.size() + commandMap.size() == 2; // 2 because one param or cmd is represented by long AND short flag in their maps


        // Header
        helpMessage.append(generateHead(parameterMap));

        // Parameters
        if (!printSingleParameter && !parameterMap.isEmpty()) {
            helpMessage.append(centerString("Available Parameters:\n#\n"));
        }

        for (String flag : flagsInDefinitionOrder ) {
            Parameter<?> parameter = parameterMap.get(flag);
            helpMessage.append(generateSingleHelpString(parameter.getFullFlag(), parameter.getShortFlag(),
                                     shortTypes.get(parameter.getType()), formatMandatory(parameter.isMandatory()),
                                     parameter.getDescription(), parameter.hasDefault(), "default:  ",
                                     parameter.getDefaultAsString()));
        }

        // Commands
        if (!printSingleParameter && !commandMap.isEmpty()) {
            helpMessage.append(centerString("Available Commands:\n#\n"));
        }

        for (String cmd : commandsInDefinitionOrder ) {
            Command command = commandMap.get(cmd);
            helpMessage.append(generateSingleHelpString(command.getFullCommandName(), command.getShortCommandName(),
                                                        " ".repeat(longestUsedTypeSize + 2), "(/)",
                                                        command.getDescription(), command.isPartOfToggle(),
                                                        "cannot be combined with:  ",
                                                        command.cannotBeCombinedWith()));
        }

        helpMessage.append("#".repeat(consoleWidth));


        return helpMessage.toString();
    }

    /**
     * Generates the header section of the help message.
     * The header includes information about the available parameter types and their abbreviations.
     *
     * @param parameterMap the map containing all parameters and their metadata
     * @return the generated header as a string
     */
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
        // show each abbreviation for each type used
        for (Class<?> type : usedTypes) {
            if (type.isArray()) arrayParamUsed = true;
            String shortType = getShortType(type);
            String typeInformation = shortType + "=" +
                    type.getSimpleName().replaceFirst("\\[]", "");
            longestUsedTypeSize = Math.max(longestUsedTypeSize, shortType.length());
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
        if (!currentLine.isEmpty()) header.append(centerString(currentLine.toString())).append("\n");

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
     * Centers a given string within the console's width.
     *
     * @param stringToCenter the string to be centered
     * @return the centered string with padding and a leading #
     */
    private static String centerString(String stringToCenter) {
        int freeSpace = (consoleWidth - stringToCenter.length()) / 2 - 1;
        return "#" + " ".repeat(freeSpace) + stringToCenter;
    }

    /**
     * Calculates the short version of the given parameter type.
     * If no predefined abbreviation exists, it generates one based on the type name.
     *
     * @param type the class of the parameter type
     * @return the short abbreviation of the type
     */
    private static String getShortType(Class<?> type) {
        String simpleName = type.getSimpleName();
        String shortType = shortTypes.get(type);
        if (shortType != null) return shortType;

        for (int i = 1; i < simpleName.length(); i++) {
            shortType = "[" + simpleName.substring(0, i);
            shortType = type.isArray() ? shortType + "+]" : shortType + "]";
            if (!shortTypes.containsValue(shortType)) {
                shortTypes.put(type, shortType);
                return shortType;
            }
        }

        return simpleName.replaceFirst("\\[]", "");
    }

    /**
     * Returns the corresponding symbol to indicate whether a parameter is mandatory or optional.
     *
     * @param isMandatory true if the parameter is mandatory, false otherwise
     * @return "(!)" for mandatory parameters, "( )" for optional parameters
     */
    private static String formatMandatory(boolean isMandatory) {
        if (isMandatory) return "(!)";
        else return "( )";
    }

    /**
     * Generates the help string for a single parameter or command.
     *
     * @param fullName the full name of the parameter or command
     * @param shortName the short name of the parameter or command
     * @param type the type of the parameter
     * @param mandatory the string indicating whether the parameter is mandatory
     * @param description the description of the parameter or command
     * @param hasDefaultOrToggle true if the parameter has a default value or toggle
     * @param defaultOrToggle the label for default values or toggles
     * @param value the default value or toggle details
     * @return the generated help string for the parameter or command
     */
    private static String generateSingleHelpString(String fullName, String shortName, String type, String mandatory,
                                                   String description, boolean hasDefaultOrToggle, String defaultOrToggle, String value) {

        StringBuilder helpMessage = new StringBuilder();
        String informationLine = (generateInformationLine(fullName, shortName,
                                                          type, mandatory));

        if (description == null || description.isEmpty()) {
            description = "No description available!";
        }

        helpMessage.append(formatLastPartInLine(informationLine, description));
        if (hasDefaultOrToggle) helpMessage.append(formatLastPartInLine(defaultOrToggle, value));

        return helpMessage.append("#\n").toString();
    }

    /**
     * Generates the information line for a parameter.
     * This includes the full name, short name, type, and whether it is mandatory or optional.
     *
     * @param fullName the full name of the parameter
     * @param shortName the short name of the parameter
     * @param type the type of the parameter
     * @param mandatory the string indicating whether the parameter is mandatory
     * @return the formatted information line as a string
     */
    private static String generateInformationLine(String fullName, String shortName,
                                                  String type, String mandatory) {
        String preFormat = String.format("##  %%-%ds  %%-%ds  %%-%ds  ",
                                         longestFullSize, longestShortSize, longestUsedTypeSize);
        String informationLine = String.format(preFormat, fullName, shortName, type);

        return informationLine + mandatory + "  ";
    }

    /**
     * Formats the last part of a help message line, introducing line breaks
     * if the description or default value exceeds the available space.
     *
     * @param informationLine the already formatted part of the line
     * @param lastPart the remaining part to be formatted
     * @return the complete line with line breaks as needed
     */
    private static String formatLastPartInLine(String informationLine, String lastPart) {
        lastPart = lastPart.trim();
        int fillSpace = 17 + longestFullSize + longestShortSize + longestUsedTypeSize;
        String filler = String.format(String.format("#%%%ds", fillSpace), informationLine);
        if (informationLine.length() + lastPart.length() <= consoleWidth) return filler + lastPart + "\n";

        StringBuilder fullPart = new StringBuilder(informationLine);

        int freeSpace = consoleWidth - filler.length();
        while (lastPart.length() > freeSpace) {
            String lead = lastPart.substring(0, freeSpace);

            if (lead.contains(" ")) {
                // Look for the last index which has a space symbol " "
                int breakPoint = lead.lastIndexOf(" ");
                if (breakPoint > 0) {
                    // Cut up to this position and append it
                    fullPart.append(lead, 0, breakPoint).append("\n").append(filler);
                    lastPart = lastPart.substring(breakPoint + 1).trim();
                } else {
                    // No space found, break after freeSpace
                    fullPart.append(lead).append("\n").append(filler);
                    lastPart = lastPart.substring(freeSpace).trim();
                }
            } else {
                // No space found, break after freeSpace
                fullPart.append(lead).append("\n").append(filler);
                lastPart = lastPart.substring(freeSpace).trim();
            }
        }
        fullPart.append(lastPart).append("\n");

        return fullPart.toString();
    }
}
