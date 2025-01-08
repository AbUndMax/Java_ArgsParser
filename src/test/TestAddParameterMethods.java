import ArgsParser.*;
import static org.junit.jupiter.api.Assertions.*;

import ArgsParser.ArgsExceptions.ToggleArgsException;
import ArgsParser.ParameterTypes.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAddParameterMethods {

    // Test each addParameter method for each type:
    
    
// String methods:

    @Test
    public void testAddMandatoryStringParameter() {
        String[] args = {"--String", "this is a string"};
        ArgsParser parser = new ArgsParser();
        StrParameter str = parser.addParameter(new StrParameter("String", "s", "desc", true));
        parser.parse(args);

        assertEquals("this is a string", str.getArgument());
    }

    @Test
    public void testAddOptionalStringParameter() {
        String[] args = {"--String", "this is a string"};
        ArgsParser parser = new ArgsParser();
        StrParameter str = parser.addParameter(new StrParameter("String", "s", "desc", false));
        parser.parse(args);

        assertEquals("this is a string", str.getArgument());
    }

    @Test
    public void testAddDefaultStringParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        StrParameter defString = parser.addParameter(new StrParameter("This is the default", "DefaultString", "df", "desc"));
        parser.parse(args);

        assertEquals("This is the default", defString.getArgument());
    }

    @Test
    public void testAddDefaultStringParameterWithArgument() {
        String[] args = {"--DefaultString", "this is a string"};
        ArgsParser parser = new ArgsParser();
        StrParameter defString = parser.addParameter(new StrParameter("This is the default", "DefaultString", "df", "desc"));
        parser.parse(args);

        assertEquals("this is a string", defString.getArgument());
    }

    @Test
    public void testAddStringArrayParameter() {
        String[] args = {"--StringArray", "this is a string", "and this is another"};
        ArgsParser parser = new ArgsParser();
        StrArrParameter stringArray = parser.addParameter(new StrArrParameter("StringArray", "strA", "desc", true));
        parser.parse(args);

        assertArrayEquals(new String[]{"this is a string", "and this is another"}, stringArray.getArgument());
    }
    
    @Test
    public void testAddDefaultStringArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        StrArrParameter defaultStringArray = parser.addParameter(new StrArrParameter(new String[]{"this is the default", "and this is another"},"StringArray", "strA", "desc"));
        parser.parse(args);
        
        assertArrayEquals(new String[]{"this is the default", "and this is another"}, defaultStringArray.getArgument());
    }

    @Test
    public void testAddStringArrayParameterWithArgument() {
        String[] args = {"--StringArray", "this is a string", "and this is another"};
        ArgsParser parser = new ArgsParser();
        StrArrParameter defaultStringArray = parser.addParameter(new StrArrParameter(new String[]{"this", "is", "default"}, "StringArray", "strA", "desc"));
        parser.parse(args);

        assertArrayEquals(new String[]{"this is a string", "and this is another"}, defaultStringArray.getArgument());
    }


