package Cashier;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;
/**
 *
 * @author heshan
 */
public class Refund {
    private final SimpleStringProperty patientID = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty billID = new SimpleStringProperty("");
    private final SimpleStringProperty service = new SimpleStringProperty("");
    private final SimpleStringProperty bill = new SimpleStringProperty("");
    private ImageView refund;
    
    /*
    public Refund() {
        this("", "","","","","");
    }
    */
    
    public Refund(String patientID,String date, String billID, String service, String bill,  ImageView refund) {
        setPatientID(patientID);
        setDate(date);
        setBillID(billID);
        setService(service);
        setBill(bill);
        setRefund(refund);
    }

    public String getPatientID() {
        return patientID.get();
    }
 
    public void setPatientID(String value) {
        patientID.set(value);
    }
    
    public String getDate() {
        return date.get();
    }
 
    public void setDate(String value) {
        date.set(value);
    }
    
    public String getBillID() {
        return billID.get();
    }
    
    public void setBillID(String value) {
        billID.set(value);
    }
    
    public String getService() {
        return service.get();
    }
    
    public void setService(String value) {
        service.set(value);
    }
    
    public String getBill() {
        return bill.get();
    }
    
    public void setBill(String value) {
        bill.set(value);
    }
    
    public ImageView getRefund() {
        return this.refund;
    }
    
    public void setRefund(ImageView value) {
        refund = value;
    }
    
}







