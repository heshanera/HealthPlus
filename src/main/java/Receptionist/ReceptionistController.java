package Receptionist;

import com.hms.hms_test_2.LogoutController;
import com.hms.hms_test_2.SuccessIndicatorController;
import com.hms.hms_test_2.Validate;
import com.hms.hms_test_2.WarningController;
import com.hms.hms_test_2.AllMessagesController;
import com.hms.hms_test_2.CurrentUserSummaryController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;


public class ReceptionistController extends AnchorPane {

    /**
     *
     */
    public Receptionist receptionist;
    public String username;
    
    /**
     *
     * @param username
     */
    public ReceptionistController(String username) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Receptionist.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        receptionist = new Receptionist(username);
        this.username = username;
        receptionist.saveLogin(username);

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    
    @FXML private LineChart<String,Number> lineChart;
    
    @FXML private NumberAxis yAxis ;
    
    public void fillLineChart() {
       
        lineChart.getData().clear();
        
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        
        /*
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(24);
        xAxis.setTickUnit(3);
        */
        int max1 = 0;
        int max2 = 0;
        
        try
        {
            ArrayList<ArrayList<String>> docApp = receptionist.getDocAppointments();
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
            
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = appointments.get(i);
                series1.getData().add(new XYChart.Data(month, no));
            }
            
            max1 = Collections.max(appointments);
            
        }catch(Exception e){}    
        
        try
        {
            ArrayList<ArrayList<String>> labApp = receptionist.getLabAppointments();
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
            yAxis.setUpperBound(max1 + 2);
            yAxis.setTickUnit((max1 + 5)/5);
        } else {
            yAxis.setUpperBound(max2 + 2);
            yAxis.setTickUnit((max2 + 5)/5);
        }
        yAxis.setLowerBound(0);
        
