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

package net.xpkgclient.exceptions;

/**
 * Exception thrown when a boolean statement is invalid.
 */
public class XPkgInvalidBoolStatement extends XPkgParseException {

    /**
     * Create a new exception with a basic message when there is an invalid item in the boolean expression.
     *
     * @param item The invalid item.
     */
    public XPkgInvalidBoolStatement(String item) {
        super("Invalid item in boolean statement: '" + item + "'");
    }

    /**
     * Create a new exception saying that either multiple {@code &}'s or multiple {@code |}'s are together, with nothing in between.
     *
     * @param item The
     */
    public XPkgInvalidBoolStatement(char item) throws XPkgExecutionException {
        super("Invalid item in boolean statement: Multiple '" + item + "'s are together, with nothing in between");
        if (item != '&' && item != '|')
            throw new XPkgExecutionException("Can not call the constructor XPkgInvalidBoolStatement(char item) with a character that is not '&' or '|'", this);
    }

    /**
     * Create a new exception saying that two {@code &}'s or {@code |}'s are together (unknown which), with nothing in between.
     */
    public XPkgInvalidBoolStatement() {
        super("Invalid item in boolean statement: Multiple '&'s or '|'s are together, with nothing in between");
    }


    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgInvalidBoolStatement(int line, XPkgInvalidBoolStatement e) {
        super("Parse error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgInvalidBoolStatement setLine(int line) {
        return new XPkgInvalidBoolStatement(line, this);
    }
}
