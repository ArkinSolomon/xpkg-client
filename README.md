# X-PKG, for X-Plane
---

This project is an add-on manager for X-Plane 11 and 12, which allows developers to create add-on scripts that users can easily install, manage, and upgrade.

## Project setup

This project uses Eclipse as well as the [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/).

## Developer Documentation

### Scripting

#### Introduction

Scripting in XPKGS is fairly simple and very assembly like. The language is designed for developers to easily write
install scripts and move files around. Most developers, in fact, will not have to even write scripts, as most packages
can have installation scripts written for them through the developer portal.

Scripts are very limited in what they can do, only being allowed to access resources from repositories added by the user
as well as only being allowed to modify files within the X-Plane directory. However, scripts have enough capability to
do more than enough to modify the X-Plane directory in order to install the most complex of plugins.

All scripting files are written in .xpkgs format. using XPKGS (X-Pkg Script), a custom scripting language built
specifically for X-Pkg.

#### Terms

##### Packages vs Resources

Resources and packages are very similar. Packages are just resources that are executable, this means that they have
an `install.xpkgs` in their root.

#### Format

Scripts contain two parts, the head and the body (or the code). The head and body are separated by three hyphens on one
line (`---`). This is the only part in the file where these three symbols are to occur (including comments).

Lines starting with a `#` are treated as comments and ignored.

The script head contains metadata which can change how the script is executed. The entire head consists of line
separated keys (known as *head keys*) and values such as:

```
script_type: install
package_type: plugin
---
```

All both head keys and values **NOT** case-sensitive, although by convention all items are lowercase.

The format of the body looks very similar to assembly, the first word of a line is a command or a flow control statement. The body arguments are space separated, there can be no spaces within arguments.

Commands are **NOT** case-sensitive, though by convention they are lowercase.

#### Variables

Variables are a crucial part of any programming language and are in XPKGS as well. Variables start with dollar
signs (`$`) and have a word after them, such as `$myVar`. Variables **ARE** case-sensitive, and by convention, variables
are named with camelCase.

Variables are also loosely typed, meaning that you can change the type of a variable by changing the type of value. For
instance consider the following:

```
---
setstr $myVar Hello World!
# $myVar is now a String
get $myVar a_resource_id
# $myVar is now a Resource
```

*If you're confused about the code see the reference below*

There are three main ways to set variables. Commands can sometimes take variable names as parameters. These will be
overwritten with a value from a command. Variables can also be set using either the `SET` or `SETSTR` commands.

#### Booleans

Booleans are very simplistic in XPKGS. The primitive true/false values are simply `TRUE` or `FALSE`. These values are **
NOT** case-sensitive, though by convention they are in all caps. To invert a boolean prepend a `!` to the beginning of a
variable or primitive. To perform a logical and operation between two booleans use the `&` character. To perform a
logical or between two booleans, use the `|` character.

If statements **DO NOT** support parenthetical execution, and are instead evaluated NOT first, then AND, then OR. Adding
a parenthesis will throw an error.

#### Strings

Strings are denoted in the reference as `...STRING`. Strings are always the last argument in any command. There is no
quotes or wrappers around the string just type it out as normal (even if it has spaces).

#### *Pathlike*

Pathlike variables are just formatted strings. They always start with a slash, and can not contain characters such
as `..`, `~`, or `%`.

#### *ResourceId*

A resource id is just a string whose value is a resource id.

#### Resources

Resources are essentially paths to immutable directories. Items of resources can be accessed using pathlike strings.

#### MutableResources

A mutable resource is similar to a resource, however they can be modified.

#### Files

A file is a path to a single file, or a directory. Files usually point to an item within a resource or a mutable
resource. Unlike resources, `FILE` variables don't need to point to an existing item.

##### *Directory*

A directory is just a file where its value points to a directory.

#### Environment variables

Your script by default has variables built in for determining things such as X-Plane version, selected optional
dependencies and other prompts given to the user, as well as operating system information. All environment variables and
types can be seen in the reference. These variables can not be overwritten.

##### *BOOL* `$IS_MAC_OS`

True if the operating system is macOS.

##### *BOOL* `$IS_WINDOWS`

True if the operating system is Windows.

##### *BOOL* `$IS_LINUX`

True if the operating system is Linux.

##### *BOOL* `$IS_OTHER_OS`

True if the operating system is a different operating system.

##### *MUTABLERESOURCE* `$XP`

