import ArgsParser.*;
import ArgsParser.ArgsExceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void testAddDefaultStringParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<String> deafult = parser.addDefaultStringParameter("DefaultString", "df", "desc", "this is the default");
        parser.parse();

        assertEquals("this is the default", deafult.getArgument());
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
    public void testAddDefaultStringArrayParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<String[]> defaultStringArray = parser.addDefaultStringArrayParameter("StringArray", "strA", "desc", new String[]{"this is the default", "and this is another"});
        parser.parse();
        
        assertArrayEquals(new String[]{"this is the default", "and this is another"}, defaultStringArray.getArgument());
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
    public void testAddDefaultIntegerParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer> defaultInteger = parser.addDefaultIntegerParameter("DefaultInteger", "di", "desc", 10);
        parser.parse();

        assertEquals(10, defaultInteger.getArgument());
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
    public void testAddDefaultIntegerArrayParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Integer[]> defaultIntegerArray = parser.addDefaultIntegerArrayParameter("IntegerArray", "intA", "desc", new Integer[]{4, 5, 6});
        parser.parse();

        assertArrayEquals(new Integer[]{4, 5, 6}, defaultIntegerArray.getArgument());
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
    public void testAddDefaultDoubleParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double> defaultDouble = parser.addDefaultDoubleParameter("DefaultDouble", "dd", "desc", 10.0);
        parser.parse();

        assertEquals(10.0, defaultDouble.getArgument());
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
    public void testAddDefaultDoubleArrayParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Double[]> defaultDoubleArray = parser.addDefaultDoubleArrayParameter("DoubleArray", "dblA", "desc", new Double[]{4.4, 5.5, 6.6});
        parser.parse();

        assertArrayEquals(new Double[]{4.4, 5.5, 6.6}, defaultDoubleArray.getArgument());
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
    public void testAddDefaultBooleanParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean> defaultBoolean = parser.addDefaultBooleanParameter("DefaultBoolean", "db", "desc", true);
        parser.parse();

        assertTrue(defaultBoolean.getArgument());
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
    public void testAddDefaultBooleanArrayParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Boolean[]> defaultBooleanArray = parser.addDefaultBooleanArrayParameter("BooleanArray", "boolA", "desc", new Boolean[]{true, false, true});
        parser.parse();

        assertArrayEquals(new Boolean[]{true, false, true}, defaultBooleanArray.getArgument());
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
    public void testAddDefaultCharacterParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character> defaultCharacter = parser.addDefaultCharacterParameter("DefaultCharacter", "dc", "desc", 'z');
        parser.parse();

        assertEquals('z', defaultCharacter.getArgument());
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
    public void testAddDefaultCharacterArrayParameter() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        Parameter<Character[]> defaultCharacterArray = parser.addDefaultCharacterArrayParameter("CharacterArray", "charA", "desc", new Character[]{'x', 'y', 'z'});
        parser.parse();

        assertArrayEquals(new Character[]{'x', 'y', 'z'}, defaultCharacterArray.getArgument());
    }
}
