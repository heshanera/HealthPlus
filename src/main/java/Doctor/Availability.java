package Doctor;

import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author heshan
 */
public class Availability {
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty time = new SimpleStringProperty("");
    private final SimpleStringProperty id = new SimpleStringProperty("");
    
    public Availability() {
        this("", "","");
    }
 
    public Availability(String date, String time,String id) {
        setDate(date);
        setTime(time);
        setId(id);
    }

    public String getDate() {
        return date.get();
    }
 
    public void setDate(String value) {
        date.set(value);
    }
        
    public String getTime() {
        return time.get();
    }
    
    public void setTime(String value) {
        time.set(value);
    }
    
    public String getId() {
        return id.get();
    }
    
    public void setId(String value) {
        id.set(value);
    }
    
}



