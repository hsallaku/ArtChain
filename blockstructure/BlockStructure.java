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
//Can replace "SHA-256" with other hash algos such as SHA-512 to generate different hashes
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
        ArrayList<Block> blocks = new ArrayList<Block>();
        String genesisData = "abc";
        String genesisHash = toHexString(getSHA(genesisData));
        blocks.add(new Block(0, "abc",
                "0000000000000000000000000000000000000000000000000000000000000000", genesisHash));
        Scanner input = new Scanner(System.in);
        int userChoice;
        int count = 1;
        do {
            System.out.println("\nBlock Creation Menu:");
            System.out.println("1. Create new block");
            System.out.println("2. Display blocks");
            System.out.println("3. Exit");
            System.out.print("Enter a menu option: ");
            userChoice = input.nextInt();
            switch (userChoice) {
                case 1:
                    input.nextLine();
                    System.out.println("\nBlock number " + count);
                    System.out.println("Enter data for block: ");
                    String blockData = input.nextLine();
                    String hash = toHexString(getSHA(blockData));
                    blocks.add(new Block(count, blockData,
                            blocks.get(count - 1).getHash(), hash));
                    System.out.println("Block " + count + " added.");
                    count++;
                    break;
                case 2:
                    for (int i = 0; i < blocks.size(); i++) {
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
                    System.exit(0);
                default:
                    System.out.println("Invalid entry.");
                    break;
            }
        } while (userChoice != 0);
    }
}
