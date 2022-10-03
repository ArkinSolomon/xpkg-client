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
 * Interface which when implemented has allows an exception to point to a single line.
 *
 * @param <T> The exception class which extends {@link net.xpkgclient.exceptions.XPkgException}
 */
public interface ILineException<T extends XPkgException> {

    /**
     * Returns a new exception with the line number.
     *
     * @param line The line to set the exception to.
     * @return The new exception.
     */
    T setLine(int line);
}
