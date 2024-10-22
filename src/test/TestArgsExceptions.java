import ArgsParser.*;
import ArgsParser.ArgsExceptions.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestArgsExceptions {


    @Test
    public void testNoArgumentsProvidedWithOnlyOptionalParams() { // just checks if the programs still runs if only optionals are defined
        String[] args = {};
        ArgsParser parser = new ArgsParser(args);
        parser.addOptionalStringParameter("optional", "o", null);
        parser.parse();

    }

    @Test
    public void testNoArgumentsProvidedWithMandatoryParams() {
        String[] args = {};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> mandatoryParam = parser.addMandatoryStringParameter("optional", "o", null);

        Exception exception = assertThrows(NoArgumentsProvidedArgsException.class, parser::parseUnchecked);
        assertEquals("\n<!> No arguments provided\n" +
                             "\n" +
                             "> Use --help for more information.\n", exception.getMessage());
    }

    @Test
    public void testUnknownFlag() {
        String[] args = {"-w", "file.txt", "-s", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "s", "descr");

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("\n<!> unknown flag: -w\n" +
                             "\n" +
                             "> Use --help for more information.\n", exception.getMessage());
    }

    @Test
    public void testUnknownFlagWithMultipleFlagsAndWrongInput() {
        String[] args = {"-f", "file.txt", "--save", "save.txt", "-s"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "w", "descr");

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("\n<!> unknown flag: -s\n" +
                             "> did you mean: --save ?\n" +
                             "\n" +
                             "> Use --help for more information.\n", exception.getMessage());
    }

    @Test
    public void testMissingShorts() {
        String[] args = {"-f", "/to/file", "--save", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("--file", "m", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("--save", "s", null);
        Set<String> fullFlags = new HashSet<>(List.of("file", "save"));
        Set<String> shortFlags = new HashSet<>(List.of("m", "s"));

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("\n<!> unknown flag: -f\n" +
                             "> did you mean: --file ?\n" +
                             "\n" +
                             "> Use --help for more information.\n", exception.getMessage());
    }


    @Test
    public void testSuggestionForFullFlag() {
        String[] args = {"-f", "/to/file", "--sve", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("--file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("--save", "s", null);

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("\n<!> unknown flag: --sve\n" +
                             "> did you mean: --save ?\n" +
                             "\n" +
                             "> Use --help for more information.\n", exception.getMessage());
    }

    @Test
    public void testMisspelledHelp() {
        String[] args = {"--hp"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("--file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("--save", "s", null);

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("\n<!> unknown flag: --hp\n" +
                             "> did you mean: --help ?\n" +
                             "\n" +
                             "> Use --help for more information.\n", exception.getMessage());
    }

    @Test
    public void testMissingArgument() {
        String[] args = {"--file", "--save", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "m", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "s", "descr");

        Exception exception = assertThrows(MissingArgArgsException.class, parser::parseUnchecked);
        assertEquals(new MissingArgArgsException("--file").getMessage(), exception.getMessage());
    }

    @Test
    public void testMissingLastArgument() {
        String[] args = {"--file", "file.txt", "--save"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("--file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("--save", "s", "descr");

        Exception exception = assertThrows(MissingArgArgsException.class, parser::parseUnchecked);
        assertEquals(new MissingArgArgsException("--save").getMessage(), exception.getMessage());
    }

    @Test
    public void testMandatoryArgMissing() {
        String[] args = {"--file", "file.txt", "--optional", "optional.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "s", "descr");
        Parameter<String> optional = parser.addMandatoryStringParameter("optional", "o", "descr");

        Exception exception = assertThrows(MandatoryArgNotProvidedArgsException.class, parser::parseUnchecked);
        assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save").getMessage(), exception.getMessage());
    }

    @Test
    public void testTooManyArguments() {
        String[] args = {"--file", "file.txt", "--save", "save.txt", "extra"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "s", "descr");

        Exception exception = assertThrows(TooManyArgumentsArgsException.class, parser::parseUnchecked);
        assertEquals(new TooManyArgumentsArgsException("--save").getMessage(), exception.getMessage());
    }

    @Test
    public void testGetArgumentAsDoubleWithWrongInput() {
        String[] args = {"--file", "file.txt", "--double", "5.5.5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<Double> doub = parser.addMandatoryDoubleParameter("double", "d", "descr");

        Exception exception = assertThrows(InvalidArgTypeArgsException.class, parser::parseUnchecked);
        assertEquals(new InvalidArgTypeArgsException("--double", "Double", "Unsupported type!").getMessage(), exception.getMessage());
    }

    @Test
    public void testHelp() {
        String[] args = {"-pf4", "--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Double> doub = parser.addDefaultDoubleParameter("parameterFlag4", "pf4", "description", 5.6);

        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        System.out.println(exception.getMessage());
    }

    @Test
    public void testLargerHelp() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addDefaultStringParameter("file", "s", "this is a super long description that forces the Help printout to introduce a newline", "/home/user/projects/one/two/my_project/source/main/java/com/example/myapp/ExampleClassThatWonTDoAnythingElseThanBeeingAnExample.java");
        Parameter<Double> doub = parser.addOptionalDoubleParameter("double", "d", "this one is optional");
        Parameter<Integer> integer = parser.addDefaultIntegerParameter("integer", "i", null, 5);
        Parameter<String[]> stringArr = parser.addStringArrayParameter("array", "a", "array of at least 2 strings", true);

        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        System.out.println(exception.getMessage());
    }

    @Test
    public void testSameFlagNameDefinitionException() {
        String[] args = {"--file", "file.txt", "--file2", "file2.txt"};
        ArgsParser parser = new ArgsParser(args);

        parser.addMandatoryStringParameter("file", "f", "descr");

        // Hier wird erwartet, dass beim Hinzufügen eines Parameters mit demselben Namen eine IllegalArgumentException geworfen wird
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                parser.addMandatoryStringParameter("file", "f2", "descr")
        );

        assertEquals("Flag already exists: --file", exception.getMessage());
    }

    @Test
    public void testNoFlags() {
        String[] args = {"file.txt"};
        ArgsParser parser = new ArgsParser(args);

        parser.addMandatoryStringParameter("file", "f", "descr");

        Exception exception = assertThrows(MandatoryArgNotProvidedArgsException.class, parser::parseUnchecked);
        assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--file").getMessage(), exception.getMessage());
    }

    @Test
    public void testMultipleMandatoryArgumentsMissing() {
        String[] args = {"--load", "file.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> load = parser.addMandatoryStringParameter("load", "l", "descr");
        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "s", "descr");

        Exception exception = assertThrows(MandatoryArgNotProvidedArgsException.class, parser::parseUnchecked);
        assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save\n--file").getMessage(), exception.getMessage());
    }

    @Test
    public void testHelpForOneFlag() {
        String [] args = {"--file", "--help"};
        ArgsParser parser = new ArgsParser(args);
        
        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        
        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        System.out.println(exception.getMessage());
    }

    @Test
    public void testSuggestionFoMissspelledFlag() {
        String[] args = {"--paraeterflg4", "5.6"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> example = parser.addMandatoryStringParameter("parameterFlag", "pf", "");
        Parameter<Integer> example2 = parser.addOptionalIntegerParameter("parameterFlag2", "pf2", null);
        Parameter<String> example3 = parser.addMandatoryStringParameter("parameterFlag3", "pf3", "This is a description for the parameter");
        Parameter<Double> argWithDefault = parser.addDefaultDoubleParameter("parameterFlag4", "pf4", "description", 5.6);

        ArgsException exception = assertThrows(ArgsException.class, parser::parseUnchecked);
        assertEquals("\n<!> unknown flag: --paraeterflg4\n" +
                             "> did you mean: --parameterFlag4 ?\n" +
                             "\n" +
                             "> Use --help for more information.\n", exception.getMessage());
    }

    @Test
    public void testDoubleParseCall() {
        String[] args = {"--file", "file.txt", "--file2", "file2.txt"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> file2 = parser.addMandatoryStringParameter("file2", "f2", "descr");
        parser.parse();

        Exception exception = assertThrows(IllegalStateException.class, parser::parseUnchecked);
        assertEquals(new IllegalStateException(".parse() was already called!").getMessage(), exception.getMessage());
    }

    @Test
    public void testSameFlagProvidedTwice() {
        String[] args = {"--file", "file.txt", "-f", "file2.txt"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> file2 = parser.addMandatoryStringParameter("file2", "f2", "descr");

        Exception exception = assertThrows(FlagAlreadyProvidedArgsException.class, parser::parseUnchecked);
        assertEquals("\n<!> Redundant specification of arguments to: --file/-f\n" +
                             "\n" +
                             "> Use --help for more information.\n", exception.getMessage());
    }
}
