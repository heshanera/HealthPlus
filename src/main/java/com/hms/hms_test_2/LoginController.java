package com.hms.hms_test_2;

import Admin.AdminController;
import Cashier.CashierController;
import Doctor.DoctorController;
import LabAssistant.LabAssistantController;
import Pharmacist.PharmacistController;
import Receptionist.ReceptionistController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.PopOver;

public class LoginController extends AnchorPane {

    public LoginController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
          
    
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    
    @FXML
    private void login() throws IOException
    {
        
        String user = username.getText();
        String pass = password.getText();
       
        //System.out.println(user +" " + pass);
        
        User tmpUser = new User();
        
        
        try{
    
            String userType = tmpUser.checkUser(user, pass);
            // String userType = "lab_assistant";  
            System.out.println(userType);
            
            switch (userType)
            {
                case "doctor":
                    loadDoctor(user);
                    ((Node)(username)).getScene().getWindow().hide();
                    break;

                case "lab_assistant": 
                    loadLabAssistant(user);
                    ((Node)(username)).getScene().getWindow().hide();
                    break;

                case "pharmacist":
                    loadPharmacist(user);
                    ((Node)(username)).getScene().getWindow().hide();
                    break;

                case "cashier":
                    loadCashier(user);
                    ((Node)(username)).getScene().getWindow().hide();
                    break;
                case "receptionist":
                    loadReceptionist(user);
                    ((Node)(username)).getScene().getWindow().hide();
                    break;
                case "admin":
                    loadAdmin(user);
                    ((Node)(username)).getScene().getWindow().hide();
                    break;
                case "false":
                    showPopup("Incorrect Password", password);
                    break;    
            }    
        }catch(Exception e) {
        
            // incorect username
            showPopup("Incorrect Username", username);
            e.printStackTrace();
        
        }    
    
    }       
    
    
    public void loadDoctor(String username)
    {
        Stage stage = new Stage();
        DoctorController doctor = new DoctorController(username);
        
        doctor.fillAreaChart();
        doctor.setAppointments();
        doctor.loadProfileData(); 
        doctor.MakeAvailabilityTable();
        doctor.loadDrugList();
        doctor.loadTestList();
        doctor.setPaceholders();
        doctor.addFocusListener();
        doctor.loadNameList();
        
        stage.setScene(new Scene(doctor));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    public void loadPharmacist(String username)
    {
        Stage stage = new Stage();
        PharmacistController pharmacist = new PharmacistController(username);
        
        pharmacist.loadProfileData(); 
        pharmacist.makeStockTable();
        pharmacist.fillBarChart();
        pharmacist.fillPieChart();
        pharmacist.setPaceholders();
        pharmacist.loadNameList();
        pharmacist.addFocusListener();
        
        stage.setScene(new Scene(pharmacist));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
     
    public void loadReceptionist(String username)
    {
        Stage stage = new Stage();
        ReceptionistController receptionist = new ReceptionistController(username);
        
        receptionist.loadProfileData(); 
        receptionist.makeSummaryTable();
        receptionist.fillLineChart();
        receptionist.fillCurrentDoctors();
        //receptionist.fillConsultationAreas();
        receptionist.setPaceholders();
        stage.setScene(new Scene(receptionist));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    public void loadCashier(String username)
    {
        Stage stage = new Stage();
        CashierController cashier = new CashierController(username);
        
        cashier.loadProfileData(); 
        cashier.makeHistoryTable();
        cashier.fillLineChart();
        cashier.setPaceholders();
        cashier.loadNameList();
        cashier.addFocusListener();
        cashier.loadRefunds();
        
        stage.setScene(new Scene(cashier));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    public void loadAdmin(String username)
    {
        Stage stage = new Stage();
        AdminController admin = new AdminController(username);
        
        admin.loadProfileData(); 
        
        //admin.loadTheme();
        //admin.filldatabaseStorageChart();
        //admin.fillLineChart();
        admin.filldatabaseStorageChart("u");
        admin.addFocusListener();
        admin.loadDatabaseInfo();
        admin.fillAccountCounts();
        admin.setPaceholders();
        
        stage.setScene(new Scene(admin));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    public void loadLabAssistant(String username)
    {
        Stage stage = new Stage();
        LabAssistantController lab = new LabAssistantController(username);
        lab.loadProfileData(); 
        lab.fillPieChart();
        lab.setAppointments();
        lab.fillLabAppiontments();
        lab.addFocusListener();
        lab.setPaceholders();
        lab.fillTodayAppointments();
        
        stage.setScene(new Scene(lab));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }        
    
    PopOver popOver = new PopOver();
    
    private void showPopup(String message, TextField text)
    { 

        if (popOver == null) 
        {
            popOver = new PopOver();
        }
        ErrorController popup = new ErrorController();
        popup.addMessage(message);

        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(text);
    }
    
    
}



