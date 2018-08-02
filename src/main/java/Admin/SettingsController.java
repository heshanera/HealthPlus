package Admin;

import com.hms.hms_test_2.DatabaseOperator;
import com.hms.hms_test_2.LoginController;
import com.hms.hms_test_2.SuccessIndicatorController;
import com.hms.hms_test_2.WarningController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.PopOver;

public class SettingsController extends AnchorPane {

    
    private Admin admin;
    private AdminController adminC;
    
    public SettingsController(Admin admin,AdminController adminC) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Settings.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        this.admin = admin;
        this.adminC = adminC;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    String result = "";
    InputStream inputStream;
    
    public void loadConfigFile() throws IOException
    {
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                    prop.load(inputStream);
            } else {
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            String connection = prop.getProperty("connection");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            String database = prop.getProperty("database");
            
            databaselbl.setText(database);
            connectionlbl.setText(connection);
            dbDriver.setText(DatabaseOperator.dbClassName);
            dbUsernamelbl.setText(user);

            //result = "connection = " + connection + ", " + user + ", " + password +", "+ database;
            //System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);
        } catch (Exception e) {
                System.out.println("Exception: " + e);
        } finally {
		inputStream.close();
	}
    
    }        
    
    @FXML
    private void checkConnection()
    {
        ArrayList<ArrayList<String>> data = admin.checkConnection();
        //System.out.println(data);
        if ( data.get(1).get(0).equals(admin.username))
        {
            showSuccessIndicator();
        }    
    } 
    
    private PopOver popOver4;
    
    @FXML private TextField databaselbl;
    @FXML private TextField connectionlbl;
    @FXML private TextField dbUsernamelbl;
    @FXML private TextField dbDriver;
    @FXML private PasswordField dbPasswordlbl;
   
    private void showPasswordPopup()
    { 

        if (popOver4 == null) {
            popOver4 = new PopOver();
            popOver4.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
            
        }
        WarningController popup = new WarningController();
        popup.addMessage("Password!");

        popOver4.setContentNode(popup);
        popOver4.setAutoFix(true);
        popOver4.setAutoHide(true);
        popOver4.setHideOnEscape(true);
        popOver4.setDetachable(false);
        popOver4.show(dbPasswordlbl);
    }
    
    
    
    @FXML private Button backup;
    DirectoryChooser chooser = new DirectoryChooser();
    
    @FXML private void makeBackup()
    {
        String pass = dbPasswordlbl.getText();
        if (pass.equals(""))
        {
           showPasswordPopup();
            
        } else {
            
            try{
                String ip="127.0.0.1";
                String databaseSchema = admin.database;
                String user = admin.dbUsername;
                String path = "/home/heshan/";

                Stage stage = new Stage();
                chooser.setTitle("Select Export Directory");
                File selectedDirectory = chooser.showDialog(stage);
                path = selectedDirectory.getAbsolutePath()+"/";

                boolean result = admin.export( ip, databaseSchema, user, pass, path );
                if (result == true) 
                {    
                    showSuccessIndicator();
                    dbPasswordlbl.setText("");
                } else {

                    showPasswordPopup();
                }
            }catch(Exception e){}    
        }    
    }        
    
    @FXML private Button editDatabaseInfoButton;
    
    @FXML private void editDatabaseInfo()
    {
        if(editDatabaseInfoButton.getText().equals("Edit")) {  
            
            String pass = dbPasswordlbl.getText();
            if (pass.equals(""))
            {
               showPasswordPopup();

            } else {
            
            databaselbl.setEditable(true);
            connectionlbl.setEditable(true);
            dbUsernamelbl.setEditable(true);
            dbDriver.setEditable(true);
            
            editDatabaseInfoButton.setText("Save");
            
            }
            
        } else {
            
            try {
                
                
                Properties prop = new Properties();
                String propFileName = "config.properties";
                
                prop.setProperty("connection", connectionlbl.getText());
                prop.setProperty("user", dbUsernamelbl.getText());
                prop.setProperty("database", databaselbl.getText());    
                
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    URL url = classLoader.getResource("config.properties");
                    prop.store(new FileOutputStream(new File(url.toURI())), null);
                 } catch (Exception e) {
                     // handle exception
                 }
                
                
                /*
                String user = prop.getProperty("user");
                String password = prop.getProperty("password");
                String database = prop.getProperty("database");

                databaselbl.setText(database);
                
                dbDriver.setText(DatabaseOperator.dbClassName);
                dbUsernamelbl.setText(user);
                
                
                */    
                //result = "connection = " + connection + ", " + user + ", " + password +", "+ database;
                //System.out.println(result + "\nProgram Ran on " + time + " by user=" + user);

            } catch (Exception e) {
                    System.out.println("Exception: " + e);
            } finally {
                try{
                    inputStream.close();
                }catch(Exception e){}    
            }
            
            
            databaselbl.setEditable(false);
            connectionlbl.setEditable(false);
            dbUsernamelbl.setEditable(false);
            dbDriver.setEditable(false);
            
            editDatabaseInfoButton.setText("Edit");
            
            
            
        }
        
        
        
    }        
    
    
    @FXML private void restart()
    {   
        
        String pass = dbPasswordlbl.getText();
        if (pass.equals(""))
        {
           showPasswordPopup();

        } else {
            Stage stage2; 
            stage2 = (Stage) adminC.getScene().getWindow();
            stage2.close();

            Stage stage3; 
            stage3 = (Stage) closeAccounts.getScene().getWindow();
            stage3.close();

            Stage stage = new Stage();
            LoginController login = new LoginController();
            stage.setScene(new Scene(login));
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            //set Stage boundaries to visible bounds of the main screen
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
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
    
    @FXML
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


