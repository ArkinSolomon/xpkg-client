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

import net.xpkgclient.versioning.VersionSelect;

import java.util.function.Supplier;

/**
 * An edge of the dependency graph. Read as: "The package {@code dependent} depends on {@code dependency} within the version {@code selection}."
 *
 * @param dependent The id of the package that the edge is from.
 * @param dependency The id of the package that the edge is to.
 * @param selection The version selection that the dependency id needs to depend on.
 */
public record DependencyEdge(PackageNode dependent, PackageNode dependency, VersionSelect selection) {
}
