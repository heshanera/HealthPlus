package Admin;

import Doctor.DoctorController;
import com.hms.hms_test_2.AllMessagesController;
import com.hms.hms_test_2.CurrentUserSummaryController;
import com.hms.hms_test_2.DatabaseOperator;
import com.hms.hms_test_2.LogoutController;
import com.hms.hms_test_2.SuccessIndicatorController;
import com.hms.hms_test_2.Validate;
import com.hms.hms_test_2.WarningController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.chart.*;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author heshan
 */
public class AdminController extends AnchorPane {

    /**
     * 
     */
    public Admin admin;

    /**
     *
     */
    public String username;
    
    /**
     * Constructor of the class
     * @param username username of the User
     */
    public AdminController(String username) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Admin.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        admin = new Admin(username);
        this.username = username;
        admin.saveLogin(username);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    
    @FXML
    private PieChart userPieChart;
     
    /**
     * Fill the pie chart that contains the information of the database information
     */
    
    @FXML private Label pieChartType;
    HashMap<String,String> userLog;
    
    @FXML
    public void filldatabaseStorageChart(String a) 
    {
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.clear();
        
        if (a.equals("u")) {
            
            userLog = new HashMap<String,String>();
            userLog.clear();
            userLog.put("doctor","Doctor");
            userLog.put("lab_assistant","Lab Assistant");
            userLog.put("cashier","Cashier");
            userLog.put("pharmacist","Pharmacist");
            userLog.put("receptionist","Receptionist");
            userLog.put("admin","Admin");
            
            String userTypes[] = {"doctor","lab_assistant","cashier","pharmacist","receptionist","admin"};
            for(int i = 0; i < 6; i++)
            {
                String count = admin.getSysUserCount(userTypes[i]);
                String user = userLog.get(userTypes[i]);
                pieChartData.add(new PieChart.Data(user, Integer.parseInt(count)));
            }    
            
            pieChartData.forEach(data1 ->
                    data1.nameProperty().bind(
                            Bindings.concat(
                                    data1.getName(), " (", data1.pieValueProperty(), ")"
                            )
                    )
            );

            
            //userPieChart.setLabelLineLength(10);
            userPieChart.setLegendSide(Side.BOTTOM);
            
            userPieChart.setData(pieChartData);
            pieChartType.setText("System Users");
        
        } else {
        
            int patientsUserAccounts = Integer.parseInt(admin.getPatientCount());
            int patients = Integer.parseInt(admin.getAllPatientCount());
            
            int tmpPatients = patients - patientsUserAccounts;
            
            pieChartData.add(new PieChart.Data("Website Members", patientsUserAccounts));
            pieChartData.add(new PieChart.Data("Other", tmpPatients));
            
            pieChartData.forEach(data1 ->
                    data1.nameProperty().bind(
                            Bindings.concat(
                                    data1.getName(), " (", data1.pieValueProperty(), ")"
                            )
                    )
            );
            
            userPieChart.setData(pieChartData);
            pieChartType.setText("Patients");
        }    
        
    }
    
    @FXML private ComboBox userPropotionCombo;
    
    @FXML private void showUsersChart()
    {
        String type = (String)userPropotionCombo.getSelectionModel().getSelectedItem();
        switch (type)
        {
            case "System Users":
                type = "u";
                break;
            case "Patients":
                type = "p";
                break;    
        }    
        filldatabaseStorageChart(type); 
    }        
    
    

    /*
    @FXML
    private LineChart<String, Number> linechart;
    
    /**
     * Fill the line chart about the data usage within the last 6 months
     */
    /*
    @FXML
    public void fillLineChart() {

        yAxis = new NumberAxis(0, 2000, 200);
        
        
        XYChart.Series used= new XYChart.Series();
        used.setName("Used");
        used.getData().add(new XYChart.Data("January", 400));
        used.getData().add(new XYChart.Data("February", 450));
        used.getData().add(new XYChart.Data("March", 500));
        used.getData().add(new XYChart.Data("April", 520));
        used.getData().add(new XYChart.Data("May", 550));
        used.getData().add(new XYChart.Data("June", 650));
        used.getData().add(new XYChart.Data("July", 670));
        used.getData().add(new XYChart.Data("August", 750));
      
        linechart.getData().addAll(used);
    }
    */
    /*
    @FXML
    private NumberAxis yAxis;
    @FXML
    private AreaChart<String, Number> areachart;
    
    /**
     * Area chart Fill info about the users that currently logged in
     */
    /*
    @FXML
    public void fillAreaChart() {

        XYChart.Series users= new XYChart.Series();
        users.setName("Users");
        users.getData().add(new XYChart.Data("8.00", 0));
        users.getData().add(new XYChart.Data("8.30", 3));
        users.getData().add(new XYChart.Data("9.00", 8));
        users.getData().add(new XYChart.Data("9.30", 10));
        users.getData().add(new XYChart.Data("10.00", 12));
        users.getData().add(new XYChart.Data("10.30", 12));
        users.getData().add(new XYChart.Data("11.00", 13));
        users.getData().add(new XYChart.Data("11.30", 13));
        users.getData().add(new XYChart.Data("12.00", 11));
      
        yAxis.setTickUnit(3);
        
        areachart.getData().addAll(users);

    }
    
    */
    
