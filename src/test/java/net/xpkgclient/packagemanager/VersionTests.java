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

package net.xpkgclient.packagemanager;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class tests creating versions from strings or constructors and comparing them.
 */
public final class VersionTests {

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testVersionEquality() {
        Version v = new Version(1, 2, 5);
        assertEquals(v, new Version(1, 2, 5));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testPreReleaseVersionEquality() {
        Version v = new Version(4, 5, 2, 'a', 75);
        assertEquals(v, new Version(4, 5, 2, 'a', 75));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testVersionInequality() {
        Version v = new Version(6, 3, 5);
        assertNotEquals(v, new Version(1, 1, 1));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testVersionInequalityVsNonPreRelease() {
        Version v = new Version(6, 3, 5);
        assertNotEquals(v, new Version(1, 1, 1, 'a', 9));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testVersionCreationTwoNumbers() {
        Version v = new Version(1, 2, 0);
        assertEquals(v, new Version(1, 2));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testVersionCreationOneNumber() {
        Version v = new Version(1, 0, 0);
        assertEquals(v, new Version(1));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testVersionStringCreationAllNumbers() {
        Version v = new Version(1, 2, 7);
        assertEquals(v, new Version("1.2.7"));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testVersionStringCreationTwoNumbers() {
        Version v = new Version(1, 2, 0);
        assertEquals(v, new Version("1.2"));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testVersionStringCreationOneNumber() {
        Version v = new Version(1, 0, 0);
        assertEquals(v, new Version("1"));
    }

    @Test
    void testVersionCreationZeros() {
        assertThrows(InvalidVersionException.class, () -> new Version(0, 0, 0));
    }

    @Test
    void testVersionStringCreationZeros() {
        assertThrows(InvalidVersionException.class, () -> new Version("0.0.0"));
    }

    @Test
    void testVersionCreationStringNoNumbers() {
        assertThrows(InvalidVersionException.class, () -> new Version(""));
    }

    @Test
    void testVersionCreationStringLeadingDot() {
        assertThrows(InvalidVersionException.class, () -> new Version(".1.19.2"));
    }

    @Test
    void testVersionCreationStringTrailingDot() {
        assertThrows(InvalidVersionException.class, () -> new Version("1.19.32."));
    }

    @Test
    void testVersionCreationStringLeadingAndTrailingDot() {
        assertThrows(InvalidVersionException.class, () -> new Version(".15.19.32."));
    }

    @Test
    void testVersionCreationStringDoubleDot() {
        assertThrows(InvalidVersionException.class, () -> new Version("15..."));
    }

    @Test
    void testVersionCreationStringFourParts() {
        assertThrows(InvalidVersionException.class, () -> new Version("15.51.51.35"));
    }

    @Test
    void testVersionCreationStringOnlyDot() {
        assertThrows(InvalidVersionException.class, () -> new Version("."));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testPreReleaseCreationStringAlpha() {
        Version v = new Version(5, 8, 10, 'a', 78);
        assertEquals(v, new Version("5.8.10a78"));
    }

    @Test
    @SneakyThrows(InvalidVersionException.class)
    void testPreReleaseCreationStringBeta() {
        Version v = new Version(5, 8, 10, 'b', 78);
        assertEquals(v, new Version("5.8.10b78"));
    }

    @Test
    void testPreReleaseZeroThrows() {
        assertThrows(InvalidVersionException.class, () -> new Version(4,2,3,'a',0));
    }

    @Test
    void testPreReleaseCreationStringZero() {
        assertThrows(InvalidVersionException.class, () -> new Version("1.3.45b0"));
    }

    @Test
    void testPreReleaseHasOnlyNonZeroStringCreation(){
        assertThrows(InvalidVersionException.class, () -> new Version("1.4.32b0"));
    }

    @Test
    void testPreReleaseCreationStringAllZeros(){
        assertThrows(InvalidVersionException.class, () -> new Version("0.0.02a0"));
    }
}
