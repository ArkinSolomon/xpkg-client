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
 * This exception is called when a method is called when it shouldn't be.
 */
public class XPkgInvalidCallException extends XPkgExecutionException {

    /**
     * Create a new exception and show the stack trace.
     *
     * @param message The message for the exception.
     * @param index The index in the callstack
     */
    public XPkgInvalidCallException(String message, int index) {
        super("Invalid call to method '" + getCallerMethodName(index) + "': " + message);
    }

    /**
     * Get the name of a method in the current call stack
     *
     * @param i The index of the method to get in the call stack.
     * @return The name of the method in the call stack.
     */
    private static String getCallerMethodName(int i) {
        //noinspection OptionalGetWithoutIsPresent
        return StackWalker.
                getInstance().
                walk(stream -> stream.skip(i).findFirst().get()).
                getMethodName();
    }
}
