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

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.packagemanager.ExecutionContext;
import net.xpkgclient.exceptions.XPkgArgLenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ConfigSetupExtension.class)
class ContextCommandTests {

    @Test
    void testNormal() throws IOException {
        ExecutionContext context = ExecutionContext.createBlankContext();
        assertDoesNotThrow(() -> ContextCommand.execute(new String[]{}, context), "Context command threw exception that was not expected while printing information about open execution context.");
        context.close();
    }

    @Test
    void testClosedContextPrint() throws IOException {
        ExecutionContext context = ExecutionContext.createBlankContext();
        context.close();
        assertDoesNotThrow(() -> ContextCommand.execute(new String[]{}, context), "Context command threw exception that was not expected while printing information about closed execution context");
    }

    @Test
    void testArgLen() throws IOException{
        ExecutionContext context = ExecutionContext.createBlankContext();
        assertThrows(XPkgArgLenException.class, () -> ContextCommand.execute(new String[]{"an_argument"}, context));
        context.close();
    }
}
