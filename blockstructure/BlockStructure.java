package blockstructure;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.ArrayList;

public class BlockStructure {

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        //Calls built-in Java method for generating SHA-256 algorithm.
        //Can replace "SHA-256" with other hash algos such as SHA-512 to generate different hashes.
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        //Calls built-in Java digest method to return an array of bytes to be used in toHexString method
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        //Converts array of bytes into signum
        BigInteger number = new BigInteger(1, hash);
        
        //Converts to hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
        
        //Supposed to insert leading zeroes to hash. Could not get to work.
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        
        return hexString.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        //Creates an ArrayList of Block objects called 'blocks'
        ArrayList<Block> blocks = new ArrayList<Block>();
        
        //Gensis block creation start
        //Premade data for a genesis block
        String genesisData = "abc";
        
        //Uses premade data to generate a SHA256 hash
        String genesisHash = toHexString(getSHA(genesisData));
        
        //Calls the ArrayList 'add' function to add a new Block object to the 'blocks' ArrayList
        //using premade data and hash obtained from it. Since there is no previous hash to use,
        //a 64-bit string of 0's is used.
        blocks.add(new Block(0, "abc",
                "0000000000000000000000000000000000000000000000000000000000000000", genesisHash));
        //Gensis block creation end
        
        //Switch-case menu start
        //Creates a Scanner object called 'input' for future use in switch-case menu
        Scanner input = new Scanner(System.in);
        
        //Variable initialization for use in switch-case menu
        int userChoice;
        int count = 1;
        
        //Do-while loop that runs until user enters 0
        do {
            //Case menu 
            System.out.println("\nBlock Creation Menu:");
            System.out.println("1. Create new block");
            System.out.println("2. Display blocks");
            System.out.println("3. Exit");
            
            System.out.print("Enter a menu option: ");
            userChoice = input.nextInt();
            
            switch (userChoice) {
                case 1:
                    //Clears the Scanner buffer
                    input.nextLine();
                    
                    //Asks user to enter data for the block's data field
                    System.out.println("\nBlock number " + count);
                    System.out.println("Enter data for block: ");
                    String blockData = input.nextLine();
                    
                    //Takes user-inputted data uses it as an argument for the getSHA function, then converts the output of
                    //the getSHA function to a String
                    String hash = toHexString(getSHA(blockData));
                    
                    //Calls the 'add' function of the 'blocks' ArrayList, using 'count' for the block #, user-inputted
                    //'blockData' for the block data, 'blocks.get(count-1).getHash()' that fetches the previous block's hash,
                    //and 'hash' as the block's hash
                    blocks.add(new Block(count, blockData,
                            blocks.get(count - 1).getHash(), hash));
                    
                    System.out.println("Block " + count + " added.");
                    
                    //Increments 'count' so that the next block # will be one greater than the current block's block #
                    count++;
                    break;
                    
                case 2:
                    //For loop that iterates over the entire 'blocks' ArrayList
                    for (int i = 0; i < blocks.size(); i++) {
                        //Calls Block object getters in order to display a block's info
                        System.out.println("\nBlock " + i + ":");
                        System.out.print("\tTimestamp: \t");
                        System.out.println("\t" + blocks.get(i).getTime());
                        System.out.print("\tData: \t");
                        System.out.println("\t" + blocks.get(i).getData());
                        System.out.print("\tPrevious hash: ");
                        System.out.println("\t" + blocks.get(i).getPrevHash());
                        System.out.print("\tHash: \t");
                        System.out.println("\t" + blocks.get(i).getHash());
                    }
                    break;
                    
                case 3:
                    //Exit case. May not be necessary
                    System.exit(0);
                    
                default:
                    //Input validation
                    System.out.println("Invalid entry.");
                    break;
            }
        } while (userChoice != 0);
    }
}
