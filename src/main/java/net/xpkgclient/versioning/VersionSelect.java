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

package net.xpkgclient.versioning;

import lombok.Getter;

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
    private List<VersionRange> ranges = new ArrayList<>();

    /**
     * Create a new selection checker from a string.
     *
     * @param selectionStr The selection string, comma separated.
     */
    public VersionSelect(String selectionStr) {
        String[] selectionSections = selectionStr.split(",");

        try {
            for (String selection : selectionSections) {
                VersionRange range = new VersionRange(
                        Version.MIN_VERSION,
                        Version.MAX_VERSION
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

                    ranges.add(new VersionRange(minVersion, maxVersion));
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

                    range.setMin(lowerVersion);
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
                    range.setMax(upperVersion);
                }

                if (range.getMinVersionNum() > range.getMaxVersionNum()) {
                    isValid = false;
                    break;
                }
                ranges.add(range);
            }

            if (!isValid)
                return;
            ranges = simplify(ranges);
        } catch (InvalidVersionException e) {
            isValid = false;
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new version selection from a version ranges. Does not copy {@code ranges}.
     *
     * @param ranges The ranges for this version selection.
     */
    public VersionSelect(List<VersionRange> ranges){
        this.ranges = ranges;
    }

    /**
     * Simplify the list of ranges.
     *
     * @param ranges The ranges to simplify.
     * @return A new deep copy of the ranges simplified.
     */
    public static List<VersionRange> simplify(List<VersionRange> ranges) {

        // Create a deep copy of the list
        ranges = ranges.stream().map(r -> new VersionRange(r.getMinVersion().copy(), r.getMaxVersion().copy())).sorted().toList();

        int curr = 0;
        while (ranges.size() > 1 && curr < ranges.size() - 1) {
            VersionRange r = VersionRange.tryMerge(ranges.get(curr), ranges.get(curr + 1));
            if (r == null)
                ++curr;
            else {
                ranges.set(curr, r);
                ranges.remove(curr + 1);
            }
        }

        return ranges;
    }

    /**
     * Check to see whether a version falls within this selection.
     *
     * @param version Check if a version falls within a version selection.
     * @return True if the number is within the selection.
     */
    public boolean containsVersion(Version version) {
        for (VersionRange range : ranges) {
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
    public VersionRange[] getRanges() {
        return ranges.toArray(VersionRange[]::new);
    }

    /**
     * Get the string that can be used to create this version selection.
     *
     * @return The string representation of this selection.
     */
    @Override
    public String toString() {
        ArrayList<String> rangeStrings = new ArrayList<>(ranges.size());

        for (VersionRange range : ranges) {
            if (range.getMinVersionNum() == range.getMaxVersionNum())
                rangeStrings.add(range.getMinVersion().toString());
            else if (range.getMinVersion().equals(Version.MIN_VERSION) && range.getMaxVersion().equals(Version.MAX_VERSION))
                return "*";
            else if (range.getMinVersion().equals(Version.MIN_VERSION))
                return "-%s".formatted(range.getMaxVersion());
            else if (range.getMaxVersion().equals(Version.MAX_VERSION))
                return "%s-".formatted(range.getMinVersion());
            else
                rangeStrings.add("%s-%s".formatted(range.getMinVersion(), range.getMaxVersion()));
        }

        return String.join(",", rangeStrings);
    }
}