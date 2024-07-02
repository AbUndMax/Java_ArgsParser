import ArgsParser.*;
import ArgsParser.Argserror.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class TestArgsParser {

    @Test
    public void noArgumentsProvided() {
        ArgsParser parser = new ArgsParser(new String[] {});
        Parameter file = parser.addParameter("-f", true);
        try {
            parser.parseArgs();
        } catch (ArgsException e) {
            assertEquals(new NoArgumentsProvidedArgsException().getMessage(), e.getMessage());
        }
        String result = file.getArgument();
    }

    @Test
    public void testGetArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter file = parser.addParameter("file", true);
        try {
            parser.parseArgs();
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
        Parameter file = parser.addParameter("-file", true);
        Parameter save = parser.addParameter("--save", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            assertEquals(new UnknownFlagArgsException("-f").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testGetArgumentWithMultipleFlagsAndWrongInput() {
        ArgsParser parser = new ArgsParser(new String[] {"-f", "file.txt", "--save", "save.txt", "-s"});
        Parameter file = parser.addParameter("file", "f", true);
        Parameter save = parser.addParameter("save", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            assertEquals(new UnknownFlagArgsException("-s").getMessage(), e.getMessage());
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    @Test
    public void testMissingArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "--save", "save.txt"});
        Parameter file = parser.addParameter("file", true);
        Parameter save = parser.addParameter("save", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            assertEquals(new MissingArgArgsException("--file").getMessage(), e.getMessage());
        }
        String result = save.getArgument();
    }

    @Test
    public void testMissingLastArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--file", "file.txt", "--save"});
        Parameter file = parser.addParameter("file", true);
        Parameter save = parser.addParameter("--save", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            assertEquals(new MissingArgArgsException("--save").getMessage(), e.getMessage());
        }
        String result = save.getArgument();
    }

    @Test
    public void testMissingShorts() {
        ArgsParser parser = new ArgsParser(new String[]{"-f", "/to/file", "--save", "save.txt"});
        Parameter file = parser.addParameter("--file", true);
        Parameter save = parser.addParameter("--save", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            assertEquals(new UnknownFlagArgsException("-f").getMessage(), e.getMessage());
        }
        String result = file.getArgument();
    }

    @Test
    public void testMandatoryArgMissing() {
        ArgsParser parser = new ArgsParser(new String[]{"--file", "file.txt", "--optional", "optional.txt"});
        Parameter file = parser.addParameter("file", true);
        Parameter save = parser.addParameter("save", true);
        Parameter optional = parser.addParameter("optional", false);
        try {
            parser.parseArgs();
        } catch (ArgsException e) {
            assertEquals(new MandatoryArgNotProvidedArgsException("Mandatory parameters are missing:\n--save\n").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testTooManyArguments() {
        ArgsParser parser = new ArgsParser(new String[]{"--file", "file.txt", "--save", "save.txt", "extra"});
        Parameter file = parser.addParameter("file", true);
        Parameter save = parser.addParameter("save", true);
        try {
            parser.parseArgs();
        } catch (ArgsException e) {
            assertEquals(new TooManyArgumentsArgsException("--save").getMessage(), e.getMessage());
        }
    }

    @Test
    public void testGetArgumentAsString() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "-int", "5"});
        Parameter file = parser.addParameter("file", true);
        Parameter integer = parser.addParameter("integer", "int", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    @Test
    public void testGetArgumentWithGenericType() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--integer", "5"});
        Parameter file = parser.addParameter("file", true);
        Parameter integer = parser.addParameter("integer", Integer.class , true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = integer.getCastedArgument();
        Integer expected = 5;

        assertEquals(expected, result);
    }

    @Test
    public void testBooleanGetArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--boolean", "true"});
        Parameter file = parser.addParameter("file", true);
        Parameter bool = parser.addParameter("boolean", Boolean.class , true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(bool.getCastedArgument());
    }

    @Test
    public void testGetArgumentAsDouble() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--double", "5.5"});
        Parameter file = parser.addParameter("file", true);
        Parameter doub = parser.addParameter("double", Double.class , true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double result = doub.getCastedArgument();
        Double expected = 5.5;
        String StringResult = doub.getArgument();

        assertEquals(expected, result);
    }

    @Test
    public void testGetArgumentAsDoubleWithWrongInput() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt", "--double", "5.5.5"});
        Parameter file = parser.addParameter("file", true);
        Parameter doub = parser.addParameter("double", Double.class , true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            assertEquals(new InvalidArgTypeArgsException("--double").getMessage(), e.getMessage());
        }
    }

    @Test
    public void useDefaultValue() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter file = parser.addParameter("file", true);
        Parameter doub = parser.addParameter("double", false, 12.3);
        try {
            parser.parseArgs();
        } catch (Exception e) {
        }

        String result = doub.getArgument();

        Assert.assertEquals("12.3", result);
    }

    @Test
    public void useDefaultValueCast() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter file = parser.addParameter("file", true);
        Parameter doub = parser.addParameter("double", false, 12.3);
        try {
            parser.parseArgs();
        } catch (Exception e) {
        }

        Double result = doub.getCastedArgument();
        Double expected = 12.3;

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStringDefault() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "file.txt"});
        Parameter file = parser.addParameter("file", true, "default");
        Parameter doub = parser.addParameter("double", false, 12.3);
        try {
            parser.parseArgs();
        } catch (Exception e) {
        }

        String expected = "default";
        String result = file.getArgument();

        Assert.assertEquals(expected, result);
    }
}
