Using bash to invoke MAC Service
================================

## Sample MAC requests using bash scripts
### What is [HLA-B*40:MUD](://mac.b12x.org/api/decode?typing=HLA-B*40:MUD`) decoded ?
``./mac-encode.sh 'A*01:01/A*01:02'``  
returns  
``A*01:AB``


## Sample MAC requests using curl
### What is [HLA-B*40:MUD](://mac.b12x.org/api/decode?typing=HLA-B*40:MUD`) decoded ?
``curl https://mac.b12x.org/api/decode?typing=HLA-B*40:MUD``  
returns  
``  RAD	16/67``

### What MAC designation should I use ?
```
curl https://mac.b12x.org/api/encode \
-H "content-type: text/plain" \
-d "HLA-B*40:01/HLA-B*40:07/HLA-B*40:10/HLA-B*40:22N/HLA-B*40:30/HLA-B*40:34"
```
returns  
``HLA-B*40:MUD``

### What is the MAC definition of [RAD](“https://mac.b12x.org/api/codes/RAD”) ?
``curl “https://mac.b12x.org/api/codes/RAD”``  
returns  
``  RAD	16/67``


## Resource Links
 * [curl home page](https://curl.haxx.se/)
 * http://hla.alleles.org/nomenclature/index.html - HLA nomenclature
 * https://github.com/ANHIG/IMGTHLA
