package Doctor;

import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author heshan
 */
public class Message {
    private final SimpleStringProperty sender = new SimpleStringProperty("");
    private final SimpleStringProperty subject = new SimpleStringProperty("");
    private final SimpleStringProperty message = new SimpleStringProperty("");
    
    public Message() {
        this("", "", "");
    }
 
    public Message(String sender, String subject, String message) {
        setSender(sender);
        setSubject(subject);
        setMessage(message);
    }

    public String getSender() {
        return sender.get();
    }
 
    public void setSender(String value) {
        sender.set(value);
    }
        
    public String getSubject() {
        return subject.get();
    }
    
    public void setSubject(String value) {
        subject.set(value);
    }
    
    public String getMessage() {
        return message.get();
    }
    
    public void setMessage(String value) {
        message.set(value);
    }
}



