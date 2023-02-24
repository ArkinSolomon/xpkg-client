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

import net.xpkgclient.versioning.Version;

import java.util.Objects;

/**
 * A single node for a package in the dependency tree.
 */
public record PackageNode(String packageId, Version version) {

    /**
     * Get the hash code for this package node. Any other node with the same package id will have the same hash code.
     *
     * @return The hash code for this package node.
     */
    @Override
    public int hashCode() {
        return packageId.hashCode();
    }
}
