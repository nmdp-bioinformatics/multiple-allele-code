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

import java.util.List;

/**
 * Responsible for interacting with Multiple Allele Codes(MAC).
 */
public interface MultipleAlleleCodeService {
    
    /**
     * Encode a list of alleles into a single allele code designation.
     * @param imgtHlaRelease  IMGT/HLA Release version like "3.16.0" used to validate the alleleList
     * @param alleleList  slash separated list of alleles which is GL String compliant
     * @return the allele code designation
     */
    String encode(String imgtHlaRelease, String alleleList);
    
    /**
     * Decodes the alleleRepresentation into a list of two-field alleles.
     * @param alleleRepresentation full allele string like "A*01:AB"
     * @return allele list with each allele having exactly two fields like "A*01:01/A*01:02"
     */
    String decode(String alleleRepresentation);

    /**
     * Expand the alleleRepresentation into an allele list using full IMGT/HLA names.
     * @param imgtHlaRelease  IMGT/HLA Release version like "3.16.0" used to validate the alleleList
     * @param alleleRepresentation full allele string like "HLA-B*01:AB"
     * @return allele list with each allele having exactly two fields like "A*01:01/A*01:02"
     */
    String expand(String imgtHlaRelease, String alleleRepresentation);
    
    /**
     * List the supported IMGT/HLA Releases from newest to oldest.
     * @return list of ImgtHlaRelease instances
     */
    List<ImgtHlaRelease> listImgtHlaReleases();

    /**
     * Return the defined members of a MAC.
     * It will either be an allele specific list with the first and second fields of allele names
     * or it will be generic and only have the second fields.
     */
    List<String> codeMembers(String multipleAlleleCode);
}
