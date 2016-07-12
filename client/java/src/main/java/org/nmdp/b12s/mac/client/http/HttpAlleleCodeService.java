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

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.SSLContext;

import org.nmdp.b12s.mac.client.ImgtHlaRelease;
import org.nmdp.b12s.mac.client.MultipleAlleleCodeService;
import org.nmdp.b12s.mac.client.http.TextHttpClient.TextRequest;

/**
 * AlleleCodeService based on HttpClient.
 * 
 * <pre>
 * AlleleCodeService client = new HttpAlleleCodeService(alleleCodeServiceUrl);
 * </pre>
 * 
 * Also can be created using dependency injection if the Named String
 * "alleleCodeServiceUrl" is defined.
 */
public class HttpAlleleCodeService implements MultipleAlleleCodeService, Closeable {

    private TextHttpClient textHttpClient;

    @Inject
    public HttpAlleleCodeService(@Named("alleleCodeServiceUrl") String baseAddress) {
        textHttpClient = new TextHttpClient(baseAddress);
    }
    
    public HttpAlleleCodeService(@Named("alleleCodeServiceUrl") String baseAddress, SSLContext sslContext) {
        textHttpClient = new TextHttpClient(baseAddress, sslContext);
    }


    private void checkParam(String name, String value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Null " + name + " parameter");
        }
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty " + name + " parameter");
        }
    }

    @Override
    public synchronized String encode(String imgtHlaRelease, String alleleList) {
        checkParam("alleleList", alleleList);
        String response = textHttpClient.path("encode").query("imgtHlaRelease", imgtHlaRelease).post(alleleList);
        return response;
    }

    @Override
    public synchronized String decode(String alleleRepresentation) {
        return decodeInternal(null, alleleRepresentation, false);
    }

    private String decodeInternal(String imgtHlaRelease, String alleleRepresentation, boolean expand) {
        checkParam("alleleRepresentation", alleleRepresentation);
        TextRequest decodeRequest = textHttpClient.path("decode");
        decodeRequest.query("typing", alleleRepresentation);
        if (expand) {
            decodeRequest.query("expand", "true");
        }
        decodeRequest.query("imgtHlaRelease", imgtHlaRelease);
        String response = decodeRequest.get();
        return response;
    }

    @Override
    public synchronized String expand(String imgtHlaRelease, String alleleRepresentation) {
        return decodeInternal(imgtHlaRelease, alleleRepresentation, true);
    }

    @Override
    public void close() throws IOException {
        textHttpClient.close();
    }

    @Override
    public List<ImgtHlaRelease> listImgtHlaReleases() {
        TextRequest releaseClient = textHttpClient.path("imgtHlaReleases");
        String releaseLines = releaseClient.get();
        List<ImgtHlaRelease> releases = new ArrayList<>();
        for (String line : releaseLines.split("\n")) {
            releases.add(new ImgtHlaRelease(line));
        }
        return releases;
    }

    @Override
    public List<String> codeMembers(String multipleAlleleCode) {
        checkParam("multipleAlleleCode", multipleAlleleCode);
        String line = textHttpClient.path("codes/" + multipleAlleleCode).get();
        String[] parts = line.split("\t");
        if (parts.length == 3) {
            String representation = parts[2];
            return Arrays.asList(representation.split("/"));
        }
        throw new RuntimeException(
                "Invalid response for multiple allele code " + multipleAlleleCode + " response: " + line);
    }

}
