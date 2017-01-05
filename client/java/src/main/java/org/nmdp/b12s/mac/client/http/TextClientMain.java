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
package org.nmdp.b12s.mac.client.http
;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Date;

/**
 * Simple client that may be invoked from the command line.
 */
public class TextClientMain implements Closeable {
    
    private Operation operation;

    private String sendRequest(String typings) {
        String textResponse = "";
        try {
            textResponse = macService.expand(hlaDbVersion, typings);
        } catch (IllegalArgumentException e) {
            // Notify user to correct input values.
            e.printStackTrace();
        } catch (RuntimeException e) {
            // System or network issue.  
            e.printStackTrace();
        }
        return textResponse;
    }
    
    interface Operation {
        String perform(String inputValue);
    }
    
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: [--url=<baseurl>] [--hla=<version>] ['expand'|'encode'] typings|files...");
            System.err.println("\tversion is the IMGT/HLA Release version like 3.19.0 or 3.22.0");
            System.err.println("Sample args:\t  --url=https://hml.nmdp.org/mac/api --hla=3.22.0 expand HLA-A*01:MN");
            System.exit(1);
        }
        int pos = 0;
        try (TextClientMain client = new TextClientMain("https://hml.nmdp.org/mac/api")) {
            while (pos < args.length) {
                String arg = args[pos++];
                File f = new File(arg);
                if (arg.startsWith("--hla=")) {
                    client.setHlaDbVersion(arg.substring(arg.indexOf('=') + 1));
                } else if (arg.startsWith("--url=")) {
                    client.setBaseUrl(arg);
                } else if (arg.equals("expand")) {
                    client.setOperationToExpand();
                } else if (arg.equals("encode")) {
                    client.setOperationToEncode();
                } else if (f.exists()) {
                    processFile(f, client.operation);
                } else {
                    String expanded = client.sendRequest(arg);
                    System.out.println(expanded);
                }
            }
        } finally {
            
        }
    }


    private void setOperationToExpand() {
        this.operation = new Operation(){
            public String perform(String inputValue) {
                return macService.expand(hlaDbVersion, inputValue);
            }};
    }
    
    private void setOperationToEncode() {
        this.operation = new Operation(){
            public String perform(String inputValue) {
                return macService.encode(hlaDbVersion, inputValue);
            }};
    }


    private static void processFile(File file, Operation operation) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
        LineNumberReader lnr = new LineNumberReader(reader);
        String typing;
        int invalidAlleleCount = 0;
        int lineNumber = 0;
        while (null != (typing = lnr.readLine())) {
            lineNumber = lnr.getLineNumber();
            if (typing.length() < 6 || typing.indexOf('/') > 0) {
                continue;// skip allele lists
            }
            try {
                String value = operation.perform(typing);
                if (value == null) {
                    System.out.println("NULL for : " + typing);
                }
            } catch (Exception re) {
                String message = re.toString();
                if (message.contains("Invalid allele")) {
                    invalidAlleleCount++;
                } else {
                    System.out.println("ERR :" + message
                            + "\n\t for typing: " + typing);
                }
            } finally {
                if (lineNumber % 200 == 0) {
                    System.out.println(lineNumber + "\t " + new Date());
                }
            }
        }
        System.out.println("lines= " + lineNumber);
        System.out.println("invalid alleles= " + invalidAlleleCount);
        lnr.close();
    }

    private String hlaDbVersion = null;

    private HttpAlleleCodeService macService;

    public TextClientMain(String baseurl) {
        macService = new HttpAlleleCodeService(baseurl);
        setOperationToExpand();
    }

    @Override
    public void close() throws IOException {
        macService.close();
    }

    public String getHlaDbVersion() {
        return hlaDbVersion;
    }

    private void setBaseUrl(String baseurl) {
        try {
            macService.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        macService = new HttpAlleleCodeService(baseurl);
    }


    public void setHlaDbVersion(String hlaDbVersion) {
        this.hlaDbVersion = hlaDbVersion;
    }


}
