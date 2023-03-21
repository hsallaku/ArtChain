package blockchainproject_v1;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {
    
    private int difficulty;
    private List<Block> blocks;

    public BlockChain(int difficulty) {
        this.difficulty = difficulty;
        blocks = new ArrayList<>();
        
        //create the first block
        Block b = new Block(null, "Genesis Block", System.currentTimeMillis(), 0);
        b.mineBlock(difficulty);
        blocks.add(b);
    }

    public int getDifficulty() {
        return difficulty;
    }
    
    public Block latestBlock() {
        return blocks.get(blocks.size() - 1);
    }
    
    public Block newBlock(String data) {
        Block latestBlock = latestBlock();
        return new Block(latestBlock.getHash(), data, System.currentTimeMillis(), latestBlock.getIndex() + 1);
    }
    
    public void addBlock(Block b) {
        if (b != null) {
            b.mineBlock(difficulty);
            blocks.add(b);
        }
    }
    
    public boolean isFirstBlockValid() {
        Block firstBlock = blocks.get(0);
        
        if (firstBlock.getIndex() != 0) {
            return false;
        }
        
        if (firstBlock.getPrevHash() != null) {
            return false;
        }
        
        if (firstBlock.getHash() == null || !Block.calculateHash(firstBlock).equals(firstBlock.getHash())) {
            return false;
        }
                
        return true;
    }    
    
  public boolean isValidNewBlock(Block newBlock, Block previousBlock) {
      if (newBlock != null && previousBlock != null) {
          if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
              return false;
          }
          
          if (newBlock.getPrevHash() == null || !newBlock.getPrevHash().equals(previousBlock.getHash())) {
              return false;
          }
          
          if (newBlock.getHash() == null || !Block.calculateHash(newBlock).equals(newBlock.getHash())) {
              return false;
          }
          return true;
      }
      return false;
  }  
    
    public boolean isBlockChainValid() {
        if (!isFirstBlockValid()) {
            return false;
        }
        
        for (int i = 1; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
            Block previousBlock = blocks.get(i-1);
            
            if (!isValidNewBlock(currentBlock, previousBlock)) {
                return false;
            }
        }
        return true;
    }

public String toString() {
    StringBuilder builder = new StringBuilder();
    
    for (Block block : blocks) {
        builder.append(block).append("\n");
    }
    
    return builder.toString();
    }  

}

