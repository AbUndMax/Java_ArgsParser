import ArgsParser.*;
import ArgsParser.ArgsExceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestArgsExceptions {

// CalledForHelpNotifications

    @Test
    public void testHelpForSingleFlag() {
        String[] args = {"-pf4", "--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<Double> doub = parser.addDefaultDoubleParameter("parameterFlag4", "pf4", "description", 5.6);

        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                                   (!)=mandatory | (?)=optional
                #
                ###  --parameterFlag4  -pf4  [d]  (?)  description
                #                            default:  5.6
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
        System.out.println(exception.getMessage());
    }

    @Test
    public void testHelpWithEachParameter() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> mandatoryString = parser.addMandatoryStringParameter("mandatory_string", "ms", "mandatory string description");
        Parameter<String> optionalString = parser.addOptionalStringParameter("optional_string", "os", "optional string description");
        Parameter<String> defaultString = parser.addDefaultStringParameter("default_string", "ds", "default string description", "default");
        
        Parameter<Double> mandatoryDouble = parser.addMandatoryDoubleParameter("mandatory_double", "md", "mandatory double description");
        Parameter<Double> optionalDouble = parser.addOptionalDoubleParameter("optional_double", "d", "this one is optional");
        Parameter<Double> defaultDouble = parser.addDefaultDoubleParameter("default_double", "dd", "default double description", 7.89);

        Parameter<Integer> mandatoryInteger = parser.addMandatoryIntegerParameter("mandatory_integer", "mi", "mandatory integer description");
        Parameter<Integer> optionalInteger = parser.addOptionalIntegerParameter("optional_integer", "oi", "optional integer description");
        Parameter<Integer> defaultInteger = parser.addDefaultIntegerParameter("default_integer", "i", null, 5);

        Parameter<Character> mandatoryChar = parser.addMandatoryCharacterParameter("mandatory_character", "mc", "mandatory character description");
        Parameter<Character> optionalChar = parser.addOptionalCharacterParameter("optional_character", "oc", "optional character description");
        Parameter<Character> defaultChar = parser.addDefaultCharacterParameter("default_character", "dc", "default character description", 'a');

        Parameter<Boolean> mandatoryBoolean = parser.addMandatoryBooleanParameter("mandatory_boolean", "mb", "mandatory boolean description");
        Parameter<Boolean> optionalBoolean = parser.addOptionalBooleanParameter("optional_boolean", "ob", "optional boolean description");
        Parameter<Boolean> defaultBoolean = parser.addDefaultBooleanParameter("default_boolean", "db", "default boolean description", true);
        
        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                                   (!)=mandatory | (?)=optional
                #
                #                                      Available Parameters:
                #
                ###  --mandatory_string     -ms  [s]  (!)  mandatory string description
                #
                ###  --optional_string      -os  [s]  (?)  optional string description
                #
                ###  --default_string       -ds  [s]  (?)  default string description
                #                                default:  default
                #
                ###  --mandatory_double     -md  [d]  (!)  mandatory double description
                #
                ###  --optional_double      -d   [d]  (?)  this one is optional
                #
                ###  --default_double       -dd  [d]  (?)  default double description
                #                                default:  7.89
                #
                ###  --mandatory_integer    -mi  [i]  (!)  mandatory integer description
                #
                ###  --optional_integer     -oi  [i]  (?)  optional integer description
                #
                ###  --default_integer      -i   [i]  (?)  No description available!
                #                                default:  5
                #
                ###  --mandatory_character  -mc  [c]  (!)  mandatory character description
                #
                ###  --optional_character   -oc  [c]  (?)  optional character description
                #
                ###  --default_character    -dc  [c]  (?)  default character description
                #                                default:  a
                #
                ###  --mandatory_boolean    -mb  [b]  (!)  mandatory boolean description
                #
                ###  --optional_boolean     -ob  [b]  (?)  optional boolean description
                #
                ###  --default_boolean      -db  [b]  (?)  default boolean description
                #                                default:  true
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
        System.out.println(exception.getMessage());
    }

    @Test
    public void testHelpWithArrayParameters() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String[]> stringArray = parser.addStringArrayParameter("stringArrayParam", "hap", "descr", true);
        Parameter<String[]> defaultStringArray = parser.addDefaultStringArrayParameter("stringArray", "h", "descr", new String[]{"string1", "string2", "string3"});
        Parameter<Double[]> doubleArray = parser.addDoubleArrayParameter("doubleArrayParam", "dap", "description", false);
        Parameter<Double[]> defaultDoubleArray = parser.addDefaultDoubleArrayParameter("doubleArray", "da", "description", new Double[]{1.1, 2.2, 3.3});
        Parameter<Integer[]> integerArray = parser.addIntegerArrayParameter("integerArrayParam", "iap", "description", true);
        Parameter<Integer[]> defaultIntegerArray = parser.addDefaultIntegerArrayParameter("integerArray", "ia", "description", new Integer[]{1, 2, 3});
        Parameter<Boolean[]> booleanArray = parser.addBooleanArrayParameter("booleanArrayParam", "bap", "description", false);
        Parameter<Boolean[]> defaultBooleanArray = parser.addDefaultBooleanArrayParameter("booleanArray", "ba", "description", new Boolean[]{true, false, true});
        Parameter<Character[]> characterArray = parser.addCharacterArrayParameter("characterArrayParam", "cap", "description", true);
        Parameter<Character[]> defaultCharacterArray = parser.addDefaultCharacterArrayParameter("characterArray", "ca", "description", new Character[]{'a', 'b', 'c'});

        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                                   (!)=mandatory | (?)=optional
                #
                #                                      Available Parameters:
                #
                ###  --stringArrayParam     -hap  [s+] (!)  descr
                #
                ###  --stringArray          -h    [s+] (?)  descr
                #                                 default:  [string1, string2, string3]
                #
                ###  --doubleArrayParam     -dap  [d+] (?)  description
                #
                ###  --doubleArray          -da   [d+] (?)  description
                #                                 default:  [1.1, 2.2, 3.3]
                #
                ###  --integerArrayParam    -iap  [i+] (!)  description
                #
                ###  --integerArray         -ia   [i+] (?)  description
                #                                 default:  [1, 2, 3]
                #
                ###  --booleanArrayParam    -bap  [b+] (?)  description
                #
                ###  --booleanArray         -ba   [b+] (?)  description
                #                                 default:  [true, false, true]
                #
                ###  --characterArrayParam  -cap  [c+] (!)  description
                #
                ###  --characterArray       -ca   [c+] (?)  description
                #                                 default:  [a, b, c]
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
        System.out.println(exception.getMessage());
    }

    @Test
    public void testHelpWithCommand() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser(args);

        Command command = parser.addCommand("command", "c", "this is a command");

        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                                   (!)=mandatory | (?)=optional
                #
                ###  command  c            this is a command
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testHelpWithNewLineInDescription() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> longString = parser.addDefaultStringParameter("longString", "ls",
                                                                        "This description is so long, it will " +
                                                                                "force the automatic help printout to " +
                                                                                "introduce a new line and still have a nice look :)",
                                                                        "this/path/is/so/long/it/is/actually/longer/than/any" +
                                                                                "/existing/path/that/I/have/on/my/PC/Do/You/Know/The/Word" +
                                                                                "Oberwesedampfschifffahrtsgesellschaftskapitän");
        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                                   (!)=mandatory | (?)=optional
                #
                ###  --longString  -ls  [s]  (?)  This description is so long, it will force the automatic help
                #                                 printout to introduce a new line and still have a nice look :)
                #                       default:  this/path/is/so/long/it/is/actually/longer/than/any/existing/path/
                #                                 that/I/have/on/my/PC/Do/You/Know/The/WordOberwesedampfschifffahrt
                #                                 sgesellschaftskapitän
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
        System.out.println(exception.getMessage());
    }

    @Test
    public void testNoDescriptionAvailable() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> strParam = parser.addMandatoryStringParameter("string", "s", "");
        Parameter<Integer> intParam = parser.addOptionalIntegerParameter("integer", "i", null);
        Parameter<Character> charParam = parser.addDefaultCharacterParameter("char", "c", null, 'c');
        Parameter<Boolean> booleanParam = parser.addOptionalBooleanParameter("boolean", "b", "");
        Parameter<Double> doubleParam = parser.addMandatoryDoubleParameter("double", "d", null);
        Parameter<String[]> strArrParam = parser.addStringArrayParameter("stringArray", "sArr", null, false);

        Exception exception = assertThrows(CalledForHelpNotification.class, parser::parseUnchecked);
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                                   (!)=mandatory | (?)=optional
                #
                #                                      Available Parameters:
                #
                ###  --string       -s     [s]  (!)  No description available!
                #
                ###  --integer      -i     [i]  (?)  No description available!
                #
                ###  --char         -c     [c]  (?)  No description available!
                #                          default:  c
                #
                ###  --boolean      -b     [b]  (?)  No description available!
                #
                ###  --double       -d     [d]  (!)  No description available!
                #
                ###  --stringArray  -sArr  [s+] (?)  No description available!
                #
                ####################################################################################################""";
        System.out.println(exception.getMessage());
    }

