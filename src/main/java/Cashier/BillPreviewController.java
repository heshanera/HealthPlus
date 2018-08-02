package Cashier;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

public class BillPreviewController extends AnchorPane {
    
    
    public BillPreviewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BillPreview.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
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
    
    @FXML private Label name;
    @FXML private Label service;
    @FXML private Label vat;
    @FXML private Label bill;
    @FXML private Label date;
    @FXML private Label billID;
    
    
    public void fillBillPreview(String patientName,String serviceFee, String VAT ,String billVal, String currDate, String billId)
    {
        name.setText(" "+patientName);
        service.setText(" " +serviceFee);
        vat.setText(" " +VAT);
        bill.setText(" " +billVal);
        date.setText(" " +currDate);
        billID.setText(" " +billId);
    }        
    
    
}
