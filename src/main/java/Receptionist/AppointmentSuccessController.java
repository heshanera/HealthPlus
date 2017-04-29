package Receptionist;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AppointmentSuccessController extends AnchorPane {

    public AppointmentSuccessController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AppointmentSuccess.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private Label patientID;
    @FXML private Label consultant;
    @FXML private Label date;
    @FXML private Label appID;
    
    public void fillAppointmentData(String patientId, String consult, String appDate, String appId)
    {
        patientID.setText(" "+patientId);
        consultant.setText(" "+consult);
        date.setText(" "+appDate);
        appID.setText(" "+appId);
    
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