The currently working X-Plane directory.

##### *MUTABLERESOURCE* `$TMP`

The temporary directory for temporary file storage. This directory will be deleted on script exit.

##### *RESOURCE* `$default` <u>NOT IMPLEMENTED</u>

The directory for the resource.

##### *STRING* `$SPACE`

Just a string which contains only a single space.

### XPKGS Reference

#### Head keys

##### package_type

The type of package the script is for. The main purpose of this is for quick installation and uninstallation for simple packages using the `quick` command.

Options:
- OTHER
- SCENERY
- PLUGIN
- AIRCRAFT
- LIVERY

##### script_type

The type of script. This will also affect the `quick` command.

Options:
- OTHER
- INSTALL
- UNINSTALL
- UPGRADE

##### scenery_location

The location of scenery in the `scenery_packs.ini` file.

#### Commands

##### context<sub>&nbsp;[*Debug*]<sub>

Print the current execution context for debugging.

##### copy [$FILE] [$MUTABLERESOURCE]

If a file is provided, it will copy a file to a mutable resource, keeping the same name. If a directory is provided, it
will copy the entire directory, keeping the same name, to the destination directory.

##### exists [$BOOL]* [$FILE] <u>NOT IMPLEMENTED</u>

Check if a file or directory exists. Sets the value of the first variable to true if and only if the file or directory
does exist.

##### get [$VAR]* [RESOURCE_ID]

Get a resource and store it in a new variable.

##### if [$BOOL or BOOL] <u>NOT COMMAND; NEEDS TO BE MOVED</u>

Execute code between this if and the next branch control command if the provided variable or statement evaluates to
true.

##### isfile [$BOOL]* [$FILE] <u>NOT IMPLEMENTED</u>

Determine if a file variable points to a file. Sets the value of the first variable to true if the file exists, and it
is a file.

##### isdir [$BOOL]* [$FILE] <u>NOT IMPLEMENTED</u>

Determine if a file variable points to a directory. Sets the value of the first variable to true if the file exists, and
it is a directory.

##### ispl [$BOOL]* [$PATHLIKE or ...PATHLIKE]

Determine if a variable or path is a valid path.

##### join [$STRING]* [$STRING or ...STRING]

Join the second string onto the end of the first one (stored in the first string). If the first variable does not exist,
it will be created with an empty string as its value. Note that you can only use string variables. Although it can be
used for pathlike variables, it's not recommended, use `JOINP` or `RESOLVE` instead.

##### joinp [$PATHLIKE]* [$PATHLIKE or ...PATHLIKE]

Join and format two paths together, very similar to `JOIN`. Use `JOIN` for regular strings instead.

##### mkdir [$MUTABLERESOURCE] [$PATHLIKE or ...PATHLIKE]

Create a directory at the path within the mutable resource. If the path exists it will throw an exception, check for
existence beforehand.

##### mkdirs [$MUTABLERESOURCE] [$PATHLIKE ...PATHLIKE]

Create a directory at the path within the X-Plane directory as well as all directory in between. If the path exists it
will throw an exception, check for existence beforehand.

##### point [$RESOURCE or $MUTABLERESOURCE]* [$RESOURCE or $MUTABLERESOURCE] [$PATHLIKE or ...PATHLIKE]

Create a new resource which points to a directory within a resource.

##### print [$VAR or ...STRING]

Print a variable or a string.

##### quick [$RESOURCE or RESOURCE_ID] <u>NOT IMPLEMENTED</u>

This command is largely dependent on the metadata in the head (largely on `script_type` and `package_type`). Different
metadata will produce different results. If `OTHER` is selected for either `script_type` or `package_type`. The executor
will throw an error. See wiki for more details.

##### rename [$FILE] [$STRING or ...STRING]

Rename the file or directory that the first argument points to the value of the second argument, the only valid characters are all alphanumeric. Does not allow names with '/', '\', '%', '..', or '~' in them. It also requires that the parent of the file that is being renamed is a `MUTABLERESOURCE`.

##### resolve [$FILE]* [$RESOURCE or $MUTABLERESOURCE or $DIRECTORY] [$PATHLIKE or ...PATHLIKE]

Create a file pointer to within a resource, and store them in the first variable.

##### set [$VAR]* [$VAR or BOOL]

Set the value a variable with another variable or a boolean expression.

##### setstr [$VAR]* [...STRING]

Set the value of a variable with a string.
