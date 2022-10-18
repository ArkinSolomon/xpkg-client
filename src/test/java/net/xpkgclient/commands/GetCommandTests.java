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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(ConfigSetupExtension.class)
public class GetCommandTests {

    //We haven't really done anything yet, so it just shouldn't throw an error
    @Test
    void testNormal() throws IOException {
        ExecutionContext context = ExecutionContext.createBlankContext();
        assertDoesNotThrow(() -> GetCommand.execute(new String[]{"$var", "12345678901234567890123456789012"}, context), "GET command threw exception when it was not supposed to");
        context.close();
    }
}
