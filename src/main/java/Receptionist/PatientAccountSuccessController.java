package Receptionist;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PatientAccountSuccessController extends AnchorPane {

    public PatientAccountSuccessController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PatientAccountSuccess.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private Label patientID;
    @FXML private Label name;
    @FXML private Label mobile;
    @FXML private Label gender;
    
    public void fillPatientData(String patientId, String pName, String pAge, String pGender)
    {
        patientID.setText(" "+patientId);
        name.setText(" "+pName);
        mobile.setText(" "+pAge);
        gender.setText(" "+pGender);
    
    }
    
    
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
}
