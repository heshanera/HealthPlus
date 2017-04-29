package Pharmacist;

import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author heshan
 */
public class Drug{
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty unit = new SimpleStringProperty("");
    private final SimpleStringProperty price = new SimpleStringProperty("");
    final SimpleStringProperty amount = new SimpleStringProperty("");
    private final SimpleStringProperty suppliers = new SimpleStringProperty("");
    
    public Drug() {
        this("", "", "","","","");
    }
 
    public Drug(String drugName, String drugType, String drugUnit, String unitPrice, String amount, String suppliers) {
        setName(drugName);
        setType(drugType);
        setUnit(drugUnit);
        setPrice(unitPrice);
        setAmount(amount);
        setSuppliers(suppliers);
    }

    public String getName() {
        return name.get();
    }
 
    public void setName(String value) {
        name.set(value);
    }
        
    public String getType() {
        return type.get();
    }
    
    public void setType(String value) {
        type.set(value);
    }
    
    public String getUnit() {
        return unit.get();
    }
    
    public void setUnit(String value) {
        unit.set(value);
    }
    
    public String getPrice() {
        return price.get();
    }
    
    public void setPrice(String value) {
        price.set(value);
    }
    
    public int getAmount() {
        return Integer.parseInt(amount.get());
    }
    
    public void setAmount(String value) {
        amount.set(value);
    }
    
    public String getSuppliers() {
        return suppliers.get();
    }
    
    public void setSuppliers(String value) {
        suppliers.set(value);
    }
    
}





