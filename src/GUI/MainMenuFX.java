package GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import utils.*;
import network.*;
import core.*;

public class MainMenuFX extends Application {
    
    private Stage window;
    private Scene mainMenuScene, newNodeScene, existingNodeScene, standardNodeScene, validatorNodeScene, transactionSubmissionScene, viewBlocksScene;
    private static Node currentUser = new Node();
    
    public static void main(String[] args) {
        Artwork artwork1 = new Artwork("Mona Lisa", "Some Guy", "1690");
        Artwork artwork2 = new Artwork("Starry Night", "Other Guy", "420 AD");
        Transaction trans1 = new Transaction(artwork1, "Node A", "Node B", "500");
        Transaction trans2 = new Transaction(artwork2, "Node C", "Node D", "420");
        currentUser.getBlockchain().addTransaction(trans1);
        currentUser.getBlockchain().addTransaction(trans2);
        currentUser.getBlockchain().processPendingTransactions("MinerAddress");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        
        mainMenuScene = createMainMenuScene();
        newNodeScene = createNewNodeScene();
        existingNodeScene = createExistingNodeScene();
        standardNodeScene = createStandardNodeScene();
        validatorNodeScene = createValidatorNodeScene();
        transactionSubmissionScene = createTransactionSubmissionScene();
        viewBlocksScene = createViewBlocksScene();

        // Set the initial scene
        window.setScene(mainMenuScene);
        window.setTitle("ArtChain UI");
        window.show();
    }

    private Scene createMainMenuScene() {
        Button newNodeBtn = new Button("New Node");
        newNodeBtn.setOnAction(e -> window.setScene(newNodeScene));
        Button existingNodeBtn = new Button("Existing Node");
        existingNodeBtn.setOnAction(e -> window.setScene(existingNodeScene));

        VBox mainMenuLayout = new VBox(10);
        mainMenuLayout.getChildren().addAll(newNodeBtn, existingNodeBtn);
        mainMenuLayout.setAlignment(Pos.CENTER);

        return new Scene(mainMenuLayout, 300, 250);
    }

    private Scene createNewNodeScene() {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setMaxWidth(120);
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(120);
        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            File usersFile = new File("users.json");
            Gson gson = new Gson();
            List<Node> users;
            try (FileReader reader = new FileReader(usersFile)) {
                Type userListType = new TypeToken<ArrayList<Node>>(){}.getType();
                users = gson.fromJson(reader, userListType);
            } catch (IOException ex) {
                users = new ArrayList<>();
            }

            boolean userExists = users.stream().anyMatch(user -> user.getUsername().equals(username));
            if (!userExists) {
                currentUser = new Node(username, password);
                window.setScene(standardNodeScene);
                
                System.out.println("User information saved.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Username already exists. Please choose a different username.");
            }
        
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> window.setScene(mainMenuScene));

