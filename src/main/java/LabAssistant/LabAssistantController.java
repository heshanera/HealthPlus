package LabAssistant;

import com.hms.hms_test_2.AllMessagesController;
import com.hms.hms_test_2.CurrentUserSummaryController;
import com.hms.hms_test_2.ErrorController;
import com.hms.hms_test_2.LogoutController;
import com.hms.hms_test_2.SuccessIndicatorController;
import java.io.File;
import java.io.FileOutputStream;
import javafx.fxml.FXMLLoader;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.scene.control.agenda.Agenda;
import org.controlsfx.control.PopOver;

/**
 *
 * @author heshan
 */

public class LabAssistantController extends AnchorPane {

    public LabAssistant lab;
    public String username;
    FXMLLoader fxmlLoader;
    
    public LabAssistantController(String username) {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LabAssistant.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        lab = new LabAssistant(username);
        this.username = username;
        lab.saveLogin(username);
        
        //System.out.println("Testing...");
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML
    private PieChart piechart;
    @FXML 
    public void fillPieChart() {
        
        ArrayList<ArrayList<String>> data = lab.lastMonthsReports(12);
        
        String[] test = {   
                            "Blood Grouping & Rh","Lipid Profile Test","LFT","RFT",
                            "HIV","CPK","Pathalogy Test",
                            "Complete Blood Count"
                        };
        
        
        int tmpSize = test.length;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(int i = 0; i < tmpSize; i++)
        {
            pieChartData.add(new PieChart.Data(test[i], Integer.parseInt(data.get(1).get(i))));
        }    
        
        pieChartData.forEach(data1 ->
                data1.nameProperty().bind(
                        Bindings.concat(
                                data1.getName(), " (", data1.pieValueProperty(), ")"
                        )
                )
        );
        
        piechart.setLegendSide(Side.BOTTOM);
        piechart.setLegendVisible(true);
        
        piechart.setData(pieChartData);
        
        
        
    }
    
    @FXML ComboBox serachType; 
    
    @FXML TextField appointmentIDtext;
    @FXML TextField patientNametext;
    @FXML TextField patientAgetext;
    @FXML TextField patientGendertext;
    @FXML TextField patientConsultanttext;
    
    @FXML TabPane reportTabs;
    
    @FXML Tab pt;
    @FXML Tab lpt;
    @FXML Tab bg;
    @FXML Tab cbc;
    @FXML Tab lft;
    @FXML Tab rft;
    @FXML Tab cpk;
    @FXML Tab hiv;
    
    public void setTabsDisabled()
    {
        pt.setDisable(true);
        lpt.setDisable(true);
        bg.setDisable(true);
        cbc.setDisable(true);
        lft.setDisable(true);
        rft.setDisable(true);
        cpk.setDisable(true);
        hiv.setDisable(true);
    }        
    
    public void showReport(String reportID)
    {

            String type = "";
            ArrayList<ArrayList<String>> data = null;
            
            if ( reportID.substring(0,2).equals("ur") ) {
            
                data = lab.getUrineFullReport(reportID);
                type = "ur";
                
            } else if ( reportID.substring(0,2).equals("li") ) {
                
                data = lab.getLipidTestReport(reportID);
                type = "li";
                
            } else if ( reportID.substring(0,2).equals("bg") ) {
            
                data = lab.getBloodGroupingRh(reportID);
                type = "bg";
                
            } else if ( reportID.substring(0,3).equals("cbc") ) {
            
                data = lab.getCompleteBloodCount(reportID);
                type = "cbc";
                
            } else if ( reportID.substring(0,2).equals("lv") ) {
            
                data = lab.getLiverFunctionTest(reportID);
                type = "lv";
                
            } else if ( reportID.substring(0,2).equals("re") ) {
            
                data = lab.getRenalFunctionTest(reportID);
                type = "re";
                
            } else if ( reportID.substring(0,4).equals("scpt") ) {
            
                data = lab.getSeriumCreatinePhosphokinaseTotal(reportID);
                type = "scpt";
                
            } else if ( reportID.substring(0,3).equals("scp") ) {
            
                data = lab.getSeriumCreatinePhosphokinase(reportID);
                type = "scp";
                
            }     
            
            if (data.size() > 1){
                LabReportPreviewController preview = new LabReportPreviewController(lab);
                preview.setData(data,type);

                Stage stage = new Stage();
                Scene scene = new Scene(preview);
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
            } else {
                
                //showErrorPopup("No Report", appointmentIDtext);
            }
    
    }        
    
    
    @FXML public void searchAppointment()
    {
        
        if ("Appointment ID".equals(serachType.getValue().toString()))
        {
            String appID = appointmentIDtext.getText();
        
            ArrayList<ArrayList<String>> data =  lab.getPatientDetails(appID);
            System.out.println(data);
            
            String test = "";
            try{
                test = data.get(1).get(4);
            }catch(Exception e){
                test = "false";
            }
            
            switch (test)
            {
                case "t001":
                    setTabsDisabled();
                    reportTabs.getSelectionModel().select(0);
                    pt.setDisable(false);
                    break;
                case "t002":
                    setTabsDisabled();
                    reportTabs.getSelectionModel().select(1);
                    lpt.setDisable(false);
                    break;
                case "t003":
                    setTabsDisabled();
                    reportTabs.getSelectionModel().select(2);
                    bg.setDisable(false);
                    break;
                case "t004":
                    setTabsDisabled();
                    reportTabs.getSelectionModel().select(3);
                    cbc.setDisable(false);
                    break;
                case "t005":
                    setTabsDisabled();
                    reportTabs.getSelectionModel().select(4);
                    lft.setDisable(false);
                    break;
                case "t006":
                    setTabsDisabled();
                    reportTabs.getSelectionModel().select(5);
                    rft.setDisable(false);
                    break;
                case "t007":
                    setTabsDisabled();
                    reportTabs.getSelectionModel().select(6);
                    cpk.setDisable(false);
                    break;
                case "t008":
                    setTabsDisabled();
                    reportTabs.getSelectionModel().select(7);
                    hiv.setDisable(false);
                    break;
                default:
                    showErrorPopup("No Appointment", appointmentIDtext);
                    break;
            }    
            
            patientNametext.setText(data.get(1).get(0) + " " + data.get(1).get(1) );

             try{
                    SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                    Date birth = tmpdataformat.parse(data.get(1).get(3));
                    Calendar calendarBirth = Calendar.getInstance();
                    calendarBirth.setTime(birth);
                    Calendar calendarToday = Calendar.getInstance();
                    String tmpage = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));

                    String tmpDOB = data.get(1).get(3);

                    int year = Integer.parseInt(tmpDOB.substring(0,4));
                    int month = Integer.parseInt(tmpDOB.substring(5,7));        
                    int date = Integer.parseInt(tmpDOB.substring(8,10));        
                    patientAgetext.setText(tmpage);
            }catch(Exception e){e.printStackTrace();}

            String tmpGen = data.get(1).get(2);
            if (tmpGen.equals("m")){patientGendertext.setText("Male");}
            else {patientGendertext.setText("Female");}

            Date date = new Date();
            precrptionDate.setText(date.toString());
            
        } else {
            
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
        
            String reportID = appointmentIDtext.getText();
            String type = "";
            ArrayList<ArrayList<String>> data = null;
            
            if ( reportID.substring(0,2).equals("ur") ) {
            
                data = lab.getUrineFullReport(reportID);
                type = "ur";
                
            } else if ( reportID.substring(0,2).equals("li") ) {
                
                data = lab.getLipidTestReport(reportID);
                type = "li";
                
            } else if ( reportID.substring(0,2).equals("bg") ) {
            
                data = lab.getBloodGroupingRh(reportID);
                type = "bg";
                
            } else if ( reportID.substring(0,3).equals("cbc") ) {
            
                data = lab.getCompleteBloodCount(reportID);
                type = "cbc";
                
            } else if ( reportID.substring(0,2).equals("lv") ) {
            
                data = lab.getLiverFunctionTest(reportID);
                type = "lv";
                
            } else if ( reportID.substring(0,2).equals("re") ) {
            
                data = lab.getRenalFunctionTest(reportID);
                type = "re";
                
            } else if ( reportID.substring(0,4).equals("scpt") ) {
            
                data = lab.getSeriumCreatinePhosphokinaseTotal(reportID);
                type = "scpt";
                
            } else if ( reportID.substring(0,3).equals("scp") ) {
            
                data = lab.getSeriumCreatinePhosphokinase(reportID);
                type = "scp";
                
            }     
            
            if (data.size() > 1){
                LabReportPreviewController preview = new LabReportPreviewController(lab);
                preview.setData(data,type);

                Stage stage = new Stage();
                Scene scene = new Scene(preview);
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
            } else {
                
                showErrorPopup("No Report", appointmentIDtext);
            }
        }   
        
        
        
