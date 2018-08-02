package com.hms.hms_test_2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class AllMessagesController extends AnchorPane {

    private User newSysUser;
    
    public AllMessagesController(User newSysUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AllMessages.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.newSysUser = newSysUser;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML TableView messagesTable;
    
    public void loadMessages()
    {
        ArrayList<ArrayList<String>> data = newSysUser.getMessages();
        int noOfMessages = data.size();
        
        ObservableList<AllMessages> msgList = FXCollections.observableArrayList();
        Image img;
        
        for (int i = 1; i < noOfMessages; i++)
        {
            ArrayList<String> row = data.get(i);
            
            String sender = row.get(0);
            String subject = row.get(1);
            String message = row.get(2);
            String date = row.get(3);
            String read = row.get(4);
            String id = row.get(5);
            
            ImageView imageView2;
            Image img2;
            if ( read.equals("0") )
            {
                img2 = new Image(getClass().getResource("/imgs/msgunread.png").toString(), true);
            } else {
                img2 = new Image(getClass().getResource("/imgs/msgread.png").toString(), true);
            }   
            imageView2 = new ImageView(img2);
            imageView2.setFitHeight(25);
            imageView2.setFitWidth(25);
            imageView2.setPreserveRatio(true);
            
            
            ArrayList<ArrayList<String>> data2 = newSysUser.getName(sender);
            
            String name = data2.get(1).get(0)+" "+data2.get(1).get(1);
            String user = data2.get(1).get(2);
            
            // getting the profile picture 
            try{
                String image = newSysUser.getProfilePic(sender);
                img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
            }catch(Exception e){
                img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
            }
            ImageView imageView = new ImageView(img);
            imageView.setFitHeight(35);
            imageView.setFitWidth(35);
            imageView.setPreserveRatio(true);
            //
            
            AllMessages msg = new AllMessages(  imageView, 
                                                name + " (" + user + ") "+ "\n" + subject + "\n"+ date,
                                                imageView2, sender, message, date, subject, name, user, id);    
            msgList.add(msg);
            
        }    
       
        TableColumn pic = new TableColumn<>("profile");
        pic.setCellValueFactory(new PropertyValueFactory<>("image"));
        pic.prefWidthProperty().bind(messagesTable.widthProperty().divide(8));
        pic.setResizable(false);
        
        TableColumn msg = new TableColumn<>("Message");
        msg.setCellValueFactory(new PropertyValueFactory<>("string"));
        msg.prefWidthProperty().bind(messagesTable.widthProperty().divide(1.5));
        msg.setResizable(true);
        
        TableColumn read = new TableColumn<>("");
        read.setCellValueFactory(new PropertyValueFactory<>("image2"));
        read.prefWidthProperty().bind(messagesTable.widthProperty().divide(8));
        read.setResizable(false);
        
        messagesTable.getColumns().add(pic);
        messagesTable.getColumns().add(msg);
        messagesTable.getColumns().add(read);
        messagesTable.setItems(msgList); 
        
    }  
    
    
    @FXML private Button newMessageButton;
    
    @FXML private void newMessage()
    {
        Stage stage= new Stage();
        NewMessageController newMessage = new NewMessageController(newSysUser);
        newMessage.load("");
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
    
    @FXML private void showMessage()
    {
        AllMessages message = (AllMessages)messagesTable.getSelectionModel().getSelectedItem();
        //System.out.println(message.getMessage());
        
        Stage stage= new Stage();
        ReadMessageController newMessage = new ReadMessageController(message,newSysUser);
        newMessage.fillMessage();
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
}
