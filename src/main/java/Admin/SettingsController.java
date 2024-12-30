package Admin;

import com.hms.hms_test_2.LoginController;
import com.hms.hms_test_2.SuccessIndicatorController;
import com.hms.hms_test_2.SystemConfiguration;
import com.hms.hms_test_2.WarningController;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.PopOver;

public class SettingsController extends AnchorPane {

    private Admin admin;
    private AdminController adminC;

    public SettingsController(Admin admin, AdminController adminC) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Settings.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.admin = admin;
        this.adminC = adminC;

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    String result = "";

    public void loadConfigFile() throws IOException {
        try {
            SystemConfiguration config = SystemConfiguration.getInstance();
            databaselbl.setText(config.getConfig("database"));
            connectionlbl.setText(config.getConfig("connection"));
            dbDriver.setText(config.getConfig("dbClassName"));
            dbUsernamelbl.setText(config.getConfig("user"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void checkConnection() {
        ArrayList<ArrayList<String>> data = admin.checkConnection();
        if (data.get(1).get(0).equals(admin.username)) {
            showSuccessIndicator();
        }
    }

    private PopOver popOver4;

    @FXML
    private TextField databaselbl;
    @FXML
    private TextField connectionlbl;
    @FXML
    private TextField dbUsernamelbl;
    @FXML
    private TextField dbDriver;
    @FXML
    private PasswordField dbPasswordlbl;

    private void showPasswordPopup() {

        if (popOver4 == null) {
            popOver4 = new PopOver();
            popOver4.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);

        }
        WarningController popup = new WarningController();
        popup.addMessage("Password!");

        popOver4.setContentNode(popup);
        popOver4.setAutoFix(true);
        popOver4.setAutoHide(true);
        popOver4.setHideOnEscape(true);
        popOver4.setDetachable(false);
        popOver4.show(dbPasswordlbl);
    }

    @FXML
    private Button backup;
    DirectoryChooser chooser = new DirectoryChooser();

    @FXML
    private void makeBackup() {
        String pass = dbPasswordlbl.getText();
        if (pass.equals("")) {
            showPasswordPopup();

        } else {

            try {
                SystemConfiguration config = SystemConfiguration.getInstance();
                String ip = "127.0.0.1";
                String databaseSchema = config.getConfig("database");
                String user = config.getConfig("user");
                String path = "";

                Stage stage = new Stage();
                chooser.setTitle("Select Export Directory");
                File selectedDirectory = chooser.showDialog(stage);
                path = selectedDirectory.getAbsolutePath() + "/";

                boolean result = admin.export(ip, databaseSchema, user, pass, path);
                if (result) {
                    showSuccessIndicator();
                    dbPasswordlbl.setText("");
                } else {
                    showPasswordPopup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private Button editDatabaseInfoButton;

    @FXML
    private void editDatabaseInfo() {
        if (editDatabaseInfoButton.getText().equals("Edit")) {
            String pass = dbPasswordlbl.getText();
            if (pass.equals("")) {
                showPasswordPopup();
            } else {
                databaselbl.setEditable(true);
                connectionlbl.setEditable(true);
                dbUsernamelbl.setEditable(true);
                dbDriver.setEditable(true);
                editDatabaseInfoButton.setText("Save");
            }
        } else {
            try {
                SystemConfiguration config = SystemConfiguration.getInstance();
                config.updateConfig("connection", connectionlbl.getText());
                config.updateConfig("user", dbUsernamelbl.getText());
                config.updateConfig("database", databaselbl.getText());
                config.saveConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
            databaselbl.setEditable(false);
            connectionlbl.setEditable(false);
            dbUsernamelbl.setEditable(false);
            dbDriver.setEditable(false);
            editDatabaseInfoButton.setText("Edit");
        }

    }

    @FXML
    private void restart() {
        String pass = dbPasswordlbl.getText();
        if (pass.equals("")) {
            showPasswordPopup();
        } else {
            Stage stage2;
            stage2 = (Stage) adminC.getScene().getWindow();
            stage2.close();

            Stage stage3;
            stage3 = (Stage) closeAccounts.getScene().getWindow();
            stage3.close();

            Stage stage = new Stage();
            LoginController login = new LoginController();
            stage.setScene(new Scene(login));
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            // set Stage boundaries to visible bounds of the main screen
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
    }

    @FXML
    private Button closeAccounts;

    @FXML
    private void closeViewAccounts(ActionEvent event) {

        Stage stage;
        if (event.getSource() == closeAccounts) {
            stage = (Stage) closeAccounts.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void showSuccessIndicator() {
        Stage stage = new Stage();
        SuccessIndicatorController success = new SuccessIndicatorController();
        Scene scene = new Scene(success);
        stage.setScene(scene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        // set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        stage.initStyle(StageStyle.UNDECORATED);
        scene.setFill(null);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
}
