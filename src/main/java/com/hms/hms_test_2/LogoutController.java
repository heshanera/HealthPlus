package com.hms.hms_test_2;

import Admin.AdminController;
import Cashier.CashierController;
import Doctor.Doctor;
import Doctor.DoctorController;
import Pharmacist.Pharmacist;
import Pharmacist.PharmacistController;
import Receptionist.ReceptionistController;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LogoutController extends AnchorPane {

    @FXML
    private Button mainLogoutButton;
    
    private User user;
    
    public LogoutController(Button button,User user) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Logout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        this.user = user;
        mainLogoutButton = button;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private Button logoutButton;
    @FXML private Button cancelButton;
    
    @FXML
    private void logout()
    {
        
        user.saveLogout(user.username);
        
        Stage stage = (Stage) mainLogoutButton.getScene().getWindow();
        stage.close();
        
        stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        
        LoginController login = new LoginController();
        stage.setScene(new Scene(login));
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    @FXML
    private void cancel()
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private Button saveSuccess;
    
    @FXML
    private void saveSuccessExit(ActionEvent event) {
 
    Stage stage; 
        if(event.getSource()== saveSuccess)
        {
            stage = (Stage) saveSuccess.getScene().getWindow();
            stage.close();
        }
    }
    
}
