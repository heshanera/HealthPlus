package Receptionist;

import Receptionist.Receptionist;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
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
public class AllAppointmentsController extends AnchorPane {

    private Receptionist receptionist;
    
    public AllAppointmentsController(Receptionist receptionist) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AllAppointments.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.receptionist = receptionist;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private ComboBox searchAppointmentCombo;
    @FXML private TextField searchValue;
    @FXML private Button searchButton;
    
    @FXML private ListView list1;
    @FXML private ListView list2;
    @FXML private ListView list3;
    
    @FXML private void search()
    {
        String value = searchValue.getText();
        String type = (String)searchAppointmentCombo.getSelectionModel().getSelectedItem();
        switch(type)
        {
            case "Doctor":
                type = "d";
                value = log.get(value);
                break;
            case "Patient ID":
                type = "p";
                break;
            case "Appointment ID":    
                type = "a";
                break;
        }    
        
        ArrayList<ArrayList<String>> data = receptionist.getAppointmentDetails(type,value);
        
        list1.getItems().clear();
        list2.getItems().clear();
        list3.getItems().clear();
        
        try{
            int size = data.size();
            for(int i = 1; i < size; i++)
            {
                System.out.println(data);
                list1.getItems().add(data.get(i).get(4));
                if (type.equals("p")) list2.getItems().add(log2.get(data.get(i).get(4)));
                else if (type.equals("d")) list2.getItems().add(data.get(i).get(2)+ " " + data.get(i).get(3));
                else list2.getItems().add(data.get(i).get(2)+ " " + data.get(i).get(3));
                list3.getItems().add(data.get(i).get(1));
            }
        }catch(Exception e){}    
        
        
    }        
    
    @FXML private void bindSuggestions()
    {
        String type = (String)searchAppointmentCombo.getSelectionModel().getSelectedItem();
        ArrayList<String> tmp = new ArrayList<String>(); 
        switch(type)
        {
            case "Doctor":
                TextFields.bindAutoCompletion(searchValue,docNames);
                break;
            case "Patient ID":
                TextFields.bindAutoCompletion(searchValue,tmp);
                break;
            case "Appointment ID":    
                TextFields.bindAutoCompletion(searchValue,tmp);
                break;
        }    
    
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

    @FXML private Button clearButton;
    
    @FXML private void clear()
    {
        list1.getItems().clear();
        list2.getItems().clear();
        list3.getItems().clear();
        
        searchValue.setText("");
        
    }
    
    @FXML private Label close;
    
    @FXML private void closeEditor()
    {
        Stage stage; 
        stage = (Stage) close.getScene().getWindow();
        stage.close();
    }       
    
    HashMap<String,String> log;
    HashMap<String,String> log2;
    ArrayList<String> docNames;
    
    public void loadDoctorNames()
    {
        log = new HashMap<String,String>();  
        log2 = new HashMap<String,String>();  
        docNames = new ArrayList<String>();
        
        ArrayList<ArrayList<String>> data = receptionist.getDoctorDetails();
        
        int size = data.size();
        
        for(int i = 1; i < size; i++)
        {
            log.put(data.get(i).get(1) +" "+data.get(i).get(2),data.get(i).get(0));
            log2.put(data.get(i).get(0),data.get(i).get(1) +" "+data.get(i).get(2));
            docNames.add(data.get(i).get(1) +" "+data.get(i).get(2));
        }    
        
        
    }        
    
    
    public void load()
    {
        loadDoctorNames();
    }
    
    
}