        VBox newNodeLayout = new VBox(10);
        newNodeLayout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, registerBtn, backBtn);
        newNodeLayout.setAlignment(Pos.CENTER);

        return new Scene(newNodeLayout, 300, 250);
    }

    private Scene createExistingNodeScene() {
        Label existingUsernameLabel = new Label("Username:");
        TextField existingUsernameField = new TextField();
        existingUsernameField.setMaxWidth(120); // Set preferred width for the username field
        Label existingPasswordLabel = new Label("Password:");
        PasswordField existingPasswordField = new PasswordField();
        existingPasswordField.setMaxWidth(120); // Set preferred width for the password field
        Button loginBtn = new Button("Log in");
        loginBtn.setOnAction(e -> {
            String username = existingUsernameField.getText();
            String password = StringUtil.applySha256(existingPasswordField.getText());

            File usersFile = new File("users.json");
            Gson gson = new Gson();
            List<Node> users;
            try (FileReader reader = new FileReader(usersFile)) {
                Type userListType = new TypeToken<ArrayList<Node>>(){}.getType();
                users = gson.fromJson(reader, userListType);
            } catch (IOException ex) {
                users = new ArrayList<>();
            }

            Node loggedInUser = null;
            for (Node user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    loggedInUser = user;
                    break;
                }
            }

            if (loggedInUser != null) {
                System.out.println("Login successful. User information retrieved.");
                currentUser = loggedInUser;
                if (loggedInUser.getStatus().equals("s")) {
                    window.setScene(standardNodeScene);
                } else if (loggedInUser.getStatus().equals("v")) {
                    window.setScene(validatorNodeScene);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Login failed. Invalid username or password.");
            }
        });

        Button backBtnExistingNode = new Button("Back");
        backBtnExistingNode.setOnAction(e -> window.setScene(mainMenuScene));

        VBox existingNodeLayout = new VBox(10);
        existingNodeLayout.getChildren().addAll(existingUsernameLabel, existingUsernameField, existingPasswordLabel, existingPasswordField, loginBtn, backBtnExistingNode);
        existingNodeLayout.setAlignment(Pos.CENTER);

        return new Scene(existingNodeLayout, 300, 250);
    }

    private Scene createStandardNodeScene() {
        Label standardNodeWelcome = new Label("Welcome, Standard Node!");
        Button viewBlocksBtnStandardNode = new Button("View Blocks");
        viewBlocksBtnStandardNode.setOnAction(e -> window.setScene(viewBlocksScene));
        Button submitTransactionBtnStandardNode = new Button("Submit Transaction");
        submitTransactionBtnStandardNode.setOnAction(e -> window.setScene(transactionSubmissionScene));
        Button logOutBtnStandardNode = new Button("Log Out");
        logOutBtnStandardNode.setOnAction(e -> window.setScene(mainMenuScene));

        VBox standardNodeLayout = new VBox(10);
        standardNodeLayout.getChildren().addAll(standardNodeWelcome, viewBlocksBtnStandardNode, submitTransactionBtnStandardNode, logOutBtnStandardNode);
        standardNodeLayout.setAlignment(Pos.CENTER);
        
        return new Scene(standardNodeLayout, 300, 250);
    }

    private Scene createValidatorNodeScene() {
        Label validatorNodeWelcome = new Label("Welcome, Validator Node!");
        Button viewPastTransactionsBtnValidatorNode = new Button("View Past Transactions");
        Button viewPendingTransactionsBtn = new Button("View Pending Transactions");
        Button submitTransactionBtnValidatorNode = new Button("Submit Transaction");
        submitTransactionBtnValidatorNode.setOnAction(e -> window.setScene(transactionSubmissionScene));
        Button logOutBtnValidatorNode = new Button("Log Out");
        logOutBtnValidatorNode.setOnAction(e -> window.setScene(mainMenuScene));
        
        VBox validatorNodeLayout = new VBox(10);
        validatorNodeLayout.getChildren().addAll(validatorNodeWelcome, viewPastTransactionsBtnValidatorNode, viewPendingTransactionsBtn, submitTransactionBtnValidatorNode, logOutBtnValidatorNode);
        validatorNodeLayout.setAlignment(Pos.CENTER);

        return new Scene(validatorNodeLayout, 300, 250);
    }

    private Scene createTransactionSubmissionScene() {
        Label artworkTitleLabel = new Label("Artwork Title:");
        artworkTitleLabel.setMaxWidth(Double.MAX_VALUE);
        artworkTitleLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkTitleField = new TextField();

        Label artworkArtistLabel = new Label("Artist:");
        artworkArtistLabel.setMaxWidth(Double.MAX_VALUE);
        artworkArtistLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkArtistField = new TextField();

        Label artworkDateLabel = new Label("Creation Date:");
        artworkDateLabel.setMaxWidth(Double.MAX_VALUE);
        artworkDateLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkDateField = new TextField();

        Label senderLabel = new Label("Giving Party:");
        senderLabel.setMaxWidth(Double.MAX_VALUE);
        senderLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField senderField = new TextField();

        Label receiverLabel = new Label("Receiving Party:");
        receiverLabel.setMaxWidth(Double.MAX_VALUE);
        receiverLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField receiverField = new TextField();

        Label amountLabel = new Label("Amount (USD):");
        amountLabel.setMaxWidth(Double.MAX_VALUE);
        amountLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField amountField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(artworkTitleLabel, 0, 0);
        grid.add(artworkTitleField, 1, 0);
        grid.add(artworkArtistLabel, 0, 1);
        grid.add(artworkArtistField, 1, 1);
        grid.add(artworkDateLabel, 0, 2);
        grid.add(artworkDateField, 1, 2);
        grid.add(senderLabel, 0, 4);
        grid.add(senderField, 1, 4);
        grid.add(receiverLabel, 0, 5);
        grid.add(receiverField, 1, 5);
        grid.add(amountLabel, 0, 6);
        grid.add(amountField, 1, 6);

        StackPane gridWrapper = new StackPane(grid);
        HBox gridHBox = new HBox(gridWrapper);
        gridHBox.setAlignment(Pos.CENTER);
        
        // Definition and functionality for the submit button
        Button submitTransactionFormBtn = new Button("Submit");
        submitTransactionFormBtn.setOnAction(e -> {
            String artworkTitle = artworkTitleField.getText();
            String artistName = artworkArtistField.getText();
            String creationDate = artworkDateField.getText();
            String senderAddress = senderField.getText();
            String receiverAddress = receiverField.getText();
            String amountValue = amountField.getText();

            if (artworkTitle.isEmpty() || artistName.isEmpty() || creationDate.isEmpty() || senderAddress.isEmpty() || receiverAddress.isEmpty() || amountValue.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all the fields.");
            } else {
                Artwork artwork = new Artwork(artworkTitle, artistName, creationDate);
                Transaction transaction = new Transaction(artwork, senderAddress, receiverAddress, amountValue);

                currentUser.getBlockchain().addTransaction(transaction);
                
                // Load users from the JSON file
                List<Node> users = UsersIO.loadUsers("users.json");

                // Find the current user
                for (Node user : users) {
                    if (user.getId() == currentUser.getId()) {
                        currentUser = user;
                        break;
                    }
                }

                // Add the transaction to the current user's pending transactions
                if (currentUser != null) {
                    currentUser.getBlockchain().addTransaction(transaction);
                    // Save the updated users back to the JSON file
                    UsersIO.saveUsers("users.json", users);
                }

                
                // Load the current user's blockchain
                Blockchain currentUserBlockchain = BlockchainIO.loadBlockchain("blockchain" + currentUser.getId() + ".json");

                // Add the transaction to the current user's pending transactions
                currentUserBlockchain.addTransaction(transaction);

                // Save the updated blockchain back to the JSON file
                BlockchainIO.saveBlockchain("blockchain" + currentUser.getId() + ".json", currentUserBlockchain);


                showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction submitted successfully.");
                System.out.println("Current Node's pending transactions: ");
                currentUser.getBlockchain().printPendingTransactions();

                // Clear the input fields
                artworkTitleField.clear();
                artworkArtistField.clear();
                artworkDateField.clear();
                senderField.clear();
                receiverField.clear();
                amountField.clear();
            }
        });
        
        // Definition and functionality for the back button
        Button backBtnTransactionForm = new Button("Back");
        backBtnTransactionForm.setOnAction(e -> {
            if (currentUser.getStatus().equals("s")) {
                window.setScene(standardNodeScene);
            } else if (currentUser.getStatus().equals("v")) {
                window.setScene(validatorNodeScene);
            }
        });

        // Create an HBox to align the buttons horizontally and center them
        HBox buttonsHBox = new HBox(10, backBtnTransactionForm, submitTransactionFormBtn);
        buttonsHBox.setAlignment(Pos.CENTER);

        VBox transactionSubmissionLayout = new VBox(10, gridHBox, buttonsHBox);
        transactionSubmissionLayout.setAlignment(Pos.CENTER);

        return new Scene(transactionSubmissionLayout, 300, 300);
    }
    
    private Scene createViewBlocksScene() {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(10));
        
        VBox blockContainer = new VBox(5);
        //blockContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");
        
        Label blockNumLabel = new Label("Block #:");
        blockNumLabel.setMaxWidth(Region.USE_PREF_SIZE);
        //blockNumLabel.setMaxWidth(Double.MAX_VALUE);
        blockNumLabel.setAlignment(Pos.BASELINE_LEFT);
        TextField blockNumField = new TextField(Integer.toString(currentUser.getBlockchain().getLength()));
        blockNumField.setMaxWidth(50);
        blockNumField.setEditable(false);
        blockNumField.setFocusTraversable(false);
        
        Label hashLabel = new Label("Hash:");
        //hashLabel.setMaxWidth(Double.MAX_VALUE);
        hashLabel.setAlignment(Pos.BASELINE_LEFT);
        TextField hashField = new TextField(currentUser.getBlockchain().getLatestBlock().getHash());
        hashField.setEditable(false);
        hashField.setFocusTraversable(false);
        
        Label prevHashLabel = new Label("Previous Hash:");
        //prevHashLabel.setMaxWidth(Double.MAX_VALUE);
        prevHashLabel.setAlignment(Pos.BASELINE_LEFT);
        TextField prevHashField = new TextField(currentUser.getBlockchain().getLatestBlock().getPreviousHash());
        prevHashField.setEditable(false);
        prevHashField.setFocusTraversable(false);
        
        Label timestampLabel = new Label("Timestamp:");
        //timestampLabel.setMaxWidth(Double.MAX_VALUE);
        timestampLabel.setAlignment(Pos.BASELINE_LEFT);
        TextField timestampField = new TextField(currentUser.getBlockchain().getLatestBlock().getTimeStamp());
        timestampField.setEditable(false);
        timestampField.setFocusTraversable(false);
        
        Label transactionsLabel = new Label("Transactions:");
        //transactionsLabel.setMaxWidth(Double.MAX_VALUE);
        transactionsLabel.setAlignment(Pos.BASELINE_LEFT);
        
        blockContainer.getChildren().addAll(blockNumLabel, blockNumField, hashLabel, hashField, prevHashLabel, prevHashField, timestampLabel, timestampField, transactionsLabel);
        
        VBox transactionsContainer = new VBox(5);
        
        for(int i = 0; i < 2; i++) {
            Label artworkLabel = new Label((i+1) + ".\tArtwork:");
            //artworkLabel.setMaxWidth(Double.MAX_VALUE);
            artworkLabel.setAlignment(Pos.BASELINE_LEFT);
            TextField artworkField = new TextField(currentUser.getBlockchain().getLatestBlock().getTransactions().get(i).getArtwork().getTitle());
            artworkField.setEditable(false);
            artworkField.setMouseTransparent(true);
            artworkField.setFocusTraversable(false);
            
            Label senderLabel = new Label("\tSender:");
            //senderLabel.setMaxWidth(Double.MAX_VALUE);
            senderLabel.setAlignment(Pos.BASELINE_LEFT);
            TextField senderField = new TextField(currentUser.getBlockchain().getLatestBlock().getTransactions().get(i).getSender());
            senderField.setEditable(false);
            senderField.setMouseTransparent(true);
            senderField.setFocusTraversable(false);
            
            Label receiverLabel = new Label("\tReceiver:");
            //receiverLabel.setMaxWidth(Double.MAX_VALUE);
            receiverLabel.setAlignment(Pos.BASELINE_LEFT);
            TextField receiverField = new TextField(currentUser.getBlockchain().getLatestBlock().getTransactions().get(i).getReceiver());
            receiverField.setEditable(false);
            receiverField.setMouseTransparent(true);
            receiverField.setFocusTraversable(false);
            
            Label amountLabel = new Label("\tAmount:");
            //amountLabel.setMaxWidth(Double.MAX_VALUE);
            amountLabel.setAlignment(Pos.BASELINE_LEFT);
            TextField amountField = new TextField(currentUser.getBlockchain().getLatestBlock().getTransactions().get(i).getAmount());
            amountField.setEditable(false);
            amountField.setMouseTransparent(true);
            amountField.setFocusTraversable(false);
            
            transactionsContainer.getChildren().addAll(artworkLabel, artworkField, senderLabel, senderField, receiverLabel, receiverField, amountLabel, amountField);
        }
        
        blockContainer.getChildren().add(transactionsContainer);
        mainContainer.getChildren().add(blockContainer);
        
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        

        return new Scene(scrollPane, 475, 300);
        
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
    }
    
}

