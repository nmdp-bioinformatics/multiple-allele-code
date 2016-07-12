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

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigProperty {

    private static Logger logger = LoggerFactory.getLogger(ConfigProperty.class);

    public static String getProperty(String propertyName, Object defaultValue) {
        return internalGetProperty(propertyName, defaultValue, true);
    }

    private static String internalGetProperty(String propertyName, Object defaultValue, boolean logValue) {
        String source = "sys";
        String value = System.getProperty(propertyName);
        if (value == null) {
            source = "env";
            value = System.getenv(propertyName);
        }
        if (value == null) {
            source = "default";
            value = String.valueOf(defaultValue);
        }
        logger.info("{} {} {}", source, propertyName, logValue ? value : "***");
        return value;
    }

    public static URL getFileAsUrl(String propertyName, Class<?> classpathClass, Object defaultResourcePath) {
        String value = getProperty(propertyName, "classpath:" + defaultResourcePath);
        try {
            if (value.startsWith("classpath:")) {
                String resourcePath = value.substring(value.indexOf(':') + 1);
                return classpathClass.getResource(resourcePath);
            } else {
                return new URL(value);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static char[] getPropertyPassword(String propertyName, String defaultValue) {
        return internalGetProperty(propertyName, defaultValue, false).toCharArray();
    }

    public static char[] getPropertyPassword(String propertyName, char[] clientPassword) {
        String value = internalGetProperty(propertyName, "", false);
        if (value.isEmpty()) {
            return clientPassword;
        }
        return value.toCharArray();
    }

}
