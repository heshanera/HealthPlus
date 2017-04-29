package Pharmacist;

import com.hms.hms_test_2.SuccessIndicatorController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author heshan
 */
public class AddNewDrugController extends AnchorPane {

    private Pharmacist pharmacist;
    private PharmacistController pharmacistC;
    
    public AddNewDrugController(Pharmacist pharmacist,PharmacistController pharmacistC) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddNewDrug.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.pharmacist = pharmacist;
        this.pharmacistC = pharmacistC;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML private ComboBox genericName;
    @FXML private ComboBox brandName;
    @FXML private ComboBox drugType;
    @FXML private ComboBox drugUnit;
    @FXML private TextField drugPrice;
    
    @FXML private TextField stock;
    @FXML private TextField drugUnit2;
    
    @FXML private DatePicker manuDate;
    @FXML private DatePicker expDate;
    @FXML private ComboBox supplier;
  
    @FXML private Button add;
    
    public void loadGenericNames()
    {
        ArrayList<ArrayList<String>> generic = pharmacist.getGenericNames();
        ObservableList<String> generic2 = FXCollections.observableArrayList();
        int size = generic.size();
        for(int i = 1; i < size; i++)
        {
            String tmp  = generic.get(i).get(0);
            if (!generic2.contains(tmp))
            {
                generic2.add(tmp);
            }    
        }    
        generic2.add("");
        genericName.getItems().clear();
        genericName.getItems().addAll(generic2);
    }
    
    
    @FXML private void showBrandNames()
    {
        brandName.setDisable(false);
        
        String brands = (String)genericName.getSelectionModel().getSelectedItem();
        ArrayList<ArrayList<String>> drugBrands = pharmacist.getBrandNames(brands);
        ObservableList<String> drugBrands2 = FXCollections.observableArrayList();
        int size = drugBrands.size();
        for(int i = 1; i < size; i++)
        {
            String tmp  = drugBrands.get(i).get(0);
            if (!drugBrands2.contains(tmp))
            {
                drugBrands2.add(tmp);
            }    
        }    
        drugBrands2.add("");
        brandName.getItems().clear();
        brandName.getItems().addAll(drugBrands2);
    }       
    
    @FXML private void showDrugType()
    {
        drugType.setDisable(false);
        
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("tablet");
        types.add("syrup");
        types.add("powder");
        drugType.getItems().clear();
        drugType.getItems().addAll(types);
    }        
    
    
    @FXML private void showDrugUnit()
    {
        drugUnit.setDisable(false);
        
        ObservableList<String> units = FXCollections.observableArrayList();
        units.add("mg");
        units.add("g");
        units.add("ml");
        drugUnit.getItems().clear();
        drugUnit.getItems().addAll(units);
    }        
    
    
    @FXML private void showDrugPrice()
    {
        drugPrice.setDisable(false);
        drugUnit2.setText((String)drugUnit.getSelectionModel().getSelectedItem());
    }        
    
    
    @FXML private void stockDetails()
    {
        String tmp = drugPrice.getText();
        try{
            if (Integer.parseInt(tmp) > 0)
            {
                stock.setDisable(false);
                manuDate.setDisable(false);
                expDate.setDisable(false);
                supplier.setDisable(false);
                drugUnit2.setDisable(false);
                
                ArrayList<ArrayList<String>> suppliers = pharmacist.getSupplierNames2();
                System.out.println(suppliers);
                ObservableList<String> suppliers2 = FXCollections.observableArrayList();
                int size = suppliers.size();
                for(int i = 1; i < size; i++)
                {
                    String tmp2  = suppliers.get(i).get(0);
                    if (!suppliers2.contains(tmp2))
                    {
                        suppliers2.add(tmp2);
                    }    
                } 
                suppliers2.add("");
                supplier.getItems().clear();
                supplier.getItems().addAll(suppliers2);
            }
        }catch(Exception e){
        
            stock.setDisable(true);
            manuDate.setDisable(true);
            expDate.setDisable(true);
            supplier.setDisable(true);
            drugUnit.setDisable(true);
        }    
    }        
    
    /*
    genericName;
    brandName;
    drugType;
    drugUnit;
    drugPrice;
    
    stock;
    drugUnit2;
    
    manuDate;
    expDate;
    supplier;
    */
    
    @FXML private void addNewStock()
    {
        String genName = (String)genericName.getSelectionModel().getSelectedItem();
        String branName = (String)brandName.getSelectionModel().getSelectedItem();
        String suppName = (String)supplier.getSelectionModel().getSelectedItem();
        
        String type = (String)drugType.getSelectionModel().getSelectedItem();
        String unit = (String)drugUnit.getSelectionModel().getSelectedItem();
        String price = drugPrice.getText();
        
        
        // checking for Generic Name //////////////////////////////////////////////
        String in = pharmacist.checkForGenName(genName);
        
        String genericID = "";
        String brandID = "";
        String supplierID = "";
        
        if (!in.equals("0")) {
        
            genericID = in;
            
        } else {
            
           genericID =  pharmacist.addNewDrug2(genName);
        }    
        
        // checking for Brand Name ////////////////////////////////////////////////
        in = pharmacist.checkForBrandName(branName);
        
        if (!in.equals("0")) {
            
            brandID = in;
        
        } else {
            
            brandID = pharmacist.addNewBrand(branName,genName,type,unit,price);
        }
        
        // checking for Supplier Name /////////////////////////////////////////////
         in = pharmacist.checkForSupplierName(suppName);
        
        if (!in.equals("0")) {
            
            supplierID = in;
        
        } else {
            
            supplierID = pharmacist.addNewSupplier(suppName);
        }
        
        String stockAmount = stock.getText();
        String remainingQuantity = stockAmount;
        LocalDate manufacDate = manuDate.getValue();
        LocalDate expirDate = expDate.getValue();
   
        LocalDate today = LocalDate.now();        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String tdy = format.format(today);        
        
        String manuDate = format.format(manufacDate);
        String expDate = format.format(expirDate);
        
                
        boolean result = pharmacist.updateStock(genericID,brandID,stockAmount,manuDate,expDate,supplierID,tdy);
        if (result == true) 
        {   
            Stage stage; 
            stage = (Stage) close.getScene().getWindow();
            stage.close();
            
            pharmacistC.makeStockTable();
            pharmacistC.fillBarChart();
            pharmacistC.fillPieChart();
            
            
            showSuccessIndicator();    
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
    
    
}


