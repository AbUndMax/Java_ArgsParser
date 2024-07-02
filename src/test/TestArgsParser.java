import ArgsParser.*;
import ArgsParser.Argserror.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


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
}
