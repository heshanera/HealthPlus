package Receptionist;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author heshan
 */
public class Popover2Controller extends AnchorPane {

    public Popover2Controller() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Popover2.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML
    private ListView daysList;

    public void fillDaysList(ObservableList<String> items)
    {
        daysList.setItems(items);
    }
    
    @FXML
    public void closePopUp()
    {
        Stage stage = (Stage) daysList.getScene().getWindow();
        stage.close();
    }        
    
    

}
