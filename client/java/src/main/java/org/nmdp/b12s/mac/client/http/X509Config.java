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
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * Sample Java X.509 client based on 
 * <a href=http://hc.apache.org/httpcomponents-client-4.5.x/httpclient/examples/org/apache/http/examples/client/ClientCustomSSL.java">ClientCustomSSL.java</a>
 *
 */
public class X509Config {

    
    public static SSLContext defaultSslContext() {
        
        try {
            URL trustKeyStoreUrl = ConfigProperty.getFileAsUrl("TRUST_JKS_URL", X509Config.class, "/trusted.jks");
            char[] trustPassword = ConfigProperty.getPropertyPassword("TRUST_JKS_PWD", "changeit");
            URL clientKeyStoreUrl = ConfigProperty.getFileAsUrl("CLIENT_JKS_FILE", X509Config.class, "/test-client.jks");
            char[] clientPassword = ConfigProperty.getPropertyPassword("CLIENT_JKS_URL", "changeit");
            char[] clientKeyPassword = ConfigProperty.getPropertyPassword("CLIENT_KEY_PWD", clientPassword);
            SSLContext sslContext = SSLContexts.custom()
                    // Configure trusted certs
                    .loadTrustMaterial(trustKeyStoreUrl, trustPassword)
                    // Configure client certificate
                    .loadKeyMaterial(clientKeyStoreUrl, clientPassword, clientKeyPassword)
                    .build();
            return sslContext;
        } catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException
                | CertificateException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
        URL trustKeyStoreUrl = X509Config.class.getResource("/trusted.jks");

        URL clientKeyStoreUri = X509Config.class.getResource("/test-client.jks");
        
        SSLContext sslContext = SSLContexts.custom()
                // Configure trusted certs
                .loadTrustMaterial(trustKeyStoreUrl, "changeit".toCharArray())
                // Configure client certificate
                .loadKeyMaterial(clientKeyStoreUri, "changeit".toCharArray(), "changeit".toCharArray())
                .build();

        try (TextHttpClient httpClient = new TextHttpClient("https://macbeta.b12x.org/mac/api", sslContext)) {
            
        }
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        
        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build()){

            HttpGet httpget = new HttpGet("https://macbeta.b12x.org/mac/api/codes/AA");
            System.out.println("executing request " + httpget.getRequestLine());

            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    Charset charset = StandardCharsets.UTF_8;
                    for (Header contentType : response.getHeaders("Content-Type")) {
                        System.out.println("Content-Type: " + contentType);
                        for (String part : contentType.getValue().split(";")) {
                            if (part.startsWith("charset=")) {
                                String charsetName = part.split("=")[1];
                                charset = Charset.forName(charsetName);
                            }
                        }
                    }
                    System.out.println("Response content length: " + entity.getContentLength());
                    String content = EntityUtils.toString(entity, charset);
                    System.out.println(content);
                }
                EntityUtils.consume(entity);
            }
        }
    }

}
