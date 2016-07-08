#!/bin/bash -x
curl -E test-client.p12:changeit https://macbeta.b12x.org/mac/api/codes/AA --cacert macbeta.b12x.org.cer -v
# -E file:password   - to provide X.509 client-side certificate
# --cacert macbeta.b12x.org.cer   - server certificate since it is not currently signed by a CA
echo
