## PoSWBA - Proof of Storage Work and Burned Activity

### Howto:

##### Step 1:

1. Miners using PoW generate shards and store them somewhere.
2. When they want to generate new block, they hash each shard with given seed.
If result is less than target then block is mined

##### Step 2 (optional):

0. Miner transmits header of found block all over the network.
1. Moneyholders burn their coins.
Each burned coin is gived unique id and tied to address that burned it.
2. Using special algorithm N ids of burned coins selected.
Owner of each selected coin signs block header. When each owner signs block block is found.