    @FXML
    private TabPane mainTabPane;
    
    /**
     *
     */
    @FXML
    public void loadTheme()
    {
        //System.out.println("Initial testing");
        
        Properties prop = new Properties();
	InputStream input = null;

	try 
        {
            input = new FileInputStream("hms.config");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            String theme = (String)prop.getProperty("theme");
            System.out.println(theme);
            // stylesheets="@../styles/tabbedPane.css"
            switch (theme)
            {
                case "theme1":
                    mainTabPane.getStylesheets().add("styles/tabbedPane.css");
                    break;
                case "theme2":
                    mainTabPane.getStylesheets().add("styles/tabbedPaneTheme2.css");
                    break;
                default:
                    mainTabPane.getStylesheets().add("styles/tabbedPane.css");
                    break;
            
            }
                    
	} 
        catch (IOException ex) 
        {}
        finally 
        {
            if (input != null) 
            {
                try 
                {
                        input.close();
                }
                catch(IOException e) 
                {}
            }
	}
    
    }
    
    private PopOver popOver2;
    
    @FXML
    private void showPopup(Label label, ArrayList<ArrayList<String>> data)
    {
        if (popOver2 == null) {
            popOver2 = new PopOver();
            popOver2.setArrowLocation(ArrowLocation.RIGHT_CENTER);
            
            AdminMessageController popup = new AdminMessageController();
            popup.setPaceholders();
            
            popOver2.setContentNode(popup);
            popOver2.setAutoFix(true);
            popOver2.setAutoHide(true);
            popOver2.setHideOnEscape(true);
            popOver2.setDetachable(false);
        }
        popOver2.show(label);
    }
    
    /*
    @FXML Label docMsg;
    @FXML Label labMsg;
    @FXML Label pharmacistMsg;
    @FXML Label cashierMsg;
    @FXML Label receptionistMsg;
    @FXML Label patientMsg;
    
    @FXML private void sendDocMsg()
    {
        ArrayList<ArrayList<String>> data = null;
        showPopup(docMsg, data);
    }        

    @FXML private void sendLabMsg()
    {
        ArrayList<ArrayList<String>> data = null;
        showPopup(labMsg, data);
    }        

    @FXML private void sendPharmacistMsg()
    {
        ArrayList<ArrayList<String>> data = null;
        showPopup(pharmacistMsg, data);
    }        

    @FXML private void sendCashierMsg()
    {
        ArrayList<ArrayList<String>> data = null;
        showPopup(cashierMsg, data);
    }        

    @FXML private void sendReceptionistMsg()
    {
        ArrayList<ArrayList<String>> data = null;
        showPopup(receptionistMsg, data);
    }        

    @FXML private void sendPatientMsg()
    {
        ArrayList<ArrayList<String>> data = null;
        showPopup(patientMsg, data);
    }        
    */
    
    
    @FXML ComboBox userType;
    
