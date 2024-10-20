import ArgsParser.*;
import ArgsParser.ArgsExceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestArgsParser {

    @Test
    public void testNoArgumentsProvided() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> file = parser.addStringParameter("file", "f", " ",true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new NoArgumentsProvidedArgsException().getMessage(), e.getMessage());
            return;
        }
        String result = file.getArgument();
    }

    @Test
    public void testParameterDotGetArgument() {
        String[] args = {"--file", "file.txt"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> file = parser.addStringParameter("file", "f", " ", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    // write multiple tests with diffrent number of parameters and flags as well as wrong inouts like flag behind flag

    @Test
    public void testUnknownParameter() {
        String[] args = {"-w", "file.txt", "-s", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: -w\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }
    }

    @Test
    public void testGetArgumentWithMultipleFlagsAndWrongInput() {
        String[] args = {"-f", "file.txt", "--save", "save.txt", "-s"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "w", "descr", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"file", "save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"f", "w"}));
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: -s\n" +
                                 "> did you mean: --save ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
            return;
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    @Test
    public void testMissingShorts() {
        String[] args = {"-f", "/to/file", "--save", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("--file", "m", "descr", true);
        Parameter<String> save = parser.addStringParameter("--save", "s", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"file", "save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"m", "s"}));
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: -f\n" +
                                 "> did you mean: --file ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
            return;
        }
        String result = file.getArgument();
    }

    @Test
    public void testSuggestionForFullFlag() {
        String[] args = {"-f", "/to/file", "--sve", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("--file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("--save", "s", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"--file", "--save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"-m", "-s"}));
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: --sve\n" +
                                 "> did you mean: --save ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }
    }

    @Test
    public void testMissspelledHelp() {
        String[] args = {"--hp"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("--file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("--save", "s", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"--file", "--save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"-m", "-s"}));
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: --hp\n" +
                                 "> did you mean: --help ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }
    }

    @Test
    public void testMissingArgument() {
        String[] args = {"--file", "--save", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "m", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MissingArgArgsException("--file").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testMissingLastArgument() {
        String[] args = {"--file", "file.txt", "--save"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("--file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("--save", "s", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MissingArgArgsException("--save").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testMandatoryArgMissing() {
        String[] args = {"--file", "file.txt", "--optional", "optional.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        Parameter<String> optional = parser.addStringParameter("optional", "o", "descr", false);
        try {
            parser.parseUnchecked();
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save").getMessage(), e.getMessage());
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testTooManyArguments() {
        String [] args = {"--file", "file.txt", "--save", "save.txt", "extra"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            assertEquals(new TooManyArgumentsArgsException("--save").getMessage(), e.getMessage());
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetArgumentAsString() {
        String[] args = {"--file", "file.txt", "-int", "5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", true);
        Parameter<Integer> integer = parser.addIntegerParameter("integer", "int", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("file.txt", file.getArgument());
    }

    @Test
    public void testGetArgumentWithGenericType() {
        String[] args = {"--file", "file.txt", "--integer", "5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Integer> integer = parser.addIntegerParameter("integer", "int", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = integer.getArgument() + 5;
        Integer expected = 5 + 5;

        assertEquals(expected, result);
    }

    @Test
    public void testBooleanGetArgument() {
        String[] args = {"--file", "file.txt", "--boolean", "true"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Boolean> bool = parser.addBooleanParameter("boolean", "b", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(bool.getArgument());
    }

    @Test
    public void testGetArgumentAsDouble() {
        String[] args = {"--file", "file.txt", "--double", "5.5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = doub.getArgument() + 5;
        Double expected = 5.5 + 5;

        assertEquals(expected, result);
    }

    @Test
    public void testGetArgumentAsDoubleWithWrongInput() {
        String[] args = {"--file", "file.txt", "--double", "5.5.5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new InvalidArgTypeArgsException("--double", "Double", "multiple points").getMessage(), e.getMessage());
        }
    }

    @Test
    public void useDefaultValue() {
        String[] args = {"--file", "file.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "descr", 12.3);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
        }

        Double expected = 12.3 + 3;
        Double result = doub.getArgument() + 3;

        assertEquals(expected, result);
    }

    @Test
    public void testDefaultOverride() {
        String[] args = {"--file", "file.txt", "--double", "5.5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "descr", 12.3);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
        }

        Double result = doub.getArgument();
        Double expected = 5.5;

        assertEquals(expected, result);
    }

    @Test
    public void testHelp() {
        String[] args = {"--file", "file.txt", "--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descri", "/home/user/projects/one/two/my_project/source/main/java/com/example/myapp/ExampleClassThatWonTDoAnythingElseThanBeeingAnExample.java");
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "des", 12.3);
        try {
            parser.parseUnchecked();
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testLargerHelp() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "s", "aasdijasoidjoai sjdoiajsd oijaosidja oijsdoaijsd oijaojovn eoin oilnsdo vöinasdv", "/home/user/projects/one/two/my_project/source/main/java/com/example/myapp/ExampleClassThatWonTDoAnythingElseThanBeeingAnExample.java");
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", 12.3);
        Parameter<Boolean> bool = parser.addBooleanParameter("boolean", "b", "des", true);
        Parameter<Integer> integer = parser.addIntegerParameter("integer", "i", "des", 5);
        try {
            parser.parseUnchecked();
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testAddIntegerParameterWithDescriptionAndMandatory() {
        String[] args = {"--number", "42"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Integer> number = parser.addIntegerParameter("number", "n", "An integer number", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(Integer.valueOf(42), number.getArgument());
    }

    // Test für addIntegerParameter(String, String, boolean)
    @Test
    public void testAddIntegerParameterMandatory() {
        String[] args = {"--number", "42"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Integer> number = parser.addIntegerParameter("number", "n", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testAddIntegerParameterWithDescriptionAndDefaultValue() {
        String[] args = {"--number", "42"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Integer> number = parser.addIntegerParameter("number", "n", "An integer number", 42);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addIntegerParameter(String, String, Integer)
    @Test
    public void testAddIntegerParameterDefaultValue() {
        String[] args = {"--number", "42"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Integer> number = parser.addIntegerParameter("number", "n", 42);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addDoubleParameter(String, String, String, boolean)
    @Test
    public void testAddDoubleParameterWithDescriptionAndMandatory() {
        String[] args = {"--number", "42.5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Double> number = parser.addDoubleParameter("number", "n", "A double number", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addDoubleParameter(String, String, boolean)
    @Test
    public void testAddDoubleParameterMandatory() {
        String[] args = {"--number", "42.5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Double> number = parser.addDoubleParameter("number", "n", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addDoubleParameter(String, String, String, Double)
    @Test
    public void testAddDoubleParameterWithDescriptionAndDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {});

        Parameter<Double> number = parser.addDoubleParameter("number", "n", "A double number", 42.5);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addDoubleParameter(String, String, Double)
    @Test
    public void testAddDoubleParameterDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {});

        Parameter<Double> number = parser.addDoubleParameter("number", "n", 42.5);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addBooleanParameter(String, String, String, boolean)
    @Test
    public void testAddBooleanParameterWithDescriptionAndMandatory() {
        String[] args = {"--flag", "true"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Boolean> flag = parser.addBooleanParameter("flag", "f", "A boolean flag", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, boolean)
    @Test
    public void testAddBooleanParameterMandatory() {
        String[] args = {"--flag", "true"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Boolean> flag = parser.addBooleanParameter("flag", "f", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, String, Boolean)
    @Test
    public void testAddBooleanParameterWithDescriptionAndDefaultValue() {
        String[] args = {"--flag", "false"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Boolean> flag = parser.addBooleanParameter("flag", "f", "A boolean flag", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = !flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, Boolean)
    @Test
    public void testAddBooleanParameterDefaultValue() {
        String[] args = {"-f", "true"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Boolean> flag = parser.addBooleanParameter("flag", "f", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addCharacterParameter(String, String, String, boolean)
    @Test
    public void testAddCharacterParameterWithDescriptionAndMandatory() {
        String[] args = {"--char", "c"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Character> character = parser.addCharacterParameter("char", "c", "A character", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    // Test für addCharacterParameter(String, String, boolean)
    @Test
    public void testAddCharacterParameterMandatory() {
        String[] args = {"--char", "c"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Character> character = parser.addCharacterParameter("char", "c", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    // Test für addCharacterParameter(String, String, String, Character)
    @Test
    public void testAddCharacterParameterWithDescriptionAndDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {});

        Parameter<Character> character = parser.addCharacterParameter("char", "c", "A character", 'c');
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    // Test für addCharacterParameter(String, String, Character)
    @Test
    public void testAddCharacterParameterDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {});

        Parameter<Character> character = parser.addCharacterParameter("char", "c", 'c');
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    @Test
    public void testStringDefault() {
        ArgsParser parser = new ArgsParser(new String[] {});

        Parameter<String> string = parser.addStringParameter("string", "s", "default");
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        String result = string.getArgument();
        assertEquals("default", result);
    }

    @Test
    public void testBool() {
        String[] args = {"--bool", "true"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Boolean> bool = parser.addBooleanParameter(true, "bool", "b", "descr");
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = bool.getArgument();
        assertTrue(result);
    }

    @Test
    public void testBool2() {
        String[] args = {"--bool", "false"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Boolean> bool = parser.addBooleanParameter(false, "bool", "b");
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = bool.getArgument();
        assertFalse(result);
    }

    @Test
    public void testSameFlagNameException() {
        String[] args = {"--file", "file.txt", "--file", "file2.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            Parameter<String> file2 = parser.addStringParameter("file", "f2", "descr", true);
            parser.parseUnchecked();
        } catch (Exception e) {
            assertEquals("Flag already exists: --file", e.getMessage());
        }
    }

    @Test
    public void testNoFlags() {
        String[] args = {"file.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--file").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testMultipleMandatoryArgumentsMissing() {
        String[] args = {"--load", "file.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> load = parser.addStringParameter("load", "l", "descr", false);
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save\n--file").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testHelpForOneFlag() {
        String [] args = {"--file", "--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testShortFlag() {
        String[] args = {"-pf4", "5.6", "5.6"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> example = parser.addStringParameter("parameterFlag", "pf", true);
        Parameter<Integer> example2 = parser.addIntegerParameter("parameterFlag2", "pf2", false);
        Parameter<String> example3 = parser.addStringParameter("parameterFlag3", "pf3", "This is a description for the parameter", true);
        Parameter<Double> argWithDefault = parser.addDoubleParameter("parameterFlag4", "pf4", "description", 5.6);
        try {
            parser.parseUnchecked();

        } catch (CalledForHelpNotification help) {
            System.out.println(help.getMessage());
            System.exit(0);

        } catch (ArgsException e) {
            assertEquals(new TooManyArgumentsArgsException("-pf4").getMessage(), e.getMessage());
        }

    }

    @Test
    public void testReadmeExample() {
        String[] args = {"--paraeterflg4", "5.6"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> example = parser.addStringParameter("parameterFlag", "pf", true);
        Parameter<Integer> example2 = parser.addIntegerParameter("parameterFlag2", "pf2", false);
        Parameter<String> example3 = parser.addStringParameter("parameterFlag3", "pf3", "This is a description for the parameter", true);
        Parameter<Double> argWithDefault = parser.addDoubleParameter("parameterFlag4", "pf4", "description", 5.6);
        try {
            parser.parseUnchecked();

        } catch (CalledForHelpNotification help) {
            System.out.println(help.getMessage());
            System.exit(0);

        } catch (ArgsException e) {
            assertEquals("\n<!> unknown flag: --paraeterflg4\n" +
                                 "> did you mean: --parameterFlag4 ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }

    }

    @Test
    public void testGetArgument() {
        String[] args = {"--file", "file.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String filePath = parser.getArgumentOf("file");

        assertEquals(file.getArgument(), filePath);
    }

    @Test
    public void testClassCastExceptionInGetArgument() {
        String[] args = {"--file", "file.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Integer filePath = parser.getArgumentOf("file");
        } catch (ClassCastException e) {
            assertEquals(new ClassCastException("class java.lang.String cannot be cast to class java.lang.Integer " +
                                                        "(java.lang.String and java.lang.Integer are in module java.base " +
                                                        "of loader 'bootstrap')").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testDirectParse() {
        String[] args = {"--file", "file.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        parser.parse();

        assertEquals("file.txt", file.getArgument());
    }

    @Test
    public void testStringArrayParameter() {
        String[] args = {"--file", "file1.txt", "file2.txt", "file3.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String[]> files = parser.addStringArrayParameter("file", "f", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] result = files.getArgument();
        String[] expected = new String[] {"file1.txt", "file2.txt", "file3.txt"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testIntegerArrayParameter() {
        String[] args = {"--integer", "1", "2", "3"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<int[]> integers = parser.addIntegerArrayParameter("integer", "f", "descr", true);
        try {
            parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] result = integers.getArgument();
        int[] expected = new int[] {1, 2, 3};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDoubleArrayParameter() {
        String[] args = {"--double", "1.34", "2.45", "3.56"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<double[]> doubles = parser.addDoubleArrayParameter("double", "f", "descr", true);
        try {
            parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[] result = doubles.getArgument();
        double[] expected = new double[] {1.34, 2.45, 3.56};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBooleanArrayParameter() {
        String[] args = {"--boolean", "true", "false"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Boolean[]> booleans = parser.addBooleanArrayParameter("boolean", "f", "descr", true);
        try {
            parser.parseUnchecked();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean[] result = booleans.getArgument();
        Boolean[] expected = new Boolean[] {true, false};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCharacterArrayParameter() {
        String[] args = {"--character", "a", "b", "c"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<char[]> characters = parser.addCharacterArrayParameter("character", "f", "descr", true);
        try {
            parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        char[] result = characters.getArgument();
        char[] expected = new char[] {'a', 'b', 'c'};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testArrayParameterWithMultipleFlags() {
        String[] args = {"--file", "file1.txt", "file2.txt", "file3.txt", "-str", "vierzig"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String[]> files = parser.addStringArrayParameter("file", "f", "descr", true);
        Parameter<String> stringNumber = parser.addStringParameter("string", "str", "descr", true);
        parser.parse();

        String[] result = files.getArgument();
        String[] expected = new String[] {"file1.txt", "file2.txt", "file3.txt"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testTwoArrayParameterBetweenNormalParameters() {
        String[] args = {"--text", "hello friend", "--file", "file1.txt", "file2.txt", "file3.txt", "-n", "12.4", "13.5", "--end", "testEnd"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> text = parser.addStringParameter("text", "t", "descr", true);
        Parameter<String[]> files = parser.addStringArrayParameter("file", "f", "descr", true);
        Parameter<double[]> doubles = parser.addDoubleArrayParameter("double", "n", "descr", true);
        Parameter<String> end = parser.addStringParameter("end", "e", "descr", true);
        parser.parse();

        double[] result = doubles.getArgument();
        double[] expected = new double[] {12.4, 13.5};
        assertArrayEquals(expected, result);
    }

    // tests for the checked parse() method

//    @Test
//    public void testDirectHelp() {
//        ArgsParser parser = new ArgsParser(new String[] {"--help"});
//        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
//        parser.parse();
//    }

//    @Test
//    public void testArgsExceptionWithDirectParse() {
//        ArgsParser parser = new ArgsParser(new String[] {"-f"});
//        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
//        parser.parse();
//    }
}
