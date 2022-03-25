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
public class AllMessagesController<newSysUser> extends AnchorPane {

    private User newSysUser;
    public AllMessagesController(User newSysUser) throws IOException {
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
    public void loadMessages() {
        User.fillMessages(newSysUser);
    }

    @FXML private Button newMessageButton;

    @FXML private void currentMessage(User newSysUser)
    {
        Stage stage= new Stage();
        NewMessageController newMessage = new NewMessageController(newSysUser);
        newMessage.load("");
        Scene scene = new Scene(newMessage);
        stage.setScene(scene);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }


    @FXML private void  viewMessage()
    {
        AllMessages message = (AllMessages)messagesTable.getSelectionModel().getSelectedItem();

        //System.out.println(message.getMessage());
        Stage stage= new Stage();
        ReadMessageController newMessage = new ReadMessageController(message,newSysUser);
        newMessage.fillMessage();
        Scene scene = new Scene(newMessage);
        stage.setScene(scene);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
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
