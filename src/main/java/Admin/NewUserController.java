/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author heshan
 */
class NewUserController extends AnchorPane {

    public NewUserController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewUser.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private TextField userName;
    @FXML private TextField userid;     
    @FXML private TextField password;
    
    public void loadData(String username, String id, String pass)
    {
        userName.setText(username);
        userid.setText(id);
        password.setText(pass);
    }        
    
    @FXML
    private Button closeUser;
    @FXML
    private void closeUserWindow(ActionEvent event) {
 
    Stage stage; 
    Parent root;
        if(event.getSource()== closeUser)
        {
            stage = (Stage) closeUser.getScene().getWindow();
            stage.close();
        }
    } 
}
