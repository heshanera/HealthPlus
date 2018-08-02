package Admin;

import Pharmacist.Drug;
import com.hms.hms_test_2.SuccessIndicatorController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author heshan
 */
public class ReportsController extends AnchorPane {

    
    private Admin admin;
    
    public ReportsController(Admin admin) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Reports.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.admin = admin;
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML ComboBox patientAttendenceCombo;
    @FXML AreaChart<String,Number> patientAttendence;
    
    @FXML private NumberAxis pyaxis;
    
    public void fillPatientAttendence(String doc)
    {
        String doctor = "All";
        //System.out.println(doc);
        int max1 = 1;
        if (!doc.equals("All"))
        {    
            doctor = doc.split("\\[")[1].split("\\]")[0];
        }
        ArrayList<ArrayList<String>> data = admin.getPatientAttendence(doctor);
        String date = "";
        
        ArrayList<String> months = new ArrayList<String>(); 
        ArrayList<Integer> patients = new ArrayList<Integer>();
        
        int size = data.size();
        for(int i = 1; i < size; i ++)
        {    
            date = data.get(i).get(0);
            DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate date2 = LocalDate.parse(date, fomatter1);

            DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
            String Month = fomatter2.format(date2);
            
            System.out.println(Month);
            if ( months.contains(Month) ) {
            
                int indx = months.indexOf(Month);
                int tmp = patients.remove(indx);
                patients.add(indx,(tmp+1));
                
            } else {
                
                months.add(Month);
                patients.add(1);
            }    
             
        }
        
        XYChart.Series series1 = new XYChart.Series();
        if ( doctor.equals("All")) series1.setName("All Doctors");
        else series1.setName(doc.split("\\[")[0]);
        size = months.size();
        for(int i = 0; i < size; i++)
        {
            String month = months.get(i);
            int no = patients.get(i);
            if ( max1 < no ) max1 = no;
            series1.getData().add(new XYChart.Data(month, no));
        }    
        patientAttendence.getData().clear();
        patientAttendence.getData().addAll(series1);
        
        ArrayList<ArrayList<String>> data2 = admin.getDoctorNames();
        ArrayList<String> names = new ArrayList<String>();
        names.add("All");
        size = data2.size();
        for(int i = 1; i < size; i++)
        {
            names.add(data2.get(i).get(0)+" "+data2.get(i).get(1) +" [" +data2.get(i).get(3)+"]");
        }    
        
        pyaxis.setAutoRanging(false);
        pyaxis.setUpperBound(max1 + 1);
        pyaxis.setTickUnit((max1 + 5)/5);
        pyaxis.setLowerBound(0);
        
        
        Platform.runLater(() -> patientAttendenceCombo.getItems().clear());
        Platform.runLater(() -> patientAttendenceCombo.getItems().addAll(names));
        
        
    }        
    
    @FXML private void patientAttendencefromCombo()
    {
        String doctor = (String)patientAttendenceCombo.getSelectionModel().getSelectedItem();
        try{
            fillPatientAttendence(doctor);
            if (!doctor.equals("All"))
            {    
                doctor = doctor.split("\\[")[0];
            }
            patientAttendenceCombo.setPromptText(doctor);
        }catch(Exception e){}    
    }        
    
    @FXML private ComboBox reportsCombo;
    @FXML private PieChart labReportPieChart;
    
