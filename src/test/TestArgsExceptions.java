import ArgsParser.*;
import ArgsParser.ArgsExceptions.*;
import ArgsParser.ParameterTypes.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class TestArgsExceptions {

// CalledForHelpNotifications

    @Test
    public void testHelpForSingleFlag() {
        String[] args = {"-pf4", "--help"};
        ArgsParser parser = new ArgsParser();

        DblParameter doub = parser.addParameter(new DblParameter(5.6, "parameterFlag4", "pf4", "description"));

        Exception exception = assertThrows(CalledForHelpNotification.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                            (!)=mandatory | (?)=optional | (/)=command
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
        ArgsParser parser = new ArgsParser();

        // -------------------------------------------------------------------
        // STRING
        // -------------------------------------------------------------------

        // Mandatory string
        StrParameter mandatoryString = parser.addParameter(
                new StrParameter("mandatory_string", "ms", "mandatory string description", true)
        );
        // Optional string
        StrParameter optionalString = parser.addParameter(
                new StrParameter("optional_string", "os", "optional string description", false)
        );
        // Default string
        StrParameter defaultString = parser.addParameter(
                new StrParameter("default", "default_string", "ds", "default string description")
        );


        // -------------------------------------------------------------------
        // DOUBLE
        // -------------------------------------------------------------------

        // Mandatory double
        DblParameter mandatoryDouble = parser.addParameter(
                new DblParameter("mandatory_double", "md", "mandatory double description", true)
        );
        // Optional double
        DblParameter optionalDouble = parser.addParameter(
                new DblParameter("optional_double", "d", "this one is optional", false)
        );
        // Default double
        DblParameter defaultDouble = parser.addParameter(
                new DblParameter(7.89, "default_double", "dd", "default double description")
        );


        // -------------------------------------------------------------------
        // INTEGER
        // -------------------------------------------------------------------

        // Mandatory integer
        IntParameter mandatoryInteger = parser.addParameter(
                new IntParameter("mandatory_integer", "mi", "mandatory integer description", true)
        );
        // Optional integer
        IntParameter optionalInteger = parser.addParameter(
                new IntParameter("optional_integer", "oi", "optional integer description", false)
        );
        // Default integer
        IntParameter defaultInteger = parser.addParameter(
                new IntParameter(5, "default_integer", "i", "No description available!")
        );


        // -------------------------------------------------------------------
        // CHARACTER
        // -------------------------------------------------------------------

        // Mandatory character
        ChrParameter mandatoryChar = parser.addParameter(
                new ChrParameter("mandatory_character", "mc", "mandatory character description", true)
        );
        // Optional character
        ChrParameter optionalChar = parser.addParameter(
                new ChrParameter("optional_character", "oc", "optional character description", false)
        );
        // Default character
        ChrParameter defaultChar = parser.addParameter(
                new ChrParameter('a', "default_character", "dc", "default character description")
        );


        // -------------------------------------------------------------------
        // BOOLEAN
        // -------------------------------------------------------------------

        // Mandatory boolean
        BolParameter mandatoryBoolean = parser.addParameter(
                new BolParameter("mandatory_boolean", "mb", "mandatory boolean description", true)
        );
        // Optional boolean
        BolParameter optionalBoolean = parser.addParameter(
                new BolParameter("optional_boolean", "ob", "optional boolean description", false)
        );
        // Default boolean
        BolParameter defaultBoolean = parser.addParameter(
                new BolParameter(true, "default_boolean", "db", "default boolean description")
        );


        // -------------------------------------------------------------------
        // FLOAT (NEW)
        // -------------------------------------------------------------------

        // Mandatory float
        FltParameter mandatoryFloat = parser.addParameter(
                new FltParameter("mandatory_float", "mf", "mandatory float description", true)
        );
        // Optional float
        FltParameter optionalFloat = parser.addParameter(
                new FltParameter("optional_float", "of", "optional float description", false)
        );
        // Default float
        FltParameter defaultFloat = parser.addParameter(
                new FltParameter(3.14f, "default_float", "df", "default float description")
        );


        // -------------------------------------------------------------------
        // PATH (NEW)
        // -------------------------------------------------------------------

        // Mandatory path
        PthParameter mandatoryPath = parser.addParameter(
                new PthParameter("mandatory_path", "mp", "mandatory path description", true, false)
        );
        // Optional path
        PthParameter optionalPath = parser.addParameter(
                new PthParameter("optional_path", "op", "optional path description", false, false)
        );
        // Default path
        PthParameter defaultPath = parser.addParameter(
                new PthParameter(Path.of("/some/default/path"), "default_path", "dp", "default path description", false)
        );


        // -------------------------------------------------------------------
        // STRING ARRAY (example from original code, optional)
        // -------------------------------------------------------------------

        StrArrParameter strArrParam = parser.addParameter(
                new StrArrParameter("stringArray", "sArr", "description for string array", false)
        );


        // -------------------------------------------------------------------
        // FLOAT ARRAY (NEW)
        // -------------------------------------------------------------------

        FltArrParameter mandatoryFloatArray = parser.addParameter(
                new FltArrParameter("mandatory_float_array", "mfa", "mandatory float array description", true)
        );
        FltArrParameter optionalFloatArray = parser.addParameter(
                new FltArrParameter("optional_float_array", "ofa", "optional float array description", false)
        );
        FltArrParameter defaultFloatArray = parser.addParameter(
                new FltArrParameter(new Float[]{1.1f, 2.2f}, "default_float_array", "dfa", "default float array description")
        );


        // -------------------------------------------------------------------
        // PATH ARRAY (NEW)
        // -------------------------------------------------------------------

        PthArrParameter mandatoryPathArray = parser.addParameter(
                new PthArrParameter("mandatory_path_array", "mpa", "mandatory path array description", true, false)
        );
        PthArrParameter optionalPathArray = parser.addParameter(
                new PthArrParameter("optional_path_array", "opa", "optional path array description", false, false)
        );
        PthArrParameter defaultPathArray = parser.addParameter(
                new PthArrParameter(
                        new Path[]{Path.of("/path/one"), Path.of("/path/two")},
                        "default_path_array",
                        "dpa",
                        "default path array description",
                        false
                )
        );

        Exception exception = assertThrows(CalledForHelpNotification.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                            (!)=mandatory | (?)=optional | (/)=command
                #
                #                                      Available Parameters:
                #
                ###  --mandatory_string       -ms    [s]  (!)  mandatory string description
                #
                ###  --optional_string        -os    [s]  (?)  optional string description
                #
                ###  --default_string         -ds    [s]  (?)  default string description
                #                                    default:  default
                #
                ###  --mandatory_double       -md    [d]  (!)  mandatory double description
                #
                ###  --optional_double        -d     [d]  (?)  this one is optional
                #
                ###  --default_double         -dd    [d]  (?)  default double description
                #                                    default:  7.89
                #
                ###  --mandatory_integer      -mi    [i]  (!)  mandatory integer description
                #
                ###  --optional_integer       -oi    [i]  (?)  optional integer description
                #
                ###  --default_integer        -i     [i]  (?)  No description available!
                #                                    default:  5
                #
                ###  --mandatory_character    -mc    [c]  (!)  mandatory character description
                #
                ###  --optional_character     -oc    [c]  (?)  optional character description
                #
                ###  --default_character      -dc    [c]  (?)  default character description
                #                                    default:  a
                #
                ###  --mandatory_boolean      -mb    [b]  (!)  mandatory boolean description
                #
                ###  --optional_boolean       -ob    [b]  (?)  optional boolean description
                #
                ###  --default_boolean        -db    [b]  (?)  default boolean description
                #                                    default:  true
                #
                ###  --mandatory_float        -mf    [f]  (!)  mandatory float description
                #
                ###  --optional_float         -of    [f]  (?)  optional float description
                #
                ###  --default_float          -df    [f]  (?)  default float description
                #                                    default:  3.14
                #
                ###  --mandatory_path         -mp    [p]  (!)  mandatory path description
                #
                ###  --optional_path          -op    [p]  (?)  optional path description
                #
                ###  --default_path           -dp    [p]  (?)  default path description
                #                                    default:  /some/default/path
                #
                ###  --stringArray            -sArr  [s+] (?)  description for string array
                #
                ###  --mandatory_float_array  -mfa   [f+] (!)  mandatory float array description
                #
                ###  --optional_float_array   -ofa   [f+] (?)  optional float array description
                #
                ###  --default_float_array    -dfa   [f+] (?)  default float array description
                #                                    default:  [1.1, 2.2]
                #
                ###  --mandatory_path_array   -mpa   [p+] (!)  mandatory path array description
                #
                ###  --optional_path_array    -opa   [p+] (?)  optional path array description
                #
                ###  --default_path_array     -dpa   [p+] (?)  default path array description
                #                                    default:  [/path/one, /path/two]
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
        System.out.println(exception.getMessage());
    }

    @Test
    public void testHelpWithArrayParameters() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser();

        // String Array (mandatory)
        StrArrParameter stringArray = parser.addParameter(
                new StrArrParameter("stringArrayParam", "hap", "descr", true)
        );
        // String Array with default
        StrArrParameter defaultStringArray = parser.addParameter(
                new StrArrParameter(new String[]{"string1", "string2", "string3"},
                                    "stringArray", "sAr", "descr")
        );
        // Double Array (optional)
        DblArrParameter doubleArray = parser.addParameter(
                new DblArrParameter("doubleArrayParam", "dap", "description", false)
        );
        // Double Array with default
        DblArrParameter defaultDoubleArray = parser.addParameter(
                new DblArrParameter(new Double[]{1.1, 2.2, 3.3},
                                    "doubleArray", "da", "description")
        );
        // Integer Array (mandatory)
        IntArrParameter integerArray = parser.addParameter(
                new IntArrParameter("integerArrayParam", "iap", "description", true)
        );
        // Integer Array with default
        IntArrParameter defaultIntegerArray = parser.addParameter(
                new IntArrParameter(new Integer[]{1, 2, 3},
                                    "integerArray", "ia", "description")
        );
        // Boolean Array (optional)
        BolArrParameter booleanArray = parser.addParameter(
                new BolArrParameter("booleanArrayParam", "bap", "description", false)
        );
        // Boolean Array with default
        BolArrParameter defaultBooleanArray = parser.addParameter(
                new BolArrParameter(new Boolean[]{true, false, true},
                                    "booleanArray", "ba", "description")
        );
        // Character Array (mandatory)
        ChrArrParameter characterArray = parser.addParameter(
                new ChrArrParameter("characterArrayParam", "cap", "description", true)
        );
        // Character Array with default
        ChrArrParameter defaultCharacterArray = parser.addParameter(
                new ChrArrParameter(new Character[]{'a', 'b', 'c'},
                                    "characterArray", "ca", "description")
        );
        Exception exception = assertThrows(CalledForHelpNotification.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                            (!)=mandatory | (?)=optional | (/)=command
                #
                #                                      Available Parameters:
                #
                ###  --stringArrayParam     -hap  [s+] (!)  descr
                #
                ###  --stringArray          -sAr  [s+] (?)  descr
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
        ArgsParser parser = new ArgsParser();

        Command command = parser.addCommand(new Command("command", "c", "this is a command"));

        Exception exception = assertThrows(CalledForHelpNotification.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                            (!)=mandatory | (?)=optional | (/)=command
                #
                ###  command  c       (/)  this is a command
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testHelpWithCommandsAndParameters() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser();
        Command command = parser.addCommand(new Command("command", "c", "this is a command"));
        StrParameter newParam1 = parser.addParameter(new StrParameter("newParam1", "np1", "this is the first new parameter", true));
        IntParameter newParam2 = parser.addParameter(new IntParameter("newParam2", "np2", "this is the second new parameter", false));
        Command newCommand = parser.addCommand(new Command("newCommand", "nc", "this is another command"));

        Exception exception = assertThrows(CalledForHelpNotification.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                            (!)=mandatory | (?)=optional | (/)=command
                #
                #                                      Available Parameters:
                #
                ###  --newParam1  -np1  [s]  (!)  this is the first new parameter
                #
                ###  --newParam2  -np2  [i]  (?)  this is the second new parameter
                #
                #                                       Available Commands:
                #
                ###  command      c          (/)  this is a command
                #
                ###  newCommand   nc         (/)  this is another command
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testOneCommandAndOneParameter() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser();
        Command command = parser.addCommand(new Command("command", "c", "this is a command"));
        StrParameter newParam1 = parser.addParameter(new StrParameter("newParam1", "np1", "this is the first new parameter", true));

        Exception exception = assertThrows(CalledForHelpNotification.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                            (!)=mandatory | (?)=optional | (/)=command
                #
                #                                      Available Parameters:
                #
                ###  --newParam1  -np1  [s]  (!)  this is the first new parameter
                #
                #                                       Available Commands:
                #
                ###  command      c          (/)  this is a command
                #
                ####################################################################################################""";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testHelpWithNewLineInDescription() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser();

        StrParameter longString = parser.addParameter(new StrParameter("this/path/is/so/long/it/is/actually/longer/than/any" +
                                                                               "/existing/path/that/I/have/on/my/PC/Do/You/Know/The/Word" +
                                                                               "Oberwesedampfschifffahrtsgesellschaftskapitän",
                                                                       "longString", "ls",
                                                                       "This description is so long, it will " +
                                                                               "force the automatic help printout to " +
                                                                               "introduce a new line and still have a nice look :)"));
        Exception exception = assertThrows(CalledForHelpNotification.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                ############################################### HELP ###############################################
                #   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
                #       ('+' marks a flag that takes several arguments of the same type whitespace separated)
                #                            (!)=mandatory | (?)=optional | (/)=command
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
        ArgsParser parser = new ArgsParser();

        // String (mandatory)
        StrParameter strParam = parser.addParameter(
                new StrParameter("string", "s", "", true)
        );
        // Integer (optional)
        IntParameter intParam = parser.addParameter(
                new IntParameter("integer", "i", null, false)
        );
        // Character (with default value)
        ChrParameter charParam = parser.addParameter(
                new ChrParameter('c', "char", "c", null)
        );
        // Boolean (optional)
        BolParameter booleanParam = parser.addParameter(
                new BolParameter("boolean", "b", "", false)
        );
        // Double (mandatory)
        DblParameter doubleParam = parser.addParameter(
                new DblParameter("double", "d", null, true)
        );
        // String array (optional)
        StrArrParameter strArrParam = parser.addParameter(
                new StrArrParameter("stringArray", "sArr", null, false)
        );

        Exception exception = assertThrows(CalledForHelpNotification.class, () -> parser.parseUnchecked(args));
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
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));

        Exception exception = assertThrows(HelpAtWrongPositionArgsException.class, () -> parser.parseUnchecked(args));
    }

    @Test
    public void testHelpBeforeFlag() {
        String[] args = {"--help", "--file"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));

        Exception exception = assertThrows(HelpAtWrongPositionArgsException.class, () -> parser.parseUnchecked(args));
    }

// FlagAlreadyProvidedArgsException

    @Test
    public void testSameFlagProvidedTwice() {
        String[] args = {"--file", "file.txt", "-f", "file2.txt"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        StrParameter file2 = parser.addParameter(new StrParameter("file2", "f2", "descr", true));

        Exception exception = assertThrows(FlagAlreadyProvidedArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals("""
                             
                             <!> Redundant specification of arguments to: --file/-f
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

// InvalidArgTypeArgsException

    @Test
    public void testGetArgumentAsDoubleWithWrongInput() {
        String[] args = {"--file", "file.txt", "--double", "5.5.5"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        DblParameter doub = parser.addParameter(new DblParameter("double", "d", "descr", true));

        Exception exception = assertThrows(InvalidArgTypeArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals(new InvalidArgTypeArgsException("--double", "Double", "Provided argument does not match the parameter type!").getMessage(), exception.getMessage());
    }

// MandatoryArgNotProvidedArgsException

    @Test
    public void testMandatoryArgMissing() {
        String[] args = {"--file", "file.txt", "--optional", "optional.txt"};
        ArgsParser parser = new ArgsParser();

        // Using the new StrParameter for mandatory parameters
        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("save", "s", "descr", true));
        StrParameter optional = parser.addParameter(new StrParameter("optional", "o", "descr", true));

        Exception exception = assertThrows(MandatoryArgNotProvidedArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save").getMessage(),
                     exception.getMessage());
    }

    @Test
    public void testMultipleMandatoryArgumentsMissing() {
        String[] args = {"--load", "file.txt"};
        ArgsParser parser = new ArgsParser();

        // Again, switching to StrParameter with isMandatory = true
        StrParameter load = parser.addParameter(new StrParameter("load", "l", "descr", true));
        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("save", "s", "descr", true));

        Exception exception = assertThrows(MandatoryArgNotProvidedArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals(
                new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save\n--file").getMessage(),
                exception.getMessage()
        );
    }

// MissingArgArgsException

    @Test
    public void testMissingArgument() {
        String[] args = {"--file", "--save", "save.txt"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "m", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("save", "s", "descr", true));

        Exception exception = assertThrows(MissingArgArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals(new MissingArgArgsException("--file").getMessage(), exception.getMessage());
    }

    @Test
    public void testMissingLastArgument() {
        String[] args = {"--file", "file.txt", "--save"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("save", "s", "descr", true));

        Exception exception = assertThrows(MissingArgArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals(new MissingArgArgsException("--save").getMessage(), exception.getMessage());
    }

// NoArgumentsProvidedArgsException

    @Test
    public void testNoArgumentsProvidedWithMandatoryParams() {
        String[] args = {};
        ArgsParser parser = new ArgsParser();

        StrParameter mandatoryParam = parser.addParameter(new StrParameter("optional", "o", null, true));

        Exception exception = assertThrows(NoArgumentsProvidedArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals("""
                             
                             <!> No arguments provided
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testNoArgumentsProvidedWithOnlyOptionalParams() { // just checks if the programs still runs if only optionals are defined
        String[] args = {};
        ArgsParser parser = new ArgsParser();

        StrParameter stringParam = parser.addParameter(new StrParameter("optional", "o", null, false));

        assertDoesNotThrow(() -> parser.parseUnchecked(args));
    }

// TooManyArgumentsArgsException

    @Test
    public void testTooManyArguments() {
        String[] args = {"--file", "file.txt", "--save", "save.txt", "extra"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("save", "s", "descr", true));

        Exception exception = assertThrows(TooManyArgumentsArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals(new TooManyArgumentsArgsException("--save").getMessage(), exception.getMessage());
    }

// UnknownFlagArgsException

    @Test
    public void testUnknownFlag() {
        String[] args = {"-w", "file.txt", "-s", "save.txt"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("save", "s", "descr", true));

        Exception exception = assertThrows(UnknownFlagArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals("""
                             
                             <!> unknown flag or command: -w
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testUnknownFlagWithMultipleFlagsAndWrongInput() {
        String[] args = {"-f", "file.txt", "--save", "save.txt", "-s"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("save", "w", "descr", true));

        Exception exception = assertThrows(UnknownFlagArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals("""
                             
                             <!> unknown flag or command: -s
                             > did you mean: --save ?
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testMissingShorts() {
        String[] args = {"-f", "/to/file", "--save", "save.txt"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("--file", "m", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("--save", "s", null, true));

        Exception exception = assertThrows(UnknownFlagArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals("""
                             
                             <!> unknown flag or command: -f
                             > did you mean: --file ?
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testMisspelledHelp() {
        String[] args = {"--hp"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("--file", "f", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("--save", "s", null, true));

        Exception exception = assertThrows(UnknownFlagArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals("""
                             
                             <!> unknown flag or command: --hp
                             > did you mean: --help ?
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testSuggestionForFullFlag() {
        String[] args = {"-f", "/to/file", "--sve", "save.txt"};
        ArgsParser parser = new ArgsParser();

        StrParameter file = parser.addParameter(new StrParameter("--file", "f", "descr", true));
        StrParameter save = parser.addParameter(new StrParameter("--save", "s", null, true));

        Exception exception = assertThrows(UnknownFlagArgsException.class, () -> parser.parseUnchecked(args));
        assertEquals("""
                             
                             <!> unknown flag or command: --sve
                             > did you mean: --save ?
                             
                             > Use --help for more information.
                             """, exception.getMessage());
    }

    @Test
    public void testNoFlags() {
        String[] args = {"file.txt"};
        ArgsParser parser = new ArgsParser();

        StrParameter string = parser.addParameter(new StrParameter("file", "f", "descr", true));

        Exception exception = assertThrows(UnknownFlagArgsException.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                <!> unknown flag or command: file.txt
                > did you mean: --file ?
                
                > flag or command expected in first position!
                
                > Use --help for more information.
                """;
        assertEquals(expected, exception.getMessage());
    }

// Runtime Exceptions

    @Test
    public void testDefinitionOfSameFlagTwice() {
        String[] args = {"--file", "file.txt", "--file2", "file2.txt"};
        ArgsParser parser = new ArgsParser();

        parser.addParameter(new StrParameter("file", "f", "descr", true));

        // Hier wird erwartet, dass beim Hinzufügen eines Parameters mit demselben Namen eine IllegalArgumentException geworfen wird
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                parser.addParameter(new StrParameter("file", "f2", "descr", true))
        );

        assertEquals("Flag already exists: --file", exception.getMessage());
    }

    @Test
    public void testDoubleParseCall() {
        String[] args = {"--file", "file.txt", "--file2", "file2.txt"};
        ArgsParser parser = new ArgsParser();
        StrParameter file = parser.addParameter(new StrParameter("file", "f", "descr", true));
        StrParameter file2 = parser.addParameter(new StrParameter("file2", "f2", "descr", true));
        parser.parse(args);

        Exception exception = assertThrows(IllegalStateException.class, () -> parser.parseUnchecked(args));
        assertEquals(new IllegalStateException(".parse() was already called!").getMessage(), exception.getMessage());
    }

    @Test
    public void testArgsIsNull() {
        String[] args = null;
        ArgsParser parser = new ArgsParser();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parseUnchecked(null));
        assertEquals("Args cannot be null!", exception.getMessage());
    }

    @Test
    public void testTryToUseHelpAsFlagName() {
        String[] args = {"--help"};
        ArgsParser parser = new ArgsParser();

        assertThrows(IllegalArgumentException.class, () -> {
            parser.addParameter(new StrParameter("help", "f", "", true));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            parser.addParameter(new StrParameter("hlp", "h", "", true));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            parser.addCommand(new Command("--help", "f", ""));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            parser.addCommand(new Command("hlp", "-h", ""));
        });
    }

    @Test
    public void testToggle() {
        String[] args = {"comm", "comm2"};
        ArgsParser parser = new ArgsParser();
        Command comd = parser.addCommand(new Command("command", "comm", "command1"));
        Command comd2 = parser.addCommand(new Command("command2", "comm2", "command2"));
        parser.toggle(comd, comd2);

        Exception exception = assertThrows(ToggleArgsException.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                <!> The following commands cannot be combined:\s
                [command / comm]
                [command2 / comm2]""";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testNotExistingPathArgsException() {
        String[] args = {"--path", "/home/ExampleUser"};
        ArgsParser parser = new ArgsParser();
        PthParameter pathParam = parser.addParameter(new PthParameter("path", "p", "path example", true, true));
        Exception exception = assertThrows(NotExistingPathArgsException.class, () -> parser.parseUnchecked(args));
        String expected = """
                
                <!> /home/ExampleUser does not exist!
                	Invalid path!""";
        assertEquals(expected, exception.getMessage());
    }
}