    /*
    @FXML private void addDoctor()
    {

        SingleSelectionModel<Tab> selectionModel = mainTabPane.getSelectionModel();  
        selectionModel.select(1);
        userType.setValue("Doctor");
    
    }
    
    @FXML private void addLab()
    {

        SingleSelectionModel<Tab> selectionModel = mainTabPane.getSelectionModel();  
        selectionModel.select(1);
        userType.setValue("Lab Assistant");
    
    } 
    
    @FXML private void addPharmacist()
    {

        SingleSelectionModel<Tab> selectionModel = mainTabPane.getSelectionModel();  
        selectionModel.select(1);
        userType.setValue("Pharmacist");
    
    } 
    
    @FXML private void addCashier()
    {

        SingleSelectionModel<Tab> selectionModel = mainTabPane.getSelectionModel();  
        selectionModel.select(1);
        userType.setValue("Cashier");
    
    } 
    
    @FXML private void addRecep()
    {

        SingleSelectionModel<Tab> selectionModel = mainTabPane.getSelectionModel();  
        selectionModel.select(1);
        userType.setValue("Receptionist");
    
    } 
    */
    
    
    @FXML private void showManual()
    {}        
    
    
    @FXML
    private void viewDoctorAccounts()
    {
        Stage stage = new Stage();
        UserAccountController userAccounts = new UserAccountController("doctor",admin);
        
        ArrayList<ArrayList<String>> data = admin.getUserInfo("doctor");
        userAccounts.fillUserDetail(data);
        
        
        Scene scene = new Scene(userAccounts);
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
    private void viewLabAccounts()
    {
        Stage stage = new Stage();
        UserAccountController userAccounts = new UserAccountController("lab_assistant",admin);
        
        ArrayList<ArrayList<String>> data = admin.getUserInfo("lab_assistant");
        userAccounts.fillUserDetail(data);
        
        Scene scene = new Scene(userAccounts);
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
    private void viewPharmacistAccounts()
    {
        Stage stage = new Stage();
        UserAccountController userAccounts = new UserAccountController("pharmacist",admin);
        ArrayList<ArrayList<String>> data = admin.getUserInfo("pharmacist");
        userAccounts.fillUserDetail(data);
        
        Scene scene = new Scene(userAccounts);
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
    private void viewCashierAccounts()
    {
        Stage stage = new Stage();
        UserAccountController userAccounts = new UserAccountController("cashier",admin);
        
        ArrayList<ArrayList<String>> data = admin.getUserInfo("cashier");
        System.out.println(data);
        userAccounts.fillUserDetail(data);
        
        Scene scene = new Scene(userAccounts);
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
    private void viewReceptionistAccounts()
    {
        Stage stage = new Stage();
        UserAccountController userAccounts = new UserAccountController("receptionist",admin);
        
        ArrayList<ArrayList<String>> data = admin.getUserInfo("receptionist");
        userAccounts.fillUserDetail(data);
        
        Scene scene = new Scene(userAccounts);
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
    private void viewPatientAccounts()
    {/*
        Stage stage = new Stage();
        UserAccountController userAccounts = new UserAccountController();
        
        //ArrayList<ArrayList<String>> data = admin.getPersonInfo();
        
        Scene scene = new Scene(userAccounts);
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
        stage.show(); */
    } 
   
    @FXML
    private void viewOnlineAccounts()
    {
        Stage stage = new Stage();
        UserAccountController userAccounts = new UserAccountController("",admin);
        
        ArrayList<ArrayList<String>> data = admin.getOnlineInfo();
        userAccounts.fillUserDetail(data);
        
        Scene scene = new Scene(userAccounts);
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
    private void showSettings() throws IOException
    {
        Stage stage = new Stage();
        SettingsController settings = new SettingsController(admin,this);
        settings.loadConfigFile();
        
        Scene scene = new Scene(settings);
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
    
    
    @FXML private void showReports()
    {
        Stage stage = new Stage();
        ReportsController reports = new ReportsController(admin);
        
        reports.fillPatientAttendence("All");
        reports.fillPieChart(12);
        reports.fillAppointmentChart("a");
        reports.fillCancelledAppointmentChart("a");
        
        
        reports.fillStockChart();
        reports.fillSupplierChart();
        
        LocalDate date = LocalDate.now();
        LocalDate date2 = date.minusMonths(12);
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        
        reports.fillTotalIncomeBarGraph(fomatter.format(date2),fomatter.format(date));
        reports.fillIcome("a",fomatter.format(date2),fomatter.format(date));
        
        Scene scene = new Scene(reports);
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
    
    //// Registering a new User /////////////////////////////////////////////////////////////////////////
    
    
    @FXML private TextField userFirstName;
    @FXML private TextField userLastName;
    @FXML private TextField userNIC;
    @FXML private TextField userMobile;
    
    @FXML private Label closeNotific;
    
    private PopOver popOver3;
    private TextField slmcRegText;
    
    @FXML
    private void getDoctorReg()
    {
        if (popOver3 == null) {
            popOver3 = new PopOver();
            popOver3.setArrowLocation(ArrowLocation.TOP_CENTER);
            
            slmcRegText = new TextField();
            slmcRegText.setPromptText("SLMC Registration no");
            
            popOver3.setContentNode(slmcRegText);
            popOver3.setAutoFix(true);
            popOver3.setAutoHide(true);
            popOver3.setHideOnEscape(true);
            popOver3.setDetachable(false);
            
        }
        popOver3.setTitle("SLMC registration");
        popOver3.show(newUser);
    }
    
    private void showPopup2(String message, TextField text)
    { 

        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
            
        }
        WarningController popup = new WarningController();
        popup.addMessage(message);

        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(text);
    }
    
    private Label validatedUser = new Label();
    
    @FXML 
    private void validateUserMobile()
    {    
        try{
            String tmpmobile = userMobile.getText();
            String result = Validate.mobile(tmpmobile);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup2(result,userMobile);
                validatedUser.setText("0");
            }
        }catch(Exception e){}     
    }   
    
    private void validateUserNIC()
    {    
        try{
            String tmpnic = userNIC.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup2("xxxxxxxxxV",userNIC);
                validatedUser.setText("0");
            }
        }catch(Exception e){}     
    }   
    
    
    @FXML
    private void closeNotifications()
    {
        error.setVisible(false);
        notification.setVisible(false);
        closeNotific.setVisible(false);
    }        
    
    
    @FXML
    private Button newUser;
    @FXML
    private Button closeUser;
    @FXML
    private Label notification;
    @FXML
    private Label error;
    
    @FXML
    private void createUser(ActionEvent event) throws IOException {
        
        boolean slmcValid = false;
        boolean fields = true;
        validatedUser.setText("1");
        error.setVisible(false);
        notification.setVisible(false);
        closeNotific.setVisible(false);
        
        String slmcReg = "";
        String type = "";
        if ( userType.getValue() != null)
        {    
            type = userType.getValue().toString();
        }
        switch (type)
        {
            case "Doctor":
                    type = "doctor";
                    if (popOver3 != null)
                    {
                        String tmpID = slmcRegText.getText();
                        getDoctorReg();
                        if (!"".equals(tmpID) )
                        {
                            slmcValid = true;
                            slmcReg = tmpID;
                            notification.setVisible(false);
                            closeNotific.setVisible(false);
                            popOver3.hide();
                        }    
                    }
                    else{
                        if ( error.isVisible() != true )
                        {    
                            notification.setVisible(true);
                            closeNotific.setVisible(true);
                        }
                        notification.setText(" Enter The Doctor SLMC Registration Number");
                        getDoctorReg();
                    }
                    
                    break;
            case "Lab Assistant":
                    type = "lab_assistant";
                    break;
            case "Pharmacist":
                    type = "pharmacist";
                    break;
            case "Cashier":
                    type = "cashier";
                    break;
            case "Receptionist":
                    type = "receptionist";
                    break;        
        }    
        
        String firstName =  userFirstName.getText();
        String lastName =  userLastName.getText();
        String nic =  userNIC.getText();
        String mobile =  userMobile.getText();
        
        ArrayList<String> result = null;

        if ( firstName.equals("") || lastName.equals("") || nic.equals("") || mobile.equals("") || type.equals("") )
        {
            fields = false;
            if ( notification.isVisible() != true ) 
            {
                error.setVisible(true);
                closeNotific.setVisible(true);
            }
            error.setText(" Please fill the mandatory fields");
            
        }
        
        if (type.equals("doctor")){
        
            if ((fields == true) & (slmcValid == true) & (validatedUser.getText().equals("1")) )
            {
                result = admin.createNewUser(firstName,lastName,type,nic,mobile,slmcReg);
            }    
        }else{         
            System.out.println(fields);
            if (fields)
            {   
                result = admin.createNewUser(firstName,lastName,type,nic,mobile,"");
            }    
        }
        
        
        if (result != null && result.size() == 3 )
        {    
                        
            Stage stage = new Stage();
            NewUserController userAccounts = new NewUserController();
            userAccounts.loadData(result.get(0),result.get(1),result.get(2));

            Scene scene = new Scene(userAccounts);
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
            
            userFirstName.setText("");
            userLastName.setText("");
            userNIC.setText("");
            userMobile.setText("");  
            
            error.setVisible(false);
            notification.setVisible(false);
            closeNotific.setVisible(false);
        }    
   }
    
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
      
    @FXML private TextField databaselbl;
    @FXML private TextField connectionlbl;
    @FXML private TextField dbUsernamelbl;
    @FXML private TextField dbDriver;
    @FXML private PasswordField dbPasswordlbl;
    
    public void loadDatabaseInfo()
    {
        databaselbl.setText(admin.database);
        connectionlbl.setText(DatabaseOperator.CONNECTION);
        dbDriver.setText(DatabaseOperator.dbClassName);
        dbUsernamelbl.setText(admin.dbUsername);
        //dbPasswordlbl.setText(admin.dbPassword); 
    }        
    
    
    @FXML
    private void checkConnection()
    {
        ArrayList<ArrayList<String>> data = admin.checkConnection();
        System.out.println(data);
        if ( data.get(1).get(0).equals(admin.username))
        {
            showSuccessIndicator();
        }    
    }    
    
    DirectoryChooser chooser = new DirectoryChooser();
    
    @FXML private Button backup;
    
    private PopOver popOver4;
    
    private void showPasswordPopup()
    { 

        if (popOver4 == null) {
            popOver4 = new PopOver();
            popOver4.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
            
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
    
    @FXML 
    private void makeBackup()
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
    
    @FXML private TextField userSearch;
    @FXML private TextField userIDlbl;      
    HashMap<String,String> log;
    
    public void loadUsers()
    {
        ArrayList<ArrayList<String>> data = admin.getUserNameAndID();
        
        log = new HashMap<String,String>();
        ArrayList<String> possibleSuggestions = new ArrayList<String>();
        
        int size = data.size();
        for(int i = 1; i < size; i++)
        {
            log.put(data.get(i).get(0)+" "+data.get(i).get(1),data.get(i).get(3)+" "+data.get(i).get(2));
            possibleSuggestions.add(data.get(i).get(3) +" " + data.get(i).get(0)+" "+data.get(i).get(1));
        }    
        
        TextFields.bindAutoCompletion(userSearch,possibleSuggestions);
    }        
    
    @FXML private void getName()
    {
        
        try{
            String data[] = userSearch.getText().split(" ");
            if (data.length == 3)
            {    
                userSearch.setText(" "+data[1]+" "+data[2]);

                userIDlbl.setText(log.get(data[1]+" "+data[2]).split(" ")[0]);
                //type.setText(log.get(data[1]+" "+data[2]).split(" ")[1]);
            }
            
        }catch(Exception e){}
        
    }
    
    
    @FXML private void searchUser()
    {
        
        String userid = userIDlbl.getText();
        
        if (!userid.equals(""))
        {    
            Stage stage= new Stage();
            SysUserController user = new SysUserController(this,userid);
            user.load();
            Scene scene = new Scene(user);
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
    
    
    @FXML private void clearUserSearch()
    {
        userSearch.setText("");
        userIDlbl.setText("");
        
        userIDlbl.setPromptText("User ID");
    }        
    
    @FXML private void clearNewUser()
    {
        
        userType.getSelectionModel().clearSelection();
        userFirstName.setText("");
        userLastName.setText("");
        userNIC.setText("");
        userMobile.setText("");
    }        
    
    
    @FXML private ListView onlineList; 
    @FXML private ListView onlineList2; 
    
    public void fillOnlineUsers()
    {
        
        ArrayList<ArrayList<String>> data = admin.getOnlineInfo();
        ArrayList<String> data2 = new ArrayList<String>();
        ArrayList<String> data3 = new ArrayList<String>();
        int size = data.size();
        for(int i = 1; i < size; i++)
        {
            data2.add(" " + data.get(i).get(0) + " " + data.get(i).get(1));
            data3.add(userLog.get(data.get(i).get(4)));
        }    
        
        onlineList2.getItems().addAll(data2);
        onlineList.getItems().addAll(data3);
        
    }        
    
    @FXML private TextField systemStorage;
    @FXML private TextField databaseStoragetxt;
    @FXML private TextField activeUserstxt;
    @FXML private TextField suspendedtxt;
    
    
    public void fillStorageInfo()
    {
        System.out.println(admin.database);
        //System.out.println(admin.getSchemaSize(admin.database));
        
        //String database = admin.getSchemaSize(admin.database).get(1).get(0);
        
        
        /*** dummy Values ***/
        String system = "0M";//admin.getDirectorySize("/home/heshan/NetBeansProjects/hms_test_2/");
        
        systemStorage.setText(system.split("M")[0] + " MB");
        databaseStoragetxt.setText("2.3 MB");
        /************/
        
        String suspended = admin.getsuspendUser();
        String active = admin.getActiveUser();
        activeUserstxt.setText(active);
        suspendedtxt.setText(suspended);
        
    }
    
    @FXML private Label doctorCount;
    @FXML private Label labCount;
    @FXML private Label pharmacistCount;
    @FXML private Label cashierCount;
    @FXML private Label receptionistCount;
    @FXML private Label patientCount;
    @FXML private Label currentUsersCount;
    
    public void fillAccountCounts()
    {
        String docAmount = admin.getSysUserCount("doctor");
        String labAmount = admin.getSysUserCount("lab_assistant");
        String pharAmount = admin.getSysUserCount("pharmacist");
        String cashAmount = admin.getSysUserCount("cashier");
        String recepAmount = admin.getSysUserCount("receptionist");
        
        String patientAmount = admin.getPatientCount();
        String onlineAmount = admin.getOnlineCount();
        
        
        doctorCount.setText(docAmount);
        labCount.setText(labAmount);
        pharmacistCount.setText(pharAmount);
        cashierCount.setText(cashAmount);
        receptionistCount.setText(recepAmount);
        
        patientCount.setText(patientAmount);
        currentUsersCount.setText(onlineAmount);
    
    
    }        
    
    
    @FXML private Label showSuspendButton;
    ListView suspendList = new ListView();
    
    @FXML private void showSuspended()
    {
        ObservableList<String> items = FXCollections.observableArrayList ();
        ArrayList<ArrayList<String>> data = admin.getSuspendedUsers();
        int size = data.size();
        
        for(int i = 1; i < size; i++)
        {
            String fname = data.get(i).get(0);
            String lname = data.get(i).get(1);
            String type = data.get(i).get(3);
            String uid = data.get(i).get(2);
            
            items.add(uid + "\t" + type + "\t" +fname + " " + lname);
        }    
        
        
        
        suspendList.setItems(items);
        
        
        if (popOver == null) 
        {
            popOver = new PopOver();
        }

        suspendList.setMaxSize(320, 100);
        suspendList.setMinSize(320, 100);
        
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
        popOver.setContentNode(suspendList);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(suspendedtxt);
        
        
    }        
    
    
    public void getSuspendedInfo()
    {
        
        AdminController admin = this;
        
        suspendList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String tmp = (String)suspendList.getSelectionModel().getSelectedItem();
                String userid = tmp.split("\\t")[0];
                
                System.out.println(tmp);
                System.out.println(userid);
                
                Stage stage= new Stage();
                SysUserController user = new SysUserController(admin,userid);
                user.load();
                Scene scene = new Scene(user);
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
        });
    }
    
    @FXML private Button showUserButton;
    
    @FXML private void showUser()
    {
        CurrentUserSummaryController user = new CurrentUserSummaryController(admin);
        user.load();
        
        if (popOver == null) 
        {
            popOver = new PopOver();
        }
        
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popOver.setContentNode(user);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(showUserButton);
    }        
    
        
    // Changing the profile pictute //
    
    @FXML ImageView profileImage;
    
    @FXML Button editProfilePicButton;
    FileChooser chooser2 = new FileChooser();
    
    Label path = new Label();
    Label name = new Label();
    
    @FXML public void  editProfilePic() throws MalformedURLException, IOException
    {
        
        if ( editProfilePicButton.getText().equals("Edit") )
        {
            ArrayList<String> types = new ArrayList<String>();
            types.add("png"); types.add("jpeg"); types.add("jpg");
            types.add("PNG"); types.add("JPEG"); types.add("JPG");
            
            Stage stage = new Stage();
            chooser.setTitle("Select Export Directory");
            File file = chooser2.showOpenDialog(stage);
            
            if (file != null)
            {
                String img = file.toURI().toURL().toExternalForm();

                if (!types.contains(file.getName().split("\\.(?=[^\\.]+$)")[1])) {



                } else {   

                    path.setText(file.getAbsolutePath()); 
                    name.setText(file.getName()); 

                    profileImage.setImage(new Image(img));
                    editProfilePicButton.setText("Save");
                }
            }    
                
        } else if ( editProfilePicButton.getText().equals("Save") ) {
            
           
            Path source = Paths.get(path.getText()); 

            System.out.println(name.getText());

            String imageName = this.username+"ProfPic."+(name.getText().split("\\.(?=[^\\.]+$)")[1]);
            OutputStream os = new FileOutputStream(new File("src/main/resources/imgs/profilePics/"+imageName));
            
            Files.copy(source, os);

            admin.setProfilePic(imageName);
            editProfilePicButton.setText("Edit");
        }
        
    }
    
    public void loadProfileImage()
    {
        Image img;
        try{
            String image = admin.getProfilePic();
            img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
        }catch(Exception e){
            img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
        }
        profileImage.setImage(img);
        
    } 
    
    ////////////////////////////////
    
    ////// Loading messages ////////
    
    @FXML private Button AllMessages;
    
    @FXML
    private void showAllMessages()
    { 

        if (popOver == null) 
        {
            popOver = new PopOver();
        }
        AllMessagesController popup = new AllMessagesController(admin);
        popup.loadMessages();
        
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(AllMessages);
        
    }
    
    ////////////////////////////////
    
    /////////////////////////////////////////////////////////////////////////////////////////////////
    
    /// Loading ther profile info ////////////////////////////////////////
  
    @FXML
    private TextField adminName;
    @FXML    
    private TextField adminNIC;
    @FXML
    private DatePicker adminDOB;
    @FXML
    private TextField adminAge;
    @FXML
    private ComboBox adminGender;
    @FXML
    private TextField adminNationality;
    @FXML
    private TextField adminReligion;
    @FXML
    private TextField adminMobile;
    @FXML
    private TextField adminEmail;   
    @FXML
    private TextField adminAddress;
    
    @FXML
    private TextField adminUserName;
    @FXML
    private TextField adminUserType;
    @FXML
    private TextField adminUserID;
    @FXML
    private TextField adminPassword;
    @FXML
    private TextField adminNewPassword;
    @FXML
    private TextField adminConfirmPassword;
            
    /**
     * Loads the profile information in the UI
     */
    @FXML
    public void loadProfileData() 
    {

        HashMap<String,String> adminPersonalInfo =  admin.getProfileInfo();
		
        adminName.setText(adminPersonalInfo.get("first_name") + " " + adminPersonalInfo.get("last_name"));
        adminNIC.setText(adminPersonalInfo.get("nic"));
        adminNationality.setText(adminPersonalInfo.get("nationality"));
        adminReligion.setText(adminPersonalInfo.get("religion"));
        adminMobile.setText(adminPersonalInfo.get("mobile"));
        adminEmail.setText(adminPersonalInfo.get("email"));
        adminAddress.setText(adminPersonalInfo.get("address"));

        try{
                SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                Date birth = tmpdataformat.parse(adminPersonalInfo.get("date_of_birth"));
                Calendar calendarBirth = Calendar.getInstance();
                calendarBirth.setTime(birth);
                Calendar calendarToday = Calendar.getInstance();
                String tmpage = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));
                
                String tmpDOB = adminPersonalInfo.get("date_of_birth");
                
                int year = Integer.parseInt(tmpDOB.substring(0,4));
                int month = Integer.parseInt(tmpDOB.substring(5,7));        
                int date = Integer.parseInt(tmpDOB.substring(8,10));        
                adminDOB.setValue(LocalDate.of(year, month, date));
                adminAge.setText(tmpage);
        }catch(Exception e){e.printStackTrace();}
        
        
        String tmpGen = adminPersonalInfo.get("gender");
        if (tmpGen.equals("m")){adminGender.getSelectionModel().select("Male");}
        else {adminGender.getSelectionModel().select("Female");}
  

        adminUserName.setText(adminPersonalInfo.get("user_name"));
        adminUserType.setText(adminPersonalInfo.get("user_type"));
        adminUserID.setText(adminPersonalInfo.get("user_id"));
       
    } 
    
    @FXML
    private Button editBasicInfoButton;
    
    @FXML 
    private void editBasicInfo()
    {
        String currentState = editBasicInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            adminName.setDisable(false);
            adminNIC.setDisable(false);
            adminGender.setDisable(false);
            adminNationality.setDisable(false);
            adminReligion.setDisable(false);
            adminMobile.setDisable(false);
            adminEmail.setDisable(false);
            adminAddress.setDisable(false);
            
            editBasicInfoButton.setText("Save");
        }
        else if ( currentState.equals("Save"))
        {
            adminName.setDisable(true);
            adminNIC.setDisable(true);
            adminGender.setDisable(true);
            adminNationality.setDisable(true);
            adminReligion.setDisable(true);
            adminMobile.setDisable(true);
            adminEmail.setDisable(true);
            adminAddress.setDisable(true);
            
            String info = "";
				
            String[] name = adminName.getText().split(" ");
            String gender = (String)adminGender.getSelectionModel().getSelectedItem();
            if (gender.equals("Male")){gender = "m";}
            else {gender = "f";}
            //String marital = receptionMaritalComboDoc.getText();
            String nationality = (String)adminNationality.getText();
            String religion = (String)adminReligion.getText();
            String mobile = adminMobile.getText();
            String email = adminEmail.getText();
            String address = adminAddress.getText();

            info += "first_name " + name[0] + "#last_name " + name[1];
            info += "#gender " + gender;
            info += "#nationality " + nationality;
            info += "#religion " + religion;
            info += "#mobile " + mobile;
            info += "#email " + email;
            info += "#address " + address;

            //System.out.println(info);

            boolean success = admin.updateProfileInfo(info);
            
            editBasicInfoButton.setText("Edit");
            //saveProgress.setProgress(0.5);  
            if (success == true) showSuccessIndicator();
            //waitFor();
            
            
            //stage.close();
            
        }    
    }
    
    @FXML
    private Button editUserInfoButton;
    
    @FXML 
    private void editUserInfo()
    {
        String currentState = editUserInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            adminUserName.setDisable(false);
            
            editUserInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            adminUserName.setDisable(true);
            
            String info = "user_name " + adminUserName.getText();
            boolean success = admin.updateAccountInfo(info);
            if (success == true) showSuccessIndicator();
            editUserInfoButton.setText("Edit");
        }
    }    
    
    @FXML
    private Button editPasswordInfoButton;
    
    @FXML 
    private void editPasswordInfo()
    {
        String currentState = editPasswordInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            adminPassword.setDisable(false);
            adminNewPassword.setDisable(false);
            adminConfirmPassword.setDisable(false);
            
            editPasswordInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            
            boolean result = false;
            //result = checkCurrentPassword(String userId, String password)
            
            if (result == true)
            {
                if ( adminNewPassword.getText().equals( adminConfirmPassword.getText() ) )
                {
                    String info = "password " + adminConfirmPassword.getText();
                    boolean success =  admin.updateAccountInfo(info);
                    
                    adminPassword.setDisable(true);
                    adminNewPassword.setDisable(true);
                    adminConfirmPassword.setDisable(true);
                    
                    
                    if (success == true) showSuccessIndicator();
                    editPasswordInfoButton.setText("Edit");
                }    
            }
            
        }
    }
    
    public void setPaceholders()
    {
        loadProfileImage();
        fillOnlineUsers();
        fillStorageInfo();
        loadUsers();
        getSuspendedInfo();
    }  
    
    
    
    
    /**
     *
     */
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
    
    @FXML
    private Button logoutButton;
    @FXML
    private void logout()
    {
        Stage stage= new Stage();
        LogoutController logout = new LogoutController(logoutButton,admin);
        Scene scene = new Scene(logout);
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
    
    private PopOver popOver;
    
    @FXML private Label docIcon;
    @FXML private Label labIcon;
    @FXML private Label pharmacistIcon;
    @FXML private Label cashierIcon;
    @FXML private Label receptionistIcon;
    @FXML private Label patientIcon;
    
    @FXML
    private void showDocPopup()
    {
        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
            
            UserOptionPopOverController popup = new UserOptionPopOverController();
            popup.fillAreaChart();
            
            popOver.setContentNode(popup);
            popOver.setAutoFix(true);
            popOver.setAutoHide(true);
            popOver.setHideOnEscape(true);
            popOver.setDetachable(false);
        }
        popOver.show(docIcon);
    }

    @FXML
    private void showLabPopup()
    {        
        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
            
            UserOptionPopOverController popup = new UserOptionPopOverController();
            popup.fillAreaChart();
            
            popOver.setContentNode(popup);
            popOver.setAutoFix(true);
            popOver.setAutoHide(true);
            popOver.setHideOnEscape(true);
            popOver.setDetachable(false);
        }
        popOver.show(labIcon);
    }

    @FXML
    private void showPharmacistPopup()
    {        
        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
            
            UserOptionPopOverController popup = new UserOptionPopOverController();
            popup.fillAreaChart();
            
            popOver.setContentNode(popup);
            popOver.setAutoFix(true);
            popOver.setAutoHide(true);
            popOver.setHideOnEscape(true);
            popOver.setDetachable(false);
        }
        popOver.show(pharmacistIcon);
    }

