package LabAssistant;

import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author heshan
 */
public class PrescriptionListController extends AnchorPane {

    public PrescriptionListController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PrescriptionListPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    LabAssistantController lab;
    @FXML TableView prescList;
    
    public void fillTableData(ArrayList<ArrayList<String>> data,LabAssistantController lab)
    {
        
        int noOfSlots = data.size();
        final ObservableList<Prescription> data2 = FXCollections.observableArrayList(); 
        for (int i = 1; i < noOfSlots; i++)
        {
            String prescID = data.get(i).get(0);
            String prescDate = data.get(i).get(1);
            String prescconsultant = data.get(i).get(3)+ " "+data.get(i).get(4);
            String prescTests = data.get(i).get(2);
            
            data2.add(new Prescription(prescID,prescDate,prescconsultant,prescTests));
        } 
        prescList.setItems(data2);
        this.lab = lab;
    }        

    @FXML private void LoadPrescriptionInfo()
    {
        Prescription prescription = (Prescription)prescList.getSelectionModel().getSelectedItem();
        int index = prescList.getSelectionModel().selectedIndexProperty().get();
        //System.out.println(bill.getPatientID());
        
        String doctor = prescription.getDoctor();
        String tests = prescription.getPrescription();
        String date = prescription.getDate();
        
        lab.fillPrescriptionInfo(doctor,tests,date);
        
        Stage stage = new Stage();
        stage = (Stage)prescList.getScene().getWindow();
        stage.close();

    } 
 
    @FXML
    private void close()
    {
        Stage stage = new Stage();
        stage = (Stage)prescList.getScene().getWindow();
        stage.close();
    }        
   
}
