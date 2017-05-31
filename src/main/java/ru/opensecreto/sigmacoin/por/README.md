# PoR - Proof of Resources

## General

Algorithm is based on 4 problems:
* **Proof of work** - miner has to solve computationally hard problem.
* **Proof of capacity** - miner has to store solution from PoW stage.
* **Proof of burn** - coin holders have to burn their coins to get chance to take part in PoA stage.
Burned coins can not be send anymore.
* **Proof of activity** - every burned coin is unique.
To make block become full-fledged, each block must be signed with unique set of burned coins.

#### Step 1

1. Miners using PoW generate shards and store them somewhere.
2. When they want to generate new block, they hash each shard with given seed.
If result is less than target then block is mined

#### Step 2 (optional)

0. Miner transmits header of found block all over the network.
1. Money holders burn their coins.
Each burned coin is gived unique id and tied to address that burned it.
2. Using special algorithm N ids of burned coins selected.
Owner of each selected coin signs block header. When each owner signs block block is found.

## Implementation

1. PoW - proof of work - done by miner
2. PoC - proof of capacity - done by miner
3. PoB - proof of burn - done by coin owners
4. PoA - proof of activity - done by owners of burned coins

#### PoW implementation

0. `id` = 8-byte unsigned int
`seed` = 64 bytes
`publicKey` = ED25519 public key
1. Generate lot of `hash(id # seed # publicKey)`. `id` is increased with each hash. `#` means concatenation.
2. Choose `N` hashes. Xor each hash `result = hash1 ^ hash2 ^ ... ^ hashN`.
3. If `result` array contains only zeroes, then shard is mined, else repeat step 2 with different `id`.
Shard contains information about all `id`s, `seed`, and `publicKey`.
4. Store shard for PoC step
