/**
 * This file is part of project mac-client from the multiple-allele-code repository.
 *
 * mac-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mac-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mac-client.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.nmdp.b12s.mac.client;

/**
 * Immutable class containing information about IMGT/HLA Releases
 */
public class ImgtHlaRelease {

    private String version;
    private String yearMonth;
    
    public ImgtHlaRelease(String line) {
        String[] parts = line.trim().split(" ");
        if (parts.length == 2) {
            version = parts[0];
            yearMonth = parts[1];
        } else {
            throw new IllegalArgumentException("invalid line format(version year-month): " + line);
        }
    }

    /**
     * 
     * @return IMGT/HLA Release version
     */
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @return year-month like "2012-10"
     */
    public String getYearMonth() {
        return yearMonth;
    }
    
    public String toString() {
        return version + " " + yearMonth;
    }
}
