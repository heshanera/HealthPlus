package Doctor;

import LabAssistant.LabReport;
import com.hms.hms_test_2.AllMessagesController;
import com.hms.hms_test_2.CurrentUserSummaryController;
import com.hms.hms_test_2.LogoutController;
import com.hms.hms_test_2.WarningController;
import com.hms.hms_test_2.SuccessIndicatorController;
import com.hms.hms_test_2.Validate;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import org.controlsfx.control.textfield.TextFields;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.stage.StageStyle;
import javafx.geometry.Rectangle2D;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;
import jfxtras.scene.control.agenda.Agenda;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

/**
 *
 * @author heshan
 */
public class DoctorController extends AnchorPane {

    public Doctor doc;
    public String username;    
    /**
     *
     * @param username
     */
    public DoctorController(String username) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Doctor.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        doc = new Doctor(username);
        this.username = username;
        doc.saveLogin(username);

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    
    
    @FXML 
    private Label doctorUsername;
    @FXML
    private Agenda appointmentTable;
    
    /**
     *
     */
    @FXML
    public void setAppointments()
    {
        
        appointmentTable.setDisable(false);
        
        ArrayList<ArrayList<String>> tableData0 = doc.getAppointments();
        int noOfApp = (tableData0.size());
        System.out.println(tableData0);
        //System.out.println(noOfApp);
        //System.out.println(tableData0);
        
        final List<Agenda.AppointmentImplLocal> Appointments = FXCollections.observableArrayList();
        
        for (int i = 1; i < noOfApp; i++)
        {
            String[] tmp = tableData0.get(i).get(1).split(" ");
            String date = tmp[0];
            String time = tmp[1].substring(0,5);
            
            int hour1 = Integer.parseInt(time.split(":")[0]);
            int minute1 = Integer.parseInt(time.split(":")[1]);
            int hour = hour1;
            int minute = minute1;
            
            if ( minute < 30 ) 
            {
                minute += 30;
                
            } else {
                
                int tmpVal = (minute + 30) - 60;
                hour++;
                minute = tmpVal;
                
            }
            

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate finDate = LocalDate.parse(date, formatter);
            
            System.out.println(finDate.atTime(hour1, minute1));
            
            Appointments.add(
                new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime(finDate.atTime(hour1, minute1))
                .withEndLocalDateTime(finDate.atTime(hour, minute))
                .withDescription(tableData0.get(i).get(0))
                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"))    
            );
        }
        
        appointmentTable.appointments().addAll(Appointments);
    }
    
   @FXML 
   private AreaChart<String,Number> patientSummary;
   
    /**
     *
     */
   @FXML private NumberAxis yaxis ;
   @FXML public void fillAreaChart()
   {
        
        
        ArrayList<ArrayList<String>> data = doc.getPatientAttendence(doc.slmcRegNo);
        String date = "";
        
        ArrayList<String> months = new ArrayList<String>(); 
        ArrayList<Integer> patients = new ArrayList<Integer>();
        
        int size = data.size();
        for(int i = 1; i < size; i ++)
        {    
            date = data.get(i).get(0);
            DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate date2 = LocalDate.parse(date, fomatter1);

            DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
            String Month = fomatter2.format(date2);
            
            System.out.println(Month);
            if ( months.contains(Month) ) {
            
                int indx = months.indexOf(Month);
                int tmp = patients.remove(indx);
                patients.add(indx,(tmp+1));
                
            } else {
                
                months.add(Month);
                patients.add(1);
            }    
             
        }
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Patients in the Last "+months.size()+" Months");
        size = months.size();
        int max = 0;
        for(int i = 0; i < size; i++)
        {
            String month = months.get(i);
            int no = patients.get(i);
            if ( max < no ) max = no;  
            series1.getData().add(new XYChart.Data(month, no));
        }    
        
        yaxis.setUpperBound(max);
        yaxis.setAutoRanging(false);
        yaxis.setTickUnit(1);
        yaxis.setLowerBound(0);
        
        patientSummary.getData().clear();
        patientSummary.getData().addAll(series1);
        
   }
    
   @FXML private Label todayAppointments;
   
   public void setTodayAppointments()
   {
       String apps = doc.getTodayAppointments();
       todayAppointments.setText(apps);
   }        
   
   
   @FXML
   private Button newPatientDoc;
    
   @FXML
   private ComboBox searchTypePatientDoctor;
   
   @FXML
   private Label noSearchType;
   
   @FXML
   private TextField patientSearchValue;
   
   @FXML
   private TextField patientFirstName;
   @FXML    
   private TextField patientLastName;
   @FXML
   private TextField patientAge;
   @FXML
   private TextField patientGender;
   @FXML
   private TextField patientEmail;
   @FXML
   private Label patientIdLabel;
   
   
    
