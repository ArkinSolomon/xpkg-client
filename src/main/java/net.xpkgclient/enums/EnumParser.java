/*
 * Copyright (c) 2022. XPkg-Client Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied limitations under the License.
 */

package net.xpkgclient.enums;

import net.xpkgclient.commands.CommandName;
import net.xpkgclient.exceptions.XPkgInvalidHeadKeyException;
import net.xpkgclient.exceptions.XPkgInvalidHeadValException;
import net.xpkgclient.exceptions.XPkgParseException;

//This class contains static methods for parsing different enumerations
public class EnumParser {

    // Get a HeadKey enumeration from a string
    public static HeadKey getHeadKey(String headKey) throws XPkgInvalidHeadKeyException {
        return switch (headKey.toUpperCase()) {
            case "SCRIPT_TYPE" -> HeadKey.SCRIPT_TYPE;
            case "PACKAGE_TYPE" -> HeadKey.PACKAGE_TYPE;
            default -> throw new XPkgInvalidHeadKeyException(headKey);
        };
    }

    // Get a ScriptType enumeration from a string
    public static ScriptType getScriptType(String scriptType) throws XPkgInvalidHeadValException {
        return switch (scriptType.toUpperCase()) {
            case "OTHER" -> ScriptType.OTHER;
            case "INSTALL" -> ScriptType.INSTALL;
            case "UNINSTALL" -> ScriptType.UNINSTALL;
            case "UPGRADE" -> ScriptType.UPGRADE;
            default -> throw new XPkgInvalidHeadValException(HeadKey.SCRIPT_TYPE, scriptType);
        };
    }

    // Get a PackageType enumeration from a string
    public static PackageType getPackageType(String packageType) throws XPkgInvalidHeadValException {
        return switch (packageType.toUpperCase()) {
            case "OTHER" -> PackageType.OTHER;
            case "SCENERY" -> PackageType.SCENERY;
            case "AIRCRAFT" -> PackageType.AIRCRAFT;
            case "PLUGIN" -> PackageType.PLUGIN;
            case "LIVERY" -> PackageType.LIVERY;
            default -> throw new XPkgInvalidHeadValException(HeadKey.PACKAGE_TYPE, packageType);
        };
    }

    // Get a Command enumeration from a string
    public static CommandName getCommand(String command) throws XPkgParseException {
        return switch (command.toUpperCase()) {
            case "QUICK" -> CommandName.QUICK;
            case "GET" -> CommandName.GET;
            case "PRINT" -> CommandName.PRINT;
            case "IF" -> CommandName.IF;
            case "ELIF" -> CommandName.ELIF;
            case "ELSE" -> CommandName.ELSE;
            case "ENDIF" -> CommandName.ENDIF;
            case "SET" -> CommandName.SET;
            case "SETSTR" -> CommandName.SETSTR;
            case "MKDIR" -> CommandName.MKDIR;
            case "MKDIRS" -> CommandName.MKDIRS;
            case "ISPL" -> CommandName.ISPL;
            case "JOIN" -> CommandName.JOIN;
            case "JOINP" -> CommandName.JOINP;
            case "CONTEXT" -> CommandName.CONTEXT;
            default -> throw new XPkgParseException("Invalid command: '" + command + "' is not a valid command");
        };
    }
}
