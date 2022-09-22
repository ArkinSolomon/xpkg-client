# X-PKG, for X-Plane
---

This project is an add-on manager for X-Plane 11 and 12, which allows developers to create add-on scripts that users can easily install, manage, and upgrade.

## Project setup

This project uses Eclipse as well as the [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/).

## Developer Documentation

### Scripting

#### Introduction

Scripting in XPKGS is fairly simple and very assembly like. The language is designed for developers to easily write install scripts and move files around. Most developers, in fact, will not have to even write scripts, as most packages can have install scripts written for them through the developer portal.

Scripts are very limited in what they can do, only being allowed to access resources from repositories added by the user as well as only being allowed to modify files within the X-Plane directory. However scripts have enough capability to do more than enough to modify the X-Plane directory in order to install the most complex of plugins.

All scripting files are written in .xpkgs format. using XPKGS (X-Pkg Script), a custom scripting language built specifically for X-Pkg.

#### Format

Scripts contain two parts, the head and the body (or the code). The head and body are separated by three hyphens on one line (`---`). This is the only part in the file where these three symbols are to occur (including comments).

Lines starting with a `#` are treated as comments and ignored.

The script head contains metadata which can change how the script is executed. The entire head consists of line separated keys (known as *head keys*) and values such as:

```
script_type: install
package_type: plugin
---
```

All both head keys and values **NOT** case-sensitive, although by convention all items are lowercase.

The format of the body looks very similar to assembly, the first word of a line is a command or a flow control statement. The body arguments are space separated, there can be no spaces within arguments.

Commands are **NOT** case-sensitive, though by convention they are lowercase.

#### Variables

Variables are a crucial part of any programming language and are in XPKGS as well. Variables start with dollar signs (`$`) and have a word after them, such as `$myVar`. Variables **ARE** case sensitive, and by convention, variables are named with camelCase.

Variables are also loosely typed, meaning that you can change the type of a variable by changing the type of value. For instance consider the following:

```
---
setstr $myVar Hello World!
# $myVar is now a String
get $myVar a_resource_id
# $myVar is now a Resource
```
*If you're confused about the code see the reference below*

There are three main ways to set variables. Commands can sometimes take variable names as parameters. These will be over written with a value from a command. Variables can also be set using either the `SET` or `SETSTR` commands.

#### Booleans

Booleans are very simplistic in XPKGS. The primitive true/false values are simply `TRUE` or `FALSE`. These values are **NOT** case-sensitive, though by convention they are in all caps. To invert a boolean prepend a `!` to the beginning of a variable or primitive. To perform a logical and operation between two booleans use the `&` character. To perform a logical or between two booleans, use the `|` character.

If statements **DO NOT** support parenthetical execution, and are instead evaluated NOT first, then AND, then OR. Adding a parenthesis will throw an error.

#### Strings

Strings are denoted in the reference as `...STRING`. Strings are always the last argument in any command. There is no quotes or wrappers around the string just type it out as normal (even if it has spaces).

#### Pathlike

Pathlike variables are just formatted strings. They always start with a slash, and can not contain characters such as `..`, `~`, or `%`.

#### Resources

Resources are essentially paths to immutable folders. Items of resources can be accessed using pathlike strings.

#### MutableResources

A mutable resource is similar to a resource, however they are able to be modified.

#### Environment variables

Your script by default has variables built in for determining things such as X-Plane version, selected optional dependencies and other prompts given to the user, as well as operating system information. All environment variables and types can be seen in the reference. These variables can not be overwritten.

##### *BOOL* `$IS_MAC_OS`

True if the operating system is MacOS.

##### *BOOL* `$IS_WINDOWS`

True if the operating system is Windows.

##### *BOOL* `$IS_LINUX`

True if the operating system is Linux.

##### *BOOL* `$IS_OTHER_OS`

True if the operating system is a different operating system.

##### *STRING* `$XP_DIR` **NEEDS TO BE MUTABLERESOURCE**

The path to the currently working X-Plane directory.

##### *STRING* `$TMP` **NEEDS TO BE MUTABLERESOURCE**

The path to the temporary directory.

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

##### get [$VAR] [RESOURCE_ID]

Get a resource and store it in a new variable.

##### if [$BOOL or BOOL]

Execute code between this if and the next branch control command if the provided variable or statement evaluates to true.

##### ispl [$BOOL] [$PATHLIKE or ...PATHLIKE]

Determine if a variable or path is a valid path.

##### join [$STRING] [$STRING or ...STRING]

Join the second string onto the end of the first one (stored in the first string). Note that you can only use variables, although it can be used for paths, it's not recommended, use `joinp` instead.

##### joinp [$PATHLIKE] [$PATHLIKE or ...PATHLIKE]

Join and format the two paths together. Don't use for strings, use `join` for strings instead.

##### mkdir [$PATHLIKE or ...PATHLIKE] **NEED TO MKDIR IN MUTABLERESOURCE**

Create a directory at the path within the X-Plane directory.

##### mkdirs [$PATHLIKE ...PATHLIKE] **NEED TO MKDIR IN MUTABLERESOURCE**

Create a directory at the path within the X-Plane directory as well as all directory in between.

##### print [$VAR or ...STRING]

Print a variable or a string.

##### quick [$RESOURCE or RESOURCE_ID]

**TODO**

This command is largely dependent on the metadata in the head (largely on `script_type` and `package_type`). Different metadata will produce different results. If `OTHER` is selected for either `script_type` or `package_type`. The executor will throw an error. See wiki for more details.

##### set [$VAR] [$VAR or BOOL]

Set the value a variable with another variable or a boolean expression.

##### setstr [$VAR] [...STRING]

Set the value of a variable with a string.
