package Pharmacist;

import com.hms.hms_test_2.AllMessagesController;
import com.hms.hms_test_2.CurrentUserSummaryController;
import com.hms.hms_test_2.LogoutController;
import com.hms.hms_test_2.SuccessIndicatorController;
import com.hms.hms_test_2.Validate;
import com.hms.hms_test_2.WarningController;
import java.io.File;
import java.io.FileOutputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import org.controlsfx.control.ListSelectionView;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.StageStyle;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;

public class PharmacistController extends AnchorPane {

    /**
     *
     */
    public Pharmacist pharmacist;

    /**
     *
     */
    public String username;
    
    /**
     *
     * @param username
     */
    public PharmacistController(String username) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Pharmacist.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        pharmacist = new Pharmacist(username);
        this.username = username;
        pharmacist.saveLogin(username);

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    /*
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
       try{ 
        createUserPharmacist("user003");   
        loadProfileData(); 
        makeStockTable();
        
       }catch(Exception e){}  
       
       try{
           fillBarChart();
           fillPieChart();
       } catch(Exception e){} 
    }
    */
    
    @FXML
    private Button searchPatientButton;
    @FXML
    private ComboBox searchTypePatientPharmacist;
    @FXML
    private TextField patientSearchValue;
    
    @FXML
    private void searchPatient(ActionEvent event) throws IOException 
    {
        if (searchTypePatientPharmacist.getSelectionModel().getSelectedItem() != null )
        {    
            String selectedValue = searchTypePatientPharmacist.getSelectionModel().getSelectedItem().toString();

            ArrayList<ArrayList<String>> prescriptionData = new ArrayList<>();
            String searchValue = patientSearchValue.getText();
            if (!searchValue.equals(""))
            {    
                searchTypePatientPharmacist.setStyle("-fx-border-color: #999 #999 #999 #999;");
                patientSearchValue.setStyle("-fx-border-color: #999 #999 #999 #999;");
                switch (selectedValue) 
                {
                    case "Patient ID":
                        //System.out.println("testing1");
                        prescriptionData = pharmacist.getPrescriptionInfo(searchValue);
                        break;
                    case "Name":
                        //System.out.println("testing2");
                        //patientData = pharmacist.getPrescriptionInfo(searchValue);
                        break;
                    case "NIC":
                        //System.out.println("testing3");
                        //patientData = pharmacist.getPrescriptionInfo(searchValue);
                        break;
                    default:
                        break;
                }

                System.out.println(prescriptionData);
                
                if (prescriptionData.size() > 1)
                {
                    fillListSelection(prescriptionData); 
                    
                } else {

                }
                    
            }
            else
            {
                patientSearchValue.setStyle("-fx-border-color: red;");
            }    
        }
        else
        {
            searchTypePatientPharmacist.setStyle("-fx-border-color: red;");
        }
    
    
    }
    
    @FXML private Button addNewDrugButton;
    
