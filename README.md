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
2. Define the parameters you want the user to input. several fields can be specified:
   - The name of the parameter
   - A short flag for the parameter
   - If the parameter is mandatory or not
   - A description of the parameter
   - An optional type definition for the parameter (default is String, no casting)
```Java
    Parameter example = parser.addParameter("parameterFlag", true);
    Parameter example2 = parser.addParameter("parameterFlag2", "pf2", Integer.class, false);
    Parameter example3 = parser.addParameter("parameterFlag3", "pf3", "This is a description for the parameter", true);
```
3. After all parameters are added, call the parse method and catch possible ArgsExceptions like No Arguemnts provided, 
Missing argument for a specific flag, mandatory Arguments not provided, unknown Flag or too many arguments provided.
```Java
    try {
        parser.parseArgs();
    } catch (ArgsException e) {
        System.out.println(e.getMessage());
        System.exit(1);
    }
```
4. Now the arguments given by the user can be accessed by calling the .getArgument() method on the parameter variable for the specific parameter.
It is also possible to directly get the argument as a specific type.
```Java
    String providedArgument = example.getArgument();
    Integer integerArgument = example2.getArgument();
}
```
