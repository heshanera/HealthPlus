package com.hms.hms_test_2;

import Admin.AdminController;
import Cashier.CashierController;
import Doctor.Doctor;
import Doctor.DoctorController;
import LabAssistant.LabAssistantController;
import Pharmacist.PharmacistController;
import Receptionist.ReceptionistController;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        
        /*
        stage = new Stage();
        LabAssistantController lab = new LabAssistantController("user002");
        
        lab.loadProfileData(); 
        lab.fillPieChart();
        lab.fillLabAppiontments();
        lab.loadLabTests();
        
        stage.setScene(new Scene(lab));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        */

        /*
        stage = new Stage();
        PharmacistController pharmacist = new PharmacistController("user003");
        
        pharmacist.loadProfileData(); 
        pharmacist.makeStockTable();
        pharmacist.fillBarChart();
        pharmacist.fillPieChart();
        pharmacist.setPaceholders();
        
        stage.setScene(new Scene(pharmacist));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        */
        
        /*
        stage = new Stage();
        AdminController admin = new AdminController("user006");
        admin.loadProfileData(); 
        
        //admin.loadTheme();
        admin.filldatabaseStorageChart();
        admin.fillLineChart();
        admin.fillAreaChart();
        
        stage.setScene(new Scene(admin));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        */
        
        /*
        DoctorController doctor = new DoctorController("user001");
        
        doctor.fillAreaChart();
        doctor.setAppointments();
        doctor.loadProfileData(); 
        doctor.MakeAvailabilityTable();
        doctor.loadDrugList();
        doctor.loadTestList();
        doctor.setPaceholders();
        
        stage.setScene(new Scene(doctor));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        */
        
        /*
        stage = new Stage();
        ReceptionistController receptionist = new ReceptionistController("user005");
        
        receptionist.loadProfileData(); 
        receptionist.makeSummaryTable();
        receptionist.fillLineChart();
        receptionist.fillCurrentDoctors();
        //receptionist.fillConsultationAreas();

        stage.setScene(new Scene(receptionist));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        */
        
        /*
        stage = new Stage();
        CashierController cashier = new CashierController("user004");
        
        cashier.loadProfileData(); 
        cashier.makeHistoryTable();
        cashier.fillLineChart();
        
        stage.setScene(new Scene(cashier));
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        */
        
        
        LoginController login = new LoginController();
        stage.setScene(new Scene(login));
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
