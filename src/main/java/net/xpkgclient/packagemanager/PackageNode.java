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
import lombok.Setter;
import lombok.SneakyThrows;
import net.xpkgclient.versioning.Version;

import java.util.Objects;

/**
 * A single node for a package in the dependency tree.
 */
public final class PackageNode implements Cloneable {

    /**
     * The id of the package node.
     *
     * @return The id of the package node.
     */
    @Getter
    private String packageId;

    /**
     * The version of the package node.
     *
     * @param version The new version of the package node.
     * @return The version of the package node.
     */
    @Getter
    @Setter
    private Version version;

    /**
     * Create a new node for a package at a specific version.
     *
     * @param packageId The id of the package node.
     * @param version   The version of the package node.
     */
    public PackageNode(String packageId, Version version) {
        this.packageId = packageId;
        this.version = version;
    }

    /**
     * Get the hash code for this package node. Any other node with the same package id will have the same hash code.
     *
     * @return The hash code for this package node.
     */
    @Override
    public int hashCode() {
        return packageId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PackageNode) obj;
        return Objects.equals(this.packageId, that.packageId) &&
                Objects.equals(this.version, that.version);
    }

    @Override
    public String toString() {
        return packageId + '@' + version;
    }

    @Override
    @SneakyThrows(CloneNotSupportedException.class)
    protected PackageNode clone() {
        PackageNode clone = (PackageNode) super.clone();
        clone.version = version.clone();
        clone.packageId = packageId;
        return clone;
    }
}
