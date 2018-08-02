package LabAssistant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LabReportPreviewController extends AnchorPane {

    
    @FXML private LabAssistant lab;
    
    public LabReportPreviewController(LabAssistant lab) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LabReportPreview.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        this.lab = lab;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    
    @FXML TableView report;
    
    public void setData(ArrayList<ArrayList<String>> data, String type)
    {
        System.out.println(data);
        ObservableList<LabReport> data2 = FXCollections.observableArrayList();
        
        String[] urArray = {
            "Test ID","prescription_id","Appearance","S.G (Refractometer)",
            "PH","Protein","Glucose","Ketone Bodies","Bilirubin","Urobilirubin",
            "Contrifuged Depositsphase Contrast Microscopy","Pus Cells",
            "Red Cells","Epithelial Cells","Casts","Cristals"
        };

        String[] liArray = {
            "Test ID","prescription_id","Cholestrol HDL","cholestrolLDL",
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
            "Test ID","prescription_id","CPK Total"
        };
        
        String[] scpArray = {
            "Test ID","prescription_id","HIV 1 & 2 ELISA"
        };
        
        String[] tmpArray = null;
        
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
        
        ArrayList<ArrayList<String>> data4 = lab.getPatientInfo(tmp);
        
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
        
        
        System.out.println(data);
        
        for(int i = 1; i < size; i++)
        {
            System.out.println(i);
            if ( ( i != 1 ) && ( i != size-2 ) && ( i != size-1 ) )
            {    
                data2.add(new LabReport(tmpArray[i]+" (g/dL)", data.get(1).get(i)));
            }    
            else if ( i == size-2 )
            {
                data2.add(new LabReport("", ""));
            }    
            else if ( i == size-1 )
            {
                data2.add(new LabReport("Date", data.get(1).get(i)));
            }    
            
        }
        report.setItems(data2);
        //System.out.println(data);
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

