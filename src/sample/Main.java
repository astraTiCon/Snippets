package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Snippets");
        Scene scene = new Scene(root);
        primaryStage.setScene( scene );
        primaryStage.show();
    }

    @Override
    public void stop(){
        if (Controller.treeDataStructure != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(Controller.treeFilename);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(Controller.treeDataStructure);
                objectOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}