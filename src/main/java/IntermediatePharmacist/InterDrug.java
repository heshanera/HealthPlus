package IntermediatePharmacist;

import javafx.beans.property.SimpleStringProperty;

public class InterDrug{
    private final SimpleStringProperty name = new SimpleStringProperty("");
    final SimpleStringProperty amount = new SimpleStringProperty("");

    public InterDrug() {
        this("", "", "","","","");
    }

    public InterDrug(String drugName, String drugType, String drugUnit, String unitPrice, String amount, String suppliers) {
        setName(drugName);
        setAmount(amount);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }
    public int getAmount() {
        return Integer.parseInt(amount.get());
    }

    public void setAmount(String value) {
        amount.set(value);
    }

}





