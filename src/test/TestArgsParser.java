import ArgsParser.*;
import ArgsParser.ArgsExceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestArgsParser {

    @Test
    public void testNoArgumentsProvided() {
        ArgsParser parser = new ArgsParser(new String[] {});
        Parameter<String> file = parser.addStringParameter("file", "f", " ",true);
        try {
            parser.parseAndThrow();
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
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", " ", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    // write multiple tests with diffrent number of parameters and flags as well as wrong inouts like flag behind flag

    @Test
    public void testUnknownParameter() {
        ArgsParser parser = new ArgsParser(new String[] {"-f", "file.txt", "-s", "save.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new UnknownFlagArgsException("-f").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testGetArgumentWithMultipleFlagsAndWrongInput() {
        ArgsParser parser = new ArgsParser(new String[] {"-f", "file.txt", "--save", "save.txt", "-s"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "w", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new UnknownFlagArgsException("-s").getMessage(), e.getMessage());
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    @Test
    public void testMissingArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "--save", "save.txt"});
        Parameter<String> file = parser.addStringParameter("file", "m", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MissingArgArgsException("--file").getMessage(), e.getMessage());
        }
        String result = save.getArgument();
    }

    @Test
    public void testMissingLastArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--file", "file.txt", "--save"});
        Parameter<String> file = parser.addStringParameter("--file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("--save", "s", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MissingArgArgsException("--save").getMessage(), e.getMessage());
        }
        String result = save.getArgument();
    }

    @Test
    public void testMissingShorts() {
        ArgsParser parser = new ArgsParser(new String[]{"-f", "/to/file", "--save", "save.txt"});
        Parameter<String> file = parser.addStringParameter("--file", "m", "descr", true);
        Parameter<String> save = parser.addStringParameter("--save", "s", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new UnknownFlagArgsException("-f").getMessage(), e.getMessage());
        }
        String result = file.getArgument();
    }

    @Test
    public void testMandatoryArgMissing() {
        ArgsParser parser = new ArgsParser(new String[]{"--file", "file.txt", "--optional", "optional.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        Parameter<String> optional = parser.addStringParameter("optional", "o", "descr", false);
        try {
            parser.parseAndThrow();
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save").getMessage(), e.getMessage());
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testTooManyArguments() {
        ArgsParser parser = new ArgsParser(new String[]{"--file", "file.txt", "--save", "save.txt", "extra"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            assertEquals(new TooManyArgumentsArgsException("--save").getMessage(), e.getMessage());
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetArgumentAsString() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "-int", "5"});
        Parameter<String> file = parser.addStringParameter("file", "f", true);
        Parameter<Integer> integer = parser.addIntegerParameter("integer", "int", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    @Test
    public void testGetArgumentWithGenericType() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--integer", "5"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Integer> integer = parser.addIntegerParameter("integer", "int", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = integer.getArgument() + 5;
        Integer expected = 5 + 5;

        assertEquals(expected, result);
    }

    @Test
    public void testBooleanGetArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--boolean", "true"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Boolean> bool = parser.addBooleanParameter("boolean", "b", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(bool.getArgument());
    }

    @Test
    public void testGetArgumentAsDouble() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--double", "5.5"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = doub.getArgument() + 5;
        Double expected = 5.5 + 5;

        assertEquals(expected, result);
    }

    @Test
    public void testGetArgumentAsDoubleWithWrongInput() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--double", "5.5.5"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new InvalidArgTypeArgsException("--double", "Double", "multiple points").getMessage(), e.getMessage());
        }
    }

    @Test
    public void useDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "descr", 12.3);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
        }

        Double expected = 12.3 + 3;
        Double result = doub.getArgument() + 3;

        assertEquals(expected, result);
    }

    @Test
    public void testDefaultOverride() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--double", "5.5"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "descr", 12.3);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
        }

        Double result = doub.getArgument();
        Double expected = 5.5;

        assertEquals(expected, result);
    }

    @Test
    public void testHelp() {
        ArgsParser parser = new ArgsParser(new String[] {"--help"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descri", "/home/user/projects/one/two/my_project/source/main/java/com/example/myapp/ExampleClassThatWonTDoAnythingElseThanBeeingAnExample.java");
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", "des", 12.3);
        try {
            parser.parseAndThrow();
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testLargerHelp() {
        ArgsParser parser = new ArgsParser(new String[] {"--help"});
        Parameter<String> file = parser.addStringParameter("file", "s", "aasdijasoidjoai sjdoiajsd oijaosidja oijsdoaijsd oijaojovn eoin oilnsdo vöinasdv", "/home/user/projects/one/two/my_project/source/main/java/com/example/myapp/ExampleClassThatWonTDoAnythingElseThanBeeingAnExample.java");
        Parameter<Double> doub = parser.addDoubleParameter("double", "d", 12.3);
        Parameter<Boolean> bool = parser.addBooleanParameter("boolean", "b", "des", true);
        Parameter<Integer> integer = parser.addIntegerParameter("integer", "i", "des", 5);
        try {
            parser.parseAndThrow();
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testAddIntegerParameterWithDescriptionAndMandatory() {
        ArgsParser parser = new ArgsParser(new String[] {"--number", "42"});
        Parameter<Integer> number = parser.addIntegerParameter("number", "n", "An integer number", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addIntegerParameter(String, String, boolean)
    @Test
    public void testAddIntegerParameterMandatory() {
        ArgsParser parser = new ArgsParser(new String[] {"--number", "42"});
        Parameter<Integer> number = parser.addIntegerParameter("number", "n", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addIntegerParameter(String, String, String, Integer)
    @Test
    public void testAddIntegerParameterWithDescriptionAndDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {});
        Parameter<Integer> number = parser.addIntegerParameter("number", "n", "An integer number", 42);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addIntegerParameter(String, String, Integer)
    @Test
    public void testAddIntegerParameterDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {});
        Parameter<Integer> number = parser.addIntegerParameter("number", "n", 42);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
        }
        Integer result = number.getArgument();
        assertEquals(Integer.valueOf(42), result);
    }

    // Test für addDoubleParameter(String, String, String, boolean)
    @Test
    public void testAddDoubleParameterWithDescriptionAndMandatory() {
        ArgsParser parser = new ArgsParser(new String[] {"--number", "42.5"});
        Parameter<Double> number = parser.addDoubleParameter("number", "n", "A double number", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addDoubleParameter(String, String, boolean)
    @Test
    public void testAddDoubleParameterMandatory() {
        ArgsParser parser = new ArgsParser(new String[] {"--number", "42.5"});
        Parameter<Double> number = parser.addDoubleParameter("number", "n", true);
        try {
            parser.parseAndThrow();
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
            parser.parseAndThrow();
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
            parser.parseAndThrow();
        } catch (Exception e) {
        }
        Double result = number.getArgument();
        assertEquals(Double.valueOf(42.5), result);
    }

    // Test für addBooleanParameter(String, String, String, boolean)
    @Test
    public void testAddBooleanParameterWithDescriptionAndMandatory() {
        ArgsParser parser = new ArgsParser(new String[] {"--flag", "true"});
        Parameter<Boolean> flag = parser.addBooleanParameter("flag", "f", "A boolean flag", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, boolean)
    @Test
    public void testAddBooleanParameterMandatory() {
        ArgsParser parser = new ArgsParser(new String[] {"--flag", "true"});
        Parameter<Boolean> flag = parser.addBooleanParameter("flag", "f", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, String, Boolean)
    @Test
    public void testAddBooleanParameterWithDescriptionAndDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {"--flag", "false"});
        Parameter<Boolean> flag = parser.addBooleanParameter("flag", "f", "A boolean flag", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = !flag.getArgument();
        assertTrue(result);
    }

    // Test für addBooleanParameter(String, String, Boolean)
    @Test
    public void testAddBooleanParameterDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {"-f", "true"});
        Parameter<Boolean> flag = parser.addBooleanParameter("flag", "f", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = flag.getArgument();
        assertTrue(result);
    }

    // Test für addCharacterParameter(String, String, String, boolean)
    @Test
    public void testAddCharacterParameterWithDescriptionAndMandatory() {
        ArgsParser parser = new ArgsParser(new String[] {"--char", "c"});
        Parameter<Character> character = parser.addCharacterParameter("char", "c", "A character", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    // Test für addCharacterParameter(String, String, boolean)
    @Test
    public void testAddCharacterParameterMandatory() {
        ArgsParser parser = new ArgsParser(new String[] {"--char", "c"});
        Parameter<Character> character = parser.addCharacterParameter("char", "c", true);
        try {
            parser.parseAndThrow();
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
            parser.parseAndThrow();
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
            parser.parseAndThrow();
        } catch (Exception e) {
        }
        Character result = character.getArgument();
        assertEquals(Character.valueOf('c'), result);
    }

    @Test
    public void testStringDeafult() {
        ArgsParser parser = new ArgsParser(new String[] {});
        Parameter<String> string = parser.addStringParameter("string", "s", "default");
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
        }
        String result = string.getArgument();
        assertEquals("default", result);
    }

    @Test
    public void testBool() {
        ArgsParser parser = new ArgsParser(new String[] {"--bool", "true"});
        Parameter<Boolean> bool = parser.addBooleanParameter(true, "bool", "b", "descr");
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = bool.getArgument();
        assertTrue(result);
    }

    @Test
    public void testBool2() {
        ArgsParser parser = new ArgsParser(new String[] {"--bool", "false"});
        Parameter<Boolean> bool = parser.addBooleanParameter(false, "bool", "b");
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean result = bool.getArgument();
        assertFalse(result);
    }

    @Test
    public void testSameFlagNameException() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--file", "file2.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            Parameter<String> file2 = parser.addStringParameter("file", "f2", "descr", true);
            parser.parseAndThrow();
        } catch (Exception e) {
            assertEquals("Flag already exists: file", e.getMessage());
        }
    }

    @Test
    public void testNoFlags() {
        ArgsParser parser = new ArgsParser(new String[] {"file.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--file").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testMultipleMandatoryArgumentsMissing() {
        ArgsParser parser = new ArgsParser(new String[] {"--load", "file.txt"});
        Parameter<String> load = parser.addStringParameter("load", "l", "descr", false);
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        Parameter<String> save = parser.addStringParameter("save", "s", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save\n--file").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testHelpForOneFlag() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "--help"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (CalledForHelpNotification e) {
            System.out.println(e.getMessage());
        } catch (ArgsException e) {
        }
    }

    @Test
    public void testShortFlag() {
        ArgsParser parser = new ArgsParser(new String[]{"-pf4", "5.6", "5.6"});
        Parameter<String> example = parser.addStringParameter("parameterFlag", "pf", true);
        Parameter<Integer> example2 = parser.addIntegerParameter("parameterFlag2", "pf2", false);
        Parameter<String> example3 = parser.addStringParameter("parameterFlag3", "pf3", "This is a description for the parameter", true);
        Parameter<Double> argWithDefault = parser.addDoubleParameter("parameterFlag4", "pf4", "description", 5.6);
        try {
            parser.parseAndThrow();

        } catch (CalledForHelpNotification help) {
            System.out.println(help.getMessage());
            System.exit(0);

        } catch (ArgsException e) {
            assertEquals(new TooManyArgumentsArgsException("-pf4").getMessage(), e.getMessage());
        }

    }

    @Test
    public void testGetArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            parser.parseAndThrow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String filePath = ArgsParser.getArgumentOf("file");

        assertEquals(file.getArgument(), filePath);
    }

    @Test
    public void testClassCastExceptionInGetArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        try {
            parser.parseAndThrow();
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
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter<String> file = parser.addStringParameter("file", "f", "descr", true);
        parser.parse();

        assertEquals("file.txt", file.getArgument());
    }

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