    @FXML
    private void searchNewPatientDoc(ActionEvent event) throws IOException 
    {
        if (searchTypePatientDoctor.getSelectionModel().getSelectedItem() != null )
        {    
            String selectedValue = searchTypePatientDoctor.getSelectionModel().getSelectedItem().toString();
            //System.out.println("testing");
            //noSearchType.setText("");
            
            ArrayList<ArrayList<String>> personalData;
            ArrayList<ArrayList<String>> medicalData;
	    ArrayList<ArrayList<String>> historyData;
            
            ArrayList<ArrayList<ArrayList<String>>> patientData = new ArrayList<>();
            String searchValue = patientSearchValue.getText();
            if (!searchValue.equals(""))
            {    
                try{
                
                    searchTypePatientDoctor.setStyle("-fx-border-color: #999 #999 #999 #999;");
                    patientSearchValue.setStyle("-fx-border-color: #999 #999 #999 #999;");
                    switch (selectedValue) 
                    {
                        case "Patient ID":
                            patientData = doc.getPatientInfo("id",searchValue);
                            break;
                        case "Name":
                            //System.out.println("testing2");
                            String patientid = patientLog.get(searchValue);
                            patientData = doc.getPatientInfo("id",patientid);
                            patientSearchValue.setText(patientid);
                            searchTypePatientDoctor.setValue("Patient ID");
                            break;
                        case "NIC":
                            //System.out.println("testing3");
                            patientData = doc.getPatientInfo("nic",searchValue);
                            patientSearchValue.setText(patientData.get(1).get(1).get(1));
                            searchTypePatientDoctor.setValue("Patient ID");
                            
                            
                            break;
                        default:
                            break;
                    }
                    personalData = patientData.get(0);
                    medicalData = patientData.get(1);
                    historyData = patientData.get(2);

                    //System.out.println(personalData);
                    
                    if (personalData.size() > 1)
                    {

                        patientFirstName.setText(personalData.get(1).get(7));
                        patientLastName.setText(personalData.get(1).get(8));

                        try{
                        SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                        Date birth = tmpdataformat.parse(personalData.get(1).get(4));
                        Calendar calendarBirth = Calendar.getInstance();
                        calendarBirth.setTime(birth);
                        Calendar calendarToday = Calendar.getInstance();
                        int age = calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR);
                        patientAge.setText(Integer.toString(age));    
                        }catch(ParseException e){}

                        if (personalData.get(1).get(3).equals("m")) patientGender.setText("Male");
                        else patientGender.setText("Female");

                        patientEmail.setText(personalData.get(1).get(9));

                        // filling the medical history
                        fillPatientHistory(historyData);
                        fillPatientAllergies(medicalData);


                    } else {

                        patientFirstName.setText("");
                        patientLastName.setText("");
                        patientGender.setText("");
                        patientAge.setText(""); 
                        patientEmail.setText("");

                    }
                    
                } catch (Exception e){}
                
                    
            }
            else
            {
                patientSearchValue.setStyle("-fx-border-color: red;");        
            }    
        }
        else
        {
            searchTypePatientDoctor.setStyle("-fx-border-color: red;");
            //patientSearchValue.setText("Select The Search Type!");
        }
    
    
    }
    
    @FXML
    private ListView historyList;
    
    @FXML
    private Label hisTime1;
    @FXML
    private Label hisTime2;
    @FXML
    private Label hisTime3; 
            
    @FXML
    private TextArea hisDetail1;
    @FXML
    private TextArea hisDetail2;
    @FXML
    private TextArea hisDetail3;       
            
    @FXML
    private GridPane historyPane;
    
    @FXML
    private Pagination historyPagination;
     
    @FXML
    private BorderPane patientHistory(int pageIndex)
    {
        if (searchTypePatientDoctor.getSelectionModel().getSelectedItem() != null )
        {    
            String selectedValue = searchTypePatientDoctor.getSelectionModel().getSelectedItem().toString();
            //System.out.println("testing");
            //noSearchType.setText("");
            
            ArrayList<ArrayList<String>> personalData;
            ArrayList<ArrayList<String>> medicalData;
	    ArrayList<ArrayList<String>> historyData;
            
            ArrayList<ArrayList<ArrayList<String>>> patientData = new ArrayList<>();
            String searchValue = patientSearchValue.getText();
            if (!searchValue.equals(""))
            {    
                switch (selectedValue) 
                {
                    case "Patient ID":
                        //System.out.println("testing1");
                        patientData = doc.getPatientInfo("id",searchValue);                   
                        break;
                    case "Name":
                        //System.out.println("testing2");
                        patientData = doc.getPatientInfo("name",searchValue);
                        break;
                    case "NIC":
                        //System.out.println("testing3");
                        patientData = doc.getPatientInfo("nic",searchValue);
                        break;
                    default:
                        break;
                }
                personalData = patientData.get(0);
                medicalData = patientData.get(1);
		historyData = patientData.get(2);
                
                
                if (historyData.size() > 1)
                {
                    int noOfSlots = (historyData.size()-1);
                    //System.out.println(noOfSlots);
                    //System.out.println(currentTimeTableData0);
                    
                         


                    int fromIndex = (pageIndex * 3) + 1;
                    int toIndex = Math.min(fromIndex + 3, historyData.size());
                    //availabilityTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
                    
                    try{
                        hisTime1.setText(historyData.get(fromIndex).get(0));
                        hisDetail1.setText(historyData.get(fromIndex).get(1));
                    }catch(Exception ex){
                        hisTime1.setText("");   
                        hisDetail1.setText("");
                    }    
                    try{
                        hisTime2.setText(historyData.get(fromIndex+1).get(0));
                        hisDetail2.setText(historyData.get(fromIndex+1).get(1));
                    }catch(Exception ex){
                        hisTime2.setText("");   
                        hisDetail2.setText("");
                    }
                    try{
                        hisTime3.setText(historyData.get(fromIndex+2).get(0));   
                        hisDetail3.setText(historyData.get(fromIndex+2).get(1)); 
                    }catch(Exception ex){
                        hisTime3.setText("");   
                        hisDetail3.setText("");
                    }        

                    
                    
                     
                    
                    return new BorderPane(historyPane); 
                    
                    
                } else {}
                    
            }
            else
            { }    
        }
        else
        { }
       return new BorderPane(historyPane); 
    }
    
   
    
    @FXML 
    private void createHistoryPagination(int dataSize)
    {
        historyPagination.setPageCount((dataSize / 3 + 1));
        historyPagination.setPageFactory(this::patientHistory);
    }
    
    @FXML
    private void fillPatientHistory(ArrayList<ArrayList<String>> historyData)
    {
        createHistoryPagination(historyData.size()-1);
    }
    
    
    @FXML
    private Pagination allergiesPagination;
    
    @FXML ListView allergyView;
    
    @FXML private void removeAllergy()
    {
        int selectedIdx = allergyView.getSelectionModel().getSelectedIndex();
        if ( selectedIdx >= 0 ) allergyView.getItems().remove(selectedIdx);
    }        
    
    @FXML
    private BorderPane patientAllergies(int pageIndex)
    {
        if (searchTypePatientDoctor.getSelectionModel().getSelectedItem() != null )
        {    
            String selectedValue = searchTypePatientDoctor.getSelectionModel().getSelectedItem().toString();
            //System.out.println("testing");
            //noSearchType.setText("");
            
            ArrayList<ArrayList<String>> medicalData;
            
            ArrayList<ArrayList<ArrayList<String>>> patientData = new ArrayList<>();
            String searchValue = patientSearchValue.getText();
            if (!searchValue.equals(""))
            {    
                switch (selectedValue) 
                {
                    case "Patient ID":
                        //System.out.println("testing1");
                        patientData = doc.getPatientInfo("id",searchValue);                   
                        break;
                    case "Name":
                        //System.out.println("testing2");
                        patientData = doc.getPatientInfo("name",searchValue);
                        break;
                    case "NIC":
                        //System.out.println("testing3");
                        patientData = doc.getPatientInfo("nic",searchValue);
                        break;
                    default:
                        break;
                }
                medicalData = patientData.get(1);
                if (medicalData.size() > 1)
                {
                    
                    //System.out.println(noOfSlots);
                    //System.out.println(currentTimeTableData0);
                    String[] DrugReactions = medicalData.get(1).get(0).split(",");
                    int noOfSlots = DrugReactions.length;
                    ObservableList<String> items =FXCollections.observableArrayList ();
                    for(int i = 0; i < noOfSlots; i++ )
                    {
                        items.add(DrugReactions[i]);
                    }    
                    //allergyView.setItems(items);     


                    int fromIndex = pageIndex * 10;
                    int toIndex = Math.min(fromIndex + 10, noOfSlots);
                    allergyView.setItems(FXCollections.observableArrayList(items.subList(fromIndex, toIndex)));
                    
                    
                    
                    return new BorderPane(allergyView); 
                    
                    
                } else {}
                    
            }
            else
            { }    
        }
        else
        { }
       return new BorderPane(allergyView); 
    }
    
    @FXML 
    private void createAllergiesPagination(int dataSize)
    {
        allergiesPagination.setPageCount((dataSize / 10 + 1));
        allergiesPagination.setPageFactory(this::patientAllergies);
    }
    
    @FXML
    private void fillPatientAllergies(ArrayList<ArrayList<String>> medicalData)
    {
        createAllergiesPagination(medicalData.size()-1);
    }
    
    @FXML
    private TextField allergyText;
    
    @FXML
    private void addPatientAllergies()
    {
        
        String allergy = allergyText.getText();
        try{
            if (!allergy.equals(""))
            {
                allergyView.getItems().add(allergyView.getItems().size(), allergy);
            }    

            String patientID = patientSearchValue.getText();
            try{
                boolean result = doc.allergies(allergy, patientID);
                if ( result == true ) showSuccessIndicator();
            } catch(Exception e){}    
        }catch(Exception e){}    
    }
    
    
    @FXML
    public void savePatientAllergies()
    {
        
    
    }        
    
    @FXML private TableView testResultTable;
    
    @FXML private void searchPatientTestResults()
    {
        ArrayList<ArrayList<String>> data;
        
        String testid = testID.getText();
        data = doc.getTestResults(testid);
        
        ObservableList<LabReport> data2 = FXCollections.observableArrayList();
        
        String[] urArray = {
            "Test ID","prescription_id","Appearance","S.G (Refractometer)",
            "PH","Protein","Glucose","Ketone Bodies","Bilirubin","Urobilirubin",
            "Contrifuged Depositsphase Contrast Microscopy","Pus Cells",
            "Red Cells","Epithelial Cells","Casts","Cristals"
        };

        String[] liArray = {
            "Test ID","prescription_id","Cholestrol HDL","Cholestrol LDL",
            "Triglycerides","Cholestrol LDL/HDL Ratio"
        };
        
        String[] bgArray = {
            "Test ID","prescription_id","Blood Group","Rhesus D"
        };
        
        String[] cbcArray = {
            "Test ID","prescription_id","Total White Cell Count","Differential Count",
            "Neutrophils","Lymphocytes","Monocytes","Eosonophils","Basophils",
            "Haemoglobin","Red Blood Cells","Mean Cell Volume","Haematocrit","Mean Cell Haemoglobin",
            "M.C.H Concentration","Red Cells Distribution Width","Platelet Count"
        };
        
        String[] lvArray = {
            "Test ID","prescription_id","Total Protein","Albumin","Globulin",
            "Total Bilirubin","Direct Bilirubin","SGOT(AST)","SGPT(ALT)","Alkaline Phospates"
        };
        
        String[] reArray = {
            "Test ID","prescription_id","Creatinine","Urea","Total Bilirubin",
            "Direct Bilirubin","SGOT(AST)","SGPT(ALT)","Alkaline Phospates"
        };
        
        String[] scptArray = {
            "Test ID","Test ID","prescription_id","CPK Total"
        };
        
        String[] scpArray = {
            "Test ID","prescription_id","HIV 1 & 2 ELISA"
        };
        
        String[] tmpArray = null;
        String type = data.get(2).get(0);
        
        switch (type)
        {
            case "ur":
                tmpArray = urArray;
                break;
            case "li":
                tmpArray = liArray;
                break;
            case "bg":
                tmpArray = bgArray;
                break;
            case "cbc":
                tmpArray = cbcArray;
                break;
            case "lv":
                tmpArray = lvArray;
                break;
            case "re":
                tmpArray = reArray;
                break;
            case "scpt":
                tmpArray = scptArray;
                break;
            case "scp":
                tmpArray = scpArray;
                break;    
        }    

        int size = data.get(0).size();
        
        String tmp = data.get(1).get(size-2);
        
        System.out.println(tmp);
        
        ArrayList<ArrayList<String>> data4 = doc.getLabPatientInfo(tmp);
        
        System.out.println(data4);
        
        data2.add(new LabReport("Name", data4.get(1).get(0) + " " + data4.get(1).get(1)));
        
        String tmpGen = data4.get(1).get(2);
        if (tmpGen.equals("m")){tmpGen="Male";}
        else {tmpGen="Female";}
        data2.add(new LabReport("Gender", tmpGen));
        
        try{
                SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                Date birth = tmpdataformat.parse(data4.get(1).get(3));
                Calendar calendarBirth = Calendar.getInstance();
                calendarBirth.setTime(birth);
                Calendar calendarToday = Calendar.getInstance();
                String tmpage = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));
                
                data2.add(new LabReport("Age", tmpage));
                
        }catch(Exception e){e.printStackTrace();}
        
        data2.add(new LabReport("", ""));
        
        data2.add(new LabReport(tmpArray[0], data.get(1).get(0)));
        
        data2.add(new LabReport("", ""));
        
        for(int i = 1; i < size; i++)
        {
            if ( ( i != 1 ) && ( i != size-2 ) && ( i != size-1 ) )
            {    
                System.out.println(i);
                data2.add(new LabReport(tmpArray[i], data.get(1).get(i)));
            }    
            else if ( i == size-2 )
            {
                System.out.println(i);
                data2.add(new LabReport("", ""));
            }    
            else if ( i == size-1 )
            {
                System.out.println(i);
                data2.add(new LabReport("Date", data.get(1).get(i)));
            }    
            
        }
        
        TableColumn head = new TableColumn<>("");
        head.setCellValueFactory(new PropertyValueFactory<>("constituent"));
        head.prefWidthProperty().bind(testResultTable.widthProperty().divide(3));
        head.setResizable(true);
        
        TableColumn info = new TableColumn<>("");
        info.setCellValueFactory(new PropertyValueFactory<>("result"));
        info.prefWidthProperty().bind(testResultTable.widthProperty().divide(1.8));
        info.setResizable(false);
        
        testResultTable.setFixedCellSize(25.0);
        testResultTable.getItems().clear();
        testResultTable.getColumns().clear();
        testResultTable.getColumns().add(head);
        testResultTable.getColumns().add(info);
        testResultTable.setItems(data2);
        
    }

    
    @FXML private void clearPatientTestResults()
    {
        testResultTable.getItems().clear();
    }        

    
    @FXML 
    public void loadDrugList()
    {
        
        ArrayList<String> drugData = doc.getDrugGenericInfo();	
        ObservableList<String> possibleSuggestions = FXCollections.observableArrayList();
        
        int drugDatalen = drugData.size();
        for (int i = 0; i < drugDatalen; i++)
        {
               possibleSuggestions.add(drugData.get(i));
        }
        
        //TextFields.bindAutoCompletion(txtAuto,"test","temp","tempurature","table","tablet");
        TextFields.bindAutoCompletion(txtAuto,possibleSuggestions);
    }
    
    public void loadTestList()
    {
        
        ArrayList<ArrayList<String>> testData = doc.getTestInfo();	
        ObservableList<String> possibleSuggestions = FXCollections.observableArrayList();
        
        int drugDatalen = testData.size();
        for (int i = 1; i < drugDatalen; i++)
        {
               possibleSuggestions.add(testData.get(i).get(1));
        }
        
        //TextFields.bindAutoCompletion(txtAuto,"test","temp","tempurature","table","tablet");
        TextFields.bindAutoCompletion(txtAuto1,possibleSuggestions);
    }
    
    
    HashMap<String,String> patientLog = new HashMap<String,String>();
    
    public void loadNameList()
    {
        ObservableList<String> possibleSuggestions = FXCollections.observableArrayList();

        ArrayList<ArrayList<String>> data = doc.getAllNames();
        System.out.println(data);
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
        TextFields.bindAutoCompletion(patientSearchValue,possibleSuggestions);
    }        
    
    @FXML private void convertToID()
    {
        try{
            String[] tmp = patientSearchValue.getText().split(" ");
            if ( tmp.length == 4 )
            {
                String patientID = tmp[3];
                patientSearchValue.setText(patientID);
                searchTypePatientDoctor.setValue("Patient ID");
            }
        }catch(Exception e){}    
        //System.out.println("Testing");
    }        
    
    
    PopoverController popup;
    
    @FXML
    public void checkForBrands()
    {
        String tmp = txtAuto.getText();
        ArrayList<String> data = doc.getDrugBrandInfo(tmp);
        
        //System.out.println(tmp);
        Stage stage;
        if (popup != null)
        {
            popup.close();
        }
        
        if ( data.size() > 0 ) 
        {   
            popup = new PopoverController(txtAuto);
            popup.fillBrandList(data,tmp);
            
            txtAuto.setText("");
            
            stage = new Stage();
            
            Point2D point = txtAuto.localToScene(0.0, 0.0);
            
            stage.setX(point.getX());
            stage.setY(point.getY()+30);
            
            stage.setScene(new Scene(popup));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();
            
            /*
            stage.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                popup.close();
            });
            */
        
        }    
    
    }        
    

    @FXML
    private TextField amount;
    @FXML
    private ComboBox unit;
    @FXML
    private ListView prescription;
    @FXML
    private TextField txtAuto;
    @FXML
    private void addDrugtoPresc()
    {
        String drugName = txtAuto.getText();
        String drugAmount = amount.getText();
        String drugUnit = (String)unit.getSelectionModel().getSelectedItem();

        
        if (!drugName.equals(""))
        {
            if (!drugAmount.equals(""))
            {
                if (drugUnit != null)
                {
                    String tmp = drugName + " " + drugAmount + drugUnit;
                    prescription.getItems().add(prescription.getItems().size(), tmp);
                    txtAuto.setStyle("-fx-border-color: #999 #999 #999 #999;");
                    amount.setStyle("-fx-border-color: #999 #999 #999 #999;");
                    txtAuto.setText("");
                    amount.setText("");
                } else {
                    String tmp = drugName + " " + drugAmount;
                    prescription.getItems().add(prescription.getItems().size(), tmp);
                    txtAuto.setStyle("-fx-border-color: #999 #999 #999 #999;");
                    amount.setStyle("-fx-border-color: #999 #999 #999 #999;");
                    txtAuto.setText("");
                    amount.setText("");
                }
            } else {
                amount.setStyle("-fx-border-color: red;");
                txtAuto.setStyle("-fx-border-color: #999 #999 #999 #999;");
            }
        } else {
            txtAuto.setStyle("-fx-border-color: red;");
        }
    }
    
    @FXML
    private TextField txtAuto1;
    
    @FXML 
    private ListView prescription1;
    
    @FXML
    private void addTesttoPresc()
    {
        String testName = txtAuto1.getText();

        
        if (!testName.equals(""))
        {

            String tmp = testName;
            prescription1.getItems().add(prescription1.getItems().size(), tmp);
            txtAuto1.setStyle("-fx-border-color: #999 #999 #999 #999;");
            txtAuto1.setText("");
                
        } else {
            txtAuto1.setStyle("-fx-border-color: red;");
        }
    }
    
    @FXML private void removeDrugPresc()
    {
        int selectedIdx = prescription.getSelectionModel().getSelectedIndex();
        if ( selectedIdx >= 0 ) prescription.getItems().remove(selectedIdx);
    }
    
    @FXML private void removeTestPresc()
    {
        int selectedIdx = prescription1.getSelectionModel().getSelectedIndex();
        if ( selectedIdx >= 0 )  prescription1.getItems().remove(selectedIdx);
    }        
    
    
    @FXML
    private Button clearPresc;
    
    @FXML
    private void clearPrescription()
    {
        txtAuto.setText("");
        txtAuto1.setText("");
        amount.setText("");
        unit.getSelectionModel().clearSelection();
                
        prescription.getItems().clear();
        prescription1.getItems().clear();
    }
    
    @FXML private TextField testID;
    @FXML private ListView testListView;
    
    @FXML private void clearPatient()
    {
        patientSearchValue.setText("");
        patientFirstName.setText("");
        patientLastName.setText("");
        patientAge.setText("");
        patientGender.setText("");
        patientEmail.setText("");
        
        diagnosisText.setText("");
        
        hisTime1.setText("");   
        hisDetail1.setText("");
        hisTime2.setText("");   
        hisDetail2.setText("");
        hisTime3.setText("");   
        hisDetail3.setText("");
        
        allergyText.setText("");
        allergyView.getItems().clear();
        
        testID.setText("");
        try{
            testListView.getItems().clear();
        }catch(Exception e){}
        txtAuto.setText("");
        txtAuto1.setText("");
        amount.setText("");
        unit.getSelectionModel().clearSelection();
                
        prescription.getItems().clear();
        prescription1.getItems().clear();
    }        
    

    @FXML
    private TextArea diagnosisText;
    
    @FXML
    private Button savePatientDiag;
           
    @FXML
    private void savePatientDiagnosis(ActionEvent event) {
        
        boolean labTests = false;

        if(event.getSource()== savePatientDiag)
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.format(cal.getTime());
                    
            //diagnosisText.getText();
             
            try
            {					
                /////// getting diagnosis //////////////////////////////////////////////
                String diagnostic = diagnosisText.getText();

                /////// getting prescription ///////////////////////////////////////////
                List<String> drugs =  prescription.getItems();
                String presc = "";
                int tmpSize = drugs.size();
                if (tmpSize > 0)
                {
                    for (int count = 0; count < tmpSize; count++)
                    {
                            presc += drugs.get(count) + "|";
                    }
                    presc = presc.substring(0,presc.length()-1);
                }        
                /////// getting prescribed tests //////////////////////////////////////
                List<String> tests =  prescription1.getItems();
                String presc1 = "";
                String fee = "0";
                tmpSize = tests.size();
                if (tmpSize > 0)
                {
                    
                    for (int count = 0; count < tmpSize; count++)
                    {
                        fee = Integer.toString(Integer.parseInt(fee) + Integer.parseInt(doc.getLabFee(tests.get(count)))); 
                        presc1 += tests.get(count) + "|";
                    }
                    presc1 = presc1.substring(0,presc1.length()-1);
                }    
                if ( tests.size() > 0 )
                {
                    labTests = true;
                }    
                
                
                
                System.out.println(diagnostic+"\n");
                System.out.println(presc+"\n");
                System.out.println(presc1+"\n");

                String currentPatientID = patientSearchValue.getText();
                boolean saved1 = false;
                boolean saved2 = false;
                
                if (currentPatientID.length() > 0)
                {
                    if ( diagnostic.length() > 0 )
                    {
                        diagnosisText.setText("");
                        if ( ( presc.length() > 0 ) || ( presc1.length() > 0 ) ) 
                        {    
                            saved1 = doc.prescribe(presc, presc1, currentPatientID);
                        }
                        saved2 = doc.diagnose(diagnostic, currentPatientID);
                        if ((saved1 == true) || (saved2 == true)) 
                        {    
                            showSuccessIndicator();
                            
                            txtAuto.setText("");
                            txtAuto1.setText("");
                            amount.setText("");
                            unit.getSelectionModel().clearSelection();

                            prescription.getItems().clear();
                            prescription1.getItems().clear();
                            
                            
                        }
                        String billInfo = "consultant_id "+ doc.slmcRegNo +"," + "patient_id " + currentPatientID + ",laboratory_fee " + fee;
                        
                        if (labTests == true)
                        {        
                            doc.bill(billInfo, currentPatientID, fee);
                        }    
                    }    
                    System.out.println(currentPatientID);
                }
                
                
                
            }catch(Exception ex){}	
        }
    } 
    
    @FXML private Button showUserButton;
    
    @FXML private void showUser()
    {
        CurrentUserSummaryController user = new CurrentUserSummaryController(doc);
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
   
    
    /// Loading ther profile info ////////////////////////////////////////
  
    @FXML
    private TextField doctorName;
    @FXML    
    private TextField doctorNIC;
    @FXML
    private DatePicker doctorDOB;
    @FXML
    private TextField doctorAge;
    @FXML
    private ComboBox doctorGender;
    @FXML
    private TextField doctorNationality;
    @FXML
    private TextField doctorReligion;
    @FXML
    private TextField doctorMobile;
    @FXML
    private TextField doctorEmail;   
    @FXML
    private TextField doctorAddress;
    
    @FXML
    private TextField doctorRegistarionNo;
    @FXML
    private TextArea doctorEducation;
    @FXML
    private TextArea doctorTraining;  
    @FXML
    private TextArea doctorExperience;
    @FXML
    private TextArea doctorAchivements;  
    @FXML
    private TextArea doctorOther;
    @FXML
    private TextField doctorUserName;
    @FXML
    private TextField doctorUserType;
    @FXML
    private TextField doctorUserID;
    @FXML
    private TextField doctorPassword;
    @FXML
    private TextField doctorNewPassword;
    @FXML
    private TextField doctorConfirmPassword;
    @FXML
    private TableView availabilityTable;
            
    @FXML
    public void loadProfileData() 
    {

        HashMap<String,String> docPersonalInfo =  doc.getProfileInfo();
		
        doctorName.setText(docPersonalInfo.get("first_name") + " " + docPersonalInfo.get("last_name"));
        doctorNIC.setText(docPersonalInfo.get("nic"));
        doctorNationality.setText(docPersonalInfo.get("nationality"));
        doctorReligion.setText(docPersonalInfo.get("religion"));
        doctorMobile.setText(docPersonalInfo.get("mobile"));
        doctorEmail.setText(docPersonalInfo.get("email"));
        doctorAddress.setText(docPersonalInfo.get("address"));

        try{
                SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                Date birth = tmpdataformat.parse(docPersonalInfo.get("date_of_birth"));
                Calendar calendarBirth = Calendar.getInstance();
                calendarBirth.setTime(birth);
                Calendar calendarToday = Calendar.getInstance();
                String tmpage = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));
                
                String tmpDOB = docPersonalInfo.get("date_of_birth");
                
                int year = Integer.parseInt(tmpDOB.substring(0,4));
                int month = Integer.parseInt(tmpDOB.substring(5,7));        
                int date = Integer.parseInt(tmpDOB.substring(8,10));        
                doctorDOB.setValue(LocalDate.of(year, month, date));
                doctorAge.setText(tmpage);
        }catch(Exception e){e.printStackTrace();}
        
        try{
            String tmpGen = docPersonalInfo.get("gender");
            if (tmpGen.equals("m")){doctorGender.getSelectionModel().select("Male");}
            else {doctorGender.getSelectionModel().select("Female");}

            doctorRegistarionNo.setText(docPersonalInfo.get("slmc_reg_no"));
            doctorEducation.setText(docPersonalInfo.get("education"));
            doctorExperience.setText(docPersonalInfo.get("experienced_areas"));
            doctorTraining.setText(docPersonalInfo.get("training"));
            doctorAchivements.setText(docPersonalInfo.get("achievements"));
            doctorOther.setText(docPersonalInfo.get("experience"));
        }catch(Exception e){}
        
        doctorUserName.setText(docPersonalInfo.get("user_name"));
        doctorUserType.setText(docPersonalInfo.get("user_type"));
        doctorUserID.setText(docPersonalInfo.get("user_id"));
        
    } 
    
    @FXML
    private Node createPage(int pageIndex) {

        HashMap<String,String> daysHash = new HashMap<>();
        daysHash.put("1","Monday");
        daysHash.put("2","Tuesday");
        daysHash.put("3","Wednesday");
        daysHash.put("4","Thursday");
        daysHash.put("5","Friday");
        daysHash.put("6","Saturday");
        daysHash.put("7","Sunday");
        
        ArrayList<ArrayList<String>> currentTimeTableData0 = doc.doctorTimeTable();
	int noOfSlots = (currentTimeTableData0.size()-1);
        //System.out.println(noOfSlots);
        //System.out.println(currentTimeTableData0);
            
        final ObservableList<Availability> data = FXCollections.observableArrayList(); 
        for (int i = 1; i <= noOfSlots; i++)
        {
            data.add(new Availability(daysHash.get(currentTimeTableData0.get(i).get(0)), currentTimeTableData0.get(i).get(1), currentTimeTableData0.get(i).get(2) ));
        }        
        
        
        int fromIndex = pageIndex * 8;
        int toIndex = Math.min(fromIndex + 8, data.size());
        availabilityTable.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(availabilityTable);
    }
    
    @FXML
    private Pagination availabilityPagination;
    
    @FXML 
    private void createPagination(int dataSize)
    {
        availabilityPagination.setPageCount((dataSize / 8 + 1));
        availabilityPagination.setPageFactory(this::createPage);
    }
    
    @FXML
    public void MakeAvailabilityTable()
    {
        ArrayList<ArrayList<String>> currentTimeTableData0 = doc.doctorTimeTable();
        createPagination(currentTimeTableData0.size()-1);
    }
    
    @FXML
    private Button editBasicInfoButton;
    
    @FXML 
    private void editBasicInfo()
    {
        String currentState = editBasicInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            doctorName.setDisable(false);
            doctorNIC.setDisable(false);
            doctorGender.setDisable(false);
            doctorNationality.setDisable(false);
            doctorReligion.setDisable(false);
            doctorMobile.setDisable(false);
            doctorEmail.setDisable(false);
            doctorAddress.setDisable(false);
            
            editBasicInfoButton.setText("Save");
        }
        else if ( currentState.equals("Save"))
        {
            try{
                
                doctorName.setDisable(true);
                doctorNIC.setDisable(true);
                doctorGender.setDisable(true);
                doctorNationality.setDisable(true);
                doctorReligion.setDisable(true);
                doctorMobile.setDisable(true);
                doctorEmail.setDisable(true);
                doctorAddress.setDisable(true);

                String info = "";

                String[] name = doctorName.getText().split(" ");
                String gender = (String)doctorGender.getSelectionModel().getSelectedItem();
                if (gender.equals("Male")){gender = "m";}
                else {gender = "f";}
                //String marital = receptionMaritalComboDoc.getText();
                String nationality = (String)doctorNationality.getText();
                String religion = (String)doctorReligion.getText();
                String mobile = doctorMobile.getText();
                String email = doctorEmail.getText();
                String address = doctorAddress.getText();

                info += "first_name " + name[0] + "#last_name " + name[1];
                info += "#gender " + gender;
                info += "#nationality " + nationality;
                info += "#religion " + religion;
                info += "#mobile " + mobile;
                info += "#email " + email;
                info += "#address " + address;

                //System.out.println(info);
        
                boolean success = doc.updateProfileInfo(info);
            
                editBasicInfoButton.setText("Edit");
                if (success == true) showSuccessIndicator();
            }catch(Exception e){}
        }    
    }
    
    @FXML
    public ProgressIndicator saveProgress;
    
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
    
    @FXML private void addTimeSlot()
    {
        Stage stage= new Stage();
        NewDoctorTimeSlotController addslot = new NewDoctorTimeSlotController(doc,this);
        Scene scene = new Scene(addslot);
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
    
    @FXML private void removeTimeSlot()
    {
        Availability slot = (Availability)availabilityTable.getSelectionModel().getSelectedItem();
        String id = slot.getId();
        doc.removeDoctorTime(id);
        MakeAvailabilityTable();
        
    }        
    
    /*
    @FXML
    public Button saveSuccess;
    
    @FXML
    public void saveSuccessExit(ActionEvent event) {
 
    Stage stage; 
        if(event.getSource()== saveSuccess)
        {
            stage = (Stage) saveSuccess.getScene().getWindow();
            stage.close();
        }
    }
    */
    
    // Changing the profile pictute //
    
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

            doc.setProfilePic(imageName);
            editProfilePicButton.setText("Edit");
        }
        
    }
    
    public void loadProfileImage()
    {
        Image img;
        try{
            String image = doc.getProfilePic();
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
        AllMessagesController popup = new AllMessagesController(doc);
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
    
    
    @FXML
    private void waitFor()
    {
        long start = System.currentTimeMillis();
        long end = start + 5*1000; // 5 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end)
        { }
        System.out.println("testing..");
    }        
    
    @FXML
    private Button editDoctorInfoButton;
    
    @FXML 
    private void editDoctorInfo()
    {
        String currentState = editDoctorInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            doctorRegistarionNo.setDisable(false);
            doctorEducation.setDisable(false);
            doctorTraining.setDisable(false);  
            doctorExperience.setDisable(false);
            doctorAchivements.setDisable(false);  
            doctorOther.setDisable(false);
            
            editDoctorInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            doctorRegistarionNo.setDisable(true);
            doctorEducation.setDisable(true);
            doctorTraining.setDisable(true);  
            doctorExperience.setDisable(true);
            doctorAchivements.setDisable(true);  
            doctorOther.setDisable(true);
            
            String info = "";
				
            String education = doctorEducation.getText();
            String exp = doctorExperience.getText();
            String training = doctorTraining.getText();
            String academic = doctorAchivements.getText();
            String other = doctorOther.getText();


            info += "education " + education;
            info += "#training " + training;
            info += "#experienced_areas " + exp;
            info += "#experience " + other;
            info += "#achievements " + academic;

            //System.out.println(info);

            boolean success = doc.updateDoctorInfo(info);
            if (success == true) showSuccessIndicator();
            editDoctorInfoButton.setText("Edit");
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
            doctorUserName.setDisable(false);
            
            editUserInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            doctorUserName.setDisable(true);
            
            String info = "user_name " + doctorUserName.getText();
            boolean success = doc.updateAccountInfo(info);
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
            doctorPassword.setDisable(false);
            doctorNewPassword.setDisable(false);
            doctorConfirmPassword.setDisable(false);
            
            editPasswordInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            
            boolean result = false;
            //result = checkCurrentPassword(String userId, String password)
            
            if (result == true)
            {
                if ( doctorNewPassword.getText() == doctorConfirmPassword.getText())
                {
                    String info = "password " + doctorConfirmPassword.getText();
                    boolean success =  doc.updateAccountInfo(info);
                    
                    doctorPassword.setDisable(true);
                    doctorNewPassword.setDisable(true);
                    doctorConfirmPassword.setDisable(true);
                    
                    
                    if (success == true) showSuccessIndicator();
                    editPasswordInfoButton.setText("Edit");
                }    
            }
            
        }
    }
    
    @FXML
    private Button logoutButton;
    @FXML
    private void logout()
    {
        Stage stage= new Stage();
        LogoutController logout = new LogoutController(logoutButton,doc);
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
        
        setTodayAppointments();
        
        searchTypePatientDoctor.setValue("Patient ID");
        patientSearchValue.setPromptText("search value");
        allergyText.setPromptText("new Allergy");
        txtAuto.setPromptText("drug name");
    
        amount.setPromptText("100");
        unit.setValue("mg");
    
        txtAuto1.setPromptText("test name");
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
    private void checkAmount()
    {
        try{
            String tmpID = amount.getText();
            Boolean result = Validate.checkInt(tmpID);
            if (result == true)
            {
                if (popOver != null) popOver.hide(Duration.millis(500));

            } else {
                showPopup("Integer",amount);
            }
        }catch(Exception e){}    
    }   
    
    @FXML
    private void validatePatientID()
    {
        String tmpID = patientSearchValue.getText();
        if ( tmpID.length() == 9 )
        {
            String result = Validate.patientID(tmpID);
            if (result.equals("1"))
            {
                if (popOver != null) popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,patientSearchValue);
            }   
        } else { 
            showPopup("hmsxxxxpa",patientSearchValue);
        }   
    }   
    
    @FXML
    private void validateEmail()
    {        
        try{
            String tmpemail = doctorEmail.getText();
            String result = Validate.email(tmpemail);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,doctorEmail);
            }
        }catch(Exception e){}    
    }           
            
    
    @FXML 
    private void validateMobile()
    {    
        try{
            String tmpmobile = doctorMobile.getText();
            String result = Validate.mobile(tmpmobile);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,doctorMobile);
            }
        }catch(Exception e){}     
    }   
    
    @FXML 
    private void validateNIC()
    {    
        try{
            String tmpnic = doctorNIC.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup("xxxxxxxxxV",doctorNIC);
            }
        }catch(Exception e){}     
    }   
    
    @FXML 
    private void validatePatientNIC()
    {    
        try{
            String tmpnic = patientSearchValue.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup("xxxxxxxxxV",patientSearchValue);
            }
        }catch(Exception e){}     
    }   
    
    public void addFocusListener()
    {        
        doctorNIC.focusedProperty().addListener(new ChangeListener<Boolean>()
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
        
        doctorMobile.focusedProperty().addListener(new ChangeListener<Boolean>()
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
        
        doctorEmail.focusedProperty().addListener(new ChangeListener<Boolean>()
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
        
        patientSearchValue.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if ( searchTypePatientDoctor.getValue().toString().equals("Patient ID") )
                {    
                    if (newPropertyValue){} else { validatePatientID(); }
                    
                } else if ( searchTypePatientDoctor.getValue().toString().equals("NIC") )
                {    
                    if (newPropertyValue){} else { validatePatientNIC(); }
                    
                } else if ( searchTypePatientDoctor.getValue().toString().equals("Name") )
                {    
                    if (newPropertyValue){} else { }
                }
            }
        });
        
    }  
    
}
