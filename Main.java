package blockchainproject_v1;

public class Main {
    
    public static void main(String[] args) {
        BlockChain blockchain = new BlockChain(4);
                    Utils.toJsonFile(blockchain);
        blockchain.addBlock(blockchain.newBlock("lets pass a string of data"));
                    Utils.toJsonFile(blockchain);
        blockchain.addBlock(blockchain.newBlock("lets try numbers! 1 2 buckle my shoe, 3 4 shut the door"));
                    Utils.toJsonFile(blockchain);
        blockchain.addBlock(blockchain.newBlock("now symbols!@#$%^&*()"));
                    Utils.toJsonFile(blockchain);
        
        System.out.println("Blockchain valid? " + blockchain.isBlockChainValid());
        
        System.out.println(blockchain);
    }
}
