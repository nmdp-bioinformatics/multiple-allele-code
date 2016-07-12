#!/bin/bash -x
#
# This file is part of project mac-client-parent from the multiple-allele-code repository.
#
# mac-client-parent is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# mac-client-parent is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with mac-client-parent.  If not, see <http://www.gnu.org/licenses/>.
#

curl -E test-client.p12:changeit https://macbeta.b12x.org/mac/api/codes/AA --cacert macbeta.b12x.org.cer -v
# -E file:password   - to provide X.509 client-side certificate
# --cacert macbeta.b12x.org.cer   - server certificate since it is not currently signed by a CA
echo
