Using Windows PowerShell to invoke MAC Service
================================

## Sample MAC requests using Invoke-RestMethod
### What is ``HLA-B*40:01/HLA-B*40:07/HLA-B*40:10/HLA-B*40:22N/HLA-B*40:30/HLA-B*40:34`` encoded ?
``$glstring = "HLA-B*40:01/HLA-B*40:07/HLA-B*40:10/HLA-B*40:22N/HLA-B*40:30/HLA-B*40:34"``
``$email = "$Env:username@$Env:userdnsdomain"
``Invoke-RestMethod -Method POST -body $glstring "https://hml.nmdp.org/mac/api/encode?imgtHlaRelease=3.25.0&email=$email"  -ContentType 'text/plain'
returns
``HLA-B*40:MUD``


### What is [HLA-B*40:MUD](https://hml.nmdp.org/mac/api/decode?typing=HLA-B*40:MUD`) decoded ?
``Invoke-RestMethod "https://hml.nmdp.org/mac/api/decode?typing=HLA-B*40:MUD"``  
returns  
``HLA-B*40:01/HLA-B*40:07/HLA-B*40:10/HLA-B*40:22N/HLA-B*40:30/HLA-B*40:34``

### What MAC designation should I use ?
```
Invoke-RestMethod -Method POST "https://hml.nmdp.org/mac/api/encode?imgtHlaRelease=3.25.0" -ContentType 'text/plain' -body "A*01:01/A*01:02"
```
returns  
``A*01:AB``

### What is the MAC definition of [RAD](“https://hml.nmdp.org/mac/api/codes/RAD”) ?
``curl “https://hml.nmdp.org/mac/api/codes/RAD”``  
returns  
``  RAD	16/67``


## Resource Links
 * [curl home page](https://curl.haxx.se/)
 * http://hla.alleles.org/nomenclature/index.html - HLA nomenclature
 * https://github.com/ANHIG/IMGTHLA
