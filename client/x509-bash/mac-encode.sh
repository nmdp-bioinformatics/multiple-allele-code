#!/bin/bash
# encode stdin or the first argument
mac_url=${MAC_URL-https://macbeta.b12x.org/mac/api}

source=${1-@-}
curl -E test-client.p12:changeit \
 --cacert macbeta.b12x.org.cer \
 -s -H "Content-Type: text/plain" -d "$source" ${mac_url}/encode
echo
