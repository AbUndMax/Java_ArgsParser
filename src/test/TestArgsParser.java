import ArgsParser.*;
import ArgsParser.ArgsExceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestArgsParser {

    @BeforeEach
    void setup() {
        try {
            Class<?> cls = Class.forName("ArgsParser.ArgsParser");
            Method resetMethod = cls.getDeclaredMethod("reset");
            resetMethod.setAccessible(true);
            Field field = cls.getDeclaredField("fullFlags");
            field.setAccessible(true);
            Field field2 = cls.getDeclaredField("shortFlags");
            field2.setAccessible(true);
            resetMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNoArgumentsProvided() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", " ",true);
        try {
            ArgsParser.parseUnchecked(new String[]{});
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            assertEquals(new NoArgumentsProvidedArgsException().getMessage(), e.getMessage());
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        }
        String result = file.getArgument();
    }

    @Test
    public void testParameterDotGetArgument() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", " ", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    // write multiple tests with diffrent number of parameters and flags as well as wrong inouts like flag behind flag

    @Test
    public void testUnknownParameter() {


        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("save", "s", "descr", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"file", "save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"f", "s"}));
        try {
            ArgsParser.parseUnchecked(new String[] {"-w", "file.txt", "-s", "save.txt"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: -w\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }
    }

    @Test
    public void testGetArgumentWithMultipleFlagsAndWrongInput() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("save", "w", "descr", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"file", "save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"f", "w"}));
        try {
            ArgsParser.parseUnchecked(new String[] {"-f", "file.txt", "--save", "save.txt", "-s"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: -s\n" +
                                 "> did you mean: --save ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    @Test
    public void testMissingShorts() {
        Parameter<String> file = ArgsParser.addStringParameter("--file", "m", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("--save", "s", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"file", "save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"m", "s"}));
        try {
            ArgsParser.parseUnchecked(new String[]{"-f", "/to/file", "--save", "save.txt"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: -f\n" +
                                 "> did you mean: --file ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }
        String result = file.getArgument();
    }

    @Test
    public void testSuggestionForFullFlag() {
        Parameter<String> file = ArgsParser.addStringParameter("--file", "f", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("--save", "s", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"--file", "--save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"-m", "-s"}));
        try {
            ArgsParser.parseUnchecked(new String[]{"-f", "/to/file", "--sve", "save.txt"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: --sve\n" +
                                 "> did you mean: --save ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }
        String result = file.getArgument();
    }

    @Test
    public void testMissspelledHelp() {
        Parameter<String> file = ArgsParser.addStringParameter("--file", "f", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("--save", "s", true);
        Set<String> fullFlags = new HashSet<>(List.of(new String[]{"--file", "--save"}));
        Set<String> shortFlags = new HashSet<>(List.of(new String[]{"-m", "-s"}));
        try {
            ArgsParser.parseUnchecked(new String[]{"--hp"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("\n<!> unknown flag: --hp\n" +
                                 "> did you mean: --help ?\n" +
                                 "\n" +
                                 "> Use --help for more information.\n", e.getMessage());
        }
        String result = file.getArgument();
    }

    @Test
    public void testMissingArgument() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "m", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("save", "s", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "--save", "save.txt"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MissingArgArgsException("--file").getMessage(), e.getMessage());
        }
        String result = save.getArgument();
    }

    @Test
    public void testMissingLastArgument() {
        Parameter<String> file = ArgsParser.addStringParameter("--file", "f", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("--save", "s", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[]{"--file", "file.txt", "--save"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MissingArgArgsException("--save").getMessage(), e.getMessage());
        }
        String result = save.getArgument();
    }

    @Test
    public void testMandatoryArgMissing() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("save", "s", "descr", true);
        Parameter<String> optional = ArgsParser.addStringParameter("optional", "o", "descr", false);
        try {
            ArgsParser.parseUnchecked(new String[]{"--file", "file.txt", "--optional", "optional.txt"});
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save").getMessage(), e.getMessage());
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testTooManyArguments() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("save", "s", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[]{"--file", "file.txt", "--save", "save.txt", "extra"});
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            assertEquals(new TooManyArgumentsArgsException("--save").getMessage(), e.getMessage());
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetArgumentAsString() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", true);
        Parameter<Integer> integer = ArgsParser.addIntegerParameter("integer", "int", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt", "-int", "5"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    @Test
    public void testGetArgumentWithGenericType() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<Integer> integer = ArgsParser.addIntegerParameter("integer", "int", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt", "--integer", "5"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = integer.getArgument() + 5;
        Integer expected = 5 + 5;

        assertEquals(expected, result);
    }

    @Test
    public void testBooleanGetArgument() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<Boolean> bool = ArgsParser.addBooleanParameter("boolean", "b", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt", "--boolean", "true"});
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(bool.getArgument());
    }

    @Test
    public void testGetArgumentAsDouble() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = ArgsParser.addDoubleParameter("double", "d", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt", "--double", "5.5"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = doub.getArgument() + 5;
        Double expected = 5.5 + 5;

        assertEquals(expected, result);
    }

    @Test
    public void testGetArgumentAsDoubleWithWrongInput() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = ArgsParser.addDoubleParameter("double", "d", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt", "--double", "5.5.5"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new InvalidArgTypeArgsException("--double", "Double", "multiple points").getMessage(), e.getMessage());
        }
    }

    @Test
    public void useDefaultValue() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = ArgsParser.addDoubleParameter("double", "d", "descr", 12.3);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt"});
        } catch (Exception e) {
        }

        Double expected = 12.3 + 3;
        Double result = doub.getArgument() + 3;

        assertEquals(expected, result);
    }

    @Test
    public void testDefaultOverride() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = ArgsParser.addDoubleParameter("double", "d", "descr", 12.3);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt", "--double", "5.5"});
        } catch (Exception e) {
        }

        Double result = doub.getArgument();
        Double expected = 5.5;

        assertEquals(expected, result);
    }

    @Test
    public void testHelp() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descri", "/home/user/projects/one/two/my_project/source/main/java/com/example/myapp/ExampleClassThatWonTDoAnythingElseThanBeeingAnExample.java");
        Parameter<Double> doub = ArgsParser.addDoubleParameter("double", "d", "des", 12.3);
        try {
            ArgsParser.parseUnchecked(new String[] {"--help"});
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testLargerHelp() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "s", "aasdijasoidjoai sjdoiajsd oijaosidja oijsdoaijsd oijaojovn eoin oilnsdo vöinasdv", "/home/user/projects/one/two/my_project/source/main/java/com/example/myapp/ExampleClassThatWonTDoAnythingElseThanBeeingAnExample.java");
        Parameter<Double> doub = ArgsParser.addDoubleParameter("double", "d", 12.3);
        Parameter<Boolean> bool = ArgsParser.addBooleanParameter("boolean", "b", "des", true);
        Parameter<Integer> integer = ArgsParser.addIntegerParameter("integer", "i", "des", 5);
        try {
            ArgsParser.parseUnchecked(new String[] {"--help"});
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testAddIntegerParameterWithDescriptionAndMandatory() {
        Parameter<Integer> number = ArgsParser.addIntegerParameter("number", "n", "An integer number", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--number", "42"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addIntegerParameter(String, String, boolean)
    @Test
    public void testAddIntegerParameterMandatory() {
        Parameter<Integer> number = ArgsParser.addIntegerParameter("number", "n", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--number", "42"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addIntegerParameter(String, String, String, Integer)
    @Test
    public void testAddIntegerParameterWithDescriptionAndDefaultValue() {
        Parameter<Integer> number = ArgsParser.addIntegerParameter("number", "n", "An integer number", 42);
        try {
            ArgsParser.parseUnchecked(new String[] {});
        } catch (Exception e) {
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addIntegerParameter(String, String, Integer)
    @Test
    public void testAddIntegerParameterDefaultValue() {
        Parameter<Integer> number = ArgsParser.addIntegerParameter("number", "n", 42);
        try {
            ArgsParser.parseUnchecked(new String[] {});
        } catch (Exception e) {
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addDoubleParameter(String, String, String, boolean)
    @Test
    public void testAddDoubleParameterWithDescriptionAndMandatory() {
        Parameter<Double> number = ArgsParser.addDoubleParameter("number", "n", "A double number", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--number", "42.5"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addDoubleParameter(String, String, boolean)
    @Test
    public void testAddDoubleParameterMandatory() {
        Parameter<Double> number = ArgsParser.addDoubleParameter("number", "n", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--number", "42.5"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addDoubleParameter(String, String, String, Double)
    @Test
    public void testAddDoubleParameterWithDescriptionAndDefaultValue() {
        Parameter<Double> number = ArgsParser.addDoubleParameter("number", "n", "A double number", 42.5);
        try {
            ArgsParser.parseUnchecked(new String[] {});
        } catch (Exception e) {
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addDoubleParameter(String, String, Double)
    @Test
    public void testAddDoubleParameterDefaultValue() {
        Parameter<Double> number = ArgsParser.addDoubleParameter("number", "n", 42.5);
        try {
            ArgsParser.parseUnchecked(new String[] {});
        } catch (Exception e) {
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addBooleanParameter(String, String, String, boolean)
    @Test
    public void testAddBooleanParameterWithDescriptionAndMandatory() {
        Parameter<Boolean> flag = ArgsParser.addBooleanParameter("flag", "f", "A boolean flag", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--flag", "true"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, boolean)
    @Test
    public void testAddBooleanParameterMandatory() {
        Parameter<Boolean> flag = ArgsParser.addBooleanParameter("flag", "f", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--flag", "true"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, String, Boolean)
    @Test
    public void testAddBooleanParameterWithDescriptionAndDefaultValue() {
        Parameter<Boolean> flag = ArgsParser.addBooleanParameter("flag", "f", "A boolean flag", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--flag", "false"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = !flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, Boolean)
    @Test
    public void testAddBooleanParameterDefaultValue() {
        Parameter<Boolean> flag = ArgsParser.addBooleanParameter("flag", "f", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"-f", "true"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addCharacterParameter(String, String, String, boolean)
    @Test
    public void testAddCharacterParameterWithDescriptionAndMandatory() {
        Parameter<Character> character = ArgsParser.addCharacterParameter("char", "c", "A character", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--char", "c"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    // Test für addCharacterParameter(String, String, boolean)
    @Test
    public void testAddCharacterParameterMandatory() {
        Parameter<Character> character = ArgsParser.addCharacterParameter("char", "c", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--char", "c"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    // Test für addCharacterParameter(String, String, String, Character)
    @Test
    public void testAddCharacterParameterWithDescriptionAndDefaultValue() {
        Parameter<Character> character = ArgsParser.addCharacterParameter("char", "c", "A character", 'c');
        try {
            ArgsParser.parseUnchecked(new String[] {});
        } catch (Exception e) {
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    // Test für addCharacterParameter(String, String, Character)
    @Test
    public void testAddCharacterParameterDefaultValue() {
        Parameter<Character> character = ArgsParser.addCharacterParameter("char", "c", 'c');
        try {
            ArgsParser.parseUnchecked(new String[] {});
        } catch (Exception e) {
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    @Test
    public void testStringDeafult() {
        Parameter<String> string = ArgsParser.addStringParameter("string", "s", "default");
        try {
            ArgsParser.parseUnchecked(new String[] {});
        } catch (Exception e) {
        }
        String result = string.getArgument();
        assertEquals("default", result);
    }

    @Test
    public void testBool() {
        Parameter<Boolean> bool = ArgsParser.addBooleanParameter(true, "bool", "b", "descr");
        try {
            ArgsParser.parseUnchecked(new String[] {"--bool", "true"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = bool.getArgument();
        assertTrue(result);
    }

    @Test
    public void testBool2() {
        Parameter<Boolean> bool = ArgsParser.addBooleanParameter(false, "bool", "b");
        try {
            ArgsParser.parseUnchecked(new String[] {"--bool", "false"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = bool.getArgument();
        assertFalse(result);
    }

    @Test
    public void testSameFlagNameException() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        try {
            Parameter<String> file2 = ArgsParser.addStringParameter("file", "f2", "descr", true);
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt", "--file", "file2.txt"});
        } catch (Exception e) {
            assertEquals("Flag already exists: --file", e.getMessage());
        }
    }

    @Test
    public void testNoFlags() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"file.txt"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--file").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testMultipleMandatoryArgumentsMissing() {
        Parameter<String> load = ArgsParser.addStringParameter("load", "l", "descr", false);
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = ArgsParser.addStringParameter("save", "s", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--load", "file.txt"});
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save\n--file").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testHelpForOneFlag() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "--help"});
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testShortFlag() {
        Parameter<String> example = ArgsParser.addStringParameter("parameterFlag", "pf", true);
        Parameter<Integer> example2 = ArgsParser.addIntegerParameter("parameterFlag2", "pf2", false);
        Parameter<String> example3 = ArgsParser.addStringParameter("parameterFlag3", "pf3", "This is a description for the parameter", true);
        Parameter<Double> argWithDefault = ArgsParser.addDoubleParameter("parameterFlag4", "pf4", "description", 5.6);
        try {
            ArgsParser.parseUnchecked(new String[]{"-pf4", "5.6", "5.6"});

        } catch (CalledForHelpNotification help) {
            System.out.println(help.getMessage());
            System.exit(0);

        } catch (ArgsException e) {
            assertEquals(new TooManyArgumentsArgsException("-pf4").getMessage(), e.getMessage());
        }

    }

    @Test
    public void testReadmeExample() {
        Parameter<String> example = ArgsParser.addStringParameter("parameterFlag", "pf", true);
        Parameter<Integer> example2 = ArgsParser.addIntegerParameter("parameterFlag2", "pf2", false);
        Parameter<String> example3 = ArgsParser.addStringParameter("parameterFlag3", "pf3", "This is a description for the parameter", true);
        Parameter<Double> argWithDefault = ArgsParser.addDoubleParameter("parameterFlag4", "pf4", "description", 5.6);
        try {
            ArgsParser.parseUnchecked(new String[]{"--paraeterflg4", "5.6"});

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
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        String filePath = ArgsParser.getArgumentOf("file");

        assertEquals(file.getArgument(), filePath);
    }

    @Test
    public void testClassCastExceptionInGetArgument() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        try {
            ArgsParser.parseUnchecked(new String[] {"--file", "file.txt"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Integer filePath = ArgsParser.getArgumentOf("file");
        } catch (ClassCastException e) {
            assertEquals(new ClassCastException("class java.lang.String cannot be cast to class java.lang.Integer " +
                                                        "(java.lang.String and java.lang.Integer are in module java.base " +
                                                        "of loader 'bootstrap')").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testDirectParse() {
        Parameter<String> file = ArgsParser.addStringParameter("file", "f", "descr", true);
        ArgsParser.parse(new String[] {"--file", "file.txt"});

        assertEquals("file.txt", file.getArgument());
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
