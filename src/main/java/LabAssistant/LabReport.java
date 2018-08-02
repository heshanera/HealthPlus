package LabAssistant;

import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author heshan
 */
public class LabReport {
    private final SimpleStringProperty constituent = new SimpleStringProperty("");
    private final SimpleStringProperty result = new SimpleStringProperty("");
    
    
    public LabReport() {
        this("", "");
    }
 
    public LabReport(String constituent, String result) {
        setConstituent(constituent);
        setResult(result);
    }

    public String getConstituent() {
        return constituent.get();
    }
 
    public void setConstituent(String value) {
        constituent.set(value);
    }
        
    public String getResult() {
        return result.get();
    }
    
    public void setResult(String value) {
        result.set(value);
    }
    
}
