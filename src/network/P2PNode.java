package network;

// Local Classes
import core.Block;
import core.Blockchain;
import core.Transaction;

// Native Libraries
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class P2PNode {
    private int port;
    private List<Socket> peers;
    private ServerSocket serverSocket; // Add this line
    private Blockchain blockchain;

    public P2PNode(int port, Blockchain blockchain) {
        this.port = port;
        this.peers = new CopyOnWriteArrayList<>();
        this.blockchain = blockchain;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Node started on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to a new node: " + socket.getRemoteSocketAddress());
                peers.add(socket); // Use 'peers' instead of 'connectedNodes'
                new Thread(() -> handleConnection(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket socket) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object receivedObject = inputStream.readObject();

                if (receivedObject instanceof Transaction) {
                    Transaction transaction = (Transaction) receivedObject;
                    // Add the transaction to the blockchain if it's valid
                    if (blockchain.isValidTransaction(transaction)) {
                        blockchain.addTransaction(transaction);
                        System.out.println("Received a valid transaction: " + transaction);
                    } else {
                        System.out.println("Received an invalid transaction: " + transaction);
                    }
                } else if (receivedObject instanceof Block) {
                    Block block = (Block) receivedObject;
                    // Add the block to the blockchain if it's valid
                    if (blockchain.isValidNewBlock(block)) {
                        blockchain.addBlock(block);
                        System.out.println("Received a valid block: " + block);
                    } else {
                        System.out.println("Received an invalid block: " + block);
                    }
                } else if (receivedObject instanceof Blockchain) {
                    Blockchain receivedBlockchain = (Blockchain) receivedObject;
                    // Replace the local blockchain with the received one if it's valid and longer
                    if (blockchain.isValidReceivedBlockchain(receivedBlockchain)) {
                        blockchain = receivedBlockchain;
                        System.out.println("Received a valid blockchain: " + blockchain);
                    } else {
                        System.out.println("Received an invalid blockchain: " + receivedBlockchain);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void connectToNode(String ipAddress, int port) {
        try {
            Socket socket = new Socket(ipAddress, port);
            System.out.println("Connected to a node: " + socket.getRemoteSocketAddress());
            peers.add(socket);
            new Thread(() -> handleConnection(socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(Object object) {
        for (Socket socket : peers) {
            sendData(socket, object);
        }
    }
    
    private void sendData(Socket socket, Object object) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}