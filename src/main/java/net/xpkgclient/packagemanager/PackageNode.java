/*
 * Copyright (c) 2023. Arkin Solomon.
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

package net.xpkgclient.packagemanager;

import lombok.Getter;

/**
 * A single node for a package in the dependency tree.
 */
public class PackageNode {

    /**
     * The id of the package.
     */
    @Getter
    private String packageId;

    /**
     * The installed version of the package.
     */
    @Getter
    private Version installedVersion;

    /**
     * The selections that the package version needs to satisfy.
     */
    private VersionSelect[] ranges;
}
