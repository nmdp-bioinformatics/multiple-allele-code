#!/bin/bash
mac_url=${MAC_URL-https://mac.b12x.org/api}
for arg in $*
do
  result=$(curl -s $mac_url/decode?typing=$arg)
  echo $result
done