    @FXML private void addNewDrug()
    {
        
        Stage stage= new Stage();
        AddNewDrugController addDrug = new AddNewDrugController(pharmacist,this);
        addDrug.loadGenericNames();
        Scene scene = new Scene(addDrug);
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
    
    
    @FXML
    ListSelectionView<String> view;
    @FXML
    private void fillListSelection(ArrayList<ArrayList<String>> prescriptionData) 
    {
        String[] drugs = prescriptionData.get(1).get(0).split(Pattern.quote("|"));
        
        int tmpSize = drugs.length;
        for(int i = 0; i < tmpSize; i++)
        {
            view.getSourceItems().add(drugs[i]);
        }    
    }
    
    
    @FXML private void genericNameSelect()
    {
        try{
            String selectedDrug = genericNameSelectCombo.getSelectionModel().getSelectedItem().toString();

            if (selectedDrug.equals("All")) 
            {
                fillBarChart();

            } else {    

                ArrayList<ArrayList<String>> drugs = pharmacist.getStockSummary();
                int noOfSlots = (drugs.size()-1);
                //System.out.println(noOfSlots);
                //System.out.println(currentTimeTableData0);

                HashMap<String,String> drugInfo = pharmacist.getDrugGenericInfo();

                ArrayList<ArrayList<String>> drugNames = pharmacist.getDrugNames();

                System.out.println(drugs);
                System.out.println(drugInfo);


                XYChart.Series<String, Number> series1 = new XYChart.Series();
                //System.out.println(drugInfo);
                int noOfDrugs = drugInfo.size();

                for(int i = 1; i < noOfDrugs+1; i++)
                {
                    int brandAmount = 0;
                    String brand = drugNames.get(i).get(1);
                    System.out.println(brand +" " + drugInfo.get(brand));
                    if (selectedDrug.equals(drugInfo.get(brand)))
                    {
                        ArrayList<ArrayList<String>> drugBrandAmounts = pharmacist.getDrugAmounts(drugNames.get(i).get(0));

                        System.out.println(selectedDrug);
                        System.out.println(drugBrandAmounts);

                        int size2 = drugBrandAmounts.size();
                        for(int j = 1; j < size2; j++)
                        {
                            brandAmount += Integer.parseInt(drugBrandAmounts.get(j).get(0));
                        }        
                        series1.getData().add(new XYChart.Data(brand , brandAmount));
                    }       
                }    

                series1.setName(selectedDrug); 
                barchart.getData().clear();
                barchart.getData().add(series1);
            }
        }catch(Exception e){}    
    }
    
    
    
    @FXML
    private TextField billDrug;
    @FXML
    private TextField billFee;
    @FXML
    private TextField billDate;
    
    @FXML
    private void claculatePharmacyBill()
    {
        HashMap<String,String> drugSummary = pharmacist.getDrugPrices();
        
        ObservableList<String> targetData = view.getTargetItems();
        int tmpSize = targetData.size();
        int noOfDrugs = tmpSize;
        int total = 0;
        for(int i = 0; i < tmpSize; i++)
        {
            String tmp[] = targetData.get(i).split(" ");
            String valueAndUnit = tmp[tmp.length-1];
            int amount = 0;
            
            int nameSize = targetData.get(i).length() - valueAndUnit.length() - 1;
            
            String drugName = targetData.get(i).substring(0,nameSize);
            System.out.println(drugName);
            
            try{
                
                int drugUnitPrice = Integer.parseInt( drugSummary.get(drugName).split(" ")[0] );
               
                try
                {
                    amount = Integer.parseInt(valueAndUnit);
                    total += ( amount * drugUnitPrice );
                } catch (NumberFormatException e) {
                    try
                    {
                        amount = Integer.parseInt(valueAndUnit.substring(0, valueAndUnit.length()-1));
                        total += ( amount * drugUnitPrice );

                    } catch (NumberFormatException ex) {

                        amount = Integer.parseInt(valueAndUnit.substring(0, valueAndUnit.length()-2));
                        total += ( amount * drugUnitPrice );
                    }
                }
                
                ArrayList<ArrayList<String>> stocks = pharmacist.getDrugStockID(drugName);
                int size = stocks.size();
                boolean available = false;
                for(int f = 1; f < size; f++)
                {
                    
                    System.out.println(amount);
                    System.out.println(stocks.get(f).get(1));
                    System.out.println(stocks.get(f).get(0));
                    
                    if (Integer.parseInt(stocks.get(f).get(1)) > amount)
                    {
                        pharmacist.reduceStock(amount,stocks.get(f).get(0));
                        available = true;
                        break;
                    }    
                }    
                if (available == false)
                {    
                    total -= ( amount * drugUnitPrice );
                    String unavailable = targetData.remove(i);
                    view.getSourceItems().add(unavailable);
                    i--; tmpSize--; noOfDrugs--;
                    
                }
                
                
            } catch(Exception e){
        
                String unavailable = targetData.remove(i);
                view.getSourceItems().add(unavailable);
                i--; tmpSize--; noOfDrugs--;
            } 
            //System.out.println(targetData.get(i));
            //System.out.println(amount);
        }    
        //System.out.println(view.getTargetItems());
        billDrug.setText(Integer.toString(noOfDrugs));
        billFee.setText(Integer.toString(total));  
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
        billDate.setText(dateFormat.format(cal.getTime()));
    }
    
    public void loadNameList()
    {
        ObservableList<String> possibleSuggestions = FXCollections.observableArrayList();

        ArrayList<ArrayList<String>> data = pharmacist.getAllNames();
        System.out.println(data);
        int size = data.size();
        for(int i = 1; i < size; i++)
        {
            String firstName = data.get(i).get(1);
            String lastName = data.get(i).get(2);
            String age = "";
            String id = data.get(i).get(0);

            try{
                SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                Date birth = tmpdataformat.parse(data.get(i).get(3));
                Calendar calendarBirth = Calendar.getInstance();
                calendarBirth.setTime(birth);
                Calendar calendarToday = Calendar.getInstance();
                age = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));

            }catch(Exception e){e.printStackTrace();}

            possibleSuggestions.add(age + " " + firstName + " " + lastName + " " + id);
        } 
        TextFields.bindAutoCompletion(patientSearchValue,possibleSuggestions);
    }        
    
    @FXML private void convertToID()
    {
        try{
            String[] tmp = patientSearchValue.getText().split(" ");
            if ( tmp.length == 4 )
            {
                String patientID = tmp[3];
                patientSearchValue.setText(patientID);
                searchTypePatientPharmacist.setValue("Patient ID");
            }
        }catch(Exception e){}    
        //System.out.println("Testing");
    }     
    
    
    @FXML
    private void issueBill()
    {
        String patient = patientSearchValue.getText(); 
        String billValue = billFee.getText();
        
        //("consultant_id slmc0001,patient_id hms0001pa,pharmacy_fee 0,discount 0,");
        String consultantID = "";
        if (searchTypePatientPharmacist.getSelectionModel().getSelectedItem() != null )
        {    
            String selectedValue = searchTypePatientPharmacist.getSelectionModel().getSelectedItem().toString();

            ArrayList<ArrayList<String>> prescriptionData = new ArrayList<>();
            String searchValue = patientSearchValue.getText();
            if (!searchValue.equals(""))
            {    
                searchTypePatientPharmacist.setStyle("-fx-border-color: #999 #999 #999 #999;");
                patientSearchValue.setStyle("-fx-border-color: #999 #999 #999 #999;");
                switch (selectedValue) 
                {
                    case "Patient ID":
                        //System.out.println("testing1");
                        prescriptionData = pharmacist.getPrescribedDoc(searchValue);
                        consultantID = prescriptionData.get(1).get(0);
                        break;
                    case "Name":
                        //System.out.println("testing2");
                        //patientData = pharmacist.getPrescriptionInfo(searchValue);
                        break;
                    case "NIC":
                        //System.out.println("testing3");
                        //patientData = pharmacist.getPrescriptionInfo(searchValue);
                        break;
                    default:
                        break;
                }
     
            }
            else
            {
                patientSearchValue.setStyle("-fx-border-color: red;");
            }    
        }
        else
        {
            searchTypePatientPharmacist.setStyle("-fx-border-color: red;");
        }
  
        
        if (! billValue.equals(""))
        {    
            String billInfo = "consultant_id "+ consultantID+"," + "patient_id " + patient + ",pharmacy_fee " + billValue;      
            boolean success = pharmacist.bill(billInfo,patient,billValue);

            if (success == true) 
            {
                showSuccessIndicator();
                patientSearchValue.setText("");
                patientSearchValue.setText(""); 
                billFee.setText("");
                billDate.setText("");
                
                ObservableList<String> empty = view.getTargetItems();
                int tmpSize = empty.size();
                for (int i = 0; i < tmpSize; i++ )
                {
                    empty.remove(i);
                    tmpSize--; i--;
                }

                ObservableList<String> empty2 = view.getSourceItems();
                tmpSize = empty2.size();
                for (int i = 0; i < tmpSize; i++ )
                {
                    empty2.remove(i);
                    tmpSize--; i--;
                }
                
            }
            
        }
    }
    
    @FXML
    private void clearPrescription()
    {
        patientSearchValue.setText("");
        billDrug.setText("");
        billFee.setText("");
        billDate.setText("");
        
        ObservableList<String> empty = view.getTargetItems();
        int tmpSize = empty.size();
        for (int i = 0; i < tmpSize; i++ )
        {
            empty.remove(i);
            tmpSize--; i--;
        }
        
        ObservableList<String> empty2 = view.getSourceItems();
        tmpSize = empty2.size();
        for (int i = 0; i < tmpSize; i++ )
        {
            empty2.remove(i);
            tmpSize--; i--;
        }
    }        
    
    
    @FXML
    private TableView pharmacyStock;
    
    @FXML
    private Node createStockPage(int pageIndex) {


        ArrayList<ArrayList<String>> drugs = pharmacist.getStockSummary2();
	int noOfSlots = (drugs.size()-1);
        //System.out.println(noOfSlots);
        //System.out.println(currentTimeTableData0);
            
        final ObservableList<Drug> data = FXCollections.observableArrayList(); 
        for (int i = 1; i <= noOfSlots; i++)
        {
            String name = drugs.get(i).get(1);
            String type = drugs.get(i).get(2);
            String unit = drugs.get(i).get(3);
            String price = drugs.get(i).get(4);
            String amount = drugs.get(i).get(5);
            String suppliers = drugs.get(i).get(6);
            
            data.add(new Drug(name,type,unit,price,amount,suppliers));
        }        
        
        
        int fromIndex = pageIndex * 7;
        int toIndex = Math.min(fromIndex + 7, data.size());
        pharmacyStock.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(pharmacyStock);
    }
    
    @FXML
    private Pagination stockDetails;
    
    @FXML 
    private void createStockPagination(int dataSize)
    {
        stockDetails.setPageCount((dataSize / 7 + 1));
        stockDetails.setPageFactory(this::createStockPage);
    }
    
    @FXML
    public void makeStockTable()
    {
        ArrayList<ArrayList<String>> tmpStockDetails = pharmacist.getStockSummary2();
        createStockPagination(tmpStockDetails.size()-1);
    }
    
    
    @FXML
    private PieChart piechart;
    @FXML 
    public void fillPieChart() {
        
        HashMap<String,String> supplierNames = pharmacist.getSupplierNames();
        
        ArrayList<ArrayList<String>> suppliers = pharmacist.getSupplierSummary();
        int noOfSuppliers = suppliers.get(0).size();
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < noOfSuppliers; i++)
        {
            String supplierID = suppliers.get(0).get(i);
            int stocks = Integer.parseInt(suppliers.get(1).get(i));
            pieChartData.add(new PieChart.Data(supplierNames.get(supplierID), stocks));
        }
        
        pieChartData.forEach(data1 ->
                data1.nameProperty().bind(
                        Bindings.concat(
                                data1.getName(), " (", data1.pieValueProperty().intValue(), ")"
                        )
                )
        );
        
        //piechart.setLabelLineLength(20);
        piechart.setLegendSide(Side.BOTTOM); 
        piechart.setLabelsVisible(true);
        piechart.setData(pieChartData);
        
    }
    
    
    
    
    @FXML
    private ComboBox genericNameSelectCombo;
    @FXML
    private BarChart<String, Number> barchart;
    @FXML
    public void fillBarChart()
    {
        try{
            ArrayList<ArrayList<String>> drugs = pharmacist.getStockSummary();
            int noOfSlots = (drugs.size()-1);
            //System.out.println(noOfSlots);
            //System.out.println(currentTimeTableData0);

            HashMap<String,String> drugInfo = pharmacist.getDrugGenericInfo();

            final ObservableList<Drug> data = FXCollections.observableArrayList(); 
            for (int i = 1; i <= noOfSlots; i++)
            {
                String name = drugs.get(i).get(1);
                String amount = drugs.get(i).get(3);

                data.add(new Drug(name,"0","0","0",amount,"0"));
            }    

            Collections.sort(data, new Comparator<Drug>() 
            {
                @Override
                public int compare(Drug drug1, Drug drug2)
                {
                    if ( drug1.getAmount() < drug2.getAmount() ) return drug2.getAmount();
                    else return drug1.getAmount();    
                }
            });

            ArrayList<String> names = new ArrayList<>();
            names.add("All");
            XYChart.Series<String, Number> series1 = new XYChart.Series(); 

            int noOfDrugs = 0;
            if (data.size() > 5) noOfDrugs = 5;
            else noOfDrugs = data.size();

            for (int i = 0; i < noOfDrugs; i++)
            {
                series1.getData().add(new XYChart.Data(data.get(i).getName() , data.get(i).getAmount()));
                names.add(data.get(i).getName());
            }    

            series1.setName("Availability");   
            barchart.getData().clear();
            barchart.getData().add(series1);

            Platform.runLater(() -> genericNameSelectCombo.getItems().clear());
            Platform.runLater(() -> genericNameSelectCombo.getItems().addAll(names));
        }catch(Exception e){}
    }
    
    

         
   /*
    @FXML
    private Button newPatient;
    @FXML
    private Button closePatient;
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
 
    Stage stage; 
    Parent root;
 
    if(event.getSource()== newPatient)
    {
     
    stage = new Stage();
    root = FXMLLoader.load(getClass().getResource("/fxml/newPatientPharmacy.fxml"));
    Scene scene = new Scene(root);       
    stage.setScene(scene);
    //stage.setFullScreen(true);
           
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    //set Stage boundaries to visible bounds of the main screen
    stage.setX(primaryScreenBounds.getMinX());
    stage.setY(primaryScreenBounds.getMinY());
    stage.setWidth(primaryScreenBounds.getWidth());
    stage.setHeight(primaryScreenBounds.getHeight());  
      
        
    stage.initStyle(StageStyle.UNDECORATED);
    scene.setFill(null);
    stage.initStyle(StageStyle.TRANSPARENT);
      
    stage.setTitle("My modal window");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(newPatient.getScene().getWindow());
    stage.showAndWait();


    }
   }
    
   @FXML
    private void closeButtonAction(ActionEvent event) {
 
    Stage stage; 
    Parent root;
        if(event.getSource()== closePatient)
        {
            stage = (Stage) closePatient.getScene().getWindow();
            stage.close();
        }
    } 
   */ 
    
    // Changing the profile pictute //////////////////////////////////////////////////
    
    @FXML ImageView profileImage;
    
    @FXML Button editProfilePicButton;
    FileChooser chooser = new FileChooser();
    
    Label path = new Label();
    Label name = new Label();
    
    @FXML public void  editProfilePic() throws MalformedURLException, IOException
    {
        
        if ( editProfilePicButton.getText().equals("Edit") )
        {
            ArrayList<String> types = new ArrayList<String>();
            types.add("png"); types.add("jpeg"); types.add("jpg");
            types.add("PNG"); types.add("JPEG"); types.add("JPG");
            
            Stage stage = new Stage();
            chooser.setTitle("Select Export Directory");
            File file = chooser.showOpenDialog(stage);
            
            if (file != null)
            {
                String img = file.toURI().toURL().toExternalForm();

                if (!types.contains(file.getName().split("\\.(?=[^\\.]+$)")[1])) {



                } else {   

                    path.setText(file.getAbsolutePath()); 
                    name.setText(file.getName()); 

                    profileImage.setImage(new Image(img));
                    editProfilePicButton.setText("Save");
                }
            }    
                
        } else if ( editProfilePicButton.getText().equals("Save") ) {
            
           
            Path source = Paths.get(path.getText()); 

            System.out.println(name.getText());

            String imageName = this.username+"ProfPic."+(name.getText().split("\\.(?=[^\\.]+$)")[1]);
            OutputStream os = new FileOutputStream(new File("src/main/resources/imgs/profilePics/"+imageName));
            
            Files.copy(source, os);

            pharmacist.setProfilePic(imageName);
            editProfilePicButton.setText("Edit");
        }
        
    }
    
    @FXML private Button showUserButton;
    
    @FXML private void showUser()
    {
        CurrentUserSummaryController user = new CurrentUserSummaryController(pharmacist);
        user.load();
        
        if (popOver == null) 
        {
            popOver = new PopOver();
        }
        
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popOver.setContentNode(user);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(showUserButton);
    }  
    
    public void loadProfileImage()
    {
        Image img;
        try{
            String image = pharmacist.getProfilePic();
            img = new Image(getClass().getResource("/imgs/profilePics/"+image).toString(), true);
        }catch(Exception e){
            img = new Image(getClass().getResource("/imgs/profilePics/p2.png").toString(), true);
        }
        profileImage.setImage(img);
        
    } 
    
    ////////////////////////////////
    
    ////// Loading messages ////////
    
    @FXML private Button AllMessages;
    
    @FXML
    private void showAllMessages()
    { 

        if (popOver == null) 
        {
            popOver = new PopOver();
        }
        AllMessagesController popup = new AllMessagesController(pharmacist);
        popup.loadMessages();
        
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(AllMessages);
        
    }
    
    ////////////////////////////////
    
    /// Loading ther profile info ////////////////////////////////////////
  
    @FXML
    private TextField pharmacistName;
    @FXML    
    private TextField pharmacistNIC;
    @FXML
    private DatePicker pharmacistDOB;
    @FXML
    private TextField pharmacistAge;
    @FXML
    private ComboBox pharmacistGender;
    @FXML
    private TextField pharmacistNationality;
    @FXML
    private TextField pharmacistReligion;
    @FXML
    private TextField pharmacistMobile;
    @FXML
    private TextField pharmacistEmail;   
    @FXML
    private TextField pharmacistAddress;
    
    @FXML
    private TextArea pharmacistEducation;
    @FXML
    private TextArea pharmacistTraining;  
    @FXML
    private TextArea pharmacistExperience;
    @FXML
    private TextArea pharmacistAchivements;  
    @FXML
    private TextArea pharmacistOther;
    @FXML
    private TextField pharmacistUserName;
    @FXML
    private TextField pharmacistUserType;
    @FXML
    private TextField pharmacistUserID;
    @FXML
    private TextField pharmacistPassword;
    @FXML
    private TextField pharmacistNewPassword;
    @FXML
    private TextField pharmacistConfirmPassword;

            
    @FXML
    public void loadProfileData() 
    {

        HashMap<String,String> docPersonalInfo =  pharmacist.getProfileInfo();
		
        pharmacistName.setText(docPersonalInfo.get("first_name") + " " + docPersonalInfo.get("last_name"));
        pharmacistNIC.setText(docPersonalInfo.get("nic"));
        pharmacistNationality.setText(docPersonalInfo.get("nationality"));
        pharmacistReligion.setText(docPersonalInfo.get("religion"));
        pharmacistMobile.setText(docPersonalInfo.get("mobile"));
        pharmacistEmail.setText(docPersonalInfo.get("email"));
        pharmacistAddress.setText(docPersonalInfo.get("address"));

        try{
                SimpleDateFormat tmpdataformat = new SimpleDateFormat("yyyy-MM-dd");
                Date birth = tmpdataformat.parse(docPersonalInfo.get("date_of_birth"));
                Calendar calendarBirth = Calendar.getInstance();
                calendarBirth.setTime(birth);
                Calendar calendarToday = Calendar.getInstance();
                String tmpage = Integer.toString(calendarToday.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR));
                
                String tmpDOB = docPersonalInfo.get("date_of_birth");
                
                int year = Integer.parseInt(tmpDOB.substring(0,4));
                int month = Integer.parseInt(tmpDOB.substring(5,7));        
                int date = Integer.parseInt(tmpDOB.substring(8,10));        
                pharmacistDOB.setValue(LocalDate.of(year, month, date));
                pharmacistAge.setText(tmpage);
        }catch(Exception e){e.printStackTrace();}
        
        try{
            String tmpGen = docPersonalInfo.get("gender");
            if (tmpGen.equals("m")){pharmacistGender.getSelectionModel().select("Male");}
            else {pharmacistGender.getSelectionModel().select("Female");}

            pharmacistEducation.setText(docPersonalInfo.get("education"));
            pharmacistExperience.setText(docPersonalInfo.get("experience"));
            pharmacistTraining.setText(docPersonalInfo.get("training"));
            pharmacistAchivements.setText(docPersonalInfo.get("achievements"));
            pharmacistOther.setText(docPersonalInfo.get("experienced_areas"));
        }catch(Exception e){}    
            pharmacistUserName.setText(docPersonalInfo.get("user_name"));
            pharmacistUserType.setText(docPersonalInfo.get("user_type"));
            pharmacistUserID.setText(docPersonalInfo.get("user_id"));
        
    }  
    
    @FXML
    private Button editBasicInfoButton;
    
    @FXML 
    private void editBasicInfo()
    {
        String currentState = editBasicInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            pharmacistName.setDisable(false);
            pharmacistNIC.setDisable(false);
            pharmacistGender.setDisable(false);
            pharmacistNationality.setDisable(false);
            pharmacistReligion.setDisable(false);
            pharmacistMobile.setDisable(false);
            pharmacistEmail.setDisable(false);
            pharmacistAddress.setDisable(false);
            pharmacistDOB.setDisable(false);
            
            editBasicInfoButton.setText("Save");
        }
        else if ( currentState.equals("Save"))
        {
            pharmacistName.setDisable(true);
            pharmacistNIC.setDisable(true);
            pharmacistGender.setDisable(true);
            pharmacistNationality.setDisable(true);
            pharmacistReligion.setDisable(true);
            pharmacistMobile.setDisable(true);
            pharmacistEmail.setDisable(true);
            pharmacistAddress.setDisable(true);
            pharmacistDOB.setDisable(true);
            
            String info = "";
				
            String[] name = pharmacistName.getText().split(" ");
            String gender = (String)pharmacistGender.getSelectionModel().getSelectedItem();
            if (gender.equals("Male")){gender = "m";}
            else {gender = "f";}
            //String marital = receptionMaritalComboDoc.getText();
            String nationality = (String)pharmacistNationality.getText();
            String religion = (String)pharmacistReligion.getText();
            String mobile = pharmacistMobile.getText();
            String email = pharmacistEmail.getText();
            String address = pharmacistAddress.getText();

            info += "first_name " + name[0] + "#last_name " + name[1];
            info += "#gender " + gender;
            info += "#nationality " + nationality;
            info += "#religion " + religion;
            info += "#mobile " + mobile;
            info += "#email " + email;
            info += "#address " + address;

            //System.out.println(info);

            boolean success = pharmacist.updateProfileInfo(info);
            
            editBasicInfoButton.setText("Edit");
            //saveProgress.setProgress(0.5);  
            if (success == true) showSuccessIndicator();
            //waitFor();
            
            
            //stage.close();
            
        }    
    }
    
    @FXML
    private Button editPharmacistInfoButton;
    
    @FXML 
    private void editPharmacistInfo()
    {
        String currentState = editPharmacistInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            pharmacistEducation.setDisable(false);
            pharmacistTraining.setDisable(false);  
            pharmacistExperience.setDisable(false);
            pharmacistAchivements.setDisable(false);  
            pharmacistOther.setDisable(false);
            
            editPharmacistInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            pharmacistEducation.setDisable(true);
            pharmacistTraining.setDisable(true);  
            pharmacistExperience.setDisable(true);
            pharmacistAchivements.setDisable(true);  
            pharmacistOther.setDisable(true);
            
            String info = "";
				
            String education = pharmacistEducation.getText();
            String exp = pharmacistExperience.getText();
            String training = pharmacistTraining.getText();
            String academic = pharmacistAchivements.getText();
            String other = pharmacistOther.getText();


            info += "education " + education;
            info += "#training " + training;
            info += "#other " + other;
            info += "#experience " + exp;
            info += "#achievements " + academic;

            //System.out.println(info);

            boolean success = pharmacist.updatePharmacistInfo(info);
            if (success == true) showSuccessIndicator();
            editPharmacistInfoButton.setText("Edit");
        }
    }        
    
    
    @FXML
    private Button editUserInfoButton;
    
    @FXML 
    private void editUserInfo()
    {
        String currentState = editUserInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            pharmacistUserName.setDisable(false);
            
            editUserInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            pharmacistUserName.setDisable(true);
            
            String info = "user_name " + pharmacistUserName.getText();
            boolean success = pharmacist.updateAccountInfo(info);
            if (success == true) showSuccessIndicator();
            editUserInfoButton.setText("Edit");
        }
    }    
    
    @FXML
    private Button editPasswordInfoButton;
    
    @FXML 
    private void editPasswordInfo()
    {
        String currentState = editPasswordInfoButton.getText();
        
        if ( currentState.equals("Edit"))
        {
            pharmacistPassword.setDisable(false);
            pharmacistNewPassword.setDisable(false);
            pharmacistConfirmPassword.setDisable(false);
            
            editPasswordInfoButton.setText("Save");
        } 
        else if ( currentState.equals("Save"))
        {
            
            boolean result = false;
            //result = checkCurrentPassword(String userId, String password)
            
            if (result == true)
            {
                if ( pharmacistNewPassword.getText() == pharmacistConfirmPassword.getText())
                {
                    String info = "password " + pharmacistConfirmPassword.getText();
                    boolean success =  pharmacist.updateAccountInfo(info);
                    
                    pharmacistPassword.setDisable(true);
                    pharmacistNewPassword.setDisable(true);
                    pharmacistConfirmPassword.setDisable(true);
                    
                    
                    if (success == true) showSuccessIndicator();
                    editPasswordInfoButton.setText("Edit");
                }    
            }
            
        }
    }
    
    @FXML
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
    
    @FXML
    private Button saveSuccess;
    
    @FXML
    private void saveSuccessExit(ActionEvent event) {
 
    Stage stage; 
        if(event.getSource()== saveSuccess)
        {
            stage = (Stage) saveSuccess.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML
    private Button logoutButton;
    @FXML
    private void logout()
    {
        Stage stage= new Stage();
        LogoutController logout = new LogoutController(logoutButton,pharmacist);
        Scene scene = new Scene(logout);
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
    
    public void setPaceholders()
    {
        
        searchTypePatientPharmacist.setValue("Patient ID");
        patientSearchValue.setPromptText("search value");
        
        loadProfileImage();
    }  
    
    /*******************************************************************************************************
     * Validations
     *******************************************************************************************************/
    
    private PopOver popOver;
    
    private void showPopup(String message, TextField text)
    { 

        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
            
        }
        WarningController popup = new WarningController();
        popup.addMessage(message);

        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(text);
    } 
    
    @FXML
    private void validatePatientID()
    {
       String tmpID = patientSearchValue.getText();
        if ( tmpID.length() == 9 )
        {
            String result = Validate.patientID(tmpID);
            if (result.equals("1"))
            {
                if (popOver != null) popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,patientSearchValue);
            }   
        } else { 
            showPopup("hmsxxxxpa",patientSearchValue);
        }  
    }   
    
    @FXML
    private void validateEmail()
    {        
        try{
            String tmpemail = pharmacistEmail.getText();
            String result = Validate.email(tmpemail);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,pharmacistEmail);
            }
        }catch(Exception e){}    
    }           
            
    
    @FXML 
    private void validateMobile()
    {    
        try{
            String tmpmobile = pharmacistMobile.getText();
            String result = Validate.mobile(tmpmobile);
            if (result.equals("1"))
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup(result,pharmacistMobile);
            }
        }catch(Exception e){}     
    }   
    
    @FXML 
    private void validateNIC()
    {    
        try{
            String tmpnic = pharmacistNIC.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup("xxxxxxxxxV",pharmacistNIC);
            }
        }catch(Exception e){}     
    }   
    
    @FXML 
    private void validatePatientNIC()
    {    
        try{
            String tmpnic = patientSearchValue.getText();
            ArrayList<String> result = Validate.NIC(tmpnic);
            if (result.size() != 0)
            {
                popOver.hide(Duration.millis(500));

            } else {
                showPopup("xxxxxxxxxV",patientSearchValue);
            }
        }catch(Exception e){}     
    }   
    
    public void addFocusListener()
    {        
        pharmacistNIC.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue){}
                else
                {
                    validateNIC();
                }
            }
        });
        
        pharmacistMobile.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue){}
                else
                {
                    validateMobile();
                }
            }
        });
        
        pharmacistEmail.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue){}
                else
                {
                    validateEmail();
                }
            }
        });
        
        patientSearchValue.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if ( searchTypePatientPharmacist.getValue().toString().equals("Patient ID") )
                {    
                    if (newPropertyValue){} else { validatePatientID(); }
                    
                } else if ( searchTypePatientPharmacist.getValue().toString().equals("NIC") )
                {    
                    if (newPropertyValue){} else { validatePatientNIC(); }
                    
                } else if ( searchTypePatientPharmacist.getValue().toString().equals("Name") )
                {    
                    if (newPropertyValue){} else { }
                }
            }
        });
        
    } 
   
    
}

