# ArtChain

**Members**: Haki Sallaku, Gary LaPicola, Thomas Scardino, Matthew Creese

**Introducing ArtChain**: A Decentralized Platform for Art Provenance and Ownership Management

ArtChain is an innovative blockchain-based platform designed to secure and authenticate the ownership and history of artwork in a decentralized, transparent, and immutable manner. The platform aims to tackle the challenges of forgery, dubious provenance, and ownership disputes in the art world.

**Key Features:**

-**Immutable Provenance Tracking**: ArtChain leverages blockchain technology to record the ownership and history of each artwork in an unalterable digital ledger. This ensures the integrity of the artwork's provenance, allowing collectors, institutions, and investors to establish the authenticity of a piece with confidence.

-**Transparent Ownership**: Transactions on ArtChain are visible to all participants, facilitating the verification of the chain of ownership and the transfer of an artwork. This transparency reduces the risk of hidden disputes and provides a clear view of an artwork's ownership history.

-**Secure Transactions**: ArtChain employs advanced cryptographic techniques to secure each transaction, ensuring that every change in ownership is verified and legitimate. The platform's security measures protect against fraud and forgery in the art market.

-**Decentralized Network**: ArtChain's peer-to-peer (P2P) network ensures that no central authority has control over the information. The decentralized nature of the platform guarantees the data's integrity and prevents tampering or manipulation by any single party.

-**User-friendly Interface**: ArtChain is designed for ease of use, allowing art collectors, dealers, and institutions to manage their art portfolios seamlessly. With a simple and intuitive interface, users can effortlessly create transactions, track ownership, and access artwork history.

ArtChain is poised to revolutionize the art world by providing a secure, transparent, and reliable platform for managing artwork ownership and provenance. By harnessing the power of blockchain technology, ArtChain aims to redefine trust and authentication in the art market, enabling a safer and more vibrant ecosystem for artists, collectors, and enthusiasts worldwide. Experience the future of art ownership with ArtChain.

**Contents**:

-**ArtChain**                                                                                                                                                   **core**: Artwork.java / Block.java / Blockchain.java / Transaction.java                                                                                               **main**: Main.java                                                                                                                                                     **network**: BlockchainIO.java / P2PNode.java                                                                                                                 **utils**: StringUtil.java

**Additional Files**: blockchain.json

These are the functionalities of each of the classes held in the source code of the project.

**Artwork**: This class represents an artwork and holds information such as the title, artist, creation year, and location. It has a constructor for initializing these properties and getter methods to access them.

**Block**: This class represents a block in the blockchain. It contains a list of transactions, a timestamp, a nonce, the hash of the previous block, and its own hash. It also has methods for calculating the block's hash, mining the block, and validating the block's transactions.

**Blockchain**: This class represents the blockchain itself. It has a list of blocks (chain) and a list of pending transactions. It provides methods for adding transactions, processing pending transactions, validating the chain, and more.

**Transaction**: This class represents a transaction, which includes an artwork, the sender's address, the recipient's address, and a signature. It has methods for generating a transaction ID, validating the transaction, and verifying the signature.

**Main**: The main class is the entry point for the application. It initializes a blockchain, creates a P2P node, loads and saves the blockchain, creates artwork instances, creates and signs transactions, adds transactions to the blockchain, processes pending transactions, and validates the blockchain. It also prints the blockchain's contents if it is valid.

**BlockchainIO**: This class is responsible for saving and loading the blockchain to and from a JSON file. It uses the Gson library to convert the blockchain object to and from JSON format.

**P2PNode**: This class represents a peer-to-peer node in the network. It manages connections to other nodes, receives and handles incoming data (such as transactions, blocks, and blockchain instances), and broadcasts data to all connected peers.

**StringUtil**: This utility class provides methods for hashing (SHA-256), signing data with a private key, verifying the signature with a public key, and converting keys to strings (Base64 encoding).

