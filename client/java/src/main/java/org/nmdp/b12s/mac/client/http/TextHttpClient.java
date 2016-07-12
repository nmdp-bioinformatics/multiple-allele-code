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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient that uses text/plain for requests and responses.
 */
public class TextHttpClient implements Closeable {

    public class TextRequest {

        private URIBuilder uri;
        public TextRequest(URI uri) {
            this.uri = new URIBuilder(uri);
        }

        private URI buildUrl() {
            try {
                return uri.build();
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Invalid uri: " + uri, e);
            }
        }

        private String executeTextRequest(HttpRequestBase httpRequest) {
            httpRequest.addHeader("Content-Type", "text/plain");
            httpRequest.addHeader("Accepts", "text/plain");
            long startTime = System.currentTimeMillis();
            URI url = httpRequest.getURI();
            String logMessage = url.toString();
            try (CloseableHttpResponse response = httpclient.execute(httpRequest)) {
                return toTextResponse(response, url);
            } catch (RuntimeException e) {
                logMessage = url + " " + e;
                throw e;
            } catch (IOException e) {
                logMessage = url + " " + e;
                throw new RuntimeException(e);
            } finally {
                long durationMs = System.currentTimeMillis() - startTime;
                logger.debug("{} time: {} ms", logMessage, durationMs);
            }
        }

        public String get() {
            URI url = buildUrl();
            HttpGet httpGet = new HttpGet(url);
            return executeTextRequest(httpGet);
        }

        public String post(String bodyContent) {
            URI url = buildUrl();
            HttpPost httpPost = new HttpPost(url);
            if (bodyContent != null) {
                httpPost.setEntity(new StringEntity(bodyContent, StandardCharsets.UTF_8));
            }
            return executeTextRequest(httpPost);
        }

        public TextRequest query(String paramName, String paramValue) {
            if (paramValue != null) {
                uri.addParameter(paramName, paramValue);
            }
            return this;
        }

        private String toTextResponse(HttpResponse response, URI url) {
            int status = response.getStatusLine().getStatusCode();
            String content = null;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                Charset charset = StandardCharsets.UTF_8;
                for (Header contentType : response.getHeaders("Content-Type")) {
                    for (String part : contentType.getValue().split(";")) {
                        if (part.startsWith("charset=")) {
                            String charsetName = part.split("=")[1];
                            charset = Charset.forName(charsetName);
                        }
                    }
                }
                try {
                    content = EntityUtils.toString(entity, charset);
                } catch (ParseException | IOException e) {
                    throw new RuntimeException("Unable to parse content", e);
                }
            }
            if (status == HttpStatus.SC_OK) {
                return content;
            } else if (status == HttpStatus.SC_NOT_FOUND ) {
                throw new IllegalArgumentException("not found: " + url);
            } else if (status == HttpStatus.SC_BAD_REQUEST ) { 
                logger.warn("bad request {} {}", url.getQuery(), content);
                throw new IllegalArgumentException(content);
            }
            logger.warn("err status={} {} \tquery:{}", status, content, url.getQuery());
            throw new RuntimeException(content);
        }

    }


    private final URI baseUrl;
    private CloseableHttpClient httpclient;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public TextHttpClient(@Named("alleleCodeServiceUrl") String baseAddress) {
        this(baseAddress, HttpClients.custom().build());
    }
    
    public TextHttpClient(String baseAddress, SSLContext sslcontext) {
        this(baseAddress, buildTlsClient(sslcontext));
    }
    
    private static CloseableHttpClient buildTlsClient(SSLContext sslcontext) {
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        
        CloseableHttpClient tlsClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        return tlsClient;
    }
    
    private TextHttpClient(String baseAddress, CloseableHttpClient httpClient) {
        httpclient = httpClient;
        try {
            this.baseUrl = new URI(baseAddress + "/");
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("baseAddress is invalid: " + baseAddress, e);
        }

    }

    @Override
    public void close() throws IOException {
        if (httpclient != null) {
            httpclient.close();
            httpclient = null;
        }
    }

    public TextRequest path(String path) {
        return new TextRequest(baseUrl.resolve(path));
    }

}
