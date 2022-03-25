package Cashier;
import com.hms.hms_test_2.WarningController;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.PopOver;

public class DisplayPopUp extends AnchorPane {

    public PopOver popOver;
    public void showPopup(String message, TextField text)
    {
        if (popOver == null) {
            popOver = new PopOver();
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        }
        WarningController popup = new WarningController();
        popup.addMessage(message);
        popOver.setContentNode(popup);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.show(text);
    }
}
