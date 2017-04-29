package Cashier;

import com.hms.hms_test_2.AllMessagesController;
import com.hms.hms_test_2.CurrentUserSummaryController;
import com.hms.hms_test_2.ErrorController;
import com.hms.hms_test_2.LogoutController;
import com.hms.hms_test_2.SuccessIndicatorController;
import com.hms.hms_test_2.Validate;
import com.hms.hms_test_2.WarningController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;

public class CashierController extends AnchorPane {

    /**
     *
     */
    public Cashier cashier;

    /**
     *
     */
    public String username;
    
    /**
     *
     * @param username
     */
    public CashierController(String username) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Cashier.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        cashier = new Cashier(username);
        this.username = username;
        cashier.saveLogin(username);

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML
    private AreaChart<String,Number> lineChart;
    
    @FXML private NumberAxis yAxis ;
    
    public void fillLineChart() {
       
                          
        lineChart.getData().clear();
        lineChart.setTitle("Refunds");
        
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        
        int max1 = 0;
        int max2 = 0;
        
        try
        {
            ArrayList<ArrayList<String>> docApp = cashier.getCancelledDocAppointments();
            String date = "";

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> appointments = new ArrayList<Integer>();

            int size = docApp.size();
            for(int i = 1; i < size; i ++)
            {    
                date = docApp.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = appointments.remove(indx);
                    appointments.add(indx,(tmp+1));

                } else {

                    months.add(Month);
                    appointments.add(1);
                }    
            }
            
            //patientAttendenceCombo.setValue("All");
            
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = appointments.get(i);
                series1.getData().add(new XYChart.Data(month, no));
            }
            
