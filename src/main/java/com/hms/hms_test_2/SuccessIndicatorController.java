package com.hms.hms_test_2;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SuccessIndicatorController extends AnchorPane {

    public SuccessIndicatorController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ProgressIndicator.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML
    public Button saveSuccess;
    
    @FXML
    public void saveSuccessExit(ActionEvent event) {
 
    Stage stage; 
        if(event.getSource()== saveSuccess)
        {
            stage = (Stage) saveSuccess.getScene().getWindow();
            stage.close();
        }
    }
}