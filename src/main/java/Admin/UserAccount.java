/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 *
 * @author heshan
 */
public class UserAccount {
    private final SimpleStringProperty user = new SimpleStringProperty("");
    private final SimpleStringProperty lastLogin = new SimpleStringProperty("");
    private final SimpleStringProperty online = new SimpleStringProperty("");
    
    public UserAccount() {
        this("", "", "");
    }
 
    
    public UserAccount(String user, String lastLogin, String online) {
        setUser(user);
        setLastLogin(lastLogin);
        setOnline(online);
    }
    
    public void setUser(String val) {
        user.set(val);
    }
    
    public void setLastLogin(String val) {
        lastLogin.set(val);
    }
    
    public void setOnline(String val) {
        online.set(val);
    }
    
    public String getUser() {
        return user.get();
    }

    public String getLastLogin() {
        return lastLogin.get();
    }

    public String getOnline() {
        return online.get();
    }
    
    
}
