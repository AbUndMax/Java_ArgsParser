[![Badge](https://img.shields.io/badge/release-v2.0.0-brightgreen)](https://github.com/AbUndMax/Java_ArgsParser/releases/tag/Release_v2.0.0)
# ArgsParser Tool
This tool makes it easier to define what arguments a user should input while at the same time it makes it easier for 
the programmer to access the given arguments.

## How to use
1. #### Import the ArgsParser class

```Java
import ArgsParser.ArgsParser;

public static void main(String[] args) {
    ArgsParser parser = new ArgsParser(args);
```

2. Define the parameters you want the user to input. several fields can be specified: (see below for all available "addParameter" methods)
   - The name of the parameter
   - A short flag for the parameter
   - If the parameter is mandatory or not
   - An optional description of the parameter
   - An optional default value (which sets the mandatory of the parameter to false)
   - implicit type definition for the parameter
```Java
    Parameter<String> example = parser.addStringParameter("parameterFlag", "pf", true);
    Parameter<Integer> example2 = parser.addIntegerParameter("parameterFlag2", "pf2", false);
    Parameter<String> example3 = parser.addStringParameter("parameterFlag3", "pf3", "This is a description for the parameter", true);
    Parameter<Double> argWithDefault = parser.addDoubleParameter("parameterFlag4", "pf4", "description", 5.6);
```

3. After all parameters are added, call the parse method and catch possible ArgsExceptions like No Arguments provided, 
Missing argument for a specific flag, mandatory Arguments not provided, unknown Flag or too many arguments provided. 
Additionally, a CalledForHelpNotification can be thrown if the user wants to see the help message. Thus, we want to 
exit with status code 0 if the user asks for help and with status code 1 if an error occurred.

```Java
    try {
        parser.parseArgs();
        
    } catch (CalledForHelpNotification help) {
        System.out.println(help.getMessage());
        System.exit(0);
        

    } catch (ArgsException e) {
        System.out.println(e.getMessage());
        System.exit(1);
    }
    
```

4. Now the arguments given by the user can be accessed by calling the .getArgument() method on the parameter variable for the specific parameter.
The return type of the .getArgument() method is the type that was defined when adding the parameter. The Arguments can 
directly be used in the code and have not to be assigned to a new variable.

```Java
    String providedArgument = example.getArgument();
    Double result = example2.getArgument() + argWithDefault.getArgument();
    
}
```

## Integrated --help function:
The ArgsParser tool has an integrated help function. If the user provides the flag `--help` or `-h` the tool will print
a help message with all the defined parameters. The help message will contain the full flag, the short flag, the description
and if the parameter is mandatory or not. The help message will be printed either for all parameters or only for the
parameter that was placed after the `--help` flag.

### Help example:
```
############################################### HELP ###############################################
#               [s]=String | [i]=Integer | [d]=Double | [b]=Boolean | [c]=Character
#                         (!)=mandatory parameter | (+)=optional parameter
#
#                                      Available Parameters:
#
###  --parameterFlag4  -pf4  [d] (+)  description
#                            default: 5.6
#
###  --parameterFlag2  -pf2  [i] (+)  No description available!
#
###  --parameterFlag3  -pf3  [s] (!)  This is a description for the parameter
#
###  --parameterFlag   -pf   [s] (!)  No description available!
#
####################################################################################################
```

## List of all available "addParameter" methods

#### String type:
- `addStringParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addStringParameter(String fullFlag, String shortFlag, boolean isMandatory)`
- `addStringParameter(String fullFlag, String shortFlag, String description, String defaultValue)`
- `addStringParameter(String fullFlag, String shortFlag, String defaultValue)`

#### Integer type:
- `addIntegerParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addIntegerParameter(String fullFlag, String shortFlag, boolean isMandatory)`
- `addIntegerParameter(String fullFlag, String shortFlag, String description, Integer defaultValue)`
- `addIntegerParameter(String fullFlag, String shortFlag, Integer defaultValue)`

#### Double type:
- `addDoubleParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addDoubleParameter(String fullFlag, String shortFlag, boolean isMandatory)`
- `addDoubleParameter(String fullFlag, String shortFlag, String description, Double defaultValue)`
- `addDoubleParameter(String fullFlag, String shortFlag, Double defaultValue)`

#### Boolean type: (default values are in the first position!)
- `addBooleanParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addBooleanParameter(String fullFlag, String shortFlag, boolean isMandatory)`
- `addBooleanParameter(Boolean defaultValue, String fullFlag, String shortFlag, String description)`
- `addBooleanParameter(Boolean defaultValue, String fullFlag, String shortFlag)`

#### Character type:
- `addCharacterParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addCharacterParameter(String fullFlag, String shortFlag, boolean isMandatory)`
- `addCharacterParameter(String fullFlag, String shortFlag, String description, Character defaultValue)`
- `addCharacterParameter(String fullFlag, String shortFlag, Character defaultValue)`
