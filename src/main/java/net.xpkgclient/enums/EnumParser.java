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

/**
 * This class contains static methods that converts strings to enums.
 */
public class EnumParser {

    /**
     * Get a {@link net.xpkgclient.enums.HeadKey} enumeration from a string.
     *
     * @param headKey The string to parse into a {@link net.xpkgclient.enums.HeadKey} enumeration.
     * @return The corresponding HeadKey as an enumeration.
     * @throws XPkgInvalidHeadKeyException Returned if the provided HeadKey does not exist.
     */
    public static HeadKey getHeadKey(String headKey) throws XPkgInvalidHeadKeyException {
        return switch (headKey.toUpperCase()) {
            case "SCRIPT_TYPE" -> HeadKey.SCRIPT_TYPE;
            case "PACKAGE_TYPE" -> HeadKey.PACKAGE_TYPE;
            default -> throw new XPkgInvalidHeadKeyException(headKey);
        };
    }

    /**
     * Get a {@link net.xpkgclient.enums.ScriptType} enumeration from a string.
     *
     * @param scriptType The string to parse into a {@link net.xpkgclient.enums.ScriptType} enumeration.
     * @return The corresponding ScriptType as an enumeration.
     * @throws XPkgInvalidHeadValException Returned if the provided ScriptType is not valid.
     */
    public static ScriptType getScriptType(String scriptType) throws XPkgInvalidHeadValException {
        return switch (scriptType.toUpperCase()) {
            case "OTHER" -> ScriptType.OTHER;
            case "INSTALL" -> ScriptType.INSTALL;
            case "UNINSTALL" -> ScriptType.UNINSTALL;
            case "UPGRADE" -> ScriptType.UPGRADE;
            default -> throw new XPkgInvalidHeadValException(HeadKey.SCRIPT_TYPE, scriptType);
        };
    }

    /**
     * Get a {@link net.xpkgclient.enums.PackageType} enumeration from a string.
     *
     * @param packageType The string to parse into a {@link net.xpkgclient.enums.PackageType} enumeration.
     * @return The corresponding PackageType as an enumeration.
     * @throws XPkgInvalidHeadValException Returned if the provided PackageType is not valid.
     */
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

    /**
     * Get a {@link net.xpkgclient.commands.CommandName} enumeration from a string.
     *
     * @param command The string to parse into a {@link net.xpkgclient.commands.CommandName} enumeration.
     * @return The corresponding command name as an enumeration.
     * @throws XPkgParseException Returned if the provided command name is not valid.
     */
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
            case "COPY" -> CommandName.COPY;
            case "RESOLVE" -> CommandName.RESOLVE;
            case "POINT" -> CommandName.POINT;
            case "RENAME" -> CommandName.RENAME;
            default -> throw new XPkgParseException("Invalid command: '" + command + "' is not a valid command");
        };
    }
}
