package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObserverContainer;
import gyurix.activityplanner.gui.assets.Icons;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static java.lang.Double.MAX_VALUE;

public abstract class DataRenderer extends ObserverContainer {
    private static final Cursor clickableCursor = Cursor.OPEN_HAND;

    public Pane createClickableImage(Icons icon, double sizeMultiplier, Runnable onClick) {
        ImageView img = new ImageView(icon.getImage());
        img.setPreserveRatio(true);
        Pane wrapper = new Pane(img);
        wrapper.setCursor(clickableCursor);
        wrapper.setMaxHeight(VBox.USE_PREF_SIZE);
        wrapper.setOnMouseReleased((e) -> {
            if (e.getButton() != MouseButton.PRIMARY)
                return;
            onClick.run();
        });
        attach(getScreenWidth(), () -> {
            double maxx = getScreenWidth().getData() * sizeMultiplier;
            img.setFitWidth(maxx);
            wrapper.setMaxWidth(maxx);
        });
        return wrapper;
    }

    public abstract Observable<Double> getScreenWidth();

    public Label renderText(int fontSize, Observable<String> obs) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        label.setFont(Font.font(fontSize));
        attach(obs, () -> label.setText(obs.getData()));
        return label;
    }
}
