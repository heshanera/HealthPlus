package Cashier;

import com.hms.hms_test_2.Validate;
import java.util.ArrayList;
import com.hms.hms_test_2.WarningController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

public class Validation extends DisplayPopUp{

    @FXML
    void validatePatientID(TextField patientID)
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
    void validateEmail(TextField cashierEmail)
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
        }catch(Exception e){

        }
    }

    @FXML
    void validateMobile(TextField cashierMobile)
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
        }catch(Exception e){

        }
    }

    @FXML
    void validateNIC(TextField cashierNIC)
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
    void validatePatientNIC(TextField patientID)
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

}
