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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class can parse a version select string and determine if a version is within its selection.
 */
public class VersionSelect {

    /**
     * Check if the provided version selection string was valid.
     *
     * @return True if the provided version selection string is valid.
     */
    @Getter
    private boolean isValid = true;
    private List<RangeSet> ranges = new ArrayList<>();

    /**
     * Create a new selection checker from a string.
     *
     * @param selectionStr The selection string, comma separated.
     */
    public VersionSelect(String selectionStr) {
        String[] selectionSections = selectionStr.split(",");

        try {
            for (String selection : selectionSections) {
                RangeSet range = new RangeSet(
                        new Version(0, 0, 1, 'a', 1),
                        new Version(999, 999, 999)
                );

                selection = selection.trim();

                String[] versionParts = selection.split("-");

                if (versionParts.length == 1) {
                    if (selection.equals("*")) {
                        ranges = Collections.singletonList(range);
                        break;
                    }

                    String version = versionParts[0].trim();

                    Version minVersion = new Version(version);
                    Version maxVersion = minVersion.copy();

                    if (!minVersion.isPreRelease()) {
                        String[] singleVersionParts = version.split("\\.");

                        if (singleVersionParts.length == 1)
                            maxVersion.setMinor(999);

                        if (singleVersionParts.length <= 2)
                            maxVersion.setPatch(999);

                        if (singleVersionParts.length <= 3)
                            minVersion.setAlphaOrBeta('a');
                    }

                    ranges.add(new RangeSet(minVersion, maxVersion));
                    continue;
                } else if (versionParts.length != 2) {
                    isValid = false;
                    break;
                }

                String lowerVersionStr = versionParts[0].trim();
                String upperVersionStr = versionParts[1].trim();

                Version lowerVersion = Version.fromString(lowerVersionStr);
                Version upperVersion = Version.fromString(upperVersionStr);
                boolean hasLower = !lowerVersionStr.isEmpty();
                boolean hasUpper = !upperVersionStr.isEmpty();

                if ((lowerVersion == null && hasLower) || (upperVersion == null && hasUpper) || (!hasLower && !hasUpper)) {
                    isValid = false;
                    break;
                }

                if (hasLower) {

                    // Since (for instance) 1 really means everything from 1.0.0a1 and up, we can use this hack
                    if (!lowerVersion.isPreRelease())
                        lowerVersion = new Version(lowerVersionStr + "a1");

                    range.setMinVersion(lowerVersion);
                    range.setMinVersionNum(lowerVersion.getVersionNum());
                }

                if (hasUpper) {

                    // Similarly, since (for instance) 2 really means everything up to 2.999.999, we can use this hack
                    int partLen = upperVersionStr.split("\\.").length;

                    boolean hasPre = upperVersionStr.contains("a") || upperVersionStr.contains("b");

                    if (!hasPre) {
                        if (partLen < 2)
                            upperVersion.setMinor(999);
                        if (partLen < 3)
                            upperVersion.setPatch(999);
                    }

                    range.setMaxVersion(upperVersion);
                    range.setMaxVersionNum(upperVersion.getVersionNum());
                }

                if (range.getMinVersionNum() > range.getMaxVersionNum()) {
                    isValid = false;
                    break;
                }

                ranges.add(range);
            }
        } catch (InvalidVersionException e) {
            isValid = false;
            throw new RuntimeException(e);
        }
    }

    /**
     * Check to see whether a version falls within this selection.
     *
     * @param version Check if a version falls within a version selection.
     * @return True if the number is within the selection.
     */
    boolean containsVersion(Version version) {
        for (RangeSet range : ranges) {
            long versionNum = version.getVersionNum();

            if (versionNum >= range.getMinVersionNum() && versionNum <= range.getMaxVersionNum())
                return true;
        }
        return false;
    }

    /**
     * Get a copy of the ranges (simplified).
     *
     * @return A copy of the simplified ranges.
     */
    public RangeSet[] getRanges() {
        return ranges.toArray(RangeSet[]::new);
    }

    /**
     * This class is used to store sets of version selection.
     */
    private static class RangeSet {

        /**
         * The maximum version number.
         *
         * @param maxVersionNum The new maximum version number.
         * @return The maximum version number.
         */
        @Getter
        @Setter
        private long maxVersionNum;

        /**
         * The minimum version number.
         *
         * @param minVersionNum The new maximum version number.
         * @return The maximum version number.
         */
        @Getter
        @Setter
        private long minVersionNum;

        /**
         * The maximum version object.
         *
         * @param maxVersion The new maximum version object.
         * @return The maximum version object.
         */
        @Getter
        @Setter
        private Version maxVersion;

        /**
         * The minimum version object.
         *
         * @param minVersion The new maximum version object.
         * @return The maximum version object.
         */
        @Getter
        @Setter
        private Version minVersion;

        /**
         * Create a new range set from two numbers.
         *
         * @param minVersion The minimum version of the range.
         * @param maxVersion The maximum version of the range.
         */
        public RangeSet(Version minVersion, Version maxVersion) {
            this.minVersion = minVersion;
            this.maxVersion = maxVersion;
            this.minVersionNum = minVersion.getVersionNum();
            this.maxVersionNum = maxVersion.getVersionNum();
        }
    }
}