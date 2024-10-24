[![GitHub](https://img.shields.io/badge/GitHub-Java__ArgsParser-blue?logo=github)](https://github.com/AbUndMax/Java_ArgsParser)
[![License](https://img.shields.io/badge/License-CC_BY--NC_4.0-blue)](https://github.com/AbUndMax/Java_ArgsParser/blob/main/LICENSE.md)
[![Java](https://img.shields.io/badge/Java-11+-b07219)](https://openjdk.org/projects/jdk/11/)
[![Badge](https://img.shields.io/github/v/release/AbUndMax/Java_ArgsParser?color=brightgreen)](https://github.com/AbUndMax/Java_ArgsParser/releases/latest)

# ArgsParser Library
The ArgsParser Library is a comprehensive solution 
designed to simplify the process of handling command-line arguments in Java applications.
With its user-friendly API, parameters can easily be defined, parsed, 
and accessed. 
It removes complex exception handling by providing automatic checks on common command-line input errors 
The tool supports a wide range of parameter types including Strings, Integers, Doubles, Booleans, and Characters,
catering to diverse application needs.

## Key features:

- **Definition Of Parameters:** Define parameters with flag names and shorthands, 
  mandatory/optional status, as well as optional default values and descriptions.
- **Definition Of Commands:** Define commands and easily check if they were provided.  
- **Robust Parsing-Error Handling:** Catch and handle custom exceptions for missing arguments, 
  invalid types, and other common parsing errors.
- **Automatic Exception Handling** Alternatively of handling parsing errors manually, you can use a parsing method that 
  automatically handles all exceptions and exits the program with the appropriate status code.
- **Suggestions For Misspelled Flags:** The parser will suggest the correct flag if a misspelled flag was provided.
- **Integrated Help Function:** Automatically generates a help message displaying all parameters,
  their types, and descriptions.
- **Multiple Arguments To One Flag:** Allows multiple values to be specified for a single command-line flag, making it
  easier to pass arrays of data.
- **Arguments Directly Casted:** Accessing a parameter's argument returns it as the defined type.
- **Lightweight:** Using the ArgsParser library is very simply and straight forward as shown in the example below.

## Example Code:

```java
import ArgsParser.*;

public class Example {
  public static void main(String[] args) {
    ArgsParser parser = new ArgsParser(args);
    Parameter<String> param1 = parser.addMandatoryStringParameter("parameterFlag", "pf", "description");
    Command command1 = parser.addCommand("commandName", "c", "description of the command");
    ArgsParser.parse(args);

    if (command1.isProvided()) {
    System.out.println(param1.getArgument());
    }
  }
}
```
Using  
`> java Example -pf "Hello World!"`  
in the CLI will print:  
`> Hello World!`

## How to Use
### 1. Import the `ArgsParser` Package
Import the ArgsParser package.
```java
import ArgsParser.*;
```
Instantiate an ArgsParser object, and hand over the String array `args` from the main method.

```java
public static void main(String[] args) {
    ArgsParser parser = new ArgsParser(args);
    // ...
```

### 2. Define the Parameters or Commands
This parser supports 5 **types**:
- *String, Integer, Double, Boolean, Character*

#### addParameter methods:
The type of a Parameter is defined with its respective method on the parser object.
For each type there are 5 `addParameter` methods (example for String):
- `addMandatoryStringParameter(String fullFlag, String shortFlag, String description)`
- `addOptionalStringParameter(String fullFlag, String shortFlag, String description)`
- `addDefaultStringParameter(String fullFlag, String shortFlag, String description, String defaultValue)`
- `addStringArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addDefaultStringArrayParameter(String fullFlag, String shortFlag, String description, String[] defaultValue)`
  
(see a list of all Methods at the end of this README)

#### Available Fields for each Parameter:
You can specify several fields for each parameter:

- **fullFlag**: The flag name of the parameter.
- **shortFlag**: A short version of the flag for the parameter.
- **description**: A description of the parameter, insert `null` or an empty string`""` if not needed.
- **defaultValue**: A default value that the parameter returns if no argument was provided but accessed in the program.

#### Multiple Arguments to one flag:
A **special type** are the `addArray` methods that are introduced in Version 4.0.0 of the ArgsParser. 
They allow handing several arguments to a single flag:
```
--file path/file1 path/file2 path/file3
```
Several of these Array Parameters can be defined without problems.

#### Commands:

Another added feature in Version 4.0.0 is the "command" type.
The parser checks if any commands were provided in the arguments, enabling the developer to activate different
modes or functionalities based on the commands passed. 
You simply define commands just like regular parameters, and the
parser will recognize them when parsing the input arguments.

```java
// ...
    Parameter<String> example = parser.addMandatoryStringParameter("parameterFlag", "pf", "short Description");
    Parameter<Integer> example2 = parser.addOptionalIntegerParameter("parameterFlag2", "pf2", null);
    Parameter<Double> argWithDefault = parser.addDefaultDoubleParameter("parameterFlag3", "pf3", "description", 5.6);
    Parameter<Boolean[]> booleanArrayParam = parser.addBooleanArrayParameter("boolArray", "bArr", "Array of several boolean values", false);
    Parameter<Integer[]> integerArrayParam = parser.addDefaultIntegerArrayParameter("intArray", "iArr", "Array of several integer values", new Integer[]{1, 2, 3});
    Command command = parser.addCommand("commandName", "cN", "this is a description for the command");
// ...
```

### 3. Parse the Arguments
Call the `parser.parse()` or `parser.parseUnchecked()` method, after adding all 
parameters on the ArgsParser object (here named: `parser`).

- The `parse()` method directly handles all ArgsExceptions and uses `System.exit()` if any invalid argument is 
provided to the console or `--help` / `-h` was used thus preventing to run the script with invalid input automatically.
- The `parseUnchecked()` method doesn't use `System.exit()`at any point but throws ArgsExceptions, 
which you therefore can catch and handle manually.

The ArgsParser catches possible `ArgsException` errors for common parsing 
issues such as:

- No arguments provided (`NoArgumentsProvidedArgsException`)
- Missing argument for a specific flag (`MissingArgArgsException`)
- Mandatory arguments not provided (`MandatoryArgNotProvidedArgsException`)
- Unknown flag (`UnknownFlagArgsException`)
- Too many arguments provided (`TooManyArgsProvidedArgsException`)
- Invalid argument types (`InvalidArgTypeArgsException`)
- trying to set the same flag twice (`FlagAlreadyProvidedArgsException`)
- calling help at the wrong position (`HelpAtWrongPositionArgsException `)

A `CalledForHelpNotification` can also be thrown if the user requests the help message.  
Exit with status code 0 for help requests and 1 for errors is recommended.

The Code example looks like this:
```Java
    // ...
    parser.parse();
    // ...
```

or like the following example for manually handling the ArgsExceptions:
```java
    // ...
    try {
        parser.parseUnchecked();
        
    } catch (CalledForHelpNotification help) {
        System.out.println(help.getMessage());
        System.exit(0);
        
    } catch (ArgsException e) {
        System.out.println(e.getMessage());
        System.exit(1);
    }
    // ...
```

### 4. Access the Arguments

#### direct access to arguments via its parameter
Access the console arguments given by the user by calling the `getArgument()` method on the parameter variable.  
The return type matches the type defined when adding the parameter.
The **arguments can be used directly in your code**!

```java
    // ...
    String providedArgument = example.getArgument();
    Double result = example2.getArgument() + argWithDefault.getArgument();
    //...
```

#### check provision of a command
For the commands, a simple call of `isProvided` on the Command instance will return if the command was provided in args:
```java
    //...
    if (command.isProvided()) System.out.println("command provided");
    //...
```

#### access arguments indirectly via the ArgsParser instance they were defined on
With version 3.0.0 the `getArgumentOf(String fullFlag)` method ia available, thus arguments provided to a specific
parameter flag can be accessed by the parameter flugFlag name.
But this comes with some restrictions:
- `getArgumentOf()` is generic, thus the returned value cannot be directly used in the program but has to be 
  assigned first.
- The type of the variable which gets the argument assigned has to be of the same type as the type of the parameter 
  when adding it with any `addParameter()`method!

```java
    // ...
    String providedArgument = parser.getArgumentOf("parameterFlag");
    Integer getInteger = parser.getArgumentOf("parameterFlag2");
    Double getDouble = parser.getArgumentOf("parameterFlag3");
    Double result = getInteger + getDouble;
    //...
```

The same is possible with commands:
```java
    //...
    if (parser.checkIfCommandIsProvided("commandName")) System.out.println("command still provided");
}
```

## Integrated --help function:
The ArgsParser tool has an integrated help function. If the user provides the flag `--help` or `-h` the tool will print
a help message with all the defined parameters. The help message will contain the full flag, the short flag, the 
description, and if the parameter is mandatory or not. The help message will be printed either for all parameters or only for the
parameter that was placed before the `--help` flag.

### Help example:
Calling `--help` or `-h` without anything else on the programm will print all available parameters **in the order
they were added** on the ArgsParser Instance: 

`> exampleProgramm --help`
```

############################################### HELP ###############################################
#   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
#       ('+' marks a flag that takes several arguments of the same type whitespace separated)
#                            (!)=mandatory | (?)=optional | (/)=command
#
#                                      Available Parameters:
#
###  --parameterFlag   -pf    [s]  (!)  short Description
#
###  --parameterFlag2  -pf2   [i]  (?)  No description available!
#
###  --parameterFlag3  -pf3   [d]  (?)  description
#                             default:  5.6
#
###  --boolArray       -bArr  [b+] (?)  Array of several boolean values
#
###  --intArray        -iArr  [i+] (?)  Array of several integer values
#                             default:  [1, 2, 3]
#
###  commandName       cN          (/)  this is a description for the command
#
####################################################################################################
```

while calling `--help` or `-h` with a specific parameter will only print the help message for that parameter:

`> exampleProgramm -pf3 -h`
```

############################################### HELP ###############################################
#   [s]/[s+]=String | [i]/[i+]=Integer | [c]/[c+]=Character | [b]/[b+]=Boolean | [d]/[d+]=Double
#       ('+' marks a flag that takes several arguments of the same type whitespace separated)
#                            (!)=mandatory | (?)=optional | (/)=command
#
###  --parameterFlag3  -pf3   [d]  (?)  description
#                             default:  5.6
#
####################################################################################################
```

## ArgsException examples
The ArgsParser will throw an `ArgsException` if the user provides invalid arguments.
The printouts of these exceptions look like this:

`> exampleProgramm -pf3 5.6 5.6`
```

<!> Too many arguments provided to flag: -pf3

> Use --help for more information.

```

or 

`> exampleProgramm -pf3`
```

<!> Missing argument for flag: -pf3

> Use --help for more information.

```

for misspelled flags, the Parser will even do a suggestion:

`> exampleProgramm --paraeterflg3, 5.6`
```

<!> unknown flag: --paraeterflg3
> did you mean: --parameterFlag3 ?

> Use --help for more information.

```

## List of all available "addParameter" methods

#### String type:
- `addMandatoryStringParameter(String fullFlag, String shortFlag, String description)`
- `addOptionalStringParameter(String fullFlag, String shortFlag, String description)`
- `addDefaultStringParameter(String fullFlag, String shortFlag, String description, String defaultValue)`
- `addStringArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addDefaultStringArrayParameter(String fullFlag, String shortFlag, String description, String[] defaultValue)`

#### Integer type:
- `addMandatoryIntegerParameter(String fullFlag, String shortFlag, String description)`
- `addOptionalIntegerParameter(String fullFlag, String shortFlag, String description)`
- `addDefaultIntegerParameter(String fullFlag, String shortFlag, String description, Integer defaultValue)`
- `addIntegerArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addDefaultIntegerArrayParameter(String fullFlag, String shortFlag, String description, Integer[] defaultValue)`

#### Double type:
- `addMandatoryDoubleParameter(String fullFlag, String shortFlag, String description)`
- `addOptionalDoubleParameter(String fullFlag, String shortFlag, String description)`
- `addDefaultDoubleParameter(String fullFlag, String shortFlag, String description, Double defaultValue)`
- `addDoubleArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addDefaultDoubleArrayParameter(String fullFlag, String shortFlag, String description, Double[] defaultValue)`

#### Boolean type:
- `addMandatoryBooleanParameter(String fullFlag, String shortFlag, String description)`
- `addOptionalBooleanParameter(String fullFlag, String shortFlag, String description)`
- `addDefaultBooleanParameter(String fullFlag, String shortFlag, String description, Boolean defaultValue)`
- `addBooleanArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addDefaultBooleanArrayParameter(String fullFlag, String shortFlag, String description, Boolean[] defaultValue)`

#### Character type:
- `addMandatoryCharacterParameter(String fullFlag, String shortFlag, String description)`
- `addOptionalCharacterParameter(String fullFlag, String shortFlag, String description)`
- `addDefaultCharacterParameter(String fullFlag, String shortFlag, String description, Character defaultValue)`
- `addCharacterArrayParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`
- `addDefaultCharacterArrayParameter(String fullFlag, String shortFlag, String description, Character[] defaultValue)`

#### access an Argument of ANY of these Parameters:
- `parameter.getArgument()`

#### Command type:
- `addCommand(String fullCommandName, String shortCommandName, String description)`

#### check provision of a specific command:
- `command.isProvided()`

#### indirect access of parameters / commands:
- `getArgumentOf(String fullFlag)`
- `checkIfCommandIsProvided(String fullCommandName)`
