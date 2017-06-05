# Blockchain

## Block structure

* 8 bytes - version id
* 8 bytes - height. Starts with 0. Unsigned.
* 8 bytes - timestamp. In milliseconds.
* 32 bytes - parent block hash. Created by hashing block header.
...

### Substructures