        /*
        ArrayList<ArrayList<String>> data2 = lab.getPrescriptions(patientID);
     
        Stage stage= new Stage();
        PrescriptionListController prescrip = new PrescriptionListController();
        prescrip.fillTableData(data2,this);
        Scene scene = new Scene(prescrip);
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
        */
        
    }  
    
    @FXML private void serachTypeChanged()
    {
        clear();
    }        
    
    public void clear()
    {
        appointmentIDtext.setText("");
        patientNametext.setText("");
        patientAgetext.setText("");
        patientGendertext.setText("");
        precrptionDate.setText("");
        
        setTabsDisabled();
    }        
    
    
    @FXML TextField precrptionDate;
    @FXML ListView testList;
    public void fillPrescriptionInfo(String doc, String tests, String date)
    {
        
        patientConsultanttext.setText(doc);
        
        ObservableList<String> items = FXCollections.observableArrayList();     
        String[] tmpTests = tests.split("\\|");
        //System.out.println(tests);
        int noOfTests = tmpTests.length;
        for(int i = 0; i < noOfTests; i++ )
        {   
            items.add(tmpTests[i]);
        }    
        testList.setItems(items);
        
        precrptionDate.setText(date);
        
    }        
    
    
    
    @FXML private AreaChart<String, Number> labAppointments;
    
    public void fillLabAppiontments()
    {
        
        HashMap<Integer,String> months = new HashMap<Integer,String>();
        months.put(1,"January");
        months.put(2,"February");
        months.put(3,"March");
        months.put(4,"April");
        months.put(5,"May");
        months.put(6,"June");
        months.put(7,"July");
        months.put(8,"August");
        months.put(9,"September");
        months.put(10,"October");
        months.put(11,"November");
        months.put(12,"December");
        
        
        
        ArrayList<String> data = lab.lastMonthsAppointments();
        int months2 = data.size();
        
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);

        XYChart.Series appointments= new XYChart.Series();
        appointments.setName("Appointments");
        
        int i;
        for(i = (months2-1); i >= 0; i-- )
        {
            int tmp = 0;
            if (month-i > 0 ) tmp = month-i;
            else tmp = 12-i-month;
                
            appointments.getData().add(new XYChart.Data(months.get(tmp), Integer.parseInt(data.get(i))));
        }    

      
        labAppointments.getData().addAll(appointments);
    
    
    }
    
    @FXML private Label todayAppointments;
    
    public void fillTodayAppointments()
    {
        String apps = lab.getTodayAppointments();
        todayAppointments.setText(apps);
    }    

    @FXML Agenda labAppointmentTable;
    
    public void setAppointments()
    {
        
        labAppointmentTable.setDisable(false);
        
        ArrayList<ArrayList<String>> tableData0 = lab.getAppointments();
        int noOfApp = (tableData0.size());
        System.out.println(tableData0);
        //System.out.println(noOfApp);
        //System.out.println(tableData0);
        
        final List<Agenda.AppointmentImplLocal> Appointments = FXCollections.observableArrayList();
        
        for (int i = 1; i < noOfApp; i++)
        {
            String[] tmp = tableData0.get(i).get(0).split(" ");
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
            
            String summary = tableData0.get(i).get(1);
            String group = "";
            if (tableData0.get(i).get(2).equals("0")) group = "group15";
            else group = "group3";
            
            Appointments.add(
                new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime(finDate.atTime(hour1, minute1))
                .withEndLocalDateTime(finDate.atTime(hour, minute))   
                .withDescription(tableData0.get(i).get(0))
                .withSummary(summary)
                .withDescription("A much longer test description")        
                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass(group))    
            );
        }
        
        labAppointmentTable.appointments().addAll(Appointments);
    }
    
   
    /*
    @FXML ComboBox selectTestType;
    @FXML TextArea testDescription;
   
    public void loadLabTests()
    {
        ArrayList<String> data = lab.getLabTestNames();
        selectTestType.getItems().addAll(data);
    }    

    @FXML private void fillTestInfo()
    {
        String testName =  (String)selectTestType.getValue();
        String desc = lab.getLabTestInfo(testName);
        testDescription.setText(desc);
    }        
    */
    
    
    /// Loading Test Details /////////////////////////////////////////////
    
   @FXML private TextField appearancetxt;
   @FXML private TextField sgRefractometertxt;
   @FXML private TextField phtxt;
   @FXML private TextField proteintxt;
   @FXML private TextField glucosetxt;
   @FXML private TextField ketoneBodiestxt;
   @FXML private TextField bilirubintxt;

   @FXML private TextField urobilirubintxt;
   @FXML private TextField contrifugedDepositsphaseContrastMicroscopytxt;
   @FXML private TextField pusCellstxt;
   @FXML private TextField redCellstxt;
   @FXML private TextField epithelialCellstxt;
   @FXML private TextField cristalstxt;
   @FXML private TextField caststxt;
    
    @FXML private void savePathalogyTest()
    {
        /*
     String tst_ur_id = tst_ur_idtxt.getText();
     String prescription_id = prescription_idtxt.getText();
     */
     String appID = appointmentIDtext.getText();   
        
     String appearance = appearancetxt.getText();
     String sgRefractometer = sgRefractometertxt.getText();
     String ph = phtxt.getText();
     String protein = proteintxt.getText();
     String glucose = glucosetxt.getText();
     String ketoneBodies = ketoneBodiestxt.getText();
     String bilirubin = bilirubintxt.getText();
     String urobilirubin = urobilirubintxt.getText();
     String contrifugedDepositsphaseContrastMicroscopy = contrifugedDepositsphaseContrastMicroscopytxt.getText();
     String pusCells = pusCellstxt.getText();
     String redCells = redCellstxt.getText();
     String epithelialCells = epithelialCellstxt.getText();
     String casts = caststxt.getText();
     String cristals = cristalstxt.getText();
        
     String result =   lab.UrineFullReport(appID,appearance,sgRefractometer,ph,protein,glucose,ketoneBodies,bilirubin,
                        urobilirubin,contrifugedDepositsphaseContrastMicroscopy,pusCells,redCells,
                        epithelialCells,casts,cristals);
        if (!result.equals("")) 
        {
            
            showReport(result);
            
            appointmentIDtext.setText(""); 
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
            
            appearancetxt.setText("");
            sgRefractometertxt.setText("");
            phtxt.setText("");
            proteintxt.setText("");
            glucosetxt.setText("");
            ketoneBodiestxt.setText("");
            bilirubintxt.setText("");
            urobilirubintxt.setText("");
            contrifugedDepositsphaseContrastMicroscopytxt.setText("");
            pusCellstxt.setText("");
            redCellstxt.setText("");
            epithelialCellstxt.setText("");
            caststxt.setText("");
            cristalstxt.setText("");
        }    
     
    }        
    
    @FXML private void clearPathalogyTest()
    {   
        appearancetxt.setText("");
        sgRefractometertxt.setText("");
        phtxt.setText("");
        proteintxt.setText("");
        glucosetxt.setText("");
        ketoneBodiestxt.setText("");
        bilirubintxt.setText("");
        urobilirubintxt.setText("");
        contrifugedDepositsphaseContrastMicroscopytxt.setText("");
        pusCellstxt.setText("");
        redCellstxt.setText("");
        epithelialCellstxt.setText("");
        caststxt.setText("");
        cristalstxt.setText("");
        
        clear();
    }
    
    
    
    @FXML private TextField cholestrolHDLtxt;
    @FXML private TextField cholestrolLDLtxt;
    @FXML private TextField triglyceridestxt;
    @FXML private TextField totalCholestrolLDLHDLratiotxt;
    
    @FXML private void saveLipidTest()
    {
        String appointmentId = appointmentIDtext.getText();
        
        String cholestrolHDL = cholestrolHDLtxt.getText();
        String cholestrolLDL = cholestrolLDLtxt.getText();
        String triglycerides = triglyceridestxt.getText();
        String totalCholestrolLDLHDLratio = totalCholestrolLDLHDLratiotxt.getText();

        String result =  lab.LipidTest(appointmentId,cholestrolHDL,cholestrolLDL,triglycerides,totalCholestrolLDLHDLratio );

        if (!result.equals("")) 
        {
            
            showReport(result);
            
            appointmentIDtext.setText(""); 
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
            
            cholestrolHDLtxt.setText("");
            cholestrolLDLtxt.setText("");
            triglyceridestxt.setText("");
            totalCholestrolLDLHDLratiotxt.setText("");
        }    
        
    }        
    
    @FXML private void clearLipidTest()
    {
        cholestrolHDLtxt.setText("");
        cholestrolLDLtxt.setText("");
        triglyceridestxt.setText("");
        totalCholestrolLDLHDLratiotxt.setText("");
    
        clear();
    }
    
    @FXML private TextField bloodGtxt;
    @FXML private TextField rhDtxt;
    
    @FXML private void saveBloodGroupTest()
    {
        String appointmentId = appointmentIDtext.getText();
        
        String bloodG = bloodGtxt.getText();
        String rhD = rhDtxt.getText();
        
        String result =  lab.BloodGroupingTest(appointmentId,bloodG,rhD);

        if (!result.equals("")) 
        {
            
            showReport(result);
            
            appointmentIDtext.setText(""); 
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
            
            bloodGtxt.setText("");
            rhDtxt.setText("");
        } 
    
    }
    
    @FXML private void clearBloodGroupTest()
    {
        bloodGtxt.setText("");
        rhDtxt.setText("");
        
        clear();
    }
    
    @FXML private TextField totalWhiteCellCounttxt;
    @FXML private TextField differentialCounttxt;
    @FXML private TextField neutrophilstxt;
    @FXML private TextField lymphocytestxt;
    @FXML private TextField monocytestxt;
    @FXML private TextField eosonophilstxt;
    @FXML private TextField basophilstxt;
    @FXML private TextField haemoglobintxt;
    @FXML private TextField redBloodCellstxt;
    @FXML private TextField meanCellVolumetxt;
    @FXML private TextField haematocrittxt;
    @FXML private TextField meanCellHaemoglobintxt;
    @FXML private TextField mchConcentrationtxt;
    @FXML private TextField redCellsDistributionWidthtxt;
    @FXML private TextField plateletCounttxt;
    
    @FXML private void saveCompleteBlood()
    {
        String appointmentId = appointmentIDtext.getText();
        
        String totalWhiteCellCount = totalWhiteCellCounttxt.getText();
        String differentialCount = differentialCounttxt.getText();
        String neutrophils = neutrophilstxt.getText();
        String lymphocytes = lymphocytestxt.getText();
        String monocytes = monocytestxt.getText();
        String eosonophils = eosonophilstxt.getText();
        String basophils = basophilstxt.getText();
        String haemoglobin = haemoglobintxt.getText();
        String redBloodCells = redBloodCellstxt.getText();
        String meanCellVolume = meanCellVolumetxt.getText();
        String haematocrit = haematocrittxt.getText();
        String meanCellHaemoglobin = meanCellHaemoglobintxt.getText();
        String mchConcentration = mchConcentrationtxt.getText();
        String redCellsDistributionWidth = redCellsDistributionWidthtxt.getText();
        String plateletCount = plateletCounttxt.getText();
        
        
        String result = lab.completeBloodCount(appointmentId,totalWhiteCellCount,differentialCount,neutrophils,lymphocytes,monocytes,
                eosonophils,basophils,haemoglobin,redBloodCells,meanCellVolume,haematocrit,meanCellHaemoglobin,
                mchConcentration,redCellsDistributionWidth,plateletCount);
        
        if (!result.equals("")) 
        {
            
            showReport(result);
            
            appointmentIDtext.setText(""); 
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
            
            totalWhiteCellCounttxt.setText("");
            differentialCounttxt.setText("");
            neutrophilstxt.setText("");
            lymphocytestxt.setText("");
            monocytestxt.setText("");
            eosonophilstxt.setText("");
            basophilstxt.setText("");
            haemoglobintxt.setText("");
            redBloodCellstxt.setText("");
            meanCellVolumetxt.setText("");
            haematocrittxt.setText("");
            meanCellHaemoglobintxt.setText("");
            mchConcentrationtxt.setText("");
            redCellsDistributionWidthtxt.setText("");
            plateletCounttxt.setText("");
        } 
        
    
    }
    
    @FXML private void clearCompleteBlood()
    {
        totalWhiteCellCounttxt.setText("");
        differentialCounttxt.setText("");
        neutrophilstxt.setText("");
        lymphocytestxt.setText("");
        monocytestxt.setText("");
        eosonophilstxt.setText("");
        basophilstxt.setText("");
        haemoglobintxt.setText("");
        redBloodCellstxt.setText("");
        meanCellVolumetxt.setText("");
        haematocrittxt.setText("");
        meanCellHaemoglobintxt.setText("");
        mchConcentrationtxt.setText("");
        redCellsDistributionWidthtxt.setText("");
        plateletCounttxt.setText("");
        
        clear();
    
    }
    
    
    @FXML private TextField creatininetxt;
    @FXML private TextField ureatxt;
    @FXML private TextField totalBilirubintxt;
    @FXML private TextField directBilirubintxt;
    @FXML private TextField sgotasttxt;
    @FXML private TextField sgptalttxt;
    @FXML private TextField alkalinePhospatestxt;
    
    @FXML private void saveRenalTest()
    {
        String appointmentId = appointmentIDtext.getText();
        
        String creatinine = creatininetxt.getText();
        String urea = ureatxt.getText();
        String totalBilirubin = totalBilirubintxt.getText();
        String directBilirubin = directBilirubintxt.getText();
        String sgotast = sgotasttxt.getText();
        String sgptalt = sgptalttxt.getText();
        String alkalinePhospates = alkalinePhospatestxt.getText();
        
        String result = lab.RenalFunctionTest(appointmentId,creatinine,urea,totalBilirubin,directBilirubin,sgotast,sgptalt,alkalinePhospates );
        
        if (!result.equals("")) 
        {
            
            showReport(result);
            
            appointmentIDtext.setText(""); 
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
            
            creatininetxt.setText("");
            ureatxt.setText("");
            totalBilirubintxt.setText("");
            directBilirubintxt.setText("");
            sgotasttxt.setText("");
            sgptalttxt.setText("");
            alkalinePhospatestxt.setText("");
        } 
        

    
    }        
    
    @FXML private void clearRenalTest()
    {
        creatininetxt.setText("");
        ureatxt.setText("");
        totalBilirubintxt.setText("");
        directBilirubintxt.setText("");
        sgotasttxt.setText("");
        sgptalttxt.setText("");
        alkalinePhospatestxt.setText("");
        
        clear();
    }        
            
    @FXML private TextField cpkTotaltxt;
    
    @FXML private void saveCreatineTest()
    {
        String appointmentId = appointmentIDtext.getText();
        String cpkTotal = cpkTotaltxt.getText();
        
        String result = lab.SeriumCreatinePhosphokinaseTotal(appointmentId,cpkTotal);
        
        if (!result.equals("")) 
        {
            
            showReport(result);
            
            appointmentIDtext.setText(""); 
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
            
            cpkTotaltxt.setText("");
        } 
        
    }
    
    @FXML private void clearCreatineTest()
    {
        cpkTotaltxt.setText("");
        
        clear();
    }
    
    
    
    @FXML private TextField hiv12ELISAtxt;
    
    @FXML private void saveHIV()
    {
        String appointmentId = appointmentIDtext.getText();
        String hiv12ELISA = hiv12ELISAtxt.getText();
        
        String result = lab.SeriumCreatinePhosphokinase(appointmentId,hiv12ELISA);
        
        if (!result.equals("")) 
        {
            
            showReport(result);
            
            appointmentIDtext.setText(""); 
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
            
            hiv12ELISAtxt.setText("");
        } 
    }
    
    @FXML private void clearHIV()
    {
        hiv12ELISAtxt.setText("");
        
        clear();
    }        
    
    
    @FXML private TextField totalProteintxt;
    @FXML private TextField albumintxt;
    @FXML private TextField globulintxt;
    @FXML private TextField totalBilirubintxt2;
    @FXML private TextField directBilirubintxt2;
    @FXML private TextField sgotasttxt2;
    @FXML private TextField sgptalttxt2;
    @FXML private TextField alkalinePhospatestxt2;
    
    @FXML private void saveLiverFunctionTest()
    {
        String appointmentId = appointmentIDtext.getText();
        
        String totalProtein = totalProteintxt.getText();
        String albumin = albumintxt.getText();
        String globulin = globulintxt.getText();
        String totalBilirubin = totalBilirubintxt2.getText();
        String directBilirubin = directBilirubintxt2.getText();
        String sgotast = sgotasttxt2.getText();
        String sgptalt = sgptalttxt2.getText();
        String alkalinePhospates = alkalinePhospatestxt2.getText();
        
        String result = lab.liverFunctionTest(appointmentId,totalProtein,albumin,globulin,totalBilirubin,directBilirubin,sgotast,
                         sgptalt,alkalinePhospates);
        
       if (!result.equals("")) 
        {
            
            showReport(result);
            
            appointmentIDtext.setText(""); 
            patientNametext.setText("");
            patientAgetext.setText("");
            patientGendertext.setText("");
            precrptionDate.setText("");
            
            totalProteintxt.setText("");
            albumintxt.setText("");
            globulintxt.setText("");
            totalBilirubintxt2.setText("");
            directBilirubintxt2.setText("");
            sgotasttxt2.setText("");
            sgptalttxt2.setText("");
            alkalinePhospatestxt2.setText("");
        }
        
    
    }        
    
    
    @FXML private void clearLiverFunctionTest()
    {
        totalProteintxt.setText("");
        albumintxt.setText("");
        globulintxt.setText("");
        totalBilirubintxt2.setText("");
        directBilirubintxt2.setText("");
        sgotasttxt2.setText("");
        sgptalttxt2.setText("");
        alkalinePhospatestxt2.setText("");
        
        clear();
    
    }
    
    @FXML private Button showUserButton;
    
    @FXML private void showUser()
    {
        CurrentUserSummaryController user = new CurrentUserSummaryController(lab);
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

            lab.setProfilePic(imageName);
            editProfilePicButton.setText("Edit");
        }
        
    }
    
    public void loadProfileImage()
    {
        Image img;
        try{
            String image = lab.getProfilePic();
            img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
        }catch(Exception e){
            img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
        }
        profileImage.setImage(img);
        
    } 
    
    ////////////////////////////////
    
    ////// Loading messages ////////
    
    PopOver popOver = new PopOver();
    
    @FXML private Button AllMessages;
    
    @FXML
    private void showAllMessages()
    { 

        if (popOver == null) 
        {
            popOver = new PopOver();
        }
        AllMessagesController popup = new AllMessagesController(lab);
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
    private TextField labName;
    @FXML    
    private TextField labNIC;
    @FXML
    private DatePicker labDOB;
    @FXML
    private TextField labAge;
    @FXML
    private ComboBox labGender;
    @FXML
    private TextField labNationality;
    @FXML
    private TextField labReligion;
    @FXML
    private TextField labMobile;
    @FXML
    private TextField labEmail;   
    @FXML
    private TextField labAddress;
    
    @FXML
    private TextArea labEducation;
    @FXML
    private TextArea labTraining;  
    @FXML
    private TextArea labExperience;
    @FXML
    private TextArea labAchivements;  
    @FXML
    private TextArea labOther;
    @FXML
    private TextField labUserName;
    @FXML
    private TextField labUserType;
    @FXML
    private TextField labUserID;
    @FXML
    private TextField labPassword;
    @FXML
    private TextField labNewPassword;
    @FXML
    private TextField labConfirmPassword;

    
    @FXML
    public void loadProfileData() 
    {

        HashMap<String,String> docPersonalInfo =  lab.getProfileInfo();
		
        labName.setText(docPersonalInfo.get("first_name") + " " + docPersonalInfo.get("last_name"));
        labNIC.setText(docPersonalInfo.get("nic"));
        labNationality.setText(docPersonalInfo.get("nationality"));
        labReligion.setText(docPersonalInfo.get("religion"));
        labMobile.setText(docPersonalInfo.get("mobile"));
        labEmail.setText(docPersonalInfo.get("email"));
        labAddress.setText(docPersonalInfo.get("address"));

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
                labDOB.setValue(LocalDate.of(year, month, date));
                labAge.setText(tmpage);
        }catch(Exception e){e.printStackTrace();}
        
        try{
            String tmpGen = docPersonalInfo.get("gender");
            if (tmpGen.equals("m")){labGender.getSelectionModel().select("Male");}
            else {labGender.getSelectionModel().select("Female");}

            labEducation.setText(docPersonalInfo.get("education"));
            labExperience.setText(docPersonalInfo.get("experience"));
            labTraining.setText(docPersonalInfo.get("training"));
            labAchivements.setText(docPersonalInfo.get("achievements"));
            labOther.setText(docPersonalInfo.get("experienced_areas"));
        }catch(Exception e){}
            
        labUserName.setText(docPersonalInfo.get("user_name"));
        labUserType.setText(docPersonalInfo.get("user_type"));
        labUserID.setText(docPersonalInfo.get("user_id"));
        
    }  
    
    
    @FXML
    private Button editBasicInfoButton;
   
    @FXML 
    private void editBasicInfo()
    {
        String currentState = editBasicInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            labName.setDisable(false);
            labNIC.setDisable(false);
            labGender.setDisable(false);
            labNationality.setDisable(false);
            labReligion.setDisable(false);
            labMobile.setDisable(false);
            labEmail.setDisable(false);
            labAddress.setDisable(false);
            
            editBasicInfoButton.setText("Save");
        }
        else if ( currentState.equals("Save"))
        {
            labName.setDisable(true);
            labNIC.setDisable(true);
            labGender.setDisable(true);
            labNationality.setDisable(true);
            labReligion.setDisable(true);
            labMobile.setDisable(true);
            labEmail.setDisable(true);
            labAddress.setDisable(true);
            
            String info = "";
				
            String[] name = labName.getText().split(" ");
            String gender = (String)labGender.getSelectionModel().getSelectedItem();
            if (gender.equals("Male")){gender = "m";}
            else {gender = "f";}
            //String marital = receptionMaritalComboDoc.getText();
            String nationality = (String)labNationality.getText();
            String religion = (String)labReligion.getText();
            String mobile = labMobile.getText();
            String email = labEmail.getText();
            String address = labAddress.getText();

            info += "first_name " + name[0] + "#last_name " + name[1];
            info += "#gender " + gender;
            info += "#nationality " + nationality;
            info += "#religion " + religion;
            info += "#mobile " + mobile;
            info += "#email " + email;
            info += "#address " + address;

            //System.out.println(info);

            boolean success = lab.updateProfileInfo(info);
            
            editBasicInfoButton.setText("Edit");
            //saveProgress.setProgress(0.5);  
            if (success == true) showSuccessIndicator();
            //waitFor();
            
            
            //stage.close();
            
        }    
    }
    
    @FXML
    private Button editLabInfoButton;
    
    @FXML 
    private void editLabInfo()
    {
        String currentState = editLabInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            labEducation.setDisable(false);
            labTraining.setDisable(false);  
            labExperience.setDisable(false);
            labAchivements.setDisable(false);  
            labOther.setDisable(false);
            
            editLabInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            labEducation.setDisable(true);
            labTraining.setDisable(true);  
            labExperience.setDisable(true);
            labAchivements.setDisable(true);  
            labOther.setDisable(true);
            
            String info = "";
				
            String education = labEducation.getText();
            String exp = labExperience.getText();
            String training = labTraining.getText();
            String academic = labAchivements.getText();
            String other = labOther.getText();


            info += "education " + education;
            info += "#training " + training;
            info += "#other " + other;
            info += "#experience " + exp;
            info += "#achievements " + academic;

            //System.out.println(info);

            boolean success = lab.updateLabAssistantInfo(info);
            if (success == true) showSuccessIndicator();
            editLabInfoButton.setText("Edit");
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
            labUserName.setDisable(false);
            
            editUserInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            labUserName.setDisable(true);
            
            String info = "user_name " + labUserName.getText();
            boolean success = lab.updateAccountInfo(info);
            if (success == true) showSuccessIndicator();
            editUserInfoButton.setText("Edit");
        }
    }    
    
    public void addFocusListener()
    {        
        labPassword.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    System.out.println("Textfield on focus");
                }
                else
                {
                    System.out.println("Textfield out focus");
                }
            }
        });
    }   
    
    @FXML
    private Button editPasswordInfoButton;
    
    @FXML 
    private void editPasswordInfo()
    {
        String currentState = editPasswordInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            labPassword.setDisable(false);
            labNewPassword.setDisable(false);
            labConfirmPassword.setDisable(false);
            
            editPasswordInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            
            boolean result = false;
            //result = checkCurrentPassword(String userId, String password)
            
            if (result == true)
            {
                if ( labNewPassword.getText() == labConfirmPassword.getText())
                {
                    String info = "password " + labConfirmPassword.getText();
                    boolean success =  lab.updateAccountInfo(info);
                    
                    labPassword.setDisable(true);
                    labNewPassword.setDisable(true);
                    labConfirmPassword.setDisable(true);
                    
                    
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
    private Button saveSuccess;
    
    @FXML
    private void saveSuccessExit(ActionEvent event) {
 
    Stage stage; 
        if(event.getSource()== saveSuccess)
        {
            stage = (Stage) saveSuccess.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML
    private Button logoutButton;
    @FXML
    private void logout()
    {
        Stage stage= new Stage();
        LogoutController logout = new LogoutController(logoutButton,lab);
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
        serachType.setValue("Appointment ID");
        loadProfileImage();
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

