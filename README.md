# X-PKG, for X-Plane

This project is an addon manager for X-Plane 11 and 12, which allows developers to create addon scripts that users can easily install.

This project was largely inspired by CKAN for Kerbal Space Program.

## Developer Documentation

All scripting files are written in .xpkg format.

### Scripting

Scripts contain two parts, the head and the body (or the code). Scripts are very limited in what they can do, only being allowed to access repositories added by the user as well as only being allowed to modify files within the X-Plane directory.

Lines starting with a `#` are treated as comments and ignored.

The head and body are separated by three hyphens on one line (`---`). This is the only part in the file where these three symbols are to occur (including comments).

`/` refers to the root directory of X-Plane. I.E. `/resources/plugins` refers to `X-Plane 12/resources/plugins`.

#### Head

The script head contains metadata which can change how the script is executed. The entire head consists of line seperated keys and values such as:

```
script_type: install
package_type: plugin
---
```

All items in the head are **NOT** case-sensitive.

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

#### Body

The body consists of commands and arguments. The first word of a line in the body is always a command. The body arguments are space separated, there can be no spaces within arguments.

Commands are **NOT** case-sensitive.

##### get [$FILE] [FILE_ID]

Get a file and store it in a variable with type `File`.

##### print [$VAR or ...STRING]

Print a variable or a string.

##### quick [$FILE or FILE_ID]

This command is largely dependent on the metadata in the head (largely on `script_type` and `package_type`). Different metadata will produce different results. If `OTHER` is selected for either `script_type` or `package_type`. The executor will throw an error.

Possibilities:
- INSTALL + SCENERY: TODO
- UNINSTALL + SCENERY: TODO
- UPGRADE + SCENERY: TODO
- gotta do everything else here too lol
