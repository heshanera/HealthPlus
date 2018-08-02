package com.hms.hms_test_2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author heshan
 */
public class CurrentUserSummaryController extends AnchorPane {

    private User sysUser;
    
    public CurrentUserSummaryController(User sysUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CurrentUserSummary.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.sysUser = sysUser;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private ImageView profPic;
    @FXML private Label userN;
    @FXML private Label userTAndN;
    
    public void fillUser()
    {
        HashMap<String,String> log = new HashMap<String,String>();
        log.clear();
        log.put("doctor","Doctor");
        log.put("lab_assistant","Lab Assistant");
        log.put("cashier","Cashier");
        log.put("pharmacist","Pharmacist");
        log.put("receptionist","Receptionist");
        log.put("admin","Admin");
            
        ArrayList<ArrayList<String>> data = sysUser.getCurrentUserNameAndID();
        
        userN.setText(data.get(1).get(0)+" "+data.get(1).get(1));
        userTAndN.setText(log.get(data.get(1).get(2)) +" : "+data.get(1).get(3));
        Image img;
        try{
            String image = sysUser.getProfilePic();
            img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
        }catch(Exception e){
            img = new Image(getClass().getResource("/imgs/noUser.png").toString(), true);
        }
        profPic.setImage(img);
        
    }        
    
    
    
    
    @FXML private Button saveSuccess;
    
    @FXML
    private void saveSuccessExit(ActionEvent event) {
 
    Stage stage; 
        if(event.getSource()== saveSuccess)
        {
            stage = (Stage) saveSuccess.getScene().getWindow();
            stage.close();
        }
    }
    
    
    public void load()
    {
        fillUser();
    }
    
    
    
}


