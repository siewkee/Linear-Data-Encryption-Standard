This program implements a Linear Data Encryption Standard algorithm (LDES). 

This is a mini example of a block cipher that has 2 rounds in the Feistel structure. It operates on 4-bit block and 2-bit key.
The important feature of LDES is that the S-box has been replaced with a linear operation, which makes the whole cipher 
linear. 

The implementation is to demonstrate the importance of S-box in DES. Without the S-box, the cipher 
becomes linear and can be totally broken.


