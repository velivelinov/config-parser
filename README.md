# Config Parser

A simple config parser that parses a config file and returns an object that can be queried like:

```config.get("{GROUP_NAME}")``` - returns an optional Settings object (with underlying Map) of all settings in the group.

```config.get("{GROUP_NAME}".get("{SETTING_NAME}"))``` - returns an optional SettingValue object containing the value of that setting.

```config.get("{GROUP_NAME}".getString("{SETTING_NAME}"))``` - returns a String containing the value of that setting.

```config.get("{GROUP_NAME}".getBoolean("{SETTING_NAME}"))``` - returns a Boolean containing the value of that setting.

```config.get("{GROUP_NAME}".getLong("{SETTING_NAME}"))``` - returns a Long containing the value of that setting.

```config.get("{GROUP_NAME}".getList("{SETTING_NAME}"))``` - returns a List of String containing the value of that setting.

## Pre-requisites
SBT is required in order to run the app or the tests.

## How to run
To run the tests, run the following SBT command:

```sbt test```

To parse a config file that has been placed in the `src/main/resources` folder, use the following command:

```sbt "runMain twitter.config.Main {NAME_OF_CONF_FILE} {LIST_OF_OVERRIDES}"```

where:
* {NAME_OF_CONF_FILE} is the name of your file, e.g. `normal.conf`
* {LIST_OF_OVERRIDES} - optional. This is a comma-separated list of overrides you want to provide - e.g. `staging,ubuntu`
 
Example - to run the app and reading `normal.conf` with `staging,ubuntu` as overrides:

```sbt "runMain twitter.config.Main normal.conf staging,ubuntu"```


## Assumptions made
* A setting name or an override cannot contain any of the following: new line, space, `,`, `<`, `>`, `=`, `;`, `[`, `]`.
* String setting values are enclosed in double quotes (`"`)
* Path setting values have to start with `/` but do not have to necessarily end in `/`
* A group name cannot contain the characters `[` and `]`.
* A group cannot be empty, it needs to contain at least one setting.
* A config cannot be empty, it needs to contain at least one group.
* A long value cannot take value 1 or 0 - those are parsed as Booleans (true and false respectively).
* Comments start with `;` and cannot span on multiple lines.
* When a setting has a list of strings as a value, `a,b,c,` is a valid format, the last comma is ignored and the elements of the list are `a`, `b` and `c`.

## Other options
All the parser combinators that are used are Regex based, an option of implementing that extra parsing logic would be using an implicit class, adding extra functionality to String (snippet from `AnotherParser.scala`), i.e.:

```
  implicit class Parser(str: String) {
    def parsePath(r: Regex): Try[String] = Try {
      val r(extracted) = str
      extracted
    }
  }
  
  // a path cannot contain new lines or any of the following characters: space, comma, <, >, =, ; 
  val pathRegex = "(/[^\n ,<>=;]*/?)".r
  "/var/tmp".parsePath(pathRegex)
```

This would have to be extended with logic to do two more things:
* repeat the same parser multiple times (recursion would be an option) 
* have an optional parser 

## List of improvements to be made
* Handle overflowing (upper and lower bounds) when parsing a Long value. For instance, when parsing numbers 9223372036854775808 and higher, we have to avoid a NumberFormatException.
* Handling opening and closing of files more safely - for instance, by using an Automatic Resource Management library like scala-arm.
* When opening the file, avoid .getLines - reads whole file into memory. Use streaming instead.