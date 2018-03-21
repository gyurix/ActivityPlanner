package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObserverContainer;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import static java.lang.Double.MAX_VALUE;

public class DataRenderer extends ObserverContainer {
    public Label renderText(int fontSize, Observable<String> obs) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        label.setFont(Font.font(fontSize));
        attach(obs, () -> label.setText(obs.getData()));
        return label;
    }
}
