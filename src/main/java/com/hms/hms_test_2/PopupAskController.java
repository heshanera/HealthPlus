package com.hms.hms_test_2;

import Cashier.Cashier;
import Cashier.CashierController;
import Cashier.RefundController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author heshan
 */
public class PopupAskController extends AnchorPane {

    private Label label;
    private CashierController cashier;
    private RefundController refundC;
    
    public PopupAskController(Label label, CashierController cashier,RefundController refundC) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PopupAsk.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.label = label;
        this.cashier = cashier;
        this.refundC = refundC;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private Button yesButton;
    @FXML private Button noButton;
    
    @FXML private void yes()
    {
        
        String[] tmp = label.getText().split(" "); 
        if ( tmp[0].equals("refund") )
        {
            boolean result = cashier.cashier.makeRefund(tmp[1]);
            
            if (result == true)
            {
                cashier.loadRefunds();
            
                Stage stage2; 
                stage2 = (Stage) yesButton.getScene().getWindow();
                stage2.close();
                
                refundC.fillRefundTable();
                
                cashier.showSuccessIndicator();
                
            }    

        }    
        
        
    }
    
    @FXML private void no()
    {
        
        Stage stage; 
        stage = (Stage) noButton.getScene().getWindow();
        stage.close();
    }
    
    
    @FXML private Label PopUpMessage;
    
    public void message(String msg)
    {
        PopUpMessage.setText(msg);
    }        
    
    
}