            max1 = Collections.max(appointments);
            
        } catch(Exception e){}   
        
        try
        {
            ArrayList<ArrayList<String>> labApp = cashier.getCancelledLabAppointments();
            String date = "";

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> appointments = new ArrayList<Integer>();

            int size = labApp.size();
            for(int i = 1; i < size; i ++)
            {    
                date = labApp.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = appointments.remove(indx);
                    appointments.add(indx,(tmp+1));

                } else {

                    months.add(Month);
                    appointments.add(1);
                }    
            }
            
            
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = appointments.get(i);
                series2.getData().add(new XYChart.Data(month, no));
            }
            
            max2 = Collections.max(appointments);
            
        } catch(Exception e){}    
        
        
        yAxis.setAutoRanging(false);
        if (max1 > max2) {    
            yAxis.setUpperBound(max1 + 5);
            yAxis.setTickUnit((max1 + 5)/5);
        } else {
            yAxis.setUpperBound(max2 + 5);
            yAxis.setTickUnit((max2 + 5)/5);
        }
        yAxis.setLowerBound(0);
        
       
        lineChart.getData().addAll(series1,series2);
        series1.setName("Doctor Appointments");
        series2.setName("Lab Appointments");

    }    
        
        
    @FXML
    public void showRefundTable()
    {
        Stage stage= new Stage();
        RefundController refundTable = new RefundController(this);
  
        refundTable.fillRefundTable();
        
        Scene scene = new Scene(refundTable);
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
 
    @FXML private TextField patientID;
    @FXML private TextField appointmentTot;
    @FXML private TextField pharmacyTot;
    @FXML private TextField laboratoryTot;
    
    @FXML private TextField patientName;
    @FXML private TextField docID;
    @FXML private TextField docName;
    @FXML private TextField docFee;
    @FXML private TextField hospitalFee;
    
    @FXML private TextField billDate;
    @FXML private TextField patientTotal;
    @FXML private TextField serviceFees;
    @FXML private ComboBox paymentMethod;
    @FXML private TextField vat;
    @FXML private TextField billID;


    @FXML private Button clearButton;
    @FXML private Button issueButton;
    @FXML private Button searchBill;
    @FXML private ComboBox patientSearchType; 
    
    HashMap<String,String> patientLog = new HashMap<String,String>();
    
    public void loadNameList()
    {
        ObservableList<String> possibleSuggestions = FXCollections.observableArrayList();

        ArrayList<ArrayList<String>> data = cashier.getAllNames();
        //System.out.println(data);
        int size = data.size();
        for(int i = 1; i < size; i++)
        {
            String firstName = data.get(i).get(1);
            String lastName = data.get(i).get(2);
            String age = "";
            String id = data.get(i).get(0);

            try{
                SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                Date birth = tmpdataformat.parse(data.get(i).get(3));
                Calendar calendarBirth = Calendar.getInstance();
                calendarBirth.setTime(birth);
                Calendar calendarToday = Calendar.getInstance();
                age = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));

            }catch(Exception e){e.printStackTrace();}

            possibleSuggestions.add(age + " " + firstName + " " + lastName);
            patientLog.put(age + " " + firstName + " " + lastName,id);
            
            
        } 
        TextFields.bindAutoCompletion(patientID,possibleSuggestions);
    }                
    
    @FXML private void convertToID()
    {
        try{
            String[] tmp = patientID.getText().split(" ");
            if ( tmp.length == 4 )
            {
                String patientID = tmp[3];
                this.patientID.setText(patientID);
                patientSearchType.setValue("Patient ID");
            }
        }catch(Exception e){}    
        //System.out.println("Testing");
    } 
    
    public void searchPatientBill()
    {
        String searchID = patientID.getText(); 
        
        if (patientSearchType.getSelectionModel().getSelectedItem() != null )
        {    
            String selectedValue = patientSearchType.getSelectionModel().getSelectedItem().toString();

            ArrayList<ArrayList<String>> prescriptionData = new ArrayList<>();
            String searchValue = patientID.getText();
            ArrayList<ArrayList<String>> data = null;
            if (!searchValue.equals(""))
            {    
                patientSearchType.setStyle("-fx-border-color: #999 #999 #999 #999;");
                patientID.setStyle("-fx-border-color: #999 #999 #999 #999;");
                switch (selectedValue) 
                {
                    case "Patient ID":
                        data = cashier.getPatientDetails(searchID);
                        break;
                    case "Name":
                        String patientid = patientLog.get(searchID);
                        data = cashier.getPatientDetails(patientid);
                        patientSearchType.setValue("Patient ID");
                        patientID.setText(patientid);
                        break;
                    case "NIC":
                        
                        break;
                    default:
                        break;
                }
     
            }
   
            
            System.out.println(data);
            if ( data.size() > 1 )
            {    

                String app = data.get(1).get(5);
                String pha = data.get(1).get(3);
                String lab = data.get(1).get(4);
                String doc = data.get(1).get(1);
                String hos = data.get(1).get(2);

                String doctorFee="0";
                String hosFee="0";
                if ( app == null )
                {    
                    doctorFee = "200";
                    hosFee = "150";
                }

                if ( app == null ) app = "0";
                if ( pha == null ) pha = "0";
                if ( lab == null ) lab = "0";
                if ( doc == null ) doc = "0";
                if ( hos == null ) hos = "0";

                appointmentTot.setText(app);
                pharmacyTot.setText(pha);
                laboratoryTot.setText(lab);

                patientName.setText(data.get(1).get(10) + " " + data.get(1).get(11));
                docFee.setText(doctorFee);
                hospitalFee.setText(hosFee);

                docID.setText(data.get(1).get(8));

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                Date date = new Date();

                int service = (   Integer.parseInt(app) + Integer.parseInt(pha) + Integer.parseInt(lab) + 
                                Integer.parseInt(doctorFee) + Integer.parseInt(hosFee) );

                int vatamnt = (service/100) * 5;

                billDate.setText(dateFormat.format(date));
                patientTotal.setText(Integer.toString(service + vatamnt));
                serviceFees.setText(Integer.toString(service));
                vat.setText(Integer.toString(vatamnt));

                data = cashier.getDoctorName(searchID);
                System.out.println(data);
                
                try{
                    docName.setText(data.get(1).get(0) + " " + data.get(1).get(1));
                }catch(Exception e){}
                    
            } else {
                // SHow error message
                
                showErrorPopup("Not Available", patientID);
            }    
                
        } else {
            showPopup("hmsxxxxpa",patientID);
        }    
    
    }     
    
    public void clearBill()
    {
        patientID.setText("");
        
        appointmentTot.setText("");
        pharmacyTot.setText("");
        laboratoryTot.setText("");
        
        patientName.setText("");
        docFee.setText("");
        hospitalFee.setText("");
        billDate.setText("");
        patientTotal.setText("");
        serviceFees.setText("");
        vat.setText("");
        docName.setText("");
        docID.setText("");
        
        paymentMethod.setValue("");
    
    }
    
    public void issueBill()
    {
        String searchID = patientID.getText();
        String appFee = appointmentTot.getText();
        String pharFee = pharmacyTot.getText();
        String labFee = laboratoryTot.getText();
        
        String doctorFee = docFee.getText();
        String hosFee = hospitalFee.getText();
        String tot = patientTotal.getText();
        String vatVal = vat.getText();
        String doctorID = docID.getText();
        
        String paymeth = (String)paymentMethod.getValue();
        
        String billInfo = "payment_method "+paymeth+",consultant_id "+doctorID+",patient_id "
                        + searchID+",doctor_fee "+doctorFee+",hospital_fee "+hosFee+",pharmacy_fee "+pharFee+","
                        + "laboratory_fee "+labFee+",appointment_fee "+appFee+",vat "+vatVal+",total "+tot;
        
        String billID = cashier.bill(billInfo);

        boolean result = cashier.removeFromTempBill(searchID);
        
    
        String name = patientName.getText();
        //String bill = docName.getText();
        String serviceFee = serviceFees.getText();
        String date = billDate.getText();
    
        Stage stage = new Stage();
        BillPreviewController billPreview = new BillPreviewController();
        
        billPreview.fillBillPreview(name,serviceFee,vatVal,tot,date,billID);
        
        Scene scene = new Scene(billPreview);
        stage.setScene( scene );
        
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
        
        // Clearing the TExt Fields ////////////////////////////////////////////////////////
        
        patientID.setText("");
        
        appointmentTot.setText("");
        pharmacyTot.setText("");
        laboratoryTot.setText("");
        
        patientName.setText("");
        docFee.setText("");
        hospitalFee.setText("");
        billDate.setText("");
        patientTotal.setText("");
        serviceFees.setText("");
        vat.setText("");
        docName.setText("");
        docID.setText("");
        
        paymentMethod.setValue("");
        
        ////////////////////////////////////////////////////////////////////////////////////
    
        makeHistoryTable();         // reloading the bill history /////////////////////////
    }        
    
    @FXML private Button refundDetails;
    public void loadRefunds()
    {
        String value = cashier.getNoOfRefunds();
        refundDetails.setText(value);
    }        
    
    
    public void fillPaymentHistory()
    {
        ArrayList<ArrayList<String>> data = cashier.getPaymentHistory(100);
    }    

    @FXML 
    private TableView billHistory;
    
    @FXML
    private void getSelectedRow()
    {        
        Bill bill = (Bill)billHistory.getSelectionModel().getSelectedItem();
        //System.out.println(bill.getPatientID());
        
        int serviceFees = Integer.parseInt(bill.getDoctor())+Integer.parseInt(bill.getHospital())+
                          Integer.parseInt(bill.getAppointment())+Integer.parseInt(bill.getLaboratory())+
                          Integer.parseInt(bill.getPharmacy());
        
        
        
        String name = bill.getPatientID();
        String serviceFee = Integer.toString(serviceFees);
        String vatVal = Integer.toString((serviceFees*5)/100);
        String tot = bill.getBill();
        String date = bill.getDate();
        String billID = bill.getBillID();
        
        
        Stage stage = new Stage();
        BillPreviewController billPreview = new BillPreviewController();
        
        billPreview.fillBillPreview(name,serviceFee,vatVal,tot,date,billID);
        
        Scene scene = new Scene(billPreview);
        stage.setScene( scene );
        
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
    private Node createPage(int pageIndex) {

        
        ArrayList<ArrayList<String>> billHistoryData = cashier.getPaymentHistory(150);
	int noOfSlots = (billHistoryData.size()-1);
        //System.out.println(noOfSlots);
        System.out.println(billHistoryData);
            
        final ObservableList<Bill> data = FXCollections.observableArrayList(); 
        for (int i = 1; i <= noOfSlots; i++)
        {
            String date = billHistoryData.get(i).get(1).substring(0,10);
            
            Bill tmpBill = new Bill(billHistoryData.get(i).get(0), date, 
                                billHistoryData.get(i).get(2), billHistoryData.get(i).get(3),
                                billHistoryData.get(i).get(4),billHistoryData.get(i).get(5),
                                billHistoryData.get(i).get(6),billHistoryData.get(i).get(7),
                                billHistoryData.get(i).get(8));
            
            data.add(tmpBill);
            //System.out.println(tmpBill.getLaboratory());
            
        }        
        
        int fromIndex = pageIndex * 7;
        int toIndex = Math.min(fromIndex + 7, data.size());
        billHistory.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(billHistory);
    }
    
    @FXML
    private Pagination billHistoryPagination;
    
    @FXML 
    private void createPagination(int dataSize)
    {
        billHistoryPagination.setPageCount((dataSize / 7 + 1));
        billHistoryPagination.setPageFactory(this::createPage);
    }
    
    @FXML
    public void makeHistoryTable()
    {
        ArrayList<ArrayList<String>> billHistoryData = cashier.getPaymentHistory(150);
        createPagination(billHistoryData.size()-1);
    }
    
    
    
    
    // Changing the profile pictute //////////////////////////////////////////////////
    
    @FXML ImageView profileImage;
    
    @FXML Button editProfilePicButton;
    FileChooser chooser = new FileChooser();
    
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
            File file = chooser.showOpenDialog(stage);
            
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

            cashier.setProfilePic(imageName);
            editProfilePicButton.setText("Edit");
        }
        
    }
    
    @FXML private Button showUserButton;
    
    @FXML private void showUser()
    {
        CurrentUserSummaryController user = new CurrentUserSummaryController(cashier);
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
    
    public void loadProfileImage()
    {
        Image img;
        try{
            String image = cashier.getProfilePic();
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
        AllMessagesController popup = new AllMessagesController(cashier);
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
    
    
    /// Loading ther profile info ////////////////////////////////////////
  
    @FXML
    private TextField cashierName;
    @FXML    
    private TextField cashierNIC;
    @FXML
    private DatePicker cashierDOB;
    @FXML
    private TextField cashierAge;
    @FXML
    private ComboBox cashierGender;
    @FXML
    private TextField cashierNationality;
    @FXML
    private TextField cashierReligion;
    @FXML
    private TextField cashierMobile;
    @FXML
    private TextField cashierEmail;   
    @FXML
    private TextField cashierAddress;
    
    @FXML
    private TextField cashierUserName;
    @FXML
    private TextField cashierUserType;
    @FXML
    private TextField cashierUserID;
    @FXML
    private TextField cashierPassword;
    @FXML
    private TextField cashierNewPassword;
    @FXML
    private TextField cashierConfirmPassword;
            
    @FXML
    public void loadProfileData() 
    {

        HashMap<String,String> cashierPersonalInfo =  cashier.getProfileInfo();
		
        cashierName.setText(cashierPersonalInfo.get("first_name") + " " + cashierPersonalInfo.get("last_name"));
        cashierNIC.setText(cashierPersonalInfo.get("nic"));
        cashierNationality.setText(cashierPersonalInfo.get("nationality"));
        cashierReligion.setText(cashierPersonalInfo.get("religion"));
        cashierMobile.setText(cashierPersonalInfo.get("mobile"));
        cashierEmail.setText(cashierPersonalInfo.get("email"));
        cashierAddress.setText(cashierPersonalInfo.get("address"));

        try{
                SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                Date birth = tmpdataformat.parse(cashierPersonalInfo.get("date_of_birth"));
                Calendar calendarBirth = Calendar.getInstance();
                calendarBirth.setTime(birth);
                Calendar calendarToday = Calendar.getInstance();
                String tmpage = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));
                
                String tmpDOB = cashierPersonalInfo.get("date_of_birth");
                
                int year = Integer.parseInt(tmpDOB.substring(0,4));
                int month = Integer.parseInt(tmpDOB.substring(5,7));        
                int date = Integer.parseInt(tmpDOB.substring(8,10));        
                cashierDOB.setValue(LocalDate.of(year, month, date));
                cashierAge.setText(tmpage);
        }catch(Exception e){e.printStackTrace();}
        
        try{
            String tmpGen = cashierPersonalInfo.get("gender");
            if (tmpGen.equals("m")){cashierGender.getSelectionModel().select("Male");}
            else {cashierGender.getSelectionModel().select("Female");}
        }catch(Exception e) {}

        cashierUserName.setText(cashierPersonalInfo.get("user_name"));
        cashierUserType.setText(cashierPersonalInfo.get("user_type"));
        cashierUserID.setText(cashierPersonalInfo.get("user_id"));
       
    } 
    
    @FXML
    private Button editBasicInfoButton;
    
    @FXML 
    private void editBasicInfo()
    {
        String currentState = editBasicInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            cashierName.setDisable(false);
            cashierNIC.setDisable(false);
            cashierGender.setDisable(false);
            cashierNationality.setDisable(false);
            cashierReligion.setDisable(false);
            cashierMobile.setDisable(false);
            cashierEmail.setDisable(false);
            cashierAddress.setDisable(false);
            cashierDOB.setDisable(false);
            
            editBasicInfoButton.setText("Save");
        }
        else if ( currentState.equals("Save"))
        {
            cashierName.setDisable(true);
            cashierNIC.setDisable(true);
            cashierGender.setDisable(true);
            cashierNationality.setDisable(true);
            cashierReligion.setDisable(true);
            cashierMobile.setDisable(true);
            cashierEmail.setDisable(true);
            cashierAddress.setDisable(true);
            cashierDOB.setDisable(true);
            
            String info = "";
				
            String[] name = cashierName.getText().split(" ");
            String gender = (String)cashierGender.getSelectionModel().getSelectedItem();
            if (gender.equals("Male")){gender = "m";}
            else {gender = "f";}
            //String marital = receptionMaritalComboDoc.getText();
            String nationality = (String)cashierNationality.getText();
            String religion = (String)cashierReligion.getText();
            String mobile = cashierMobile.getText();
            String email = cashierEmail.getText();
            String address = cashierAddress.getText();

            info += "first_name " + name[0] + "#last_name " + name[1];
            info += "#gender " + gender;
            info += "#nationality " + nationality;
            info += "#religion " + religion;
            info += "#mobile " + mobile;
            info += "#email " + email;
            info += "#address " + address;

            //System.out.println(info);

            boolean success = cashier.updateProfileInfo(info);
            
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
            cashierUserName.setDisable(false);
            
            editUserInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            cashierUserName.setDisable(true);
            
            String info = "user_name " + cashierUserName.getText();
            boolean success = cashier.updateAccountInfo(info);
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
            cashierPassword.setDisable(false);
            cashierNewPassword.setDisable(false);
            cashierConfirmPassword.setDisable(false);
            
            editPasswordInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            
            boolean result = false;
            //result = checkCurrentPassword(String userId, String password)
            
            if (result == true)
            {
                if ( cashierNewPassword.getText().equals( cashierConfirmPassword.getText() ) )
                {
                    String info = "password " + cashierConfirmPassword.getText();
                    boolean success =  cashier.updateAccountInfo(info);
                    
                    cashierPassword.setDisable(true);
                    cashierNewPassword.setDisable(true);
                    cashierConfirmPassword.setDisable(true);
                    
                    
                    if (success == true) showSuccessIndicator();
                    editPasswordInfoButton.setText("Edit");
                }    
            }
            
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
    
    @FXML
    private Button logoutButton;
    @FXML
    private void logout()
    {
        Stage stage= new Stage();
        LogoutController logout = new LogoutController(logoutButton,cashier);
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
    
    
    public void setPaceholders()
    {
        patientSearchType.setValue("Patient ID");
        patientID.setPromptText("search value");
        loadProfileImage();
    }        
            
    /*******************************************************************************************************
     * Validations
     *******************************************************************************************************/
    
    private PopOver popOver;
    
    private void showPopup(String message, TextField text)
    { 

        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
            
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
    private void validatePatientID()
    {
        String tmpID = patientID.getText();
        if ( tmpID.length() == 9 )
        {
            String result = Validate.patientID(tmpID);
            if (result.equals("1"))
            {
                if (popOver != null) popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,patientID);
            }   
        }
        else if ( tmpID.length() > 9 )
        { 
            showPopup("hmsxxxxpa",patientID);
        }
    }   
    
    @FXML
    private void validateEmail()
    {        
        try{
            String tmpemail = cashierEmail.getText();
            String result = Validate.email(tmpemail);
            if (result.equals("1"))
            {
                if (popOver != null) popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,cashierEmail);
            }
        }catch(Exception e){}    
    }           
            
    
    @FXML 
    private void validateMobile()
    {    
        try{
            String tmpmobile = cashierMobile.getText();
            String result = Validate.mobile(tmpmobile);
            if (result.equals("1"))
            {
                if (popOver != null) popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,cashierMobile);
            }
        }catch(Exception e){}     
    }   
    
    @FXML 
    private void validateNIC()
    {    
        try{
            String tmpnic = cashierNIC.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                if (popOver != null) popOver.hide(Duration.millis(500));

            } else {
                showPopup("xxxxxxxxxV",cashierNIC);
            }
        }catch(Exception e){}     
    }   
    
    @FXML 
    private void validatePatientNIC()
    {    
        try{
            String tmpnic = patientID.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                if (popOver != null) popOver.hide(Duration.millis(500));

            } else {
                showPopup("xxxxxxxxxV",patientID);
            }
        }catch(Exception e){}     
    }   
    
    public void addFocusListener()
    {        
        cashierNIC.focusedProperty().addListener(new ChangeListener<Boolean>()
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
        
        cashierMobile.focusedProperty().addListener(new ChangeListener<Boolean>()
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
        
        cashierEmail.focusedProperty().addListener(new ChangeListener<Boolean>()
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
        
        patientID.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if ( patientSearchType.getValue().toString().equals("Patient ID") )
                {    
                    if (newPropertyValue){} else { validatePatientID(); }
                    
                } else if ( patientSearchType.getValue().toString().equals("NIC") )
                {    
                    if (newPropertyValue){} else { validatePatientNIC(); }
                    
                } else if ( patientSearchType.getValue().toString().equals("Name") )
                {    
                    if (newPropertyValue){} else { }
                }
                
            }
        });
        
    } 
    
    private void showErrorPopup(String message, TextField text)
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


