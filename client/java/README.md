# Java Client
NOTE:  Some of the sample code uses X509 client certificates to authorize the client code, but that all that is currently needed is a valid email address.

## Useful keytool commands

### Create a keypair valid for 1 year
``keytool -genkey -alias mydomain -keyalg RSA -keystore keystore.jks -validity 365 -keysize 2048``


### Export a certificate
``keytool -export -keystore keystore.jks -alias mydomain -rfc -file mydomain.cer``

###Import a certificate
``keytool -import -alias mydomain -file mydomain.cer -keystore new-keystore.jks``

