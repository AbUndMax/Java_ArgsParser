# ArgsParser Tool
This tool makes it easier to define what arguments a user should input while at the same time it makes it easer for 
the programmer to access the given arguments.

## How to use
1. Import the ArgsParser class

```Java
import ArgsParser.ArgsParser;

public static void main(String[] args) {
    ArgsParser parser = new ArgsParser(args);
```
2. Define the parameters you want the user to input:
   1. first String is the parameters flag name
   2. second string is optional and is the short name for the flag
   3. third boolean defines if the parameter is mandatory or not
```Java
    parser.addParameter("--parameterFlag", "-pf", true);
```
3. After all parameters are added, call the parse method.
```Java
    parser.parseArgs();
```
4. Now the arguments given by the user can be called by their flag or short flag.
```Java
    String stringArgument = parser.getParameter("--parameterFlag");
    int intArgument = parser.getParameterAsInteger("--parameterFlag");
    double doubleArgument = parser.getParameterAsDouble("--pf");
    boolean booleanArgument = parser.getParameterAsBoolean("--parameterFlag");
    char charArgument = parser.getParameterAsChar("--pf");
}
```
