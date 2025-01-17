package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;
import java.util.Set;

/**
 * Exception to be thrown if an unknown flag is provided
 */
public class UnknownFlagArgsException extends ArgsException {
    public UnknownFlagArgsException(String flagName, Set<String> parameterFlags, Set<String> commandNames, boolean isFirstPosition) {
        super("unknown flag or command: " + flagName + computeSuggestion(flagName, parameterFlags, commandNames, isFirstPosition), true);
    }

    /**
     * Compute a suggestion for the user based on the flag name
     * @param userInput the flag name provided by the user
     * @param parameterFlags the full flags available
     * @param commandNames the short flags available
     * @return a suggestion for the user
     */
    protected static String computeSuggestion(String userInput, Set<String> parameterFlags, Set<String> commandNames, boolean isFirstPosition) {
        String strippedInput = stripFlagPrefix(userInput);
        double highestSimilarity = 0.0;
        String suggestion = "";

        for (String parameterFlag : parameterFlags) {
            String strippedFlag = stripFlagPrefix(parameterFlag);
            double similarity = SimilarityScore(strippedInput, strippedFlag);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                suggestion = parameterFlag;
            }
        }

        for (String help : new String[]{"--help", "-h"}) {
            String strippedFlag = stripFlagPrefix(help);
            double similarity = SimilarityScore(strippedInput, strippedFlag);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                suggestion = help;
            }
        }

        for (String commandName : commandNames) {
            commandName = stripFlagPrefix(commandName);
            double similarity = SimilarityScore(strippedInput, commandName);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                suggestion = commandName;
            }
        }

        if (highestSimilarity < 0.1) {
            return "";
        } else if (isFirstPosition && !(userInput.charAt(0) == '-')) {
            return "\n> did you mean: " + suggestion + " ?" +
                    "\n" +
                    "\n> flag or command expected in first position!";
        } else {
            return "\n> did you mean: " + suggestion + " ?";
        }

    }

    /**
     * Strip the flag prefix from the flag name
     * @param flag the flag name
     * @return the flag name without the prefix
     */
    private static String stripFlagPrefix(String flag) {
        return flag.replaceFirst("^-+", "");
    }

    /**
     * Compute the similarity score between the user input and the parameter using Levenshtein distance
     * @param userInput the user input
     * @param parameter the parameter
     * @return the similarity score
     */
    private static double SimilarityScore(String userInput, String parameter) {
        int[][] dp = new int[userInput.length() + 1][parameter.length() + 1];
        for (int i = 0; i <= userInput.length(); i++) {
            for (int j = 0; j <= parameter.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + (userInput.charAt(i - 1) == parameter.charAt(j - 1) ? 0 : 1),
                                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }

        return 1 - (double) dp[userInput.length()][parameter.length()] / Math.max(userInput.length(), parameter.length());
    }
}
