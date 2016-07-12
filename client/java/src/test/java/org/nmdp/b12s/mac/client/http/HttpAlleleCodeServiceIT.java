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

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.nmdp.b12s.mac.client.MultipleAlleleCodeService;

public class HttpAlleleCodeServiceIT extends AbstractMacServiceTests {

    
    private static HttpAlleleCodeService macService;

    @BeforeClass
    public static void initClass() {
        macService =  new HttpAlleleCodeService("https://mac.b12x.org/api");
    }
    
    @AfterClass
    public static void destroyClass() {
        try {
            macService.close();
            macService = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MultipleAlleleCodeService withMac() {
        return macService;
    }
}
