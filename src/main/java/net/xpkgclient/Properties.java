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

package net.xpkgclient;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.IOException;

/**
 * Values of properties stored in resources/project.properties.
 */
@UtilityClass
public class Properties {

    private final java.util.Properties properties = new java.util.Properties();

    /**
     * The version of X-Pkg Client defined in POM.xml.
     *
     * @return The version of X-Pkg Client defined in POM.xml.
     */
    @Getter private String version;

    /**
     * The artifact id defined in POM.xml.
     *
     * @return The artifact id defined in POM.xml.
     */
    @Getter private String artifactId;

    /**
     * Initialize all properties.
     */
    @SneakyThrows(IOException.class)
    public void init() {
        properties.load(Properties.class.getResourceAsStream("/project.properties"));
        version = properties.getProperty("version", "UNKNOWN");
        artifactId = properties.getProperty("artifactId", "UNKNOWN");
    }
}
