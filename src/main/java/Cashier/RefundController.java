package Cashier;

import com.hms.hms_test_2.PopupAskController;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RefundController extends AnchorPane {
    
    CashierController cashier;
    
    public RefundController(CashierController cashier) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RefundTable.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.cashier = cashier;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private TableView billTable;
    @FXML 
    private TableView refundTable;
    
    public void fillRefundTable() 
    {
        final ObservableList<Refund> data = FXCollections.observableArrayList();
        ArrayList<ArrayList<String>> refundData = cashier.cashier.getWaitingRefunds();
        
        int size = refundData.size();
        for(int i = 1; i < size; i++)
        {
            String refundId = refundData.get(i).get(0);
            String billId = refundData.get(i).get(1);
            //String paymentType = refundData.get(i).get(2);
            String reason = refundData.get(i).get(3);
            String amount = refundData.get(i).get(4);
            String date = refundData.get(i).get(5);
            
            Image img2 = new Image(getClass().getResource("/imgs/refund.png").toString(), true);
            ImageView imageView = new ImageView(img2);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            imageView.setPreserveRatio(true);
                    
            data.add(new Refund(refundId,date,billId,reason,amount,imageView));        
        }    
       
        refundTable.setItems(data);
    }
    
    private Label info = new Label();
    
    @FXML private void getRefundInfo()
    {
        
        Refund refund = (Refund)refundTable.getSelectionModel().getSelectedItem();
        
        TablePosition pos = refundTable.getFocusModel().getFocusedCell();
        int column = pos.getColumn();
        if (column == 5)
        {
            info.setText("refund " + refund.getPatientID());
            
            Stage stage = new Stage();
            PopupAskController popup = new PopupAskController(info,cashier,this);
            popup.message("  Make the Refund?");    

            Scene scene = new Scene(popup);
            stage.setScene( scene );

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
        
        if (info.getText().equals("1"))
        {
            System.out.println("Yes!");
        }   
        System.out.println(info.getText());
        
    }        
    
    @FXML
    public Label closeRefund;
    @FXML
    public void closeRefundTable() 
    {
        Stage stage; 
        stage = (Stage) closeRefund.getScene().getWindow();
        stage.close();
    }        
 
}
