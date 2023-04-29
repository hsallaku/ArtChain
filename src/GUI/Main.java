package GUI;

import javafx.application.Application;
import javafx.util.Callback;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import java.io.*;
import java.lang.reflect.Type;
import utils.*;
import network.*;
import core.*;

public class Main extends Application {
    
    private Stage window;
    private Scene mainMenuScene, newNodeScene, existingNodeScene, transactionSubmissionScene;
    private Pagination pagination;
    private static BNode currentUser = new BNode();
    
    public static void main(String[] args) {
        // Start the synchronization simulator in a separate thread
        SynchronizationSimulator simulator = new SynchronizationSimulator();
        Thread syncThread = new Thread(simulator);
        syncThread.setDaemon(true); // Set the thread as a daemon so it will automatically exit when the main program finishes
        syncThread.start();
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        
        mainMenuScene = createMainMenuScene();
        newNodeScene = createNewNodeScene();
        existingNodeScene = createExistingNodeScene();
        transactionSubmissionScene = createTransactionSubmissionScene();

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
            List<BNode> users;
            try (FileReader reader = new FileReader(usersFile)) {
                Type userListType = new TypeToken<ArrayList<BNode>>(){}.getType();
                users = gson.fromJson(reader, userListType);
            } catch (IOException ex) {
                users = new ArrayList<>();
            }

            boolean userExists = users.stream().anyMatch(user -> user.getUsername().equals(username));
            if (!userExists) {
                currentUser = new BNode(username, password);
                window.setScene(createStandardNodeScene());
                
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
            List<BNode> users;
            try (FileReader reader = new FileReader(usersFile)) {
                Type userListType = new TypeToken<ArrayList<BNode>>(){}.getType();
                users = gson.fromJson(reader, userListType);
            } catch (IOException ex) {
                users = new ArrayList<>();
            }

            BNode loggedInUser = null;
            for (BNode user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    loggedInUser = user;
                    break;
                }
            }

            if (loggedInUser != null) {
                currentUser = loggedInUser;
                System.out.println("Node " + currentUser.getUsername() + " has logged in.");
                if (loggedInUser.getStatus().equals("s")) {
                    window.setScene(createStandardNodeScene());
                } else if (loggedInUser.getStatus().equals("v")) {
                    window.setScene(createValidatorNodeScene());
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
        Label standardNodeWelcome = new Label("Welcome, " + currentUser.getUsername() + "!");
        Button viewBlocksBtnStandardNode = new Button("View Blocks");
        viewBlocksBtnStandardNode.setOnAction(e -> {
                if(!currentUser.getBlockchain().getChain().isEmpty())
                    window.setScene(createViewBlocksScene());
                else
                    showAlert(Alert.AlertType.INFORMATION, "EMPTY BLOCKCHAIN", "There are no block's in this node's blockchain.");
        });
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
        Label validatorNodeWelcome = new Label("Welcome, " + currentUser.getUsername() + "!");
        Button viewBlocksBtnStandardNode = new Button("View Blocks");
        viewBlocksBtnStandardNode.setOnAction(e -> {
                if(!currentUser.getBlockchain().getChain().isEmpty())
                    window.setScene(createViewBlocksScene());
                else
                    showAlert(Alert.AlertType.INFORMATION, "EMPTY BLOCKCHAIN", "There are no block's in this node's blockchain.");
        });
        Button viewPendingTransactionsBtn = new Button("Pending Transactions");
        viewPendingTransactionsBtn.setOnAction(e -> {
                if(!currentUser.getBlockchain().getPendingTransactions().isEmpty())
                    window.setScene(createPendingTransactionScene());
                else
                    showAlert(Alert.AlertType.INFORMATION, "No remaining pending transactions", "There are no remaining pending transactions.");
        });
        Button submitTransactionBtnValidatorNode = new Button("Submit Transaction");
        submitTransactionBtnValidatorNode.setOnAction(e -> window.setScene(transactionSubmissionScene));
        Button logOutBtnValidatorNode = new Button("Log Out");
        logOutBtnValidatorNode.setOnAction(e -> window.setScene(mainMenuScene));
        
        VBox validatorNodeLayout = new VBox(10);
        validatorNodeLayout.getChildren().addAll(validatorNodeWelcome, viewBlocksBtnStandardNode, viewPendingTransactionsBtn, submitTransactionBtnValidatorNode, logOutBtnValidatorNode);
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
            String senderName = senderField.getText();
            String receiverName = receiverField.getText();
            String amountValue = amountField.getText();

            if (artworkTitle.isEmpty() || artistName.isEmpty() || creationDate.isEmpty() || senderName.isEmpty() || receiverName.isEmpty() || amountValue.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all the fields.");
            } else {
                Artwork artwork = new Artwork(artworkTitle, artistName, creationDate);
                Transaction transaction = new Transaction(artwork, senderName, receiverName, amountValue);

                currentUser.getBlockchain().addTransaction(transaction);
                
                // Load the current user's blockchain
                Blockchain currentUserBlockchain = BlockchainIO.loadBlockchain("blockchain" + currentUser.getId() + ".json");

                // Add the transaction to the current user's pending transactions
                currentUserBlockchain.addTransaction(transaction);

                // Save the updated blockchain back to the JSON file
                BlockchainIO.saveBlockchain("blockchain" + currentUser.getId() + ".json", currentUserBlockchain);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction submitted successfully.");

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
            fireBackButton();
        });

        // Create an HBox to align the buttons horizontally and center them
        HBox buttonsHBox = new HBox(10, backBtnTransactionForm, submitTransactionFormBtn);
        buttonsHBox.setAlignment(Pos.CENTER);

        VBox transactionSubmissionLayout = new VBox(10, gridHBox, buttonsHBox);
        transactionSubmissionLayout.setAlignment(Pos.CENTER);

        return new Scene(transactionSubmissionLayout, 300, 300);
    }
    
    private Scene createViewBlocksScene() {
        pagination = new Pagination(currentUser.getBlockchain().getChain().size(), 0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createVBPage(pageIndex);
            }
        });

