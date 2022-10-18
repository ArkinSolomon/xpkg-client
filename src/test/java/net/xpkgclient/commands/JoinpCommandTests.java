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
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.vars.XPkgString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ConfigSetupExtension.class)
public class JoinpCommandTests {

    /**
     * Quickly create an execution context with default paths.
     *
     * @return A new execution context with three pathlike string variables ($path1 to $path3).
     * @throws IOException Thrown if there was an issue creating the temporary files while initializing the execution context.
     */
    private static @NotNull ExecutionContext newCtx() throws IOException {
        try {
            ExecutionContext context = ExecutionContext.createBlankContext();
            context.setVar("$path1", new XPkgString("/things/go/here"));
            context.setVar("$path2", new XPkgString("/oh/look bananas/"));
            context.setVar("$path3", new XPkgString("/file.txt"));
            return context;
        } catch (XPkgException e) {
            throw new IllegalStateException("Exception while setting up a test execution context", e);
        }
    }

    /**
     * Run code within a certain execution context, which is after {@code runner} runs.
     *
     * @param runner A lambda with an {@link ExecutionContext} as it's only parameter.
     * @throws IOException   Thrown when there is an issue creating temporary files in the execution context.
     * @throws XPkgException Thrown when there's an issue with the tested code.
     */
    private static void runInDefaultContext(@NotNull ContextRunner runner) throws XPkgException, IOException {
        ExecutionContext context = newCtx();
        runner.run(context);
        context.close();
    }

    @Test
    void testJoinPathNoTrailingSlashes() throws XPkgException, IOException {
        runInDefaultContext(context -> {
            JoinpCommand.execute(new String[]{"$path1", "$path3"}, context);
            assertEquals("/things/go/here/file.txt", context.getVar("$path1").toString());
        });
    }

    @Test
    void testJoinPathTrailingSlashes() throws XPkgException, IOException {
        runInDefaultContext(context -> {
            JoinpCommand.execute(new String[]{"$path2", "$path3"}, context);
            assertEquals("/oh/look bananas/file.txt", context.getVar("$path2").toString());
        });
    }

    @Test
    void testJoinPathOnlyTrailingSlashes() throws XPkgException, IOException {
        runInDefaultContext(context -> {
            JoinpCommand.execute(new String[]{"$path2", "$path2"}, context);
            assertEquals("/oh/look bananas/oh/look bananas", context.getVar("$path2").toString());
        });
    }

    @Test
    void testJoinPathWithMultiSlash() throws XPkgException, IOException {
        runInDefaultContext(context -> {
            JoinpCommand.execute(new String[]{"$path2", "/a", "path"}, context);
            assertEquals("/oh/look bananas/a path", context.getVar("$path2").toString());
        });
    }

    @Test
    void testJoinPathWithThreeVariables() throws XPkgException, IOException {
        runInDefaultContext(context -> assertThrows(XPkgArgLenException.class, () -> JoinpCommand.execute(new String[]{"$path1", "$path2", "$path3"}, context)));
    }

    @Test
    void testJoinPathWithNoArgs() throws XPkgException, IOException {
        runInDefaultContext(context -> assertThrows(XPkgArgLenException.class, () -> JoinpCommand.execute(new String[]{}, context)));
    }

    @Test
    void testJoinPathFirstArgNotVariable() throws XPkgException, IOException {
        runInDefaultContext(context -> assertThrows(XPkgInvalidVarNameException.class, () -> JoinpCommand.execute(new String[]{"not_a_var", "/a", "path", "to/some", "file.txt"}, context)));
    }

    @Test
    void testJoinPathFirstArgUndefined() throws XPkgException, IOException {
        runInDefaultContext(context -> {
            JoinpCommand.execute(new String[]{"$undef_var", "/a", "path", "to/some", "file.txt"}, context);
            assertEquals("/a path to/some file.txt", context.getVar("$undef_var").toString());
        });
    }
}
