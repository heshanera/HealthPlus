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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author heshan
 */
public class NewMessageController extends AnchorPane {

    private User newSysUser;
    
    public NewMessageController(User newSysUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewMessage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.newSysUser = newSysUser;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
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
    
    @FXML public TextField receivertxt;
    @FXML private TextField subjecttxt;
    @FXML private TextArea messagetxt;
    
    private HashMap<String,String> log;
    
    public void loadUserNames()
    {
        
        ArrayList<ArrayList<String>> data = newSysUser.getUserNameAndID();
        
        log = new HashMap<String,String>();
        ArrayList<String> possibleSuggestions = new ArrayList<String>();
        
        int size = data.size();
        for(int i = 1; i < size; i++)
        {
            log.put(data.get(i).get(0)+" "+data.get(i).get(1),data.get(i).get(3)+" "+data.get(i).get(2));
            possibleSuggestions.add(data.get(i).get(3) +" " + data.get(i).get(0)+" "+data.get(i).get(1));
        }    
        
        TextFields.bindAutoCompletion(receivertxt,possibleSuggestions);
    
    }        
    
    @FXML public TextField userid;
    @FXML private TextField type;
    
    @FXML private void getName()
    {
        
        try{
            String data[] = receivertxt.getText().split(" ");
            if (data.length == 3)
            {    
                receivertxt.setText(" "+data[1]+" "+data[2]);

                userid.setText(log.get(data[1]+" "+data[2]).split(" ")[0]);
                type.setText(log.get(data[1]+" "+data[2]).split(" ")[1]);
            }
            
        }catch(Exception e){}
        
    }
    
    /*
    @FXML private void getName2()
    {
        /*String data[] = receivertxt.getText().split(" ");
        receivertxt.setText(data[1]+" "+data[2]);
        
        System.out.println(data[1]+" "+data[2]);
        */
    //}
   

    @FXML private Button clearButton;
    
    @FXML private void clear()
    {
        receivertxt.setText("");
        subjecttxt.setText("");
        messagetxt.setText("");
        
        userid.setText("");
        type.setText("");        
    }
    
    @FXML private Button sendButton;
    
    @FXML private void send()
    {
        
        String senderID = newSysUser.userID;
        String subject = subjecttxt.getText();
        String message = messagetxt.getText();
        
        String receiver = userid.getText();
        //String receiver = "hms0001u";
        
        boolean result = true;
        result = newSysUser.sendMessage(senderID, receiver, subject, message);
        
        if (result == true)
        {
            Stage stage; 
            stage = (Stage) close.getScene().getWindow();
            stage.close();
            showSuccessIndicator();
        }    
        
        
    }        
    
    @FXML private Label close;
    
    @FXML private void closeEditor()
    {
        Stage stage; 
        stage = (Stage) close.getScene().getWindow();
        stage.close();
    }       
    
    public void load(String msgid)
    {
        
        if (!msgid.equals(""))
        {
            
            ArrayList<ArrayList<String>> data = newSysUser.getUserNameAndID();
            
            String userId = newSysUser.getMessageSenderInfo(msgid);
            int size = data.size();
            
            for(int i = 1; i < size; i++)
            {
                if ( data.get(i).get(3).equals(userId) )
                {
                    receivertxt.setText(" "+data.get(i).get(0)+" "+data.get(i).get(1));
                    type.setText(data.get(i).get(2));
                }    
            }    
            
            userid.setText(userId);
            
        }    
            
        loadUserNames();
    }
    
    
    public void showSuccessIndicator()
    {
        Stage stage= new Stage();
        SuccessIndicatorController success = new SuccessIndicatorController();
        Scene scene = new Scene(success);
        stage.setScene(scene);
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        
        stage.initStyle(StageStyle.UNDECORATED);
        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }   
    
    
}