// HelpAtWrongPositionInArgsException

    @Test
    public void testHelpAtWrongPosition() {
        String[] args = {"--file", "file.txt", "--help"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");

        Exception exception = assertThrows(HelpAtWrongPositionArgsException.class, parser::parseUnchecked);
    }

    @Test
    public void testHelpBeforeFlag() {
        String[] args = {"--help", "--file"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");

        Exception exception = assertThrows(HelpAtWrongPositionArgsException.class, parser::parseUnchecked);
    }

// FlagAlreadyProvidedArgsException

    @Test
    public void testSameFlagProvidedTwice() {
        String[] args = {"--file", "file.txt", "-f", "file2.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> file2 = parser.addMandatoryStringParameter("file2", "f2", "descr");

        Exception exception = assertThrows(FlagAlreadyProvidedArgsException.class, parser::parseUnchecked);
        assertEquals("""
                             
                             <!> Redundant specification of arguments to: --file/-f
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

// InvalidArgTypeArgsException

    @Test
    public void testGetArgumentAsDoubleWithWrongInput() {
        String[] args = {"--file", "file.txt", "--double", "5.5.5"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<Double> doub = parser.addMandatoryDoubleParameter("double", "d", "descr");

        Exception exception = assertThrows(InvalidArgTypeArgsException.class, parser::parseUnchecked);
        assertEquals(new InvalidArgTypeArgsException("--double", "Double", "Unsupported type!").getMessage(), exception.getMessage());
    }

// MandatoryArgNotProvidedArgsException

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
    public void testNoFlags() {
        String[] args = {"file.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> string = parser.addMandatoryStringParameter("file", "f", "descr");

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        String expected = """
                
                <!> unknown flag or command: file.txt
                > did you mean: --file ?
                
                > flag or command expected in first position!
                
                > Use --help for more information.
                """;
        assertEquals(expected, exception.getMessage());
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

// MissingArgArgsException

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

// NoArgumentsProvidedArgsException

    @Test
    public void testNoArgumentsProvidedWithMandatoryParams() {
        String[] args = {};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> mandatoryParam = parser.addMandatoryStringParameter("optional", "o", null);

        Exception exception = assertThrows(NoArgumentsProvidedArgsException.class, parser::parseUnchecked);
        assertEquals("""
                             
                             <!> No arguments provided
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testNoArgumentsProvidedWithOnlyOptionalParams() { // just checks if the programs still runs if only optionals are defined
        String[] args = {};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> stringParam = parser.addOptionalStringParameter("optional", "o", null);

        assertDoesNotThrow(parser::parseUnchecked);
    }

// TooManyArgumentsArgsException

    @Test
    public void testTooManyArguments() {
        String[] args = {"--file", "file.txt", "--save", "save.txt", "extra"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "s", "descr");

        Exception exception = assertThrows(TooManyArgumentsArgsException.class, parser::parseUnchecked);
        assertEquals(new TooManyArgumentsArgsException("--save").getMessage(), exception.getMessage());
    }

// UnknownFlagArgsException

    @Test
    public void testUnknownFlag() {
        String[] args = {"-w", "file.txt", "-s", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "s", "descr");

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("""
                             
                             <!> unknown flag or command: -w
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testUnknownFlagWithMultipleFlagsAndWrongInput() {
        String[] args = {"-f", "file.txt", "--save", "save.txt", "-s"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("save", "w", "descr");

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("""
                             
                             <!> unknown flag or command: -s
                             > did you mean: --save ?
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testMissingShorts() {
        String[] args = {"-f", "/to/file", "--save", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("--file", "m", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("--save", "s", null);

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("""
                             
                             <!> unknown flag or command: -f
                             > did you mean: --file ?
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testMisspelledHelp() {
        String[] args = {"--hp"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("--file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("--save", "s", null);

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("""
                             
                             <!> unknown flag or command: --hp
                             > did you mean: --help ?
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testSuggestionForFullFlag() {
        String[] args = {"-f", "/to/file", "--sve", "save.txt"};
        ArgsParser parser = new ArgsParser(args);

        Parameter<String> file = parser.addMandatoryStringParameter("--file", "f", "descr");
        Parameter<String> save = parser.addMandatoryStringParameter("--save", "s", null);

        Exception exception = assertThrows(UnknownFlagArgsException.class, parser::parseUnchecked);
        assertEquals("""
                             
                             <!> unknown flag or command: --sve
                             > did you mean: --save ?
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

// Runtime Exceptions

    @Test
    public void testDefinitionOfSameFlagTwice() {
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
    public void testArgsIsNull() {
        String[] args = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new ArgsParser(args));
        assertEquals("Args cannot be null!", exception.getMessage());
    }

    @Test
    public void testCommand() {
        String[] args = new String[]{"command"};
        ArgsParser parser = new ArgsParser(args);
        Command command = parser.addCommand("command", "c", "test command");
        parser.parse();

        assertTrue(command.isProvided());
    }
}
