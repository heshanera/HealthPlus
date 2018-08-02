package com.hms.hms_test_2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author heshan
 */
public class ReadMessageController extends AnchorPane {

    private AllMessages message;
    private User newSysUser;
    
    public ReadMessageController(AllMessages message, User newSysUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ReadMessage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.message = message;
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
    
    @FXML private Label close;
    
    @FXML private void closeEditor()
    {
        Stage stage; 
        stage = (Stage) close.getScene().getWindow();
        stage.close();
    }    
    
    @FXML private Button deleteButton;
    
    @FXML private void delete()
    { 
        String id = msgID.getText();
        boolean result = newSysUser.deleteMessage(id);
        if (result == true) 
        {    
            Stage stage; 
            stage = (Stage) close.getScene().getWindow();
            stage.close();
            showSuccessIndicator();
        }
            
    }
    
    @FXML private void reply()
    {
        Stage stage2; 
        stage2 = (Stage) close.getScene().getWindow();
        stage2.close();
        
        Stage stage= new Stage();
        NewMessageController newMessage = new NewMessageController(newSysUser);
        System.out.println(msgID.getText());
        newMessage.load(msgID.getText());
        Scene scene = new Scene(newMessage);
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
    
    @FXML private void previousMessage()
    {
    
    }
    
    @FXML private void nextMessage()
    {
    
    }        
    
    
    @FXML private Label senderName;
    @FXML private Label date;
    @FXML private Label subject;
    
    @FXML private TextArea messagetxt;
    
    private Label msgID = new Label();
    
    public void fillMessage()
    {
        
        String name = message.getName() + " (" + message.getType() + ")";
        String dt = message.getDate();
        String sbjct = message.getSubject();
        String msg = message.getMessage();
        String msgid = message.getID();
        
        newSysUser.setMessageRead(msgid);
        msgID.setText(msgid);
        
        senderName.setText(name);
        date.setText(dt);
        subject.setText(sbjct);
        messagetxt.setText(msg);
        
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