// Integer Methods

    @Test
    public void testAddMandatoryIntegerParameter() {
        String[] args = {"--Integer", "42"};
        ArgsParser parser = new ArgsParser();
        IntParameter intParam = parser.addParameter(new IntParameter("Integer", "i", "desc", true));
        parser.parse(args);

        assertEquals(42, intParam.getArgument());
    }

    @Test
    public void testAddOptionalIntegerParameter() {
        String[] args = {"--Integer", "42"};
        ArgsParser parser = new ArgsParser();
        IntParameter intParam = parser.addParameter(new IntParameter("Integer", "i", "desc", false));
        parser.parse(args);

        assertEquals(42, intParam.getArgument());
    }

    @Test
    public void testAddDefaultIntegerParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        IntParameter defaultInteger = parser.addParameter(new IntParameter(10, "DefaultInteger", "di", "desc"));
        parser.parse(args);

        assertEquals(10, defaultInteger.getArgument());
    }
    
    @Test
    public void testAddDefaultIntegerParameterWithArgument() {
        String[] args = {"--DefaultInteger", "42"};
        ArgsParser parser = new ArgsParser();
        IntParameter defaultInteger = parser.addParameter(new IntParameter(10, "DefaultInteger", "di", "desc"));
        parser.parse(args);
        
        assertEquals(42, defaultInteger.getArgument());
    }

    @Test
    public void testAddIntegerArrayParameter() {
        String[] args = {"--IntegerArray", "1", "2", "3"};
        ArgsParser parser = new ArgsParser();
        IntArrParameter integerArray = parser.addParameter(new IntArrParameter("IntegerArray", "intA", "desc", true));
        parser.parse(args);

        assertArrayEquals(new Integer[]{1, 2, 3}, integerArray.getArgument());
    }

    @Test
    public void testAddDefaultIntegerArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        IntArrParameter defaultIntegerArray = parser.addParameter(new IntArrParameter(new Integer[]{4, 5, 6}, "IntegerArray", "intA", "desc"));
        parser.parse(args);

        assertArrayEquals(new Integer[]{4, 5, 6}, defaultIntegerArray.getArgument());
    }
    
    @Test
    public void testAddIntegerArrayParameterWithArgument() {
        String[] args = {"--IntegerArray", "1", "2", "3"};
        ArgsParser parser = new ArgsParser();
        IntArrParameter intArray = parser.addParameter(new IntArrParameter(new Integer[]{4, 5, 6}, "IntegerArray", "intA", "desc"));
        parser.parse(args);
        
        assertArrayEquals(new Integer[]{1, 2, 3}, intArray.getArgument());
    }


// Double Methods

    @Test
    public void testAddMandatoryDoubleParameter() {
        String[] args = {"--Double", "42.0"};
        ArgsParser parser = new ArgsParser();
        DblParameter doubleParam = parser.addParameter(new DblParameter("Double", "d", "desc", true));
        parser.parse(args);

        assertEquals(42.0, doubleParam.getArgument());
    }

    @Test
    public void testAddOptionalDoubleParameter() {
        String[] args = {"--Double", "42.0"};
        ArgsParser parser = new ArgsParser();
        DblParameter doubleParam = parser.addParameter(new DblParameter("Double", "d", "desc", false));
        parser.parse(args);

        assertEquals(42.0, doubleParam.getArgument());
    }

    @Test
    public void testAddDefaultDoubleParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        DblParameter defaultDouble = parser.addParameter(new DblParameter( 10.0, "DefaultDouble", "dd", "desc"));
        parser.parse(args);

        assertEquals(10.0, defaultDouble.getArgument());
    }
    
    @Test
    public void testAddDefaultDoubleParameterWithArgument() {
        String[] args = {"--DefaultDouble", "42.0"};
        ArgsParser parser = new ArgsParser();
        DblParameter defaultDouble = parser.addParameter(new DblParameter( 10.0, "DefaultDouble", "dd", "desc"));
        parser.parse(args);

        assertEquals(42.0, defaultDouble.getArgument());
    }

    @Test
    public void testAddDoubleArrayParameter() {
        String[] args = {"--DoubleArray", "1.1", "2.2", "3.3"};
        ArgsParser parser = new ArgsParser();
        DblArrParameter doubleArray = parser.addParameter(new DblArrParameter("DoubleArray", "dblA", "desc", true));
        parser.parse(args);

        assertArrayEquals(new Double[]{1.1, 2.2, 3.3}, doubleArray.getArgument());
    }

    @Test
    public void testAddDefaultDoubleArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        DblArrParameter defaultDoubleArray = parser.addParameter(new DblArrParameter(new Double[]{4.4, 5.5, 6.6}, "DoubleArray", "dblA", "desc"));
        parser.parse(args);

        assertArrayEquals(new Double[]{4.4, 5.5, 6.6}, defaultDoubleArray.getArgument());
    }

    @Test
    public void testAddDefaultDoubleArrayParameterWithArgument() {
        String[] args = {"--DoubleArray", "1.1", "2.2", "3.3"};
        ArgsParser parser = new ArgsParser();
        DblArrParameter defaultDoubleArray = parser.addParameter(new DblArrParameter(new Double[]{4.4, 5.5, 6.6}, "DoubleArray", "dblA", "desc"));
        parser.parse(args);

        assertArrayEquals(new Double[]{1.1, 2.2, 3.3}, defaultDoubleArray.getArgument());
    }
    

