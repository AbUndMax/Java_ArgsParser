[![GitHub](https://img.shields.io/badge/GitHub-Java__ArgsParser-a7752f?logo=github)](https://github.com/AbUndMax/Java_ArgsParser)
[![Java](https://img.shields.io/badge/Java-11+-a7752f)](https://openjdk.org/projects/jdk/11/)
[![License](https://img.shields.io/badge/License-CC_BY--NC_4.0-blue)](https://github.com/AbUndMax/Java_ArgsParser/blob/main/LICENSE.md)
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
- **Build-in Parameter Types:** ArgsParser provides usage ready Parameters for all commonly used types like *String*, 
  *Double*, *Path* and more
- **Extensible:** Extend the library with custom parameter classes tailored to your application’s needs.


## Example Code:

```java
import ArgsParser.*;
import ArgsParser.ParameterTypes.StrParameter;

public class Example {
  public static void main(String[] args) {
    ArgsParser parser = new ArgsParser();
    // define a String mandatory parameter
    StrParameter param1 = parser.addParameter(
            new StrParameter("parameterFlag", "pf", "description", true));
    // define a command
    Command command1 = parser.addCommand(
            new Command("commandName", "c", "description of the command"));
    
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

#### addParameter method:
The addParameter method is called on an ArgsParser instance
and allows you to add different types of parameters to your parser using specific parameter classes.
Those parameters added to the parser will be checked for several conditions (see "Parse the Arguments").

#### Example:
```java
    // ...
    StrParameter example = parser.addParameter(
        new StrParameter("parameterFlag", "pf", "short Description", true));
    IntParameter example2 = parser.addParameter(
        new IntParameter("parameterFlag2", "pf2", null, false));
    DblParameter argWithDefault = parser.addParameter(
        new DblParameter(5.6, "parameterFlag3", "pf3", "description"));
    BolArrParameter booleanArrayParam = parser.addParameter(
        new BolArrParameter(new Boolean[]{true, false, false}, "boolArray", "bArr", "Array of several boolean values"));
    IntArrParameter integerArrayParam = parser.addParameter(
        new IntArrParameter(new Integer[]{1, 2, 3}, "intArray", "iArr", "Array of several integer values"));
    Command command = parser.addCommand(
        new Command("commandName", "cN", "this is a description for the command"));
    // ...
```

#### Build-in Parameter types:
Build-in ready to use classes are:
- `StrParameter` / `StrArrParameter` for String arguments.
- `IntParameter` / `IntArrParameter` for Integer arguments.
- `DblParameter` / `DblArrParameter` for Double arguments.
- `BolParameter` / `BolArrParameter` for Boolean arguments.
- `ChrParameter` / `ChrArrParameter` for Character arguments.
- `FltParameter` / `FltArrParameter` for Float arguments.
- `PthParameter` / `PthArrParameter` for Path arguments.

For each Parameter type, two constructors exist:
`xxxParameter(String fullFlag, String shortFlag, String description, boolean isMandatory)`  
or  
`xxxParameter(xxx defaultValue, String fullFlag, String shortFlag, String description)`  

#### Fields explained:
You can specify several fields for each parameter:

- **fullFlag**: The flag name of the parameter.
- **shortFlag**: A short version of the flag for the parameter.
- **description**: A description of the parameter, insert `null` or an empty string`""` if not needed.
- **defaultValue**: A default value that the parameter returns if no argument was provided but accessed in the program.
- **isMandatory**: Determines whether the flag must be provided in the arguments. If set to true and the flag is missing, an ArgsException will be thrown.

#### The PthParameter
The Parameter handling Paths provides has one additional field in each of the two constructors:  
`pathCheck`.
If this is set true, the parser will check if the provided path does exist, if not it will raise an ArgsException!

#### Add your own Parameters of a desired Type:
By creating a class extending Parameter<T> allows you to use your own Parameters with this ArgsParser!
(see "Create your own Parameters")

#### Multiple Arguments to one flag:
A **special type** of Parameters are the `xxxArrParameter` classes.
They allow handing several arguments to a single flag:
```
--file path/file1 path/file2 path/file3
```
Several of these Array Parameters can be defined without problems:
```
--file path/file1 path/file2 path/file3 --Integers 1 2 3
```

#### Commands:

Another added feature in Version 4.0.0 is the "command" type.
The parser checks if any commands were provided in the arguments, enabling the developer to activate different
modes or functionalities based on the commands passed. 
You simply define commands just like regular parameters, and the
parser will recognize them when parsing the input arguments.

#### Toggles:

ArgsParser provides the method .toggle(Command...) which takes several Command instances as arguments. This method 
restricts 
the usage of the provided Commands to only one of them!

For example:

```Java
    // ...
    Command cmd1 = parser.addCommand(
        new Command("commName1", "cmdN1", "Description of command1"));
    Command cmd2 = parser.addCommand(
        new Command("commName2", "cmdN2", "Description of command2"));
    Command cmd3 = parser.addCommand(
        new Command("commName3", "cmdN3", "Description of command3"));
    parser.toggle(cmd1, cmd2);
    // ...
```

with this only cmd1 or cmd2 are allowed to be present in args. If both commands would be present, .parseUnchecked() 
will throw ToggleArgsException or .parse() will end the program and print which commands cannot be combined!

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
- Trying to set the same flag twice (`FlagAlreadyProvidedArgsException`)
- Calling help at the wrong position (`HelpAtWrongPositionArgsException `)
- Providing a not existing path to a PthParameter with pathCheck enabled (`NotExistingPathArgsException`)
- Providing two commands that are part of a toggle (`ToggleArgsException`)

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

#### check provision of a command or Parameter
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
#                                       Available Commands:
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

## ArgsException printout examples
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

## Create your own Parameters:
By creating a class extending Parameter<T> with T of the Type that your Parameter should handle, you can implement
your own Parameters that are compatible with this ArgsParser!

### To create a custom parameter, follow these steps:
#### 1. Define the Parameter Type:
Determine the type T that the parameter will handle (e.g., Integer, String, etc.).  
(We use Integer as T for this example!)
#### 2. Create the Subclass:
Extend the Parameter<T> class, specifying the appropriate type.
```java
public class IntegerParameter extends Parameter<Integer> {
    // Implementation details
}
```
#### 3. Implement Constructors:
Provide constructors that call the superclass constructors, passing necessary parameters such as flags, description,
mandatory status, and default values if applicable.
```java
public IntegerParameter(String fullFlag, String shortFlag, String description, boolean isMandatory) {
    super(fullFlag, shortFlag, description, isMandatory, Integer.class);
}

public IntegerParameter(Integer defaultValue, String fullFlag, String shortFlag, String description) {
    super(defaultValue, fullFlag, shortFlag, description, Integer.class);
}
```
#### 4. Override castArgument:
Implement the castArgument method to convert the input string to the desired type T. Handle any
necessary validation and exception throwing within this method.
```java
@Override
protected Integer castArgument(String argument) throws InvalidArgTypeArgsException {
    try {
        return Integer.parseInt(argument);
    } catch (NumberFormatException e) {
        throw new InvalidArgTypeArgsException(getFullFlag(), "Integer", "Invalid integer value: " + argument);
    }
}
```

## List of methods:

#### add a parameter to a parser instance:
- `parser.addParameter()`

#### access an Argument of ANY of these Parameters:
- `parameter.getArgument()`

#### add a Command to a parser instance:
- `addCommand(String fullCommandName, String shortCommandName, String description)`

#### check provision of a specific command:
- `command.isProvided()`

#### check provision of a specific Parameter or if it has a value
- `parameter.isProvided()`
- `parameter.hasArgument()`

#### restriction of command usage:
- `parser.toggle(Command...)`

#### indirect access of parameters / commands:
- `getArgumentOf(String fullFlag)`
- `checkIfCommandIsProvided(String fullCommandName)`

## Full Code Example:
```java
public static void main(String[] args) {
    // initialize ArgsParser instance
    ArgsParser parser = new ArgsParser(args);
    
    // declare Parameters on the parser instance
    StrParameter example = parser.addParameter(
          new StrParameter("parameterFlag", "pf", "short Description", true));
    IntParameter example2 = parser.addParameter(
          new IntParameter("parameterFlag2", "pf2", null, false));
    DblParameter argWithDefault = parser.addParameter(
          new DblParameter(5.6, "parameterFlag3", "pf3", "description"));
    BolArrParameter booleanArrayParam = parser.addParameter(
          new BolArrParameter(new Boolean[]{true, false, false}, "boolArray", "bArr", "Array of several boolean values"));
    IntArrParameter integerArrayParam = parser.addParameter(
            new IntArrParameter(new Integer[]{1, 2, 3}, "intArray", "iArr", "Array of several integer values"));
    Command command = parser.addCommand(
          new Command("commandName", "cN", "this is a description for the command"));

    // declare Commands on the parser instance
    Command cmd1 = parser.addCommand(
          new Command("commName1", "cmdN1", "Description of command1"));
    Command cmd2 = parser.addCommand(
          new Command("commName2", "cmdN2", "Description of command2"));
    Command cmd3 = parser.addCommand(
          new Command("commName3", "cmdN3", "Description of command3"));
    parser.toggle(cmd1, cmd2);

    // parser the command-line arguments
    parser.parse();

    // example for direct access of command-line arguments via their parameters
    String providedArgument = example.getArgument();
    Double result = example2.getArgument() + argWithDefault.getArgument();

    // example for checking a command
    if (command.isProvided()) System.out.println("command provided");
    
    // example for indirect command-line argument access
    String providedArgument = parser.getArgumentOf("parameterFlag");
    Integer getInteger = parser.getArgumentOf("parameterFlag2");
    Double getDouble = parser.getArgumentOf("parameterFlag3");
    Double result = getInteger + getDouble;

    // example for indirect command check
    if (parser.checkIfCommandIsProvided("commandName")) System.out.println("command still provided");
}

```
