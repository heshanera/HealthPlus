package IntermediatePharmacist;
import com.hms.hms_test_2.Validate;
import com.hms.hms_test_2.WarningController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.control.PopOver;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
public class InterPharmacistController extends AnchorPane {

    public InterPharmacist pharmacist;

    public String username;
    public InterPharmacistController(String username) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Pharmacist.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        pharmacist.saveLogin(username);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
//    donot remove
    @FXML
    private Button searchPatientButton;
    @FXML
    private ComboBox searchTypePatientPharmacist;
    @FXML
    private TextField patientSearchValue;
    

//    Donot remove
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




    @FXML
    private TableView pharmacyStock;

    @FXML
    private Node createStockPage(int pageIndex) {


        ArrayList<ArrayList<String>> drugs = pharmacist.getStockSummary2();
        int noOfSlots = (drugs.size()-1);
        //System.out.println(noOfSlots);
        //System.out.println(currentTimeTableData0);

        final ObservableList<InterDrug> data = FXCollections.observableArrayList();
        for (int i = 1; i <= noOfSlots; i++)
        {
            String name = drugs.get(i).get(1);
            String type = drugs.get(i).get(2);
            String unit = drugs.get(i).get(3);
            String price = drugs.get(i).get(4);
            String amount = drugs.get(i).get(5);
            String suppliers = drugs.get(i).get(6);

            data.add(new InterDrug(name,type,unit,price,amount,suppliers));
        }


        int fromIndex = pageIndex * 7;
        int toIndex = Math.min(fromIndex + 7, data.size());
        pharmacyStock.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));

        return new BorderPane(pharmacyStock);
    }

    //    Donot remove
    @FXML
    private Pagination stockDetails;
    @FXML 
    private void createStockPagination(int dataSize)
    {
        stockDetails.setPageCount((dataSize / 7 + 1));
        stockDetails.setPageFactory(this::createStockPage);
    }
//    Donot remove
    @FXML
    public void makeStockTable()
    {
        ArrayList<ArrayList<String>> tmpStockDetails = pharmacist.getStockSummary2();
        createStockPagination(tmpStockDetails.size()-1);
    }
    

//    Donot remove
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

//    Donot remove
    @FXML
    public void fillBarChart()
    {
        try{
            ArrayList<ArrayList<String>> drugs = pharmacist.getStockSummary();
            int noOfSlots = (drugs.size()-1);
            //System.out.println(noOfSlots);
            //System.out.println(currentTimeTableData0);

            HashMap<String,String> drugInfo = pharmacist.getDrugGenericInfo();

            final ObservableList<InterDrug> data = FXCollections.observableArrayList();
            for (int i = 1; i <= noOfSlots; i++)
            {
                String name = drugs.get(i).get(1);
                String amount = drugs.get(i).get(3);

                data.add(new InterDrug(name,"0","0","0",amount,"0"));
            }    

            Collections.sort(data, new Comparator<InterDrug>()
            {
                @Override
                public int compare(InterDrug drug1, InterDrug drug2)
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


    


    // Changing the profile pictute //////////////////////////////////////////////////

    @FXML ImageView profileImage;

//    Donot remove
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

//            Donot remove.
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
    

//    Donot remove
    public void setPaceholders()
    {
        
        searchTypePatientPharmacist.setValue("Patient ID");
        patientSearchValue.setPromptText("search value");
        
        loadProfileImage();
    }


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


    public void addFocusListener() {
        pharmacistNIC.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                } else {
                    validateNIC();
                }
            }
        });

    }

}

