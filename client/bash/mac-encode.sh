#!/bin/bash
# encode stdin or the first argument
mac_url=${MAC_URL-http://mn4s35003:8080/mac/api}
mac_url=${MAC_URL-https://mac.b12x.org/api}

source=${1-@-}
result=$(curl -s -H "Content-Type: text/plain" -d "$source" ${mac_url}/encode)
echo $result