// Boolean Methods

    @Test
    public void testAddMandatoryBooleanParameter() {
        String[] args = {"--Boolean", "true"};
        ArgsParser parser = new ArgsParser();
        BolParameter booleanParam = parser.addParameter(new BolParameter("Boolean", "b", "desc", true));
        parser.parse(args);

        assertTrue(booleanParam.getArgument());
    }

    @Test
    public void testAddOptionalBooleanParameter() {
        String[] args = {"--Boolean", "true"};
        ArgsParser parser = new ArgsParser();
        BolParameter booleanParam = parser.addParameter(new BolParameter("Boolean", "b", "desc", false));
        parser.parse(args);

        assertTrue(booleanParam.getArgument());
    }

    @Test
    public void testAddDefaultBooleanParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        BolParameter defaultBoolean = parser.addParameter(new BolParameter(true, "DefaultBoolean", "db", "desc"));
        parser.parse(args);

        assertTrue(defaultBoolean.getArgument());
    }
    
    @Test
    public void testAddDefaultBooleanParameterWithArgument() {
        String[] args = {"--DefaultBoolean", "false"};
        ArgsParser parser = new ArgsParser();
        BolParameter defaultBoolean = parser.addParameter(new BolParameter(true, "DefaultBoolean", "db", "desc"));
        parser.parse(args);

        assertFalse(defaultBoolean.getArgument());
    }

    @Test
    public void testAddBooleanArrayParameter() {
        String[] args = {"--BooleanArray", "true", "false", "true"};
        ArgsParser parser = new ArgsParser();
        // Using the new BolArrParameter (without default value, isMandatory = true)
        BolArrParameter booleanArray = parser.addParameter(
                new BolArrParameter("BooleanArray", "boolA", "desc", true)
        );
        parser.parse(args);

        assertArrayEquals(new Boolean[]{true, false, true}, booleanArray.getArgument());
    }

    @Test
    public void testAddDefaultBooleanArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        // Using the new BolArrParameter with a default value (isMandatory omitted)
        BolArrParameter defaultBooleanArray = parser.addParameter(
                new BolArrParameter(new Boolean[]{true, false, true}, "BooleanArray", "boolA", "desc")
        );
        parser.parse(args);

        // Since no arguments were passed, we expect the default array
        assertArrayEquals(new Boolean[]{true, false, true}, defaultBooleanArray.getArgument());
    }

    @Test
    public void testAddDefaultBooleanArrayParameterWithArgument() {
        String[] args = {"--BooleanArray", "false", "true", "false"};
        ArgsParser parser = new ArgsParser();
        // Same default-based constructor
        BolArrParameter defaultBooleanArray = parser.addParameter(
                new BolArrParameter(new Boolean[]{true, false, true}, "BooleanArray", "boolA", "desc")
        );
        parser.parse(args);

        // We override the default with the passed arguments
        assertArrayEquals(new Boolean[]{false, true, false}, defaultBooleanArray.getArgument());
    }

    // Character Methods

    @Test
    public void testAddMandatoryCharacterParameter() {
        String[] args = {"--Character", "a"};
        ArgsParser parser = new ArgsParser();
        // Using the new ChrParameter (no default, isMandatory = true)
        ChrParameter characterParam = parser.addParameter(
                new ChrParameter("Character", "c", "desc", true)
        );
        parser.parse(args);

        assertEquals('a', characterParam.getArgument());
    }

    @Test
    public void testAddOptionalCharacterParameter() {
        String[] args = {"--Character", "a"};
        ArgsParser parser = new ArgsParser();
        // Using the new ChrParameter (no default, isMandatory = false)
        ChrParameter characterParam = parser.addParameter(
                new ChrParameter("Character", "c", "desc", false)
        );
        parser.parse(args);

        assertEquals('a', characterParam.getArgument());
    }

    @Test
    public void testAddDefaultCharacterParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        // Using the new ChrParameter (with default value)
        ChrParameter defaultCharacter = parser.addParameter(
                new ChrParameter('z', "DefaultCharacter", "dc", "desc")
        );
        parser.parse(args);

        // Since no argument was passed, 'z' is expected
        assertEquals('z', defaultCharacter.getArgument());
    }

    @Test
    public void testAddDefaultCharacterParameterWithArgument() {
        String[] args = {"--DefaultCharacter", "x"};
        ArgsParser parser = new ArgsParser();
        // Using the new ChrParameter (with default value)
        ChrParameter defaultCharacter = parser.addParameter(
                new ChrParameter('z', "DefaultCharacter", "dc", "desc")
        );
        parser.parse(args);

        // The default 'z' should be overridden by the parsed argument 'x'
        assertEquals('x', defaultCharacter.getArgument());
    }

    @Test
    public void testAddCharacterArrayParameter() {
        String[] args = {"--CharacterArray", "a", "b", "c"};
        ArgsParser parser = new ArgsParser();
        // Using the new ChrArrParameter (no default, isMandatory = true)
        ChrArrParameter characterArray = parser.addParameter(
                new ChrArrParameter("CharacterArray", "charA", "desc", true)
        );
        parser.parse(args);

        assertArrayEquals(new Character[]{'a', 'b', 'c'}, characterArray.getArgument());
    }

    @Test
    public void testAddDefaultCharacterArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        // Using the new ChrArrParameter (with a default array)
        ChrArrParameter defaultCharacterArray = parser.addParameter(
                new ChrArrParameter(new Character[]{'x', 'y', 'z'}, "CharacterArray", "charA", "desc")
        );
        parser.parse(args);

        // Since no arguments were passed, the default array is expected
        assertArrayEquals(new Character[]{'x', 'y', 'z'}, defaultCharacterArray.getArgument());
    }

    @Test
    public void testAddDefaultCharacterArrayParameterWithArgument() {
        String[] args = {"--CharacterArray", "a", "b", "c"};
        ArgsParser parser = new ArgsParser();
        // Using the new ChrArrParameter (with a default array)
        ChrArrParameter defaultCharacterArray = parser.addParameter(
                new ChrArrParameter(new Character[]{'x', 'y', 'z'}, "CharacterArray", "charA", "desc")
        );
        parser.parse(args);

        // The default array gets overridden by "a", "b", "c"
        assertArrayEquals(new Character[]{'a', 'b', 'c'}, defaultCharacterArray.getArgument());
    }

