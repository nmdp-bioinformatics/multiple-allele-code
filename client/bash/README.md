Using bash to invoke MAC Service
================================

## Sample MAC requests using bash scripts
### What is ``A*01:01/A*01:02`` encoded ?
``./mac-encode.sh 'A*01:01/A*01:02'``  
returns  
``A*01:AB``


## Sample MAC requests using curl
### What is [HLA-B*40:MUD](https://hml.nmdp.org/mac/api/decode?typing=HLA-B*40:MUD`) decoded ?
``curl "https://hml.nmdp.org/mac/api/decode?typing=HLA-B*40:MUD" ; echo``  
returns  
``HLA-B*40:01/HLA-B*40:07/HLA-B*40:10/HLA-B*40:22N/HLA-B*40:30/HLA-B*40:34``

### What MAC designation should I use ?
```
curl "https://hml.nmdp.org/mac/api/encode?imgtHlaRelease=3.25.0" \
-H "content-type: text/plain" \
-d "HLA-B*40:01/HLA-B*40:07/HLA-B*40:10/HLA-B*40:22N/HLA-B*40:30/HLA-B*40:34"
```
returns  
``HLA-B*40:MUD``

### What is the MAC definition of [RAD](“https://hml.nmdp.org/mac/api/codes/RAD”) ?
``curl “https://hml.nmdp.org/mac/api/codes/RAD”``  
returns  
``  RAD	16/67``


## Resource Links
 * [curl home page](https://curl.haxx.se/)
 * http://hla.alleles.org/nomenclature/index.html - HLA nomenclature
 * https://github.com/ANHIG/IMGTHLA