    @FXML
    private void showCashierPopup()
    {        
        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
            
            UserOptionPopOverController popup = new UserOptionPopOverController();
            popup.fillAreaChart();
            
            popOver.setContentNode(popup);
            popOver.setAutoFix(true);
            popOver.setAutoHide(true);
            popOver.setHideOnEscape(true);
            popOver.setDetachable(false);
        }
        popOver.show(cashierIcon);
    }

    @FXML
    private void showReceptionistPopup()
    {        
        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
            
            UserOptionPopOverController popup = new UserOptionPopOverController();
            popup.fillAreaChart();
            
            popOver.setContentNode(popup);
            popOver.setAutoFix(true);
            popOver.setAutoHide(true);
            popOver.setHideOnEscape(true);
            popOver.setDetachable(false);
        }
        popOver.show(receptionistIcon);
    }

    @FXML
    private void showPatientPopup()
    {        
        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
            
            UserOptionPopOverController popup = new UserOptionPopOverController();
            popup.fillAreaChart();
            
            popOver.setContentNode(popup);
            popOver.setAutoFix(true);
            popOver.setAutoHide(true);
            popOver.setHideOnEscape(true);
            popOver.setDetachable(false);
        }
        popOver.show(patientIcon);
    }
    
    @FXML
    private void hide()
    {
        if (popOver != null)
        {    
            popOver.hide(Duration.millis(500));
    
        }
    }    
    
     /*******************************************************************************************************
     * Validations
     *******************************************************************************************************/
    
    
    private void showPopup(String message, TextField text)
    { 

        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
            
        }
        WarningController popup = new WarningController();
        popup.addMessage(message);

        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(text);
    }
    
   
    @FXML
    private void validateEmail()
    {        
        try{
            String tmpemail = adminEmail.getText();
            String result = Validate.email(tmpemail);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,adminEmail);
            }
        }catch(Exception e){}    
    }           
            
    
    @FXML 
    private void validateMobile()
    {    
        try{
            String tmpmobile = adminMobile.getText();
            String result = Validate.mobile(tmpmobile);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,adminMobile);
            }
        }catch(Exception e){}     
    }   
    
    @FXML 
    private void validateNIC()
    {    
        try{
            String tmpnic = adminNIC.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup("xxxxxxxxxV",adminNIC);
            }
        }catch(Exception e){}     
    }   
    
    public void addFocusListener()
    {        
        userNIC.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue){}
                else
                {
                    validateUserNIC();
                }
            }
        });
        
        userMobile.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue){}
                else
                {
                    validateUserMobile();
                }
            }
        });
        
        adminNIC.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue){}
                else
                {
                    validateNIC();
                }
            }
        });
        
        adminMobile.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue){}
                else
                {
                    validateMobile();
                }
            }
        });
        
        adminEmail.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue){}
                else
                {
                    validateEmail();
                }
            }
        });
        
        
    }
    
    
}


