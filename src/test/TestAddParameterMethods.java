import ArgsParser.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAddParameterMethods {

    // Test each addParameter method for each type:
    
    
// String methods:

    @Test
    public void testAddMandatoryStringParameter() {
        String[] args = {"--String", "this is a string"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> str = parser.addMandatoryStringParameter("String", "s", "desc");
        parser.parse();

        assertEquals("this is a string", str.getArgument());
    }

    @Test
    public void testAddOptionalStringParameter() {
        String[] args = {"--String", "this is a string"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> str = parser.addOptionalStringParameter("String", "s", "desc");
        parser.parse();

        assertEquals("this is a string", str.getArgument());
    }

    @Test
    public void testAddDefaultStringParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> deafult = parser.addDefaultStringParameter("DefaultString", "df", "desc", "this is the default");
        parser.parse();

        assertEquals("this is the default", deafult.getArgument());
    }

    @Test
    public void testAddDefaultStringParameterWithArgument() {
        String[] args = {"--DefaultString", "this is a string"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> defaultParam = parser.addDefaultStringParameter("DefaultString", "df", "desc", "this is the default");
        parser.parse();

        assertEquals("this is a string", defaultParam.getArgument());
    }

    @Test
    public void testAddStringArrayParameter() {
        String[] args = {"--StringArray", "this is a string", "and this is another"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String[]> stringArray = parser.addStringArrayParameter("StringArray", "strA", "desc", true);
        parser.parse();

        assertArrayEquals(new String[]{"this is a string", "and this is another"}, stringArray.getArgument());
    }
    
    @Test
    public void testAddDefaultStringArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<String[]> defaultStringArray = parser.addDefaultStringArrayParameter("StringArray", "strA", "desc", new String[]{"this is the default", "and this is another"});
        parser.parse();
        
        assertArrayEquals(new String[]{"this is the default", "and this is another"}, defaultStringArray.getArgument());
    }

    @Test
    public void testAddStringArrayParameterWithArgument() {
        String[] args = {"--StringArray", "this is a string", "and this is another"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String[]> defaultStringArray = parser.addDefaultStringArrayParameter("StringArray", "strA", "desc", new String[]{"this", "is", "default"});
        parser.parse();

        assertArrayEquals(new String[]{"this is a string", "and this is another"}, defaultStringArray.getArgument());
    }


// Integer Methods

    @Test
    public void testAddMandatoryIntegerParameter() {
        String[] args = {"--Integer", "42"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer> integerParam = parser.addMandatoryIntegerParameter("Integer", "i", "desc");
        parser.parse();

        assertEquals(42, integerParam.getArgument());
    }

    @Test
    public void testAddOptionalIntegerParameter() {
        String[] args = {"--Integer", "42"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer> integerParam = parser.addOptionalIntegerParameter("Integer", "i", "desc");
        parser.parse();

        assertEquals(42, integerParam.getArgument());
    }

    @Test
    public void testAddDefaultIntegerParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer> defaultInteger = parser.addDefaultIntegerParameter("DefaultInteger", "di", "desc", 10);
        parser.parse();

        assertEquals(10, defaultInteger.getArgument());
    }
    
    @Test
    public void testAddDefaultIntegerParameterWithArgument() {
        String[] args = {"--DefaultInteger", "42"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer> defaultInteger = parser.addDefaultIntegerParameter("DefaultInteger", "di", "desc", 10);
        parser.parse();
        
        assertEquals(42, defaultInteger.getArgument());
    }

    @Test
    public void testAddIntegerArrayParameter() {
        String[] args = {"--IntegerArray", "1", "2", "3"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer[]> integerArray = parser.addIntegerArrayParameter("IntegerArray", "intA", "desc", true);
        parser.parse();

        assertArrayEquals(new Integer[]{1, 2, 3}, integerArray.getArgument());
    }

    @Test
    public void testAddDefaultIntegerArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer[]> defaultIntegerArray = parser.addDefaultIntegerArrayParameter("IntegerArray", "intA", "desc", new Integer[]{4, 5, 6});
        parser.parse();

        assertArrayEquals(new Integer[]{4, 5, 6}, defaultIntegerArray.getArgument());
    }
    
    @Test
    public void testAddIntegerArrayParameterWithArgument() {
        String[] args = {"--IntegerArray", "1", "2", "3"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer[]> intArray = parser.addDefaultIntegerArrayParameter("IntegerArray", "intA", "desc", new Integer[]{4, 5, 6});
        parser.parse();
        
        assertArrayEquals(new Integer[]{1, 2, 3}, intArray.getArgument());
    }


// Double Methods

    @Test
    public void testAddMandatoryDoubleParameter() {
        String[] args = {"--Double", "42.0"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double> doubleParam = parser.addMandatoryDoubleParameter("Double", "d", "desc");
        parser.parse();

        assertEquals(42.0, doubleParam.getArgument());
    }

    @Test
    public void testAddOptionalDoubleParameter() {
        String[] args = {"--Double", "42.0"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double> doubleParam = parser.addOptionalDoubleParameter("Double", "d", "desc");
        parser.parse();

        assertEquals(42.0, doubleParam.getArgument());
    }

    @Test
    public void testAddDefaultDoubleParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double> defaultDouble = parser.addDefaultDoubleParameter("DefaultDouble", "dd", "desc", 10.0);
        parser.parse();

        assertEquals(10.0, defaultDouble.getArgument());
    }
    
    @Test
    public void testAddDefaultDoubleParameterWithArgument() {
        String[] args = {"--DefaultDouble", "42.0"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double> defaultDouble = parser.addDefaultDoubleParameter("DefaultDouble", "dd", "desc", 10.0);
        parser.parse();

        assertEquals(42.0, defaultDouble.getArgument());
    }

    @Test
    public void testAddDoubleArrayParameter() {
        String[] args = {"--DoubleArray", "1.1", "2.2", "3.3"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double[]> doubleArray = parser.addDoubleArrayParameter("DoubleArray", "dblA", "desc", true);
        parser.parse();

        assertArrayEquals(new Double[]{1.1, 2.2, 3.3}, doubleArray.getArgument());
    }

    @Test
    public void testAddDefaultDoubleArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double[]> defaultDoubleArray = parser.addDefaultDoubleArrayParameter("DoubleArray", "dblA", "desc", new Double[]{4.4, 5.5, 6.6});
        parser.parse();

        assertArrayEquals(new Double[]{4.4, 5.5, 6.6}, defaultDoubleArray.getArgument());
    }

    @Test
    public void testAddDefaultDoubleArrayParameterWithArgument() {
        String[] args = {"--DoubleArray", "1.1", "2.2", "3.3"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double[]> defaultDoubleArray = parser.addDefaultDoubleArrayParameter("DoubleArray", "dblA", "desc", new Double[]{4.4, 5.5, 6.6});
        parser.parse();

        assertArrayEquals(new Double[]{1.1, 2.2, 3.3}, defaultDoubleArray.getArgument());
    }
    

// Boolean Methods

    @Test
    public void testAddMandatoryBooleanParameter() {
        String[] args = {"--Boolean", "true"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean> booleanParam = parser.addMandatoryBooleanParameter("Boolean", "b", "desc");
        parser.parse();

        assertTrue(booleanParam.getArgument());
    }

    @Test
    public void testAddOptionalBooleanParameter() {
        String[] args = {"--Boolean", "true"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean> booleanParam = parser.addOptionalBooleanParameter("Boolean", "b", "desc");
        parser.parse();

        assertTrue(booleanParam.getArgument());
    }

    @Test
    public void testAddDefaultBooleanParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean> defaultBoolean = parser.addDefaultBooleanParameter("DefaultBoolean", "db", "desc", true);
        parser.parse();

        assertTrue(defaultBoolean.getArgument());
    }
    
    @Test
    public void testAddDefaultBooleanParameterWithArgument() {
        String[] args = {"--DefaultBoolean", "false"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean> defaultBoolean = parser.addDefaultBooleanParameter("DefaultBoolean", "db", "desc", true);
        parser.parse();

        assertFalse(defaultBoolean.getArgument());
    }

    @Test
    public void testAddBooleanArrayParameter() {
        String[] args = {"--BooleanArray", "true", "false", "true"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean[]> booleanArray = parser.addBooleanArrayParameter("BooleanArray", "boolA", "desc", true);
        parser.parse();

        assertArrayEquals(new Boolean[]{true, false, true}, booleanArray.getArgument());
    }

    @Test
    public void testAddDefaultBooleanArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean[]> defaultBooleanArray = parser.addDefaultBooleanArrayParameter("BooleanArray", "boolA", "desc", new Boolean[]{true, false, true});
        parser.parse();

        assertArrayEquals(new Boolean[]{true, false, true}, defaultBooleanArray.getArgument());
    }

    @Test
    public void testAddDefaultBooleanArrayParameterWithArgument() {
        String[] args = {"--BooleanArray", "false", "true", "false"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean[]> defaultBooleanArray = parser.addDefaultBooleanArrayParameter("BooleanArray", "boolA", "desc", new Boolean[]{true, false, true});
        parser.parse();

        assertArrayEquals(new Boolean[]{false, true, false}, defaultBooleanArray.getArgument());
    }
    
    

// Character Methods

    @Test
    public void testAddMandatoryCharacterParameter() {
        String[] args = {"--Character", "a"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character> characterParam = parser.addMandatoryCharacterParameter("Character", "c", "desc");
        parser.parse();

        assertEquals('a', characterParam.getArgument());
    }

    @Test
    public void testAddOptionalCharacterParameter() {
        String[] args = {"--Character", "a"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character> characterParam = parser.addOptionalCharacterParameter("Character", "c", "desc");
        parser.parse();

        assertEquals('a', characterParam.getArgument());
    }

    @Test
    public void testAddDefaultCharacterParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character> defaultCharacter = parser.addDefaultCharacterParameter("DefaultCharacter", "dc", "desc", 'z');
        parser.parse();

        assertEquals('z', defaultCharacter.getArgument());
    }

    @Test
    public void testAddDefaultCharacterParameterWithArgument() {
        String[] args = {"--DefaultCharacter", "x"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character> defaultCharacter = parser.addDefaultCharacterParameter("DefaultCharacter", "dc", "desc", 'z');
        parser.parse();

        assertEquals('x', defaultCharacter.getArgument());
    }

    @Test
    public void testAddCharacterArrayParameter() {
        String[] args = {"--CharacterArray", "a", "b", "c"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character[]> characterArray = parser.addCharacterArrayParameter("CharacterArray", "charA", "desc", true);
        parser.parse();

        assertArrayEquals(new Character[]{'a', 'b', 'c'}, characterArray.getArgument());
    }

    @Test
    public void testAddDefaultCharacterArrayParameterWithoutArgument() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character[]> defaultCharacterArray = parser.addDefaultCharacterArrayParameter("CharacterArray", "charA", "desc", new Character[]{'x', 'y', 'z'});
        parser.parse();

        assertArrayEquals(new Character[]{'x', 'y', 'z'}, defaultCharacterArray.getArgument());
    }

    @Test
    public void testAddDefaultCharacterArrayParameterWithArgument() {
        String[] args = {"--CharacterArray", "a", "b", "c"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character[]> defaultCharacterArray = parser.addDefaultCharacterArrayParameter("CharacterArray", "charA", "desc", new Character[]{'x', 'y', 'z'});
        parser.parse();

        assertArrayEquals(new Character[]{'a', 'b', 'c'}, defaultCharacterArray.getArgument());
    }


    // Mixing addParameter Methods


    @Test
    public void testCombinationOfMandatoryArguments() {
        String[] args = new String[]{"-s", "stringInput", "-sArr", "string", "array", "-i", "12", "-iArr", "1", "2",
                "-d", "12.3", "-dArr", "1.2", "2.3", "-b", "true", "-bArr", "true", "false", "-c", "a", "-cArr", "d", "c"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> stringParam = parser.addMandatoryStringParameter("String", "s", "desc");
        Parameter<String[]> stringArrayParam = parser.addStringArrayParameter("StringArray", "sArr", "", false);
        Parameter<Integer> intParam = parser.addMandatoryIntegerParameter("Int", "i", "desc");
        Parameter<Integer[]> integerArrayParam = parser.addIntegerArrayParameter("IntArray", "iArr", "", false);
        Parameter<Double> doubleParam = parser.addMandatoryDoubleParameter("Double", "d", "desc");
        Parameter<Double[]> doubleArrayParam = parser.addDoubleArrayParameter("DoubleArray", "dArr", "", false);
        Parameter<Boolean> booleanParam = parser.addMandatoryBooleanParameter("Boolean", "b", "desc");
        Parameter<Boolean[]> booleanArrayParam = parser.addBooleanArrayParameter("BooleanArray", "bArr", "", false);
        Parameter<Character> characterParameter = parser.addMandatoryCharacterParameter("Character", "c", "desc");
        Parameter<Character[]> characterArrayParam = parser.addCharacterArrayParameter("CharacterArray", "cArr", "", false);
        parser.parse();

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
        String[] args = new String[]{"-s", "stringInput", "-sArr", "string", "array", "-iArr", "1", "2",
                "-d", "12.3", "-b", "true", "-bArr", "true", "false", "-cArr", "d", "c"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> stringParam = parser.addMandatoryStringParameter("String", "s", "desc");
        Parameter<String[]> stringArrayParam = parser.addStringArrayParameter("StringArray", "sArr", "", false);
        Parameter<Integer> intParam = parser.addOptionalIntegerParameter("Int", "i", "desc");
        Parameter<Integer[]> integerArrayParam = parser.addIntegerArrayParameter("IntArray", "iArr", "", false);
        Parameter<Double> doubleParam = parser.addMandatoryDoubleParameter("Double", "d", "desc");
        Parameter<Double[]> doubleArrayParam = parser.addDoubleArrayParameter("DoubleArray", "dArr", "", false);
        Parameter<Boolean> booleanParam = parser.addMandatoryBooleanParameter("Boolean", "b", "desc");
        Parameter<Boolean[]> booleanArrayParam = parser.addBooleanArrayParameter("BooleanArray", "bArr", "", false);
        Parameter<Character> characterParameter = parser.addOptionalCharacterParameter("Character", "c", "desc");
        Parameter<Character[]> characterArrayParam = parser.addCharacterArrayParameter("CharacterArray", "cArr", "", false);
        parser.parse();

        assertEquals("stringInput", stringParam.getArgument());
        assertArrayEquals(new String[]{"string", "array"}, stringArrayParam.getArgument());
        assertNull(intParam.getArgument());
        assertArrayEquals(new Integer[]{1, 2}, integerArrayParam.getArgument());
        assertEquals(12.3, doubleParam.getArgument(), 0.001);
        assertArrayEquals(null, doubleArrayParam.getArgument());
        assertEquals(true, booleanParam.getArgument());
        assertArrayEquals(new Boolean[]{true, false}, booleanArrayParam.getArgument());
        assertNull(characterParameter.getArgument());
        assertArrayEquals(new Character[]{'d', 'c'}, characterArrayParam.getArgument());
    }

    @Test
    public void testGetArgumentTypes() {
        String[] args = new String[]{"-s", "stringInput", "-sArr", "string", "array", "-i", "12", "-iArr", "1", "2",
        "-d", "12.3", "-dArr", "1.2", "2.3", "-b", "true", "-bArr", "true", "false", "-c", "a", "-cArr", "d", "c"};
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> stringParam = parser.addMandatoryStringParameter("String", "s", "desc");
        Parameter<String[]> stringArrayParam = parser.addStringArrayParameter("StringArray", "sArr", "", false);
        Parameter<Integer> intParam = parser.addMandatoryIntegerParameter("Int", "i", "desc");
        Parameter<Integer[]> integerArrayParam = parser.addIntegerArrayParameter("IntArray", "iArr", "", false);
        Parameter<Double> doubleParam = parser.addMandatoryDoubleParameter("Double", "d", "desc");
        Parameter<Double[]> doubleArrayParam = parser.addDoubleArrayParameter("DoubleArray", "dArr", "", false);
        Parameter<Boolean> booleanParam = parser.addMandatoryBooleanParameter("Boolean", "b", "desc");
        Parameter<Boolean[]> booleanArrayParam = parser.addBooleanArrayParameter("BooleanArray", "bArr", "", false);
        Parameter<Character> characterParameter = parser.addMandatoryCharacterParameter("Character", "c", "desc");
        Parameter<Character[]> characterArrayParam = parser.addCharacterArrayParameter("CharacterArray", "cArr", "", false);
        parser.parse();

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
        ArgsParser parser = new ArgsParser(args);

        parser.addMandatoryStringParameter("String", "s", "desc");
        parser.addStringArrayParameter("StringArray", "sArr", "", false);
        parser.addMandatoryIntegerParameter("Int", "i", "desc");
        parser.addIntegerArrayParameter("IntArray", "iArr", "", false);
        parser.addMandatoryDoubleParameter("Double", "d", "desc");
        parser.addDoubleArrayParameter("DoubleArray", "dArr", "", false);
        parser.addMandatoryBooleanParameter("Boolean", "b", "desc");
        parser.addBooleanArrayParameter("BooleanArray", "bArr", "", false);
        parser.addMandatoryCharacterParameter("Character", "c", "desc");
        parser.addCharacterArrayParameter("CharacterArray", "cArr", "", false);

        parser.parse();

        // Verifying results of getArgumentOf method
        assertEquals("stringInput", parser.getArgumentOf("--String"));
        assertArrayEquals(new String[]{"arr1", "arr2"}, (String[]) parser.getArgumentOf("--StringArray"));
        assertEquals(42, (Integer) parser.getArgumentOf("--Int"));
        assertArrayEquals(new Integer[]{1, 2}, (Integer[]) parser.getArgumentOf("--IntArray"));
        assertEquals(3.14, parser.getArgumentOf("--Double"));
        assertArrayEquals(new Double[]{1.1, 2.2}, (Double[]) parser.getArgumentOf("--DoubleArray"));
        assertEquals(true, parser.getArgumentOf("--Boolean"));
        assertArrayEquals(new Boolean[]{true, false}, (Boolean[]) parser.getArgumentOf("--BooleanArray"));
        assertEquals('x', (Character) parser.getArgumentOf("--Character"));
        assertArrayEquals(new Character[]{'a', 'z'}, (Character[]) parser.getArgumentOf("--CharacterArray"));
    }
}
