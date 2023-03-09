package blockchainproject_v1;

public class Main {
    
    public static void main(String[] args) {
        BlockChain blockchain = new BlockChain(4);
        blockchain.addBlock(blockchain.newBlock("lets pass a string of data"));
        blockchain.addBlock(blockchain.newBlock("lets try numbers! 1 2 buckle my shoe, 3 4 shut the door"));
        blockchain.addBlock(blockchain.newBlock("now symbols!@#$%^&*()"));
        
        System.out.println("Blockchain validat ? " + blockchain.isBlockChainValid());
        
        System.out.println(blockchain);
    }
}
