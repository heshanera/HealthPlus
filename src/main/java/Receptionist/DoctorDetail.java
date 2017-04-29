
package Receptionist;

import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author heshan
 */
public class DoctorDetail {
    private final SimpleStringProperty doctorID = new SimpleStringProperty("");
    private final SimpleStringProperty doctorName = new SimpleStringProperty("");
    private final SimpleStringProperty area = new SimpleStringProperty("");
    private final SimpleStringProperty availability = new SimpleStringProperty("");
    private final SimpleStringProperty days = new SimpleStringProperty("");
    
    
    public DoctorDetail() 
    {
        this("", "", "","","");
    }
 
    public DoctorDetail(String doctorID, String doctorName, String area, String availability, String days) 
    {
        setDoctorID(doctorID);
        setDoctorName(doctorName);
        setArea(area);
        setAvailability(availability);
        setDays(days);
    }

    public String getDoctorID() {
        return doctorID.get();
    }
 
    public void setDoctorID(String value) {
        doctorID.set(value);
    }
        
    public String getDoctorName() {
        return doctorName.get();
    }
    
    public void setDoctorName(String value) {
        doctorName.set(value);
    }
    
    public String getArea() {
        return area.get();
    }
    
    public void setArea(String value) {
        area.set(value);
    }
    
    public String getAvailability() {
        return availability.get();
    }
    
    public void setAvailability(String value) {
        availability.set(value);
    }
    
    public String getDays() {
        return days.get();
    }
    
    public void setDays(String value) {
        days.set(value);
    }
}



