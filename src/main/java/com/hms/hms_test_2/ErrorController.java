
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hms.hms_test_2;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author heshan
 */
public class ErrorController extends AnchorPane {

    public ErrorController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Error.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    

    @FXML Label formatLabel;
    public void addMessage(String message) {
        
        formatLabel.setText(message);

    }
    
}

