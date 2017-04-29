package Doctor;

import java.util.*;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author heshan
 */
public class PopoverController extends AnchorPane {

    TextField text;
    /**
     *
     * @param username
     */
    public PopoverController(TextField text) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Popover.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        this.text = text;

        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML
    private ListView brandList;
    @FXML 
    private Label genericName;
    
    public void fillBrandList(ArrayList<String> data, String genName)
    {
        ObservableList<String> items =FXCollections.observableArrayList ();
        int size = data.size();
        
        for(int i =0; i < size; i++)
        {
            items.add(data.get(i));
        }
        brandList.setItems(items);
        genericName.setText(genName);
    }
    
    @FXML
    private void selectBrand(KeyEvent e)
    {
        if(e.getCode() == KeyCode.ENTER)
        {
            String brand = (String)brandList.getSelectionModel().getSelectedItem();
            text.setText(brand);

            Stage stage = (Stage) brandList.getScene().getWindow();
            stage.close();
        }
        
    }
    
    public void close()
    {
        ((Node)(genericName)).getScene().getWindow().hide();
    }        
   
}
