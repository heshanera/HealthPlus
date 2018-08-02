package Admin;

import com.hms.hms_test_2.NewMessageController;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserAccountController extends AnchorPane {

    
    Admin admin = null;
    String usertype = "";
    
    public UserAccountController(String userType, Admin admin) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AdminUserAccount.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        this.admin = admin;
        this.usertype = userType;
        
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    
            
    public void loadUsers( ArrayList<ArrayList<String>> data,String userType)
    {
        
    }
    
    
    @FXML private ImageView userImage1;
    @FXML private Label userName1;
    @FXML private Label userId1;
    @FXML private Label userMobile1;
    @FXML private Label userEmail1;
    
    @FXML private ImageView userImage2;
    @FXML private Label userName2;
    @FXML private Label userId2;
    @FXML private Label userMobile2;
    @FXML private Label userEmail2;
    
    @FXML private ImageView userImage3;
    @FXML private Label userName3;
    @FXML private Label userId3;
    @FXML private Label userMobile3;
    @FXML private Label userEmail3;
    
    @FXML private ImageView userImage4;
    @FXML private Label userName4;
    @FXML private Label userId4;
    @FXML private Label userMobile4;
    @FXML private Label userEmail4;
    
    @FXML private Label tableHeader;
    
    @FXML private Button msg1;
    @FXML private Button msg2;
    @FXML private Button msg3;
    @FXML private Button msg4;
    
    private Label msgLabel1 = new Label();
    private Label msgLabel2 = new Label();
    private Label msgLabel3 = new Label();
    private Label msgLabel4 = new Label();
    
            
    @FXML
    private GridPane userDetailGrid;
    
    @FXML
    private Pagination userDetailPagination;
     
    @FXML
    private BorderPane userDetail(int pageIndex)
    {
        
        ArrayList<ArrayList<String>> data = null;
        
        if (usertype.equals("")) {
            data = admin.getOnlineInfo2();
        } else {
            data = admin.getUserInfo(usertype);
        }   
        
        
        if (data.size() > 0)
        {
            int noOfSlots = (data.size()-1);
            //System.out.println(noOfSlots);
            //System.out.println(currentTimeTableData0);




            int fromIndex = (pageIndex * 4) + 1;
            int toIndex = Math.min(fromIndex + 4, data.size());
            //availabilityTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

            try{
                userName1.setText(data.get(fromIndex).get(0)+" "+data.get(fromIndex).get(1));
                userId1.setText(data.get(fromIndex).get(2));
                userMobile1.setText(data.get(fromIndex).get(3));
                userEmail1.setText(data.get(fromIndex).get(4));
                
                Image img;
                try{
                    String image = data.get(fromIndex).get(7);
                    img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
                }catch(Exception e){
                    img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
                }
                userImage1.setImage(img);
                
                msgLabel1.setText(data.get(fromIndex).get(8));
                msg1.setDisable(false);
                
            }catch(Exception ex){
                userName1.setText("");
                userId1.setText("");
                userMobile1.setText("");
                userEmail1.setText("");
                
                Image img = new Image(getClass().getResource("/imgs/noUser.png").toString(), true);
                userImage1.setImage(img);
                
                msg1.setDisable(true);
            }    
            try{
                userName2.setText(data.get(fromIndex+1).get(0)+" "+data.get(fromIndex+1).get(1));
                userId2.setText(data.get(fromIndex+1).get(2));
                userMobile2.setText(data.get(fromIndex+1).get(3));
                userEmail2.setText(data.get(fromIndex+1).get(4));
                
                Image img;
                try{
                    String image = data.get(fromIndex+1).get(7);
                    img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
                }catch(Exception e){
                    img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
                }
                userImage2.setImage(img);
                msg2.setDisable(false);
                msgLabel2.setText(data.get(fromIndex+1).get(8));
                
            }catch(Exception ex){
                userName2.setText("");
                userId2.setText("");
                userMobile2.setText("");
                userEmail2.setText("");
                
                Image img = new Image(getClass().getResource("/imgs/noUser.png").toString(), true);
                userImage2.setImage(img);
                msg2.setDisable(true);
            }
            try{
                userName3.setText(data.get(fromIndex+2).get(0)+" "+data.get(fromIndex+2).get(1));
                userId3.setText(data.get(fromIndex+2).get(2));
                userMobile3.setText(data.get(fromIndex+2).get(3));
                userEmail3.setText(data.get(fromIndex+2).get(4));
                
                Image img;
                try{
                    String image = data.get(fromIndex+2).get(7);
                    img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
                }catch(Exception e){
                    img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
                }
                userImage3.setImage(img);
                msg3.setDisable(false);
                msgLabel3.setText(data.get(fromIndex+2).get(8));
                
            }catch(Exception ex){
                userName3.setText("");
                userId3.setText("");
                userMobile3.setText("");
                userEmail3.setText("");
                
                Image img = new Image(getClass().getResource("/imgs/noUser.png").toString(), true);
                userImage3.setImage(img);
                msg3.setDisable(true);
            } 
            try{
                userName4.setText(data.get(fromIndex+3).get(0)+" "+data.get(fromIndex+3).get(1));
                userId4.setText(data.get(fromIndex+3).get(2));
                userMobile4.setText(data.get(fromIndex+3).get(3));
                userEmail4.setText(data.get(fromIndex+3).get(4));
                
                Image img;
                try{
                    String image = data.get(fromIndex+3).get(7);
                    img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
                }catch(Exception e){
                    img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
                }
                userImage4.setImage(img);
                msg4.setDisable(false);
                msgLabel4.setText(data.get(fromIndex+3).get(8));
                
            }catch(Exception ex){
                userName4.setText("");
                userId4.setText("");
                userMobile4.setText("");
                userEmail4.setText("");
                
                Image img = new Image(getClass().getResource("/imgs/noUser.png").toString(), true);
                userImage4.setImage(img);
                msg4.setDisable(true);
            } 

            return new BorderPane(userDetailGrid); 


        } else {}
    
       return new BorderPane(userDetailGrid); 
    }
    
    @FXML private void sendMsg1()
    {
        Stage stage= new Stage();
        NewMessageController newMessage = new NewMessageController(admin);
        
        newMessage.userid.setText(msgLabel1.getText());
        newMessage.receivertxt.setText(" "+userName1.getText());
        
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
    
    @FXML private void sendMsg2()
    {
        Stage stage= new Stage();
        NewMessageController newMessage = new NewMessageController(admin);
        
        newMessage.userid.setText(msgLabel2.getText());
        newMessage.receivertxt.setText(" "+userName2.getText());
        
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
    
    @FXML private void sendMsg3()
    {
        Stage stage= new Stage();
        NewMessageController newMessage = new NewMessageController(admin);
        
        newMessage.userid.setText(msgLabel3.getText());
        newMessage.receivertxt.setText(" "+userName3.getText());
        
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
    
    @FXML private void sendMsg4()
    {
        Stage stage= new Stage();
        NewMessageController newMessage = new NewMessageController(admin);
        
        newMessage.userid.setText(msgLabel4.getText());
        newMessage.receivertxt.setText(" "+userName4.getText());
        
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
   
    
    @FXML 
    private void createUserDetailPagination(int dataSize)
    {
        int tmp = dataSize%4;
        int size = 0;
        if ( tmp == 0 )
        {
            size = dataSize / 4;
        } else {
        
            size = (dataSize / 4) + 1;
        }    
        
        
        
        userDetailPagination.setPageCount(size);
        userDetailPagination.setPageFactory(this::userDetail);
    }
    
    public void fillUserDetail(ArrayList<ArrayList<String>> data)
    {
        createUserDetailPagination(data.size()-1);
        
        switch (this.usertype)
        {
            case "doctor":
                tableHeader.setText("Doctor");
                break;
            case "lab_assistant":
                tableHeader.setText("Laboratorist");
                break;
            case "pharmacist":
                tableHeader.setText("Pharmacist");
                break;
            case "cashier":
                tableHeader.setText("Cashier");
                break;
            case "receptionist":
                tableHeader.setText("Receptionist");
                break;
        }
    }
    
    
    
    @FXML
    private Button closeAccounts;
    @FXML
    private void closeViewAccounts(ActionEvent event) {
 
    Stage stage; 
    Parent root;
        if(event.getSource()== closeAccounts)
        {
            stage = (Stage) closeAccounts.getScene().getWindow();
            stage.close();
        }
    } 
    
}    