        lineChart.getData().addAll(series1,series2);
        series1.setName("Doctor Appointments");
        series2.setName("Lab Appointments");
    }    

   

   

    
    @FXML 
    private TableView doctorSummary;
    
    @FXML
    private Node createPage(int pageIndex) {

        
        ArrayList<ArrayList<String>> doctorData = receptionist.getDoctorSummary();
        System.out.println(doctorData);
	int noOfSlots = (doctorData.size()-1);
            
        final ObservableList<DoctorDetail> data = FXCollections.observableArrayList(); 
        for (int i = 1; i <= noOfSlots; i++)
        {
            String name =  doctorData.get(i).get(2)+" " + doctorData.get(i).get(3);
            //System.out.println(name);
            String noOfDays;
            if (Integer.parseInt(doctorData.get(i).get(4)) == 1 )
                noOfDays = doctorData.get(i).get(4) + " day";
            else
                noOfDays = doctorData.get(i).get(4) + " days";
            String days = doctorData.get(i).get(5);
            data.add(new DoctorDetail(doctorData.get(i).get(0), name, doctorData.get(i).get(1), noOfDays,days));
        }        
        
        int fromIndex = pageIndex * 7;
        int toIndex = Math.min(fromIndex + 7, data.size());
        doctorSummary.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(doctorSummary);
    }
    
    @FXML
    private Pagination doctorSummaryPagination;
    
    @FXML 
    private void createPagination(int dataSize)
    {
        doctorSummaryPagination.setPageCount((dataSize / 7 + 1));
        doctorSummaryPagination.setPageFactory(this::createPage);
    }
    
    @FXML
    public void makeSummaryTable()
    {
        ArrayList<ArrayList<String>> billHistoryData = receptionist.getDoctorSummary();
        createPagination(billHistoryData.size()-1);
    }

    
    @FXML private ListView currentlyAvailableList;
    
    public void fillCurrentDoctors()
    {
        
        ObservableList<String> items = FXCollections.observableArrayList();
        
        ArrayList<ArrayList<String>> data = receptionist.getCurrentlyAvailableDoctors();
        int size = data.size();
        for(int i = 1; i < size; i++)
        {
            String name = data.get(i).get(0) + " " + data.get(i).get(1);  
            items.add(name);
        }    
        currentlyAvailableList.setItems(items);
    }        
    
    @FXML private TextField patientFirstName;
    @FXML private TextField patientLastName;
    @FXML private ComboBox patientGender;
    @FXML private TextField patientNIC;
    @FXML private DatePicker patientDOB;
    @FXML private TextField patientMobile;
    @FXML private TextField patientEmail;
    @FXML private TextField patientAddress;
    
    @FXML private Button patientSearchButton;
    @FXML private ComboBox patientSearchCombo;
    @FXML private TextField patientSearchBox;
    
    @FXML private Button patientAddButton;
    
    @FXML private void patientSearch()
    {
        if (patientSearchCombo.getSelectionModel().getSelectedItem() != null )
        {    
            String selectedValue = patientSearchCombo.getSelectionModel().getSelectedItem().toString();
            String searchValue = patientSearchBox.getText();
            
            if (selectedValue.equals("Patient ID"))
            {
                String result = Validate.patientID(searchValue);
                System.out.println(result);
                if (result.equals("1"))
                {
                    ArrayList<ArrayList<String>> data = receptionist.getPatientInfo(searchValue);
                    System.out.println(data);
                    patientFirstName.setText(data.get(1).get(7));
                    patientLastName.setText(data.get(1).get(8));

                    String gender = data.get(1).get(3);
                    if (gender.equals("m")){gender = "Male";}
                    else {gender = "Female";}

                    patientGender.setValue(gender);

                    patientNIC.setText(data.get(1).get(2));

                    patientMobile.setText(data.get(1).get(6));
                    patientEmail.setText(data.get(1).get(9));
                    
                    String tmp[] = data.get(1).get(5).split("\\|");
                    String ad = "";
                    for(int i = 0; i < tmp.length; i++)
                    {
                        ad += ( " " + tmp[i]); 
                    }    
                    patientAddress.setText(ad);

                    String tmpDOB = data.get(1).get(4);
                    System.out.println(tmpDOB);
                    int year = Integer.parseInt(tmpDOB.substring(0,4));
                    int month = Integer.parseInt(tmpDOB.substring(5,7));        
                    int date = Integer.parseInt(tmpDOB.substring(8,10));        
                    patientDOB.setValue(LocalDate.of(year, month, date));
                    
                    patientAddButton.setText("Update");
                    
                    patientIDText.setText(patientSearchBox.getText());
                    
                }
                else{ showPopup("hmsxxxxpa",patientSearchBox); }
                
            } else if (selectedValue.equals("Name")) {
            
                String patientid = patientLog.get(searchValue);
                
                String result = Validate.patientID(patientid);
                System.out.println(result);
                if (result.equals("1"))
                {
                    ArrayList<ArrayList<String>> data = receptionist.getPatientInfo(patientid);
                    System.out.println(data);
                    patientFirstName.setText(data.get(1).get(7));
                    patientLastName.setText(data.get(1).get(8));

                    String gender = data.get(1).get(3);
                    if (gender.equals("m")){gender = "Male";}
                    else {gender = "Female";}

                    patientGender.setValue(gender);

                    patientNIC.setText(data.get(1).get(2));

                    patientMobile.setText(data.get(1).get(6));
                    patientEmail.setText(data.get(1).get(9));
                    
                    String tmp[] = data.get(1).get(5).split("\\|");
                    String ad = "";
                    for(int i = 0; i < tmp.length; i++)
                    {
                        ad += ( " " + tmp[i]); 
                    }    
                    patientAddress.setText(ad);

                    String tmpDOB = data.get(1).get(4);
                    System.out.println(tmpDOB);
                    int year = Integer.parseInt(tmpDOB.substring(0,4));
                    int month = Integer.parseInt(tmpDOB.substring(5,7));        
                    int date = Integer.parseInt(tmpDOB.substring(8,10));        
                    patientDOB.setValue(LocalDate.of(year, month, date));
                    
                    patientAddButton.setText("Update");
                    patientSearchBox.setText(patientid);
                    patientSearchCombo.setValue("Patient ID");
                    patientIDText.setText(patientSearchBox.getText());
                    
                    
                    
                }
                else{ showPopup("hmsxxxxpa",patientSearchBox); }
                
                
            }
            
            
            
        }
        else{ showPopup("hmsxxxxpa",patientSearchBox); }
    }        
    
    private PopOver popOver;
    
    private void showPopup( ObservableList<String> items)
    { 

        if (popOver == null) {
            popOver = new PopOver();
            
            
        }
        Popover2Controller popup = new Popover2Controller();
        popup.fillDaysList(items);

        popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(doctorSummary);
    } 
    
    @FXML private void showDays()
    {
        DoctorDetail detail = (DoctorDetail)doctorSummary.getSelectionModel().getSelectedItem();
        int index = doctorSummary.getSelectionModel().selectedIndexProperty().get();
        //System.out.println(bill.getPatientID());
        
        String doctorID = detail.getDoctorID();
        
        HashMap<String,String> weekDays = new HashMap<>();
        weekDays.put("1","Monday");
        weekDays.put("2","Tuesday");
        weekDays.put("3","Wednesday");
        weekDays.put("4","Thursday");
        weekDays.put("5","Friday");
        weekDays.put("6","Saturday");
        weekDays.put("7","Sunday");
        
        ArrayList<String> data = receptionist.getAvailableDays(doctorID);
        int size = data.size();
        ObservableList<String> items = FXCollections.observableArrayList();
        for(int i = 0; i < size; i++)
        {           
            items.add(weekDays.get(data.get(i)));
        }    
        
        System.out.println(data);
        showPopup(items);
        /*
            Popover2Controller popup = new Popover2Controller();
            popup.fillDaysList(items);
            
            Stage stage = new Stage();
            
            Point2D point = doctorSummary.localToScene(0.0, 0.0);
            
            int vertical = (index)*30 + 50;
            
            stage.setX(point.getX()+700);
            stage.setY(point.getY()+vertical);
            
            stage.setScene(new Scene(popup));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        */
    }        
    
    @FXML Button newPatient;
    @FXML Button reduceQueButton;
    @FXML Label queLabel;
    
    @FXML private void addNewPatientToQue()
    {
        String queue = queLabel.getText();
        queLabel.setText(Integer.toString(Integer.parseInt(queue)+1));
    }        
    
    @FXML private void reduceQue()
    {
        String queue = queLabel.getText();
        int queue2 = Integer.parseInt(queue);
        if (queue2 > 0) queue2--;
        queLabel.setText(Integer.toString(queue2));
    }        
    
    @FXML public void patientAdd()
    {
        boolean valid = true;
        
        if ( patientFirstName.getText().equals("") ||  patientDOB.getValue() == null || patientGender.getValue() == null)
        {
            valid = false;
            
            
            
        } else {
        
            if (!patientNIC.getText().equals(""))
            {
                String tmpnic = receptionistNIC.getText();
                ArrayList<String> result = Validate.NIC(tmpnic);
                if (result.size() == 0)
                {
                    valid = false;
                }
            }
            
            if (!patientMobile.getText().equals(""))
            {
                String tmpmobile = patientMobile.getText();
                String result = Validate.mobile(tmpmobile);
                if (!result.equals("1"))
                {
                    valid = false;
                } 
            }
            
            if (!patientEmail.getText().equals(""))
            {
                String tmpemail = patientEmail.getText();
                String result = Validate.email(tmpemail);
                if (!result.equals("1"))
                {
                    valid = false;
                } 
            }
            
        
        } 
        
        try{
        
            String fname = patientFirstName.getText().replaceAll("\\s+$", "");
            String lname = patientLastName.getText().replaceAll("\\s+$", "");

            String gender = "";
            String tmpGender = (String)patientGender.getSelectionModel().getSelectedItem();
            if (tmpGender.equals("Male")){gender = "m";}
            else {gender = "f";}

            String nic = patientNIC.getText().replaceAll("\\s+$", "");

            LocalDate dob = patientDOB.getValue();

            String day = Integer.toString(dob.getDayOfMonth());
            if ( day.length() < 2 ) day = "0"+day; 

            String month = Integer.toString(dob.getMonthValue());
            if ( month.length() < 2 ) month = "0"+month;

            String dateOfBirth = Integer.toString(dob.getYear())+month+day;

            String mobile = patientMobile.getText().replaceAll("\\s+$", "");
            String email = patientEmail.getText().replaceAll("\\s+$", "");

            String address = patientAddress.getText().replaceAll("\\s+$", "");
            String[] tmpAddress = address.split(" ");
            int size = tmpAddress.length;
            address = "";
            for (int i = 0; i < size; i++)
            {
                address += tmpAddress[i];
                if ( (i+1) < size ) address += "|";
            }  
            
            
            
            if (valid == true)
            {    
                String op = patientAddButton.getText();
                
                if (!op.equals("Update"))
                {    
                
                    String patientInfo = "";
                    patientInfo += "nic " + nic;
                    patientInfo += ",gender " + gender;
                    patientInfo += ",date_of_birth " + dateOfBirth;
                    patientInfo += ",address " + address;
                    patientInfo += ",mobile " + mobile;
                    patientInfo += ",first_name " + fname;
                    patientInfo += ",last_name " + lname;
                    patientInfo += ",email " + email;
                    patientInfo += ",nationality " + "NULL";
                    patientInfo += ",religion " + "NULL";

                    //System.out.println(patientInfo);
                    String result = receptionist.setPatientInfo(patientInfo);
                    if (!result.equals("false")) 
                        showPatientAccountSuccessIndicator(result, (fname + " " + lname), mobile, tmpGender);
                    
                } else {
                
                    
                    address = address.replaceAll("\\s+$", "");
                    
                    String patientInfo = "";
                    patientInfo += "nic = '" + nic;
                    patientInfo += "',gender = '" + gender;
                    patientInfo += "',date_of_birth = '" + dateOfBirth;
                    patientInfo += "',address = '" + address;
                    patientInfo += "',mobile = '" + mobile;
                    patientInfo += "',first_name = '" + fname;
                    patientInfo += "',last_name = '" + lname;
                    patientInfo += "',email = '" + email;
                    patientInfo += "',nationality = " + "NULL";
                    patientInfo += ",religion = " + "NULL";

                    //System.out.println(patientInfo);
                    boolean result = receptionist.updatePatientInfo(patientSearchBox.getText(),patientInfo);
                    if (result == true) 
                        showSuccessIndicator();
                
                }    
                    
            } else {}
            
            
            
            
        } catch (Exception e) {}    
       
    }
    
    
    HashMap<String,String> patientLog = new HashMap<String,String>();
    
    public void loadNameList()
    {
        ObservableList<String> possibleSuggestions = FXCollections.observableArrayList();

        ArrayList<ArrayList<String>> data = receptionist.getAllNames();
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
        TextFields.bindAutoCompletion(patientSearchBox,possibleSuggestions);
    }        
    
    @FXML private void patientClear()
    {
        patientFirstName.setText("");
        patientLastName.setText("");
        patientGender.setValue("");
        patientNIC.setText("");
        patientDOB.setValue(null);
        patientMobile.setText("");
        patientEmail.setText("");
        patientAddress.setText("");
        
        patientAddButton.setText("Add Patient");
    
    }
    
    @FXML private ComboBox consultationAreaCombo;
    @FXML private ComboBox doctorCombo;
    @FXML private ComboBox dayCombo;
    @FXML private ComboBox timeCombo;
    @FXML private ComboBox appointmentType;
    
    @FXML private TextField patientIDText;
    @FXML private Label appType;
    
    @FXML private void selectAppointmentType()
    {
        if ( patientIDText.getText().equals(""))
        {    
            patientIDText.setDisable(false);
            String tmpType = (String)appointmentType.getValue();
            appType.setText(tmpType);
        } else {
            
            patientIDText.setDisable(false);
            String tmpType = (String)appointmentType.getValue();
            appType.setText(tmpType);
            consultationAreaCombo.setDisable(false);
            consultationAreaCombo.getItems().clear();
            this.fillConsultationAreas();
            
        }    
    }        
    
    
    @FXML private void patientIDTextFilled()
    {
        /*
        appointmentType.setDisable(true);
        consultationAreaCombo.setDisable(false);
        consultationAreaCombo.getItems().clear();
        this.fillConsultationAreas();
        */
    }        
    
    public void fillConsultationAreas()
    {
        String tmpType = (String)appointmentType.getValue();
        consultationAreaCombo.getItems().clear();
        if (tmpType.equals("Doctor"))
        {
            ArrayList<String> data = receptionist.getConsultationAreas();   
            //System.out.println(data);
            consultationAreaCombo.getItems().addAll(data);
            
        } else {
        
            ArrayList<ArrayList<String>> data = receptionist.getLabTestInfo();
            int tmpSize = data.size();
            ArrayList<String> tmpData = new ArrayList<String>();
            for (int i = 1; i < tmpSize; i++)
            {
                tmpData.add(data.get(i).get(1));
            }    
            consultationAreaCombo.getItems().addAll(tmpData);
        }   
        
    }        
    
    @FXML
    public void selectDoctors()
    {
        String tmpType = (String)appointmentType.getValue();
        doctorCombo.getItems().clear();
        if (tmpType.equals("Doctor"))
        {
            String consultationArea = (String)consultationAreaCombo.getValue();
            ArrayList<ArrayList<String>> data = receptionist.getDoctor(consultationArea);
            int size = data.size();
            ObservableList<String> items = FXCollections.observableArrayList();
            for(int i = 1; i < size; i++)
            {
                String name = data.get(i).get(1) + " " + data.get(i).get(2);
                String reg = data.get(i).get(0);
                String tmp  = reg + " " + name;            
                items.add(tmp);
            }    
            doctorCombo.getItems().addAll(items);
        
        } else {
            
            
            ArrayList<ArrayList<String>> data = receptionist.getDoctors();
            int size = data.size();
            ObservableList<String> items = FXCollections.observableArrayList();
            for(int i = 1; i < size; i++)
            {
                String name = data.get(i).get(1) + " " + data.get(i).get(2);
                String reg = data.get(i).get(0);
                String tmp  = reg + " " + name;            
                items.add(tmp);
            }    
            doctorCombo.getItems().addAll(items);
            
        }
        
        doctorCombo.setDisable(false);
        
    }        
    
    String days = "1 2 3 4 5 6 7";
    @FXML private void selectDays()
    {
        HashMap<String,String> weekDays = new HashMap<>();
        weekDays.put("1","Sunday");
        weekDays.put("2","Monday");
        weekDays.put("3","Tuesday");
        weekDays.put("4","Wednesday");
        weekDays.put("5","Thursday");
        weekDays.put("6","Friday");
        weekDays.put("7","Saturday");
        
        appDatePicker.setValue(null);
        setDates();
        
        ArrayList<Integer> day3 = new ArrayList<Integer>();
        day3.add(1);
        day3.add(2);
        day3.add(3);
        day3.add(4);
        day3.add(5);
        day3.add(6);
        
        String tmpType = (String)appointmentType.getValue();
        if (tmpType.equals("Doctor"))
        {
            String doctorID = "";
            try
            {
                doctorID = ((String)doctorCombo.getValue()).split(" ")[0];
            }catch(Exception e){}
            ArrayList<String> data = receptionist.getAvailableDays(doctorID);
            System.out.println(data);
            int size = data.size();
            days = "";
            for(int i = 0; i < 7; i++)
            {           
                String number = Integer.toString(i+1);
                if (!data.contains(number)) days += ( number + " ");       
            }    
            //System.out.println(days);
            
        } else {
            
            String testName = (String)consultationAreaCombo.getValue();
            ArrayList<String> data = receptionist.getLabAvailableDays(testName);
            System.out.println(data);
            int size = data.size();
            days = "";
            for(int i = 0; i < 7; i++)
            {           
                String number = Integer.toString(i+1);
                if (!data.contains(number)) days += ( number + " ");       
            }    
            //System.out.println(days);
            
        }    
 
        appDatePicker.setDisable(false);
    
    }        
    
    
    @FXML private void selectTime()
    {
        
        timeCombo.getItems().clear();
        String tmpType = (String)appointmentType.getValue();
        if (tmpType.equals("Doctor"))
        {
            String doctorID = ((String)doctorCombo.getValue()).split(" ")[0];
            
            
            LocalDate ld = appDatePicker.getValue();
            String dayOfWeek = "";
            if (ld != null)
                dayOfWeek = Integer.toString(ld.getDayOfWeek().getValue());
            
            ArrayList<String> data = receptionist.getAvailableTime(doctorID,dayOfWeek);  
            //System.out.println(data);
            timeCombo.getItems().addAll(data);
            
        } else {
        
            try{
                String testName = (String)consultationAreaCombo.getValue();

                LocalDate ld = appDatePicker.getValue();
                String dayOfWeek = Integer.toString(ld.getDayOfWeek().getValue());

                ArrayList<String> data = receptionist.getLabAvailableTimeSlots(testName, dayOfWeek);
                timeCombo.getItems().addAll(data);
            }catch(Exception e){}    
        }  
        timeCombo.setDisable(false);
    
    }     
    
    @FXML private Button clearAppointmentButton;
    
    @FXML private void clearAppointment()
    {
        timeCombo.setValue(null);
        appDatePicker.setValue(null);
        doctorCombo.setValue(null);
        consultationAreaCombo.setValue(null);
        patientIDText.setText("");
        
        consultationAreaCombo.setDisable(true);
        doctorCombo.setDisable(true);
        appDatePicker.setDisable(true);
        timeCombo.setDisable(true);
        
        appointmentType.setDisable(false);
        appointmentType.setValue("Appointment Type");
        
        patientIDText.setDisable(true);
    }            
    
    @FXML private Button makeAppointment;
    
    @FXML private void makeAppointment()
    {
        HashMap<String,String> weekDays = new HashMap<>();
        weekDays.put("Sunday","1");
        weekDays.put("Monday","2");
        weekDays.put("Tuesday","3");
        weekDays.put("Wednesday","4");
        weekDays.put("Thursday","5");
        weekDays.put("Friday","6");
        weekDays.put("Saturday","7");
        
        String tmpType = (String)appointmentType.getValue();
        
        String timeSlot = (String)timeCombo.getValue();
       
        LocalDate appDate = appDatePicker.getValue();
        LocalDate today = LocalDate.now();
        
        int dayOfWeek = appDate.getDayOfWeek().getValue();
        if (appDate.isAfter(today.plusDays(7)))
        {
            dayOfWeek += 7;
        }    
        String day = Integer.toString(dayOfWeek);
        System.out.println(day);
        
        String doctorID = ((String)doctorCombo.getValue()).split(" ")[0];
        String patientID = patientIDText.getText();
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy");
        String tmp_day = appDate.format(format);
        
        if (tmpType.equals("Doctor"))
        {
            String result = receptionist.makeAppointment(patientID, doctorID, day, timeSlot);	
            if (!result.equals("false")) showAppointmentSuccessIndicator(patientID, doctorID, ( tmp_day + " " + timeSlot) , result);
            
        } else {
            
            String testID = receptionist.getLabTestID((String)consultationAreaCombo.getValue()).get(0);	
            String result = receptionist.makeLabAppointment(patientID,doctorID,testID,day,timeSlot);
            if (!result.equals("false")) showAppointmentSuccessIndicator(patientID, doctorID, ( tmp_day + " " + timeSlot) , result);
        }
            
        timeCombo.setValue(null);
        appDatePicker.setValue(null);
        doctorCombo.setValue(null);
        consultationAreaCombo.setValue(null);
        patientIDText.setText("");
        
        consultationAreaCombo.setDisable(true);
        doctorCombo.setDisable(true);
        appDatePicker.setDisable(true);
        timeCombo.setDisable(true);
    }        
    
    @FXML private TextField appointmentID;
    @FXML private TextField patientName;
    @FXML private TextField appDate;
    @FXML private TextField docName;
    
    @FXML
    private void searchAppointment()
    {
        String appID = appointmentID.getText();
        
        if ( !appID.equals("") )
        {    
        
            String valid = Validate.appointmentID(appID);
            if ( valid.equals("1") )
            {    

                if ( appID.substring(0,3).equals("app") )
                {
                    try{
                        ArrayList<ArrayList<String>> data = receptionist.getAppointmentDetails(appID);

                        patientName.setText(data.get(1).get(2)+" "+data.get(1).get(3));
                        appDate.setText(data.get(1).get(1));
                        docName.setText(data.get(1).get(0) + "  " + data.get(2).get(0) + " " + data.get(2).get(1));        
                    } catch (Exception e) {}   

                } else {

                    try{
                        ArrayList<ArrayList<String>> data = receptionist.getLabAppointmentDetails(appID);

                        patientName.setText(data.get(1).get(2)+" "+data.get(1).get(3));
                        appDate.setText(data.get(1).get(1));
                        docName.setText(data.get(1).get(0) + "  " + data.get(2).get(0) + " " + data.get(2).get(1));        
                    } catch (Exception e) {}  

                }  

                patientName.setDisable(false);
                appDate.setDisable(false);
                docName.setDisable(false);
            } else {

                showPopup(valid,appointmentID);
            }
        }    
    }
    
    @FXML private void showAppointments()
    {
        Stage stage= new Stage();
        AllAppointmentsController app = new AllAppointmentsController(receptionist);
        app.load();
        Scene scene = new Scene(app);
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
    private void cancelAppointment()
    {
        String appID = appointmentID.getText();
        
        if ( appID.substring(0,3).equals("app") )
        {
            boolean result = receptionist.cancelAppointment(appID);
            if (result == true) showSuccessIndicator();
        
        } else {
            
            boolean result = receptionist.cancelLabAppointment(appID);
            if (result == true) showSuccessIndicator();
        
        }
            
        appointmentID.setText("");
        patientName.setText("");
        appDate.setText("");
        docName.setText("");
        
        patientName.setDisable(true);
        appDate.setDisable(true);
        docName.setDisable(true);
        
    }        
    
    @FXML Button appCancelClearButton;
    
    @FXML private void appCancelClear()
    {
        appointmentID.setText("");
        docName.setText("");
        patientName.setText("");        
        appDate.setText("");
                
                
    }        
    
    @FXML DatePicker appDatePicker;
    final Callback<DatePicker, DateCell> dayCellFactory =  new Callback<DatePicker, DateCell>() 
    { 
        @Override
        public DateCell call(final DatePicker datePicker) 
        {
            return new DateCell() 
            {
                @Override
                public void updateItem(LocalDate item, boolean empty) 
                {
                    super.updateItem(item, empty);
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                    LocalDate item2 = LocalDate.parse(date);    
                    
                    String[] days2 = days.split(" ");
                    
                    for (int i = 0; i < days2.length; i++)
                    {
                        if (item.getDayOfWeek().getValue() == Integer.parseInt(days2[i])) 
                        {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                    
                    if (item.isBefore(item2)) 
                    {
                            setDisable(true);
                            setStyle("-fx-background-color: #aaaaaa;");
                    }

                    if (item.isAfter(item2.plusDays(13))) 
                    {
                            setDisable(true);
                            setStyle("-fx-background-color: #aaaaaa;");
                    }
                    
                    setTooltip(new Tooltip("Make the appointment within 14 days"));

                }
            };
        }
    };
    
    
    public void setDates()
    {
        
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        LocalDate item = LocalDate.parse(date);    
        appDatePicker.setDayCellFactory(dayCellFactory);
        
        //appDatePicker.getValue().setDisabled(item);
        
        //appDatePicker.getValue().setDisable();
        
    }        
    
    /// Loading ther profile info ////////////////////////////////////////
  
    @FXML
    private TextField receptionistName;
    @FXML    
    private TextField receptionistNIC;
    @FXML
    private DatePicker receptionistDOB;
    @FXML
    private TextField receptionistAge;
    @FXML
    private ComboBox receptionistGender;
    @FXML
    private TextField receptionistNationality;
    @FXML
    private TextField receptionistReligion;
    @FXML
    private TextField receptionistMobile;
    @FXML
    private TextField receptionistEmail;   
    @FXML
    private TextField receptionistAddress;
    
    @FXML
    private TextField receptionistUserName;
    @FXML
    private TextField receptionistUserType;
    @FXML
    private TextField receptionistUserID;
    @FXML
    private TextField receptionistPassword;
    @FXML
    private TextField receptionistNewPassword;
    @FXML
    private TextField receptionistConfirmPassword;
            
    @FXML
    public void loadProfileData() 
    {

        HashMap<String,String> receptionistPersonalInfo =  receptionist.getProfileInfo();
		
        receptionistName.setText(receptionistPersonalInfo.get("first_name") + " " + receptionistPersonalInfo.get("last_name"));
        receptionistNIC.setText(receptionistPersonalInfo.get("nic"));
        receptionistNationality.setText(receptionistPersonalInfo.get("nationality"));
        receptionistReligion.setText(receptionistPersonalInfo.get("religion"));
        receptionistMobile.setText(receptionistPersonalInfo.get("mobile"));
        receptionistEmail.setText(receptionistPersonalInfo.get("email"));
        receptionistAddress.setText(receptionistPersonalInfo.get("address"));

        try{
                SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                Date birth = tmpdataformat.parse(receptionistPersonalInfo.get("date_of_birth"));
                Calendar calendarBirth = Calendar.getInstance();
                calendarBirth.setTime(birth);
                Calendar calendarToday = Calendar.getInstance();
                String tmpage = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));
                
                String tmpDOB = receptionistPersonalInfo.get("date_of_birth");
                
                int year = Integer.parseInt(tmpDOB.substring(0,4));
                int month = Integer.parseInt(tmpDOB.substring(5,7));        
                int date = Integer.parseInt(tmpDOB.substring(8,10));        
                receptionistDOB.setValue(LocalDate.of(year, month, date));
                receptionistAge.setText(tmpage);
        }catch(Exception e){e.printStackTrace();}
        
        try{
            String tmpGen = receptionistPersonalInfo.get("gender");
            if (tmpGen.equals("m")){receptionistGender.getSelectionModel().select("Male");}
            else {receptionistGender.getSelectionModel().select("Female");}


            receptionistUserName.setText(receptionistPersonalInfo.get("user_name"));
            receptionistUserType.setText(receptionistPersonalInfo.get("user_type"));
            receptionistUserID.setText(receptionistPersonalInfo.get("user_id"));
        }catch(Exception e){}
    } 
    
    @FXML
    private Button editBasicInfoButton;
    
    @FXML 
    private void editBasicInfo()
    {
        String currentState = editBasicInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            receptionistName.setDisable(false);
            receptionistNIC.setDisable(false);
            receptionistGender.setDisable(false);
            receptionistNationality.setDisable(false);
            receptionistReligion.setDisable(false);
            receptionistMobile.setDisable(false);
            receptionistEmail.setDisable(false);
            receptionistAddress.setDisable(false);
            receptionistDOB.setDisable(false);
            
            editBasicInfoButton.setText("Save");
        }
        else if ( currentState.equals("Save"))
        {
            receptionistName.setDisable(true);
            receptionistNIC.setDisable(true);
            receptionistGender.setDisable(true);
            receptionistNationality.setDisable(true);
            receptionistReligion.setDisable(true);
            receptionistMobile.setDisable(true);
            receptionistEmail.setDisable(true);
            receptionistAddress.setDisable(true);
            receptionistDOB.setDisable(true);
            
            String info = "";
				
            String[] name = receptionistName.getText().split(" ");
            String gender = (String)receptionistGender.getSelectionModel().getSelectedItem();
            if (gender.equals("Male")){gender = "m";}
            else {gender = "f";}
            //String marital = receptionMaritalComboDoc.getText();
            String nationality = (String)receptionistNationality.getText();
            String religion = (String)receptionistReligion.getText();
            String mobile = receptionistMobile.getText();
            String email = receptionistEmail.getText();
            String address = receptionistAddress.getText();

            info += "first_name " + name[0] + "#last_name " + name[1];
            info += "#gender " + gender;
            info += "#nationality " + nationality;
            info += "#religion " + religion;
            info += "#mobile " + mobile;
            info += "#email " + email;
            info += "#address " + address;

            //System.out.println(info);

            boolean success = receptionist.updateProfileInfo(info);
            
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
            receptionistUserName.setDisable(false);
            
            editUserInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            receptionistUserName.setDisable(true);
            
            String info = "user_name " + receptionistUserName.getText();
            boolean success = receptionist.updateAccountInfo(info);
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
            receptionistPassword.setDisable(false);
            receptionistNewPassword.setDisable(false);
            receptionistConfirmPassword.setDisable(false);
            
            editPasswordInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            
            boolean result = false;
            //result = checkCurrentPassword(String userId, String password)
            
            if (result == true)
            {
                if ( receptionistNewPassword.getText().equals( receptionistConfirmPassword.getText() ) )
                {
                    String info = "password " + receptionistConfirmPassword.getText();
                    boolean success =  receptionist.updateAccountInfo(info);
                    
                    receptionistPassword.setDisable(true);
                    receptionistNewPassword.setDisable(true);
                    receptionistConfirmPassword.setDisable(true);
                    
                    
                    if (success == true) showSuccessIndicator();
                    editPasswordInfoButton.setText("Edit");
                }    
            }
            
        }
    }
    
    @FXML private Button AllMessages;
    
    @FXML
    private void showAllMessages()
    { 

        if (popOver == null) 
        {
            popOver = new PopOver();
        }
        AllMessagesController popup = new AllMessagesController(receptionist);
        popup.loadMessages();
        
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(AllMessages);
        
    }
    
    @FXML private Button showUserButton;
    
    @FXML private void showUser()
    {
        CurrentUserSummaryController user = new CurrentUserSummaryController(receptionist);
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

            receptionist.setProfilePic(imageName);
            editProfilePicButton.setText("Edit");
        }
        
    }
    
    public void loadProfileImage()
    {
        Image img;
        try{
            String image = receptionist.getProfilePic();
            img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
        }catch(Exception e){
            img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
        }
        profileImage.setImage(img);
        
    }    

    @FXML
    public void showAppointmentSuccessIndicator(String patientId, String consult, String appDate, String appId)
    {
        Stage stage= new Stage();
        AppointmentSuccessController success = new AppointmentSuccessController();
        
        success.fillAppointmentData(patientId,consult,appDate,appId);
        
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
    public void showPatientAccountSuccessIndicator(String patientId, String pName, String pMobile, String pGender)
    {
        Stage stage= new Stage();
        PatientAccountSuccessController success = new PatientAccountSuccessController();
        
        success.fillPatientData( patientId, pName, pMobile, pGender);
        
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
        LogoutController logout = new LogoutController(logoutButton,receptionist);
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
        patientSearchBox.setPromptText("search value");
        patientSearchCombo.getSelectionModel().select("Patient ID");
        loadProfileImage();
        loadNameList();
    }        
            
    /*******************************************************************************************************
     * Validations
     *******************************************************************************************************/
    
    //private PopOver popOver;
    
    private void showPopup(String message, TextField text)
    { 

        if (popOver == null) 
        {
            popOver = new PopOver();
        }
        WarningController popup = new WarningController();
        popup.addMessage(message);

        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
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
        try{
            String tmpID = patientSearchBox.getText();
            if ( tmpID.length() == 9 )
            {
                String result = Validate.patientID(tmpID);
                if (result.equals("1"))
                {
                    popOver.hide(Duration.millis(500));

                } else {
                    showPopup(result,patientSearchBox);
                }   
            }
            else if ( tmpID.length() > 9 )
            { 
                showPopup("hmsxxxxpa",patientSearchBox);
            }
        }catch(Exception e){}    
    }  
    
    @FXML
    private void validatePatientID2()
    {
        try{
            String tmpID = patientIDText.getText();
            if ( tmpID.length() == 9 )
            {
                String result = Validate.patientID(tmpID);
                System.out.println(result);
                if (result.equals("1"))
                {
                    try { popOver.hide(Duration.millis(500)); } catch (Exception e) {};
                    appointmentType.setDisable(true);
                    consultationAreaCombo.setDisable(false);
                    consultationAreaCombo.getItems().clear();
                    this.fillConsultationAreas();

                } else {
                    showPopup(result,patientIDText);
                }   
            }
            else if ( tmpID.length() > 9 )
            { 
                showPopup("hmsxxxxpa",patientIDText);
            }
        }catch(Exception e){}    
    }  
    
    @FXML private void validateAppointmentID()
    {
        try{
            String result = "";
            String tmpID = appointmentID.getText();
           
            result = Validate.appointmentID(tmpID);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,appointmentID);
            }   
           
        }catch(Exception e){}  
    
    
    }        
    
    
    @FXML
    private void validateEmail()
    {        
        try{
            String tmpemail = patientEmail.getText();
            String result = Validate.email(tmpemail);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,patientEmail);
            }
        }catch(Exception e){}    
    }           
            
    
    @FXML 
    private void validateMobile() 
    {    
        try{
            String tmpmobile = patientMobile.getText();
               
                String result = Validate.mobile(tmpmobile);
                if (result.equals("1"))
                {
                    popOver.hide(Duration.millis(500));

                } else {
                    showPopup(result,patientMobile);
                }
              
        }catch(Exception e){}     
    }   
    
    
    @FXML 
    private void validateNIC() 
    {    
        try{
            String tmpnic = patientNIC.getText();
            
                ArrayList<String> result = Validate.NIC(tmpnic);
                if (result.size() != 0)
                {
                    try{
                        popOver.hide(Duration.millis(500));
                    }catch(Exception e){}
                    // setting gender of the patient
                    String tmpGen = result.get(2);
                    if (tmpGen.equals("m")){patientGender.getSelectionModel().select("Male");}
                    else {patientGender.getSelectionModel().select("Female");}
                    
                    // setting date of birth of the patient
                    
                    int year = Integer.parseInt(result.get(0));
                    int days = 0;
                    if ( ( year % 4 ) == 0 ) days = Integer.parseInt(result.get(1))-1;
                    else days = Integer.parseInt(result.get(1))-2;
                    
                    LocalDate date = LocalDate.of(year, 1, 1);
                    date = date.plusDays(days);
                    patientDOB.setValue(date);
                    	
                } else {
                    showPopup("xxxxxxxxxV",patientNIC);
                    patientDOB.setValue(null);
                    patientGender.getSelectionModel().select(null);
                    
                }
                
        }catch(Exception e){}     
    } 
    
    @FXML
    private void validateEmail2()
    {        
        try{
            String tmpemail = receptionistEmail.getText();
            String result = Validate.email(tmpemail);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,receptionistEmail);
            }
        }catch(Exception e){}    
    }           
            
    
    @FXML 
    private void validateMobile2()
    {    
        try{
            String tmpmobile = receptionistMobile.getText();
            String result = Validate.mobile(tmpmobile);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,receptionistMobile);
            }
        }catch(Exception e){}     
    }   
    
    
    @FXML 
    private void validateNIC2()
    {    
        try{
            String tmpnic = receptionistNIC.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup("xxxxxxxxxV",receptionistNIC);
            }
        }catch(Exception e){}     
    } 
        
}


