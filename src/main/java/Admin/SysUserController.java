package Admin;

import com.hms.hms_test_2.SuccessIndicatorController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
public class SysUserController extends AnchorPane {

    private AdminController admin;
    private String userID;
    
    public SysUserController(AdminController admin,String userID) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SysUser.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.admin = admin;
        this.userID = userID;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private Button suspendButton;
    @FXML private Button editButton;
    
    @FXML private Label userType;       
    
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField address;
    @FXML private TextField NIC;
    @FXML private DatePicker DOB;
    @FXML private TextField mobile;
    @FXML private TextField email;
    @FXML private ComboBox gender;

    @FXML private TextField username;
    @FXML private TextField userid;
    
    public void loadInfo()
    {
        ArrayList<ArrayList<String>> data = admin.admin.getSysUser(userID);
        
        String type = data.get(1).get(13);
        
        switch(type)
        {
            case "doctor":
                    type = "Doctor";
                    break;
            case "lab_assistant":
                    type = "Lab Assistant";
                    break;
            case "pharmacist":
                    type = "Pharmacist";
                    break;
            case "cashier":
                    type = "Cashier";
                    break;
            case "receptionist":
                    type = "Receptionist";
                    break;   
        }    
        
                
        userType.setText(type);
        
        firstName.setText(data.get(1).get(7));
        lastName.setText(data.get(1).get(8));
        address.setText(data.get(1).get(5));
        NIC.setText(data.get(1).get(2));
        
        String date = data.get(1).get(4);
        DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(date, fomatter1);

        DOB.setValue(birth);
        
        mobile.setText(data.get(1).get(6));
        email.setText(data.get(1).get(9));
        
        type = data.get(1).get(13);
        
        switch(type)
        {
            case "m":
                    type = "Male";
                    break;
            case "f":
                    type = "Female";
                    break;
        }
        
        gender.getSelectionModel().select(type);

        username.setText(data.get(1).get(12));
        userid.setText(userID);
        
        String suspend = data.get(1).get(15);
        if (suspend.equals("1")) suspendButton.setText("unsuspend");  
        
    }        
    
    
    @FXML private void suspend()
    {
        String suspend = suspendButton.getText();
        
        if (suspend.equals("suspend"))  {
            String userId = userid.getText();
            admin.admin.suspendUser(userId);
        
        } else {
            String userId = userid.getText();
            admin.admin.unsuspendUser(userId);
        }    
        
        admin.fillStorageInfo();
        Stage stage; 
        stage = (Stage) close.getScene().getWindow();
        stage.close();
        showSuccessIndicator();
    }        
    
    @FXML private Button resetPasswordButton;
    
    @FXML private void resetPassword()
    {
        String user = userid.getText();
        boolean result = admin.admin.resetPassword(user);
        if ( result == true ) showSuccessIndicator();
    
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
    
    public void load()
    {
        loadInfo();
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


