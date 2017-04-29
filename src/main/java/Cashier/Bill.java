package Cashier;

import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author heshan
 */
public class Bill {
    private final SimpleStringProperty patientID = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");
    
    private final SimpleStringProperty doctor = new SimpleStringProperty("");
    private final SimpleStringProperty hospital = new SimpleStringProperty("");
    private final SimpleStringProperty pharmacy = new SimpleStringProperty("");
    private final SimpleStringProperty laboratory = new SimpleStringProperty("");
    private final SimpleStringProperty appointment = new SimpleStringProperty("");
    
    private final SimpleStringProperty bill = new SimpleStringProperty("");
    private final SimpleStringProperty billID = new SimpleStringProperty("");
    
    
    
    public Bill() {
        this("", "", "","","","","","","");
    }
 
    public Bill(String patientID, String date, String doctor, String hospital, String pharmacy, String laboratory, String appointment,String bill,String billID) 
    {
        setPatientID(patientID);
        setDate(date);
        
        setDoctor(doctor);
        setHospital(hospital);
        setPharmacy(pharmacy);
        setLaboratory(laboratory);
        setAppointment(appointment);
        
        setBill(bill);
        setBillID(billID);
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
    
    public String getDoctor() {
        return doctor.get();
    }
    
    public void setDoctor(String value) {
        doctor.set(value);
    }
    
    public String getHospital() {
        return hospital.get();
    }
    
    public void setHospital(String value) {
        hospital.set(value);
    }
    
    public String getPharmacy() {
        return pharmacy.get();
    }
    
    public void setPharmacy(String value) {
        pharmacy.set(value);
    }
    
    public String getLaboratory() {
        return laboratory.get();
    }
    
    public void setLaboratory(String value) {
        laboratory.set(value);
    }
    
    public String getAppointment() {
        return appointment.get();
    }
    
    public void setAppointment(String value) {
        appointment.set(value);
    }
    
    public String getBill() {
        return bill.get();
    }
    
    public void setBill(String value) {
        bill.set(value);
    }
    
    public String getBillID() {
        return billID.get();
    }
    
    public void setBillID(String value) {
        billID.set(value);
    }
}






