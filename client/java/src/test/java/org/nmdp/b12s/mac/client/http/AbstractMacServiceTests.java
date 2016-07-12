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
package org.nmdp.b12s.mac.client.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;
import org.nmdp.b12s.mac.client.ImgtHlaRelease;
import org.nmdp.b12s.mac.client.MultipleAlleleCodeService;

public abstract class AbstractMacServiceTests {

    
    @Test
    public void testListReleases() {
        List<ImgtHlaRelease> imgtHlaReleases = withMac().listImgtHlaReleases();
        assertFalse("empty releases", imgtHlaReleases.isEmpty());
        for (ImgtHlaRelease release : imgtHlaReleases) {
            System.out.println(release);
        }
    }

    @Test
    public void testDecode() {
        String decoded = withMac().decode("A*01:RAD");
        System.out.println(decoded);
        assertEquals("A*01:RAD", "A*01:16/A*01:67", decoded);
    }

    @Test
    public void testExpand() {
        String expansion = withMac().expand("3.24.0", "A*01:RAD");
        System.out.println(expansion);
    }

    @Test
    public void testEncode() {
        String encoded = withMac().encode("3.24.0", "A*01:01/A*01:02");
        System.out.println(encoded);
        assertEquals("A*01:01/A*01:02", "A*01:AB", encoded);
    }

    @Test
    public void testMembers() {
        List<String> members = withMac().codeMembers("AB");
        System.out.println(members);
        assertEquals("AB", "[01, 02]", members.toString());
    }

    public abstract MultipleAlleleCodeService withMac();
}
