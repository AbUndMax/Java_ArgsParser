[![GitHub](https://img.shields.io/badge/GitHub-Java__ArgsParser-blue?logo=github)](https://github.com/AbUndMax/Java_ArgsParser)
[![License](https://img.shields.io/badge/License-MIT-blue)](https://github.com/AbUndMax/Java_ArgsParser/blob/main/LICENSE)
[![Java](https://img.shields.io/badge/Java-11+-b07219)](https://openjdk.org/projects/jdk/11/)
[![Badge](https://img.shields.io/github/v/release/AbUndMax/Java_ArgsParser?color=brightgreen)](https://github.com/AbUndMax/Java_ArgsParser/releases/latest)

# ArgsParser Library
The ArgsParser Library is a comprehensive solution 
designed to simplify the process of handling command-line arguments in Java applications.
With its user-friendly API, developers can easily define, parse, 
and access command-line parameters without the hassle of manual parsing. 
The tool supports a wide range of parameter types including Strings, Integers, Doubles, Booleans, and Characters,
catering to diverse application needs.

## Key features:

- **Easy Definition of Parameters:** Define parameters with flag names and shorthands, 
  mandatory/optional status, as well as optional default values and descriptions.
- **Robust Parsing-Error Handling:** Catch and handle custom exceptions for missing arguments, 
  invalid types, and other common parsing errors.
- **Integrated Help Function:** Automatically generates a help message displaying all parameters,
  their types, and descriptions.
- **Flexible Parameter Access:** Directly access parsed arguments with their defined types,
  streamlining the use in application logic.

## How to Use
### 1. Import the `ArgsParser` Package and initialize the `ArgsParser` class
Import the ArgsParser package.
Then initialize the `ArgsParser` class with the String[] args array of the main method.

```java
import ArgsParser.*;

public static void main(String[] args) {
    ArgsParser parser = new ArgsParser(args);
```

### 2. Define the Parameters
You can specify several fields for each parameter:

- **fullFlag**: The flag name of the parameter.
- **shortFlag**: A short version of the flag for the parameter.
- **isMandatory**: Whether the parameter is mandatory.
- **description**: An optional description of the parameter.
- **defaultValue**: An optional default value (which makes the parameter optional).
- **Type**: Type definition by calling the respective "addParameter" method.

```java
    Parameter<String> example = parser.addStringParameter("parameterFlag", "pf", true);
    Parameter<Integer> example2 = parser.addIntegerParameter("parameterFlag2", "pf2", false);
    Parameter<String> example3 = parser.addStringParameter("parameterFlag3", "pf3", "This is a description for the parameter", true);
    Parameter<Double> argWithDefault = parser.addDoubleParameter("parameterFlag4", "pf4", "description", 5.6);
```

### 3. Parse the Arguments
Call the `parseArgs()` method after adding all parameters.
Catch possible `ArgsException` errors for common parsing 
issues such as:

- No arguments provided
- Missing argument for a specific flag
- Mandatory arguments not provided
- Unknown flag
- Too many arguments provided

A `CalledForHelpNotification` can also be thrown if the user requests the help message.  
Exit with status code 0 for help requests and 1 for errors is recommended.

```java
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

### 4. Access the Arguments
Access the console arguments given by the user by calling the `getArgument()` method on the parameter variable.  
The return type matches the type defined when adding the parameter.
The arguments can be used directly in your code.

```java
    String providedArgument = example.getArgument();
    Double result = example2.getArgument() + argWithDefault.getArgument();
}
```

## Integrated --help function:
The ArgsParser tool has an integrated help function. If the user provides the flag `--help` or `-h` the tool will print
a help message with all the defined parameters. The help message will contain the full flag, the short flag, the 
description, and if the parameter is mandatory or not. The help message will be printed either for all parameters or only for the
parameter that was placed after the `--help` flag.

### Help example:
Calling `--help` or `-h` without anything else on the programm will print all available parameters: 

`> exampleProgramm --help`
```

############################################### HELP ###############################################
#               [s]=String | [i]=Integer | [c]=Character | [b]=Boolean | [d]=Double
#                                   (!)=mandatory | (+)=optional
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

while calling `--help` or `-h` with a specific parameter will only print the help message for that parameter:

`> exampleProgramm -pf4 -h`
```

############################################### HELP ###############################################
#               [s]=String | [i]=Integer | [c]=Character | [b]=Boolean | [d]=Double
#                                   (!)=mandatory | (+)=optional
#
###  --parameterFlag4  -pf4  [d] (+)  description
#                            default: 5.6
#
####################################################################################################
```

## ArgsException examples
The ArgsParser will throw an `ArgsException` if the user provides invalid arguments.
The printouts of these exceptions look like this:

`> exampleProgramm -pf4 5.6 5.6`
```

<!> Too many arguments provided to flag: -pf4

> Use --help for more information.

```

or 

`> exampleProgramm -pf4`
```

<!> Missing argument for flag: -pf4

> Use --help for more information.

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
