package LabAssistant;

import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author heshan
 */
public class Prescription {
    private final SimpleStringProperty prescID = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty doctor = new SimpleStringProperty("");
    private final SimpleStringProperty prescription = new SimpleStringProperty("");
    
    
    public Prescription() {
        this("", "", "","");
    }
 
    public Prescription(String prescID, String date, String doctor, String prescription) {
        setPrescID(prescID);
        setDate(date);
        setDoctor(doctor);
        setPrescription(prescription);
    }

    public String getPrescID() {
        return prescID.get();
    }
 
    public void setPrescID(String value) {
        prescID.set(value);
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
    
    public String getPrescription() {
        return prescription.get();
    }
    
    public void setPrescription(String value) {
        prescription.set(value);
    }
}




