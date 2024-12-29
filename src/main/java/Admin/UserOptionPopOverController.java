package Admin;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author heshan
 */
class UserOptionPopOverController extends AnchorPane {

    public UserOptionPopOverController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/UserOptionPopOver.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private NumberAxis yAxis;
    @FXML
    private AreaChart<String, Number> onlineChart;

    /**
     * Area chart Fill info about the users that currently logged in
     */
    @FXML
    public void fillAreaChart() {

        XYChart.Series<String, Number> users = new XYChart.Series<>();
        users.setName("Users");
        users.getData().add(new XYChart.Data<>("8.00", 0));
        users.getData().add(new XYChart.Data<>("8.15", 1));
        users.getData().add(new XYChart.Data<>("8.30", 2));
        users.getData().add(new XYChart.Data<>("8.45", 1));
        users.getData().add(new XYChart.Data<>("9.00", 2));
        onlineChart.getData().add(users);
    }
}