    public void fillPieChart(int months)
    {
        ArrayList<ArrayList<String>> data = admin.lastMonthsReports(months);
        String[] test = {   
                            "Blood Grouping & Rh","Lipid Profile Test","LFT","RFT",
                            "HIV","CPK","Pathalogy Test",
                            "Complete Blood Count"
                        };
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(); 
        pieChartData.clear();
        int tmpSize = test.length;
        for(int i = 0; i < tmpSize; i++)
        {
            pieChartData.add(new PieChart.Data(test[i], Integer.parseInt(data.get(1).get(i))));
        }    
        
        pieChartData.forEach(data1 ->
                data1.nameProperty().bind(
                        Bindings.concat(
                                data1.getName(), " (", data1.pieValueProperty(), ")"
                        )
                )
        );
        
        labReportPieChart.setData(pieChartData);
        
        ArrayList<Integer> month = new ArrayList<Integer>();
        for (int i = 1; i < 13; i++)
        {
            month.add(i);
        }    
        //reportsCombo.getItems().clear();
        //reportsCombo.getItems().addAll(month);
        //reportsCombo.setValue(12);
        
    }        
    
    @FXML private void fillReportsCombo()
    {
        try{
            int months = Integer.parseInt((String)reportsCombo.getSelectionModel().getSelectedItem());
            fillPieChart(months);
            //reportsCombo.setPromptText(Integer.toString(months));
        }catch(Exception e){}    
    }        
    
    @FXML private AreaChart<String,Number> appointmentChart;
    @FXML private ComboBox appointmentsCombo;
    
    @FXML private NumberAxis ayaxis;
    
