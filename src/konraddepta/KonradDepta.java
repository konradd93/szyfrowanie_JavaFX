/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konraddepta;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author Dell
 */
public class KonradDepta extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaxHeight(600);
        stage.setMaxWidth(800);
        stage.setMinHeight(540);
        stage.setMinWidth(700);
        stage.show();
    }

            @Override
    public void stop() throws IOException{
        File file = new File(FXMLDocumentController.fxmlController.filePath.getText());
        if(file.exists()){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Uwaga");
        alert.setHeaderText("Czy chcesz usunąć plik: ");
        alert.setContentText(FXMLDocumentController.fxmlController.filePath.getText());
        ButtonType buttonTypeYes = new ButtonType("Tak");
        ButtonType buttonTypeNo = new ButtonType("Nie");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            FXMLDocumentController.deleteDir(file);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}
