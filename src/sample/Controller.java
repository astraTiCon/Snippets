package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.io.*;
import java.nio.file.Paths;

// TODO: 10/06/2017 When a node is removed, or the program closed, the tree will automatically collapse -> see if the state (what is currently expanded) can be saved for next time
// TODO: 12/06/2017 Node renaming feature

public class Controller{
    @FXML TreeView<String> tree;
    @FXML Pane pane;
    @FXML TextField textField;
    @FXML TextArea textArea;
    @FXML Button saveButton;
    private static final String currentWorkingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
    private static final String dataDirectory = currentWorkingDirectory + "\\data";
    static final String treeFilename = dataDirectory + "\\tree.dat";
    static Tree treeDataStructure;
    private String lastNodePressed;

    @FXML private void initialize() {
        try {
            // Create the folder "data" if not already existent
            File dataDirectoryFileObject = new File(dataDirectory);
            if (! dataDirectoryFileObject.exists()){
                boolean exists = dataDirectoryFileObject.mkdir();
                if (!exists){
                    throw new FileNotFoundException(dataDirectory + "could not be created");
                }
            }
            // Load Tree data structure, or create it if it doesn't already exist
            if ( new File(treeFilename).exists() ){ // Load Tree data structure from file
                ObjectInputStream objectInputStream = new ObjectInputStream( new FileInputStream(treeFilename) );
                treeDataStructure = (Tree) objectInputStream.readObject();
                objectInputStream.close();
            } else{
                treeDataStructure = new Tree("root");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeTree();
//        treeDataStructure.outputTree();
    }

    @FXML private void removeButtonAction(){
        // TODO: 10/06/2017 currently this method generates an error if no node has been selected / lastNodePressed might also not have been initialized yet
        // (maybe add condition, if lastNodePressed != null)
        treeDataStructure.removeNode(lastNodePressed);
        initializeTree(); // update UI
    }

    @FXML private void textFieldAction(){

        String enteredText = textField.getText();
        if (enteredText.endsWith(",root")) {
            String splitEnteredText = enteredText.split(",")[0];
            treeDataStructure.addNode(splitEnteredText, "root");
        } else if (lastNodePressed != null) {
            treeDataStructure.addNode(enteredText, lastNodePressed);
        }
        initializeTree(); // update UI

    }

    @FXML private void saveTextAreaContent(){
        if (lastNodePressed != null){
            String filePath = currentWorkingDirectory + "\\src\\sample\\data\\" + lastNodePressed + ".txt";
            String text = textArea.getText();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                Writer bufferedWriter = new BufferedWriter(outputStreamWriter);

                bufferedWriter.write(text);

                bufferedWriter.close();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void loadTextArea() throws IOException{ // Once a node is pressed, loads corresponding file content into TextArea
        int buffer;
        String filePath = currentWorkingDirectory + "\\src\\sample\\data\\" + lastNodePressed + ".txt";
        File file = new File( filePath );
        StringBuilder stringBuilder = new StringBuilder();
        if (file.exists()){
            FileInputStream fileInputStream = new FileInputStream( file );
            while ( (buffer = fileInputStream.read()) != -1){
                char character = (char) buffer;
                stringBuilder.append(character);
            }
            fileInputStream.close();
            textArea.setText(stringBuilder.toString());
        } else {
            textArea.setText("");
        }
    }

    private void initializeTree(){
        TreeItem<String> root = new TreeItem<>("root");
        root.setExpanded(true);

        populateTree(root, treeDataStructure.root);

        double x = tree.getLayoutX();
        double y = tree.getLayoutY();
        double prefHeight = tree.getPrefHeight();
        double prefWidth = tree.getPrefWidth();

        // TODO: 29/05/2017 This way another TreeView object gets initialized, on top of the one already defined in sample.fxml
        tree = new TreeView<>(root);
        tree.setShowRoot(false); // can be moved above tree initialization?

        tree.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if (newValue != null) {
                        lastNodePressed = newValue.getValue();
                        try {
                            loadTextArea();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

        tree.setLayoutX(x);
        tree.setLayoutY(y);
        tree.setPrefHeight(prefHeight);
        tree.setPrefWidth(prefWidth);

        pane.getChildren().add(tree);
    }

    private void populateTree(TreeItem<String> treeItem, Tree.Node startNode){
        for (Tree.Node node : startNode.childrenList){
            TreeItem<String> item = new TreeItem<>(node.getName());
            item.setExpanded(true);
            treeItem.getChildren().add( item );
            if ( !node.childrenList.isEmpty() ){
                populateTree(item, node);
            }
        }
    }


}