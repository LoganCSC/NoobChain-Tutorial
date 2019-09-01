package noobchain;

import noobchain.model.Block;
import noobchain.model.BlockChain;

public class BlockChainDemo {

    /**
     * Test the block chain by adding blocks to the BLOCK_CHAIN ArrayList.
     */
    public static void main(String[] args) {

        BlockChain chain = new BlockChain(5);

        System.out.println("Trying to Mine block 1... ");
        chain.addBlock(new Block("Hi, I'm the first block", "0"));

        System.out.println("Trying to Mine block 2... ");
        chain.addBlock(new Block("Yo, I'm the second block", chain.get(chain.size()-1).hash));

        System.out.println("Trying to Mine block 3... ");
        chain.addBlock(new Block("Hey, I'm the third block", chain.get(chain.size()-1).hash));

        System.out.println("\nBlockchain is Valid: " + chain.isChainValid());

        System.out.println("\nThe block chain: ");
        System.out.println(chain.getAsJson());
    }
}
