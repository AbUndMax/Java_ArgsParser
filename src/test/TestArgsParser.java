import ArgsParser.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestArgsParser {

    @Test
    public void testGetArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--f", "file.txt"});
        Parameter file = parser.addParameter("-f", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
        String result = file.getArgument();

        assertEquals(null, result);
    }

    @Test
    public void testGetArgumentWithMultipleFlagsAndWrongInput() {
        ArgsParser parser = new ArgsParser(new String[] {"-f", "file.txt", "--s", "save.txt", "-s"});
        Parameter file = parser.addParameter("--file", "-f", true);
        Parameter save = parser.addParameter("-s", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String result = file.getArgument();

        assertEquals("file.txt", result);
    }

    @Test
    public void testMissingArgument() {
        ArgsParser parser = new ArgsParser(new String[] {"--file", "-save", "save.txt"});
        Parameter file = parser.addParameter("--file", true);
        Parameter save = parser.addParameter("--save", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String result = save.getArgument();

        assertEquals("save.txt", result);
    }

    @Test
    public void testMissingLastArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--file", "file.txt", "--save"});
        Parameter file = parser.addParameter("--file", true);
        Parameter save = parser.addParameter("--save", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String result = save.getArgument();

        assertEquals("", result);
    }

    @Test
    public void testMissingShorts() {
        ArgsParser parser = new ArgsParser(new String[]{"-f", "/to/file", "--save", "save.txt"});
        Parameter file = parser.addParameter("--file", true);
        Parameter save = parser.addParameter("--save", true);
        try {
            parser.parseArgs();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String result = file.getArgument();

        assertEquals("", result);
    }
}
