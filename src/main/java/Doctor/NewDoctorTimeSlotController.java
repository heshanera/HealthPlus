package Doctor;

import com.hms.hms_test_2.SuccessIndicatorController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.LocalTimeStringConverter;

/**
 *
 * @author heshan
 */
public class NewDoctorTimeSlotController extends AnchorPane {

    private Doctor doc;
    private DoctorController docC;
    
    public NewDoctorTimeSlotController(Doctor doc,DoctorController docC) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewDoctorTimeSlot.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.doc = doc;
        this.docC = docC;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
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
    
    @FXML private Label close;
    
    @FXML private void closeEditor()
    {
        Stage stage; 
        stage = (Stage) close.getScene().getWindow();
        stage.close();
    }       
    
    
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
    
    @FXML private Button saveButton;
    
   

    @FXML private ComboBox slotDayCombo;
    @FXML private ComboBox start;
    @FXML private ComboBox end;
    
    
    
    @FXML private void save()
    {
        String day = (String)slotDayCombo.getSelectionModel().getSelectedItem();
        String sTime = (String)start.getSelectionModel().getSelectedItem();
        String eTime = (String)end.getSelectionModel().getSelectedItem();
        
        HashMap<String,String> daysHash = new HashMap<>();
        daysHash.put("Monday","1");
        daysHash.put("Tuesday","2");
        daysHash.put("Wednesday","3");
        daysHash.put("Thursday","4");
        daysHash.put("Friday","5");
        daysHash.put("Saturday","6");
        daysHash.put("Sunday","7");
        
        
        day = daysHash.get(day);
        String timeSlot = sTime+"-"+eTime;

        
        boolean result = doc.doctorTimeTableAddSlot(day,timeSlot);
        if (result == true)
        {
            Stage stage; 
            stage = (Stage) close.getScene().getWindow();
            stage.close();
            docC.MakeAvailabilityTable();
            showSuccessIndicator();
        }    
    }        
    
}