// Path parameters

    @Test
    public void testAddMandatoryPathParameter() {
        String[] args = {"--Path", "/Users/user/Source/Code"};
        ArgsParser parser = new ArgsParser();
        PthParameter path = parser.addParameter(new PthParameter("Path", "pathA", "desc", true, false));
        parser.parse(args);
        assertEquals("/Users/user/Source/Code", path.getArgument().toString());
    }

    @Test
    public void testAddDefaultPathParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        PthParameter path = parser.addParameter(new PthParameter(Path.of("home/"), "Path", "pathA", "desc", false));
        parser.parse(args);
        assertEquals(Path.of("home/"), path.getArgument());
    }

    @Test
    public void testAddOptionalPathParameter() {
        String[] args = {"--Path", "/Users/user/Source/Code"};
        ArgsParser parser = new ArgsParser();
        PthParameter path = parser.addParameter(new PthParameter("Path", "pathA", "desc", true, false));
        parser.parse(args);
        assertEquals("/Users/user/Source/Code", path.getArgument().toString());
    }



// Commands

    @Test
    public void testCommand() {
        String[] args = {"command"};
        ArgsParser parser = new ArgsParser();
        Command command = parser.addCommand(new Command("command", "c", ""));
        parser.parse(args);

        assertTrue(command.isProvided());
    }

    @Test
    public void testIndirectCommandCheck() {
        String[] args = {"command"};
        ArgsParser parser = new ArgsParser();
        Command command = parser.addCommand(new Command("command", "c", ""));
        parser.parse(args);
        
        assertTrue(parser.checkIfCommandIsProvided("command"));
    }


    // Mixing addParameter Methods


    @Test
    public void testCombinationOfMandatoryArguments() {
        String[] args = new String[]{"-s", "stringInput", "-sArr", "string", "array", "-i", "12", "-iArr", "1", "2",
                "-d", "12.3", "-dArr", "1.2", "2.3", "-b", "true", "-bArr", "true", "false", "-c", "a", "-cArr", "d", "c"};

        ArgsParser parser = new ArgsParser();
        // String (mandatory)
        StrParameter stringParam = parser.addParameter(
                new StrParameter("String", "s", "desc", true)
        );

        // String array (optional in this example)
        StrArrParameter stringArrayParam = parser.addParameter(
                new StrArrParameter("StringArray", "sArr", "", false)
        );

        // Integer (mandatory)
        IntParameter intParam = parser.addParameter(
                new IntParameter("Int", "i", "desc", true)
        );

        // Integer array (optional)
        IntArrParameter integerArrayParam = parser.addParameter(
                new IntArrParameter("IntArray", "iArr", "", false)
        );

        // Double (mandatory)
        DblParameter doubleParam = parser.addParameter(
                new DblParameter("Double", "d", "desc", true)
        );

        // Double array (optional)
        DblArrParameter doubleArrayParam = parser.addParameter(
                new DblArrParameter("DoubleArray", "dArr", "", false)
        );

        // Boolean (mandatory)
        BolParameter booleanParam = parser.addParameter(
                new BolParameter("Boolean", "b", "desc", true)
        );

        // Boolean array (optional)
        BolArrParameter booleanArrayParam = parser.addParameter(
                new BolArrParameter("BooleanArray", "bArr", "", false)
        );

        // Character (mandatory)
        ChrParameter characterParameter = parser.addParameter(
                new ChrParameter("Character", "c", "desc", true)
        );

        // Character array (optional)
        ChrArrParameter characterArrayParam = parser.addParameter(
                new ChrArrParameter("CharacterArray", "cArr", "", false)
        );

        parser.parse(args);

        assertEquals("stringInput", stringParam.getArgument());
        assertArrayEquals(new String[]{"string", "array"}, stringArrayParam.getArgument());
        assertEquals(12, intParam.getArgument());
        assertArrayEquals(new Integer[]{1, 2}, integerArrayParam.getArgument());
        assertEquals(12.3, doubleParam.getArgument(), 0.001);
        assertArrayEquals(new Double[]{1.2, 2.3}, doubleArrayParam.getArgument());
        assertEquals(true, booleanParam.getArgument());
        assertArrayEquals(new Boolean[]{true, false}, booleanArrayParam.getArgument());
        assertEquals('a', characterParameter.getArgument());
        assertArrayEquals(new Character[]{'d', 'c'}, characterArrayParam.getArgument());
    }

    @Test
    public void testCombinationOfMandatoryAndOptionalArguments() {
        String[] args = new String[]{
                "-s", "stringInput",
                "-sArr", "string", "array",
                "-iArr", "1", "2",
                "-d", "12.3",
                "-b", "true",
                "-bArr", "true", "false",
                "-cArr", "d", "c"
        };

        ArgsParser parser = new ArgsParser();

        // String (mandatory)
        StrParameter stringParam = parser.addParameter(
                new StrParameter("String", "s", "desc", true)
        );

        // String array (optional)
        StrArrParameter stringArrayParam = parser.addParameter(
                new StrArrParameter("StringArray", "sArr", "", false)
        );

        // Integer (optional)
        IntParameter intParam = parser.addParameter(
                new IntParameter("Int", "i", "desc", false)
        );

        // Integer array (optional)
        IntArrParameter integerArrayParam = parser.addParameter(
                new IntArrParameter("IntArray", "iArr", "", false)
        );

        // Double (mandatory)
        DblParameter doubleParam = parser.addParameter(
                new DblParameter("Double", "d", "desc", true)
        );

        // Double array (optional)
        DblArrParameter doubleArrayParam = parser.addParameter(
                new DblArrParameter("DoubleArray", "dArr", "", false)
        );

        // Boolean (mandatory)
        BolParameter booleanParam = parser.addParameter(
                new BolParameter("Boolean", "b", "desc", true)
        );

        // Boolean array (optional)
        BolArrParameter booleanArrayParam = parser.addParameter(
                new BolArrParameter("BooleanArray", "bArr", "", false)
        );

        // Character (optional)
        ChrParameter characterParameter = parser.addParameter(
                new ChrParameter("Character", "c", "desc", false)
        );

        // Character array (optional)
        ChrArrParameter characterArrayParam = parser.addParameter(
                new ChrArrParameter("CharacterArray", "cArr", "", false)
        );

        parser.parse(args);

        assertEquals("stringInput", stringParam.getArgument());
        assertArrayEquals(new String[]{"string", "array"}, stringArrayParam.getArgument());
        // 'Int' was not passed => should be null (optional)
        assertNull(intParam.getArgument());
        // We did pass -iArr => expect [1,2]
        assertArrayEquals(new Integer[]{1, 2}, integerArrayParam.getArgument());
        // Double is mandatory => 12.3
        assertEquals(12.3, doubleParam.getArgument(), 0.001);
        // Double array not passed => should be null
        assertNull(doubleArrayParam.getArgument());
        // Boolean is mandatory => true
        assertTrue(booleanParam.getArgument());
        // Boolean array => [true,false]
        assertArrayEquals(new Boolean[]{true, false}, booleanArrayParam.getArgument());
        // Character optional => not passed => null
        assertNull(characterParameter.getArgument());
        // Character array => ['d','c']
        assertArrayEquals(new Character[]{'d', 'c'}, characterArrayParam.getArgument());
    }

    @Test
    public void testGetArgumentTypes() {
        String[] args = new String[]{
                "-s", "stringInput",
                "-sArr", "string", "array",
                "-i", "12",
                "-iArr", "1", "2",
                "-d", "12.3",
                "-dArr", "1.2", "2.3",
                "-b", "true",
                "-bArr", "true", "false",
                "-c", "a",
                "-cArr", "d", "c"
        };

        ArgsParser parser = new ArgsParser();

        // String (mandatory)
        StrParameter stringParam = parser.addParameter(
                new StrParameter("String", "s", "desc", true)
        );

        // String array (optional)
        StrArrParameter stringArrayParam = parser.addParameter(
                new StrArrParameter("StringArray", "sArr", "", false)
        );

        // Integer (mandatory)
        IntParameter intParam = parser.addParameter(
                new IntParameter("Int", "i", "desc", true)
        );

        // Integer array (optional)
        IntArrParameter integerArrayParam = parser.addParameter(
                new IntArrParameter("IntArray", "iArr", "", false)
        );

        // Double (mandatory)
        DblParameter doubleParam = parser.addParameter(
                new DblParameter("Double", "d", "desc", true)
        );

        // Double array (optional)
        DblArrParameter doubleArrayParam = parser.addParameter(
                new DblArrParameter("DoubleArray", "dArr", "", false)
        );

        // Boolean (mandatory)
        BolParameter booleanParam = parser.addParameter(
                new BolParameter("Boolean", "b", "desc", true)
        );

        // Boolean array (optional)
        BolArrParameter booleanArrayParam = parser.addParameter(
                new BolArrParameter("BooleanArray", "bArr", "", false)
        );

        // Character (mandatory)
        ChrParameter characterParameter = parser.addParameter(
                new ChrParameter("Character", "c", "desc", true)
        );

        // Character array (optional)
        ChrArrParameter characterArrayParam = parser.addParameter(
                new ChrArrParameter("CharacterArray", "cArr", "", false)
        );

        parser.parse(args);

        // Each .getArgument() should have the correct runtime type:
        assertEquals(String.class, stringParam.getArgument().getClass());
        assertEquals(String[].class, stringArrayParam.getArgument().getClass());
        assertEquals(Integer.class, intParam.getArgument().getClass());
        assertEquals(Integer[].class, integerArrayParam.getArgument().getClass());
        assertEquals(Double.class, doubleParam.getArgument().getClass());
        assertEquals(Double[].class, doubleArrayParam.getArgument().getClass());
        assertEquals(Boolean.class, booleanParam.getArgument().getClass());
        assertEquals(Boolean[].class, booleanArrayParam.getArgument().getClass());
        assertEquals(Character.class, characterParameter.getArgument().getClass());
        assertEquals(Character[].class, characterArrayParam.getArgument().getClass());
    }

    @Test
    public void testGetArgumentOf() {
        String[] args = new String[]{
                "-s", "stringInput",
                "-sArr", "arr1", "arr2",
                "-i", "42",
                "-iArr", "1", "2",
                "-d", "3.14",
                "-dArr", "1.1", "2.2",
                "-b", "true",
                "-bArr", "true", "false",
                "-c", "x",
                "-cArr", "a", "z"
        };
        ArgsParser parser = new ArgsParser();

        // Using new parameter classes instead of addMandatoryXYZParameter, etc.
        parser.addParameter(
                new StrParameter("String", "s", "desc", true)
        );
        parser.addParameter(
                new StrArrParameter("StringArray", "sArr", "", false)
        );
        parser.addParameter(
                new IntParameter("Int", "i", "desc", true)
        );
        parser.addParameter(
                new IntArrParameter("IntArray", "iArr", "", false)
        );
        parser.addParameter(
                new DblParameter("Double", "d", "desc", true)
        );
        parser.addParameter(
                new DblArrParameter("DoubleArray", "dArr", "", false)
        );
        parser.addParameter(
                new BolParameter("Boolean", "b", "desc", true)
        );
        parser.addParameter(
                new BolArrParameter("BooleanArray", "bArr", "", false)
        );
        parser.addParameter(
                new ChrParameter("Character", "c", "desc", true)
        );
        parser.addParameter(
                new ChrArrParameter("CharacterArray", "cArr", "", false)
        );

        parser.parse(args);

        // Now verify the results of parser.getArgumentOf(...)
        assertEquals("stringInput", parser.getArgumentOf("--String"));
        assertArrayEquals(
                new String[]{"arr1", "arr2"},
                (String[]) parser.getArgumentOf("--StringArray")
        );
        assertEquals(42, (Integer) parser.getArgumentOf("--Int"));
        assertArrayEquals(
                new Integer[]{1, 2},
                (Integer[]) parser.getArgumentOf("--IntArray")
        );
        assertEquals(3.14, parser.getArgumentOf("--Double"));
        assertArrayEquals(
                new Double[]{1.1, 2.2},
                (Double[]) parser.getArgumentOf("--DoubleArray")
        );
        assertEquals(true, parser.getArgumentOf("--Boolean"));
        assertArrayEquals(
                new Boolean[]{true, false},
                (Boolean[]) parser.getArgumentOf("--BooleanArray")
        );
        assertEquals('x', (Character) parser.getArgumentOf("--Character"));
        assertArrayEquals(
                new Character[]{'a', 'z'},
                (Character[]) parser.getArgumentOf("--CharacterArray")
        );
    }

    @Test
    public void testCombinationsOfCommandsAndFlags() {
        String[] args = new String[]{
                "c3",
                "-s", "stringInput",
                "-sArr", "arr1", "arr2",
                "-i", "42",
                "command1",
                "-iArr", "1", "2",
                "command2"
        };

        ArgsParser parser = new ArgsParser();

        // String (mandatory)
        StrParameter stringPar = parser.addParameter(
                new StrParameter("String", "s", "desc", true)
        );

        // String array (optional)
        StrArrParameter stringArrPar = parser.addParameter(
                new StrArrParameter("StringArray", "sArr", "", false)
        );

        // Integer (mandatory)
        IntParameter intPar = parser.addParameter(
                new IntParameter("Int", "i", "desc", true)
        );

        // Integer array (optional)
        IntArrParameter intArrPar = parser.addParameter(
                new IntArrParameter("IntArray", "iArr", "", false)
        );

        // Commands
        Command command1 = parser.addCommand(new Command("command1", "c1", null));
        Command command2 = parser.addCommand(new Command("command2", "c2", null));
        Command command3 = parser.addCommand(new Command("command3", "c3", null));

        // Parse
        parser.parse(args);

        // Verify parsed parameter values
        assertEquals("stringInput", stringPar.getArgument());
        assertArrayEquals(new String[]{"arr1", "arr2"}, stringArrPar.getArgument());
        assertEquals(42, intPar.getArgument());
        assertArrayEquals(new Integer[]{1, 2}, intArrPar.getArgument());

        // Verify provided commands
        assertTrue(command1.isProvided());
        assertTrue(command2.isProvided());
        assertTrue(command3.isProvided());
    }

    @Test
    public void testToggle() {
        String[] args = {"comm2"};
        ArgsParser parser = new ArgsParser();
        Command comd = parser.addCommand(new Command("command", "comm", "command1"));
        Command comd2 = parser.addCommand(new Command("command2", "comm2", "command2"));
        parser.toggle(comd, comd2);
        parser.parse(args);

        assertTrue(comd2.isProvided());
    }
}
