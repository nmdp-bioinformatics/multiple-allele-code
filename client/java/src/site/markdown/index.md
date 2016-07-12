# security-proxy
This component is used to securely access back end services

## Configuring Tomcat to use mutual 
http://tomcat.apache.org/tomcat-8.5-doc/ssl-howto.html

> cd $TOMCAT/conf



## X509
> $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA


###
cURL with MTLS 
> curl -E test-client.p12s:changeit https://localhost:8443/proxy/api/codes/AA --cacert localhost.cer -v ; echo

## Useful keytool commands and links

Create a keypair valid for 1 year
keytool -genkey -alias mydomain -keyalg RSA -keystore keystore.jks -validity 365 -keysize 2048


Export a certificate
keytool -export -keystore keystore.jks -alias mydomain -rfc -file mydomain.cer


Import a certificate
keytool -import -alias mydomain -file mydomain.cer -keystore new-keystore.jks

Convert private key in .jks to a .p12
keytool -importkeystore -srckeystore test-client.jks -destkeystore test-client.p12 -srcstoretype JKS -deststoretype PKCS12 -srcstorepass changeit -deststorepass changeit -srcalias test-client -destalias test-client -srckeypass changeit -destkeypass changeit -noprompt


### Links
http://stackoverflow.com/questions/9901248/reading-client-certificate-in-servlet


