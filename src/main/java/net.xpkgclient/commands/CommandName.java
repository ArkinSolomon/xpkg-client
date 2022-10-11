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

package net.xpkgclient.commands;

/**
 * The name of all the commands. Also includes a {@code __INTERNAL_TEST_COMMAND} command name, which should be used for testing only, and should not be implemented within the script parsers.
 */
public enum CommandName {
    __INTERNAL_TEST_COMMAND, QUICK, GET, PRINT, IF, ELIF, ELSE, ENDIF, SET, SETSTR, MKDIR, MKDIRS, ISPL, JOIN, JOINP, CONTEXT, COPY, RESOLVE, POINT, RENAME, EXISTS, ISFILE, ISDIR, GETSTR
}
