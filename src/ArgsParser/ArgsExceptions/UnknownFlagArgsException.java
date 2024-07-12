package ArgsParser.ArgsExceptions;

import ArgsParser.ArgsException;
import ArgsParser.ArgsParser;
import jdk.jshell.SourceCodeAnalysis;

import java.util.Set;

/**
 * Exception to be thrown if an unknown flag is provided
 */
public class UnknownFlagArgsException extends ArgsException {
    public UnknownFlagArgsException(String flagName, Set<String> fullFlags, Set<String> shortFlags) {
        super("unknown flag: " + flagName + computeSuggestion(flagName, fullFlags, shortFlags));
    }

    protected static String computeSuggestion(String userInput, Set<String> fullFlags, Set<String> shortFlags) {
        fullFlags.add("help");
        shortFlags.add("h");
        userInput = stripFlagPrefix(userInput);
        double highestSimilarity = 0.0;
        String suggestion = "";

        for (String fullFlag : fullFlags) {
            fullFlag = stripFlagPrefix(fullFlag);
            double similarity = SimilarityScore(userInput, fullFlag);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                suggestion = "--" + fullFlag;
            }
        }

        for (String shortFlag : shortFlags) {
            shortFlag = stripFlagPrefix(shortFlag);
            double similarity = SimilarityScore(userInput, shortFlag);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                suggestion = "-" + shortFlag;
            }
        }

        if (highestSimilarity < 0.1) {
            return "";
        } else {
            return "\n> did you mean: " + suggestion + " ?";
        }

    }

    private static String stripFlagPrefix(String flag) {
        return flag.replaceFirst("^-+", "");
    }

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