        Button backBtnViewBlocks = new Button("Back");
        backBtnViewBlocks.setOnAction(e -> {
            if (currentUser.getStatus().equals("s")) {
                window.setScene(createStandardNodeScene());
            } else if (currentUser.getStatus().equals("v")) {
                window.setScene(createValidatorNodeScene());
            }
        });

        VBox viewBlocksVBox = new VBox(pagination, backBtnViewBlocks);
        viewBlocksVBox.setAlignment(Pos.CENTER);
        viewBlocksVBox.setSpacing(10);

        Scene viewBlocksScene = new Scene(viewBlocksVBox, 500, 300);
        return viewBlocksScene;
    }

    
    public VBox createVBPage(int pageIndex) {
        Block block = currentUser.getBlockchain().getChain().get(pageIndex);
        Label blockNumberLabel = new Label("Block # " + pageIndex);
        Label hashLabel = new Label("Hash: " + block.getHash());
        Label prevHashLabel = new Label("Prev. Hash: " + block.getPreviousHash());
        Label timeStampLabel = new Label("Time Stamp: " + block.getTimeStamp());

        VBox blockInfoVBox = new VBox(10, blockNumberLabel, hashLabel, prevHashLabel, timeStampLabel);
        blockInfoVBox.setAlignment(Pos.CENTER);

        VBox transactionButtonsVBox = new VBox(10);
        transactionButtonsVBox.setAlignment(Pos.CENTER);
        
        for (int i = 0; i < block.getTransactions().size(); i++) {
            Button transactionButton = new Button("Transaction " + (i + 1));
            int transactionIndex = i;
            transactionButton.setOnAction(e -> {
                window.setScene(createTransactionViewScene(block.getTransactions().get(transactionIndex), pageIndex));
            });
            transactionButtonsVBox.getChildren().add(transactionButton);
        }

        VBox pageVBox = new VBox(20, blockInfoVBox, transactionButtonsVBox);
        pageVBox.setAlignment(Pos.CENTER);
        return pageVBox;
    }

    private Scene createTransactionViewScene(Transaction transaction, int pageIndex) {
        HBox transactionHBox = createPageHBox(transaction);
        Button backBtnTransactionView = new Button("Back");
        backBtnTransactionView.setOnAction(e -> {
            Scene viewBlocksScene = createViewBlocksScene();
            window.setScene(viewBlocksScene);
            viewBlocksScene.getRoot().requestFocus();
            ((Pagination) ((VBox) viewBlocksScene.getRoot()).getChildren().get(0)).setCurrentPageIndex(pageIndex);
        });

        VBox transactionViewVBox = new VBox(10, transactionHBox, backBtnTransactionView);
        transactionViewVBox.setAlignment(Pos.CENTER);

        HBox paddedHBox = new HBox(transactionViewVBox);
        paddedHBox.setAlignment(Pos.CENTER);
        paddedHBox.setPadding(new Insets(0, 20, 0, 20));

        return new Scene(paddedHBox, 300, 300);
    }

    private HBox createPageHBox(Transaction transaction) {
        Label artworkTitleLabel = new Label("Artwork Title:");
        artworkTitleLabel.setMaxWidth(Double.MAX_VALUE);
        artworkTitleLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkTitleField = new TextField(transaction.getArtwork().getTitle());
        artworkTitleField.setEditable(false);

        Label artworkArtistLabel = new Label("Artist:");
        artworkArtistLabel.setMaxWidth(Double.MAX_VALUE);
        artworkArtistLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkArtistField = new TextField(transaction.getArtwork().getArtist());
        artworkArtistField.setEditable(false);

        Label artworkDateLabel = new Label("Creation Date:");
        artworkDateLabel.setMaxWidth(Double.MAX_VALUE);
        artworkDateLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkDateField = new TextField(transaction.getArtwork().getCreationDate());
        artworkDateField.setEditable(false);

        Label senderLabel = new Label("Giving Party:");
        senderLabel.setMaxWidth(Double.MAX_VALUE);
        senderLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField senderField = new TextField(transaction.getSender());
        senderField.setEditable(false);

        Label receiverLabel = new Label("Receiving Party:");
        receiverLabel.setMaxWidth(Double.MAX_VALUE);
        receiverLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField receiverField = new TextField(transaction.getReceiver());
        receiverField.setEditable(false);

        Label amountLabel = new Label("Amount (USD):");
        amountLabel.setMaxWidth(Double.MAX_VALUE);
        amountLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField amountField = new TextField(transaction.getAmount());
        amountField.setEditable(false);

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

        return gridHBox;
    }
    
    private Scene createPendingTransactionScene() {
        pagination = new Pagination(currentUser.getBlockchain().getPendingTransactions().size());
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPTPage(pageIndex);
            }
        });
        
        // Definition and functionality for the back button
        Button backBtnPendingTransactions = new Button("Back");
        backBtnPendingTransactions.setOnAction(e -> {
            fireBackButton();
        });
        
        HBox buttonsHBox = new HBox(backBtnPendingTransactions);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setMaxWidth(Double.MAX_VALUE);
        buttonsHBox.setPadding(new Insets(10, 0, 10, 0)); // Add padding around the back button

        VBox layout = new VBox(pagination, buttonsHBox);
        layout.setSpacing(10);
        layout.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(layout);
        return scene;
    }
    
    public HBox createPTPage(int pageIndex) {
        Transaction transaction = currentUser.getBlockchain().getPendingTransactions().get(pageIndex);
        
        Label artworkTitleLabel = new Label("Artwork Title:");
        artworkTitleLabel.setMaxWidth(Double.MAX_VALUE);
        artworkTitleLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkTitleField = new TextField(transaction.getArtwork().getTitle());
        artworkTitleField.setEditable(false);
        
        Label artworkArtistLabel = new Label("Artist:");
        artworkArtistLabel.setMaxWidth(Double.MAX_VALUE);
        artworkArtistLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkArtistField = new TextField(transaction.getArtwork().getArtist());
        artworkArtistField.setEditable(false);

        Label artworkDateLabel = new Label("Creation Date:");
        artworkDateLabel.setMaxWidth(Double.MAX_VALUE);
        artworkDateLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField artworkDateField = new TextField(transaction.getArtwork().getCreationDate());
        artworkDateField.setEditable(false);

        Label senderLabel = new Label("Giving Party:");
        senderLabel.setMaxWidth(Double.MAX_VALUE);
        senderLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField senderField = new TextField(transaction.getSender());
        senderField.setEditable(false);

        Label receiverLabel = new Label("Receiving Party:");
        receiverLabel.setMaxWidth(Double.MAX_VALUE);
        receiverLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField receiverField = new TextField(transaction.getReceiver());
        receiverField.setEditable(false);

        Label amountLabel = new Label("Amount (USD):");
        amountLabel.setMaxWidth(Double.MAX_VALUE);
        amountLabel.setAlignment(Pos.BASELINE_RIGHT);
        TextField amountField = new TextField(transaction.getAmount());
        amountField.setEditable(false);

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
        
        Button acceptBtn = new Button("Accept");
        Button rejectBtn = new Button("Reject");

        acceptBtn.setOnAction(e -> {
            currentUser.getBlockchain().acceptPendingTransaction(transaction, pageIndex);
            updatePTPagination();
            if (currentUser.getBlockchain().getPendingTransactions().isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No remaining pending transactions", "There are no remaining pending transactions.");
                fireBackButton();
            }
        });

        rejectBtn.setOnAction(e -> {
            currentUser.getBlockchain().rejectPendingTransaction(pageIndex);
            updatePTPagination();
            if (currentUser.getBlockchain().getPendingTransactions().isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No remaining pending transactions", "There are no remaining pending transactions.");
                fireBackButton();
            }
        });

        HBox buttonsHBox = new HBox(10, acceptBtn, rejectBtn);
        buttonsHBox.setAlignment(Pos.CENTER);

        VBox pageVBox = new VBox(10, gridHBox, buttonsHBox);
        pageVBox.setAlignment(Pos.CENTER);

        return new HBox(pageVBox);
    }
    
    public void updatePTPagination() {
        // Save the updated blockchain back to the JSON file
        BlockchainIO.saveBlockchain("blockchain" + currentUser.getId() + ".json", currentUser.getBlockchain());
        pagination.setPageCount(currentUser.getBlockchain().getPendingTransactions().size());
        if (pagination.getCurrentPageIndex() >= currentUser.getBlockchain().getPendingTransactions().size()) {
            pagination.setCurrentPageIndex(currentUser.getBlockchain().getPendingTransactions().size() - 1);
        }
    }

    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void fireBackButton(){
        if (currentUser.getStatus().equals("s")) {
            window.setScene(createStandardNodeScene());
        } else if (currentUser.getStatus().equals("v")) {
            window.setScene(createValidatorNodeScene());
        }
    }
}

