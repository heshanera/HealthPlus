package Admin;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author heshan
 */
public class AdminMessageController extends AnchorPane {


    public AdminMessageController() 
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/adminMessage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML TextField receiver;
    @FXML TextArea message;
    
    
    public void setPaceholders()
    {
        receiver.setPromptText("Receiver");
        message.setPromptText("Type Message Here");
    } 
    

}