    @FXML
    public void fillAppointmentChart(String a)
    {
        appointmentChart.getData().clear();
        
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        
        int max1 = 0;
        int max2 = 0;
        
        if (a.equals("a") || a.equals("d"))
        {
            ArrayList<ArrayList<String>> docApp = admin.getDocAppointments();
            String date = "";

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> appointments = new ArrayList<Integer>();

            int size = docApp.size();
            for(int i = 1; i < size; i ++)
            {    
                date = docApp.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = appointments.remove(indx);
                    appointments.add(indx,(tmp+1));

                } else {

                    months.add(Month);
                    appointments.add(1);
                }    
            }
            
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = appointments.get(i);
                if (max1 < no) max1 = no;
                series1.getData().add(new XYChart.Data(month, no));
            }
            
        }    
        
        if (a.equals("a") || a.equals("l"))
        {
            ArrayList<ArrayList<String>> labApp = admin.getLabAppointments();
            String date = "";

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> appointments = new ArrayList<Integer>();

            int size = labApp.size();
            for(int i = 1; i < size; i ++)
            {    
                date = labApp.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = appointments.remove(indx);
                    appointments.add(indx,(tmp+1));

                } else {

                    months.add(Month);
                    appointments.add(1);
                }    
            }
            
            
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = appointments.get(i);
                if (max2 < no) max2 = no;
                series2.getData().add(new XYChart.Data(month, no));
            }
            
        }    
        
        ayaxis.setAutoRanging(false);
        if (max1 > max2) {    
            ayaxis.setUpperBound(max1 + 1);
            ayaxis.setTickUnit((max1 + 5)/5);
        } else {
            ayaxis.setUpperBound(max2 + 1);
            ayaxis.setTickUnit((max2 + 5)/5);
        }
        ayaxis.setLowerBound(0);
        
        
        switch (a)
        {
            case "d":
                appointmentChart.getData().addAll(series1);
                series1.setName("Doctor Appointments");
                break;
            case "l":
                appointmentChart.getData().addAll(series2); 
                series2.setName("Lab Appointments");
                break;
            default:
                appointmentChart.getData().addAll(series1,series2);
                series1.setName("Doctor Appointments");
                series2.setName("Lab Appointments");
        }
                
        
        /*
        appointmentsCombo.getItems().clear();
        appointmentsCombo.getItems().addAll("Doctor Appointments","Lab Appointments","Appointments");
        //appointmentsCombo.getSelectionModel().select("Appointments");
        */
    }        
    
    @FXML private void fillAppointments()
    {
        try{
            String app = (String)appointmentsCombo.getSelectionModel().getSelectedItem();
            
            if (app.equals("Doctor Appointments")) app = "d";
            else if (app.equals("Lab Appointments")) app = "l";
            else app = "a";
            
            fillAppointmentChart(app);
            //appointmentsCombo.getSelectionModel().select(app);
            
        }catch(Exception e){}   
    
    }        
    
    @FXML private AreaChart<String,Number> cancelledAppointmentChart;
    @FXML private ComboBox cancelledAppointmentsCombo;
    
    @FXML private NumberAxis cyaxis;
    
    @FXML
    public void fillCancelledAppointmentChart(String a)
    {
        cancelledAppointmentChart.getData().clear();
        
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        
        int max1 = 0;
        int max2 = 0;
        
        
        if (a.equals("a") || a.equals("d"))
        {
            ArrayList<ArrayList<String>> docApp = admin.getCancelledDocAppointments();
            String date = "";

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> appointments = new ArrayList<Integer>();

            int size = docApp.size();
            for(int i = 1; i < size; i ++)
            {    
                date = docApp.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = appointments.remove(indx);
                    appointments.add(indx,(tmp+1));

                } else {

                    months.add(Month);
                    appointments.add(1);
                }    
            }
            
            //patientAttendenceCombo.setValue("All");
            
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = appointments.get(i);
                if (max1 < no) max1 = no;
                series1.getData().add(new XYChart.Data(month, no));
            }
            
        }    
        
        if (a.equals("a") || a.equals("l"))
        {
            ArrayList<ArrayList<String>> labApp = admin.getCancelledLabAppointments();
            String date = "";

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> appointments = new ArrayList<Integer>();

            int size = labApp.size();
            for(int i = 1; i < size; i ++)
            {    
                date = labApp.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = appointments.remove(indx);
                    appointments.add(indx,(tmp+1));

                } else {

                    months.add(Month);
                    appointments.add(1);
                }    
            }
            
            
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = appointments.get(i);
                if (max2 < no) max2 = no;
                series2.getData().add(new XYChart.Data(month, no));
            }
            
        }    
        
        cyaxis.setAutoRanging(false);
        if (max1 > max2) {    
            cyaxis.setUpperBound(max1 + 1);
            cyaxis.setTickUnit((max1 + 5)/5);
        } else {
            cyaxis.setUpperBound(max2 + 1);
            cyaxis.setTickUnit((max2 + 5)/5);
        }
        cyaxis.setLowerBound(0);
        
        switch (a)
        {
            case "d":
                cancelledAppointmentChart.getData().addAll(series1);
                series1.setName("Doctor Appointments");
                break;
            case "l":
                cancelledAppointmentChart.getData().addAll(series2); 
                series2.setName("Lab Appointments");
                break;
            default:
                cancelledAppointmentChart.getData().addAll(series1,series2);
                series1.setName("Doctor Appointments");
                series2.setName("Lab Appointments");
        }
                
        
        /*
        appointmentsCombo.getItems().clear();
        appointmentsCombo.getItems().addAll("Doctor Appointments","Lab Appointments","Appointments");
        //appointmentsCombo.getSelectionModel().select("Appointments");
        */
    }        
    
    @FXML private void fillCancelledAppointments()
    {
        try{
            String app = (String)cancelledAppointmentsCombo.getSelectionModel().getSelectedItem();
            
            if (app.equals("Doctor Appointments")) app = "d";
            else if (app.equals("Lab Appointments")) app = "l";
            else app = "a";
            
            fillCancelledAppointmentChart(app);
            //appointmentsCombo.getSelectionModel().select(app);
            
        }catch(Exception e){}   
    
    }
    
    @FXML private ComboBox genericNameSelectCombo;
    @FXML private BarChart<String, Number> stockChart;
    
    public void fillStockChart()
    {
        try{
            ArrayList<ArrayList<String>> drugs = admin.getStockSummary();
            int noOfSlots = (drugs.size()-1);
            HashMap<String,String> drugInfo = admin.getDrugGenericInfo();

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
            stockChart.getData().clear();
            stockChart.getData().add(series1);

            Platform.runLater(() -> genericNameSelectCombo.getItems().clear());
            Platform.runLater(() -> genericNameSelectCombo.getItems().addAll(names));
        }catch(Exception e){}
    }
    
    
    @FXML private void genericNameSelect()
    {
        try{
            String selectedDrug = genericNameSelectCombo.getSelectionModel().getSelectedItem().toString();

            if (selectedDrug.equals("All")) 
            {
                fillStockChart();

            } else {    

                ArrayList<ArrayList<String>> drugs = admin.getStockSummary();
                int noOfSlots = (drugs.size()-1);
                //System.out.println(noOfSlots);
                //System.out.println(currentTimeTableData0);

                HashMap<String,String> drugInfo = admin.getDrugGenericInfo();

                ArrayList<ArrayList<String>> drugNames = admin.getDrugNames();

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
                        ArrayList<ArrayList<String>> drugBrandAmounts = admin.getDrugAmounts(drugNames.get(i).get(0));

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
                stockChart.getData().clear();
                stockChart.getData().add(series1);
                genericNameSelectCombo.setPromptText(selectedDrug);
            }
        }catch(Exception e){} 
    }        
    
    
    @FXML private PieChart supplierchart;
    @FXML private ComboBox supplierCombo;
    
    @FXML public void fillSupplierChart() {
        
        supplierchart.setVisible(true);
        
        HashMap<String,String> supplierNames = admin.getSupplierNames();
        
        ArrayList<ArrayList<String>> suppliers = admin.getSupplierSummary();
        int noOfSuppliers = suppliers.get(0).size();
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < noOfSuppliers; i++)
        {
            String supplierID = suppliers.get(0).get(i);
            int stocks = Integer.parseInt(suppliers.get(1).get(i));
            pieChartData.add(new PieChart.Data(supplierNames.get(supplierID), stocks));
        }
        //piechart.setLabelLineLength(20);
        
        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " (", data.pieValueProperty(), ")"
                        )
                )
        );
        
        
        supplierchart.setLegendSide(Side.BOTTOM); 
        supplierchart.setLabelsVisible(true);
        supplierchart.setData(pieChartData);

    }
    
    
    @FXML private void selectSupplierChart()
    {
        String chartType = (String)supplierCombo.getSelectionModel().getSelectedItem();
        if (chartType.equals("Propotions")) {
        
            fillSupplierChart();
            
        } else {
            
           
        
        }    
        
    }        
    
    @FXML private LineChart<String,Number> totalIncomeGraph;
    
    @FXML private ComboBox fromIncomeCombo;
    @FXML private ComboBox toIncomeCombo;
    
    public void fillTotalIncomeBarGraph(String fromDate,String toDate)
    {
        
        LocalDate date00;
        LocalDate date01;
        try{
            DateTimeFormatter fomatter0 = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            date00 = LocalDate.parse(fromDate, fomatter0);
        }catch(Exception e){
            date00 = LocalDate.now();   
        }
            
        try{
            DateTimeFormatter fomatter0 = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            date01 = LocalDate.parse(toDate, fomatter0);
        }catch(Exception e){
            date01 = LocalDate.now();
            date01 = date01.minusMonths(12);
        }
        
            
        DateTimeFormatter fomatter5 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String month1 = fomatter5.format(date00);
        String month2 = fomatter5.format(date01);
        
        
        ArrayList<ArrayList<String>> data = admin.lastTotalIncome(month1,month2);
        
        ArrayList<String> months = new ArrayList<String>(); 
        ArrayList<Integer> income = new ArrayList<Integer>();
        
        int size = data.size();
        for(int i = 1; i < size; i ++)
        {    
            String date = data.get(i).get(0);
            DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate date2 = LocalDate.parse(date, fomatter1);

            DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
            String Month = fomatter2.format(date2);
            
            System.out.println(Month);
            if ( months.contains(Month) ) {
            
                int indx = months.indexOf(Month);
                int tmp = income.remove(indx);
                income.add(indx,(tmp+Integer.parseInt(data.get(i).get(1))));
                
            } else {
                
                months.add(Month);
                income.add(Integer.parseInt(data.get(i).get(1)));
            }    
             
        }
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("From  "+fromDate+"  To  "+toDate);
        size = months.size();
        for(int i = 0; i < size; i++)
        {
            String month = months.get(i);
            int no = income.get(i);
            series1.getData().add(new XYChart.Data(month, no));
        }    
        totalIncomeGraph.getData().clear();
        totalIncomeGraph.getData().addAll(series1);
        
        ArrayList<String> mothyears = new ArrayList<String>();
        DateTimeFormatter fomatter3 = DateTimeFormatter.ofPattern("yyyy-MMM");
        LocalDate date = LocalDate.now();
        String yearMonth = "";
        for (int i= 0; i < 12; i++)
        {
            yearMonth = fomatter3.format(date);
            yearMonth += "-01";
            mothyears.add(0,yearMonth);
            date = date.minusMonths(1);
        }    
        
        
        
        Platform.runLater(() -> fromIncomeCombo.getItems().clear()); 
        Platform.runLater(() -> fromIncomeCombo.getItems().addAll(mothyears));
        Platform.runLater(() -> toIncomeCombo.getItems().clear());    
        Platform.runLater(() -> toIncomeCombo.getItems().addAll(mothyears));
        
    }        
    
    @FXML private void getIncome()
    {
        String fromDate = (String)fromIncomeCombo.getSelectionModel().getSelectedItem();
        String toDate = (String)toIncomeCombo.getSelectionModel().getSelectedItem(); 
        
        fillTotalIncomeBarGraph(fromDate,toDate);
    
        fromIncomeCombo.getSelectionModel().select(fromDate);
        toIncomeCombo.getSelectionModel().select(toDate);
        
    }        
    
    
    @FXML private LineChart incomeGraph;
    
    @FXML private ComboBox fromIncomeCombo1;
    @FXML private ComboBox toIncomeCombo1;
    
    public void fillIcome(String a,String fromDate,String toDate)
    {
        
        LocalDate date00;
        LocalDate date01;
        try{
            DateTimeFormatter fomatter0 = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            date00 = LocalDate.parse(fromDate, fomatter0);
        }catch(Exception e){
            date00 = LocalDate.now();   
        }
            
        try{
            DateTimeFormatter fomatter0 = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            date01 = LocalDate.parse(toDate, fomatter0);
        }catch(Exception e){
            date01 = LocalDate.now();
            date01 = date01.minusMonths(12);
        }
        
            
        DateTimeFormatter fomatter5 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String month1 = fomatter5.format(date00);
        String month2 = fomatter5.format(date01);
        
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        
        
        
        
        if ( a.equals("a") || a.equals("p"))
        {
            ArrayList<ArrayList<String>> data = admin.pharmacyIncome(month1,month2);

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> income = new ArrayList<Integer>();

            int size = data.size();
            for(int i = 1; i < size; i ++)
            {    
                String date = data.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                //System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = income.remove(indx);
                    income.add(indx,(tmp+Integer.parseInt(data.get(i).get(1))));

                } else {

                    months.add(Month);
                    income.add(Integer.parseInt(data.get(i).get(1)));
                }    

            }
            
            series1 = new XYChart.Series();
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = income.get(i);
                series1.getData().add(new XYChart.Data(month, no));
            }
        }
        
        if ( a.equals("a") || a.equals("d"))
        {
            ArrayList<ArrayList<String>> data = admin.appointmentIncome(month1,month2);

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> income = new ArrayList<Integer>();

            int size = data.size();
            for(int i = 1; i < size; i ++)
            {    
                String date = data.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                //System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = income.remove(indx);
                    income.add(indx,(tmp+Integer.parseInt(data.get(i).get(1))));

                } else {

                    months.add(Month);
                    income.add(Integer.parseInt(data.get(i).get(1)));
                }    
                    
            }
            
            
            series2 = new XYChart.Series();
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = income.get(i);
                series2.getData().add(new XYChart.Data(month, no));
            } 
        }
        
        if ( a.equals("a") || a.equals("l"))
        {
            ArrayList<ArrayList<String>> data = admin.laboratoryIncome(month1,month2);

            ArrayList<String> months = new ArrayList<String>(); 
            ArrayList<Integer> income = new ArrayList<Integer>();

            int size = data.size();
            for(int i = 1; i < size; i ++)
            {    
                String date = data.get(i).get(0);
                DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate date2 = LocalDate.parse(date, fomatter1);

                DateTimeFormatter fomatter2 = DateTimeFormatter.ofPattern("MMM");
                String Month = fomatter2.format(date2);

                //System.out.println(Month);
                if ( months.contains(Month) ) {

                    int indx = months.indexOf(Month);
                    int tmp = income.remove(indx);
                    income.add(indx,(tmp+Integer.parseInt(data.get(i).get(1))));

                } else {

                    months.add(Month);
                    income.add(Integer.parseInt(data.get(i).get(1)));
                }    

            }

            series3 = new XYChart.Series();
            size = months.size();
            for(int i = 0; i < size; i++)
            {
                String month = months.get(i);
                int no = income.get(i);
                series3.getData().add(new XYChart.Data(month, no));
            }
        }
        
        
        switch (a)
        {
            case "p":
                incomeGraph.getData().clear();
                incomeGraph.getData().addAll(series1);
                series1.setName("Pharmacy");
                break;
            case "d":
                incomeGraph.getData().clear();
                incomeGraph.getData().addAll(series2);
                series2.setName("Appointments");
                break;
            case "l":
                incomeGraph.getData().clear();
                incomeGraph.getData().addAll(series3); 
                series3.setName("Laboratory");
                break;
            default:
                incomeGraph.getData().clear();
                incomeGraph.getData().addAll(series1,series2,series3);
                series1.setName("Pharmacy");
                series2.setName("Appointments");
                series3.setName("Laboratory");
        }
        
        
        ArrayList<String> mothyears = new ArrayList<String>();
        DateTimeFormatter fomatter3 = DateTimeFormatter.ofPattern("yyyy-MMM");
        LocalDate date = LocalDate.now();
        String yearMonth = "";
        for (int i= 0; i < 12; i++)
        {
            yearMonth = fomatter3.format(date);
            yearMonth += "-01";
            mothyears.add(0,yearMonth);
            date = date.minusMonths(1);
        }    

        
        Platform.runLater(() -> fromIncomeCombo1.getItems().clear()); 
        Platform.runLater(() -> fromIncomeCombo1.getItems().addAll(mothyears));
        Platform.runLater(() -> toIncomeCombo1.getItems().clear());    
        Platform.runLater(() -> toIncomeCombo1.getItems().addAll(mothyears));
        
        
        
    
    }        
    
    @FXML private ComboBox graphType;
    
    @FXML private void showIncomeGraph()
    {
        String type = (String)graphType.getSelectionModel().getSelectedItem();
        String fromDate = (String)fromIncomeCombo1.getSelectionModel().getSelectedItem();
        String toDate = (String)toIncomeCombo1.getSelectionModel().getSelectedItem();     
        
        try{
        
            switch(type)
            {
                case "Pharmacy":
                    type = "p";
                    break;
                case "Laboratory":
                    type = "l";
                    break;
                case "Appointments":
                    type = "d";
                    break;   
                default:
                    type = "a";
            }    
        }catch(Exception e){type = "a";}
            
        fillIcome(type,fromDate,toDate);
        
        fromIncomeCombo1.getSelectionModel().select(fromDate);
        toIncomeCombo1.getSelectionModel().select(toDate);
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


