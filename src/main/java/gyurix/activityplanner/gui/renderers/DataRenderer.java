package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObserverContainer;
import gyurix.activityplanner.gui.assets.Icons;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.function.Consumer;

import static gyurix.activityplanner.gui.scenes.SceneUtils.formatTime;
import static java.lang.Double.MAX_VALUE;

/**
 * DataRenderer is the core of every other renderer.
 * It contains useful methods for rendering various type of data
 */
public abstract class DataRenderer extends ObserverContainer {
    /**
     * Cursor shown above clickable GUI elements
     */
    private static final Cursor clickableCursor = Cursor.OPEN_HAND;

    /**
     * Creates a picture which automatically resizes itself, when its parent window is resized
     *
     * @param icon            - The showable icon
     * @param widthMultiplier - The width multiplier of the picture
     * @return The auto resizing picture
     */
    private Pane createAutoResizingPicture(Icons icon, double widthMultiplier) {
        ImageView img = new ImageView(icon.getImage());
        img.setPreserveRatio(true);
        Pane wrapper = new Pane(img);
        wrapper.setCursor(clickableCursor);
        wrapper.setMaxHeight(VBox.USE_PREF_SIZE);
        attach(getScreenWidth(), () -> {
            double maxx = getScreenWidth().getData() * widthMultiplier;
            img.setFitWidth(maxx);
            wrapper.setMaxWidth(maxx);
        });
        return wrapper;
    }

    /**
     * Creates a clickable, auto resizing picture
     *
     * @param icon            - The showable icon
     * @param widthMultiplier - The width multiplier of the picture
     * @param onClick         - The Runnable, which should be executed when the user clicks to the picture
     * @return The created picture
     */
    protected Pane createClickablePicture(Icons icon, double widthMultiplier, Runnable onClick) {
        Pane wrapper = createAutoResizingPicture(icon, widthMultiplier);
        wrapper.setOnMouseReleased((e) -> {
            if (e.getButton() != MouseButton.PRIMARY)
                return;
            onClick.run();
        });
        return wrapper;
    }

    /**
     * Creates a clickable, auto resizing picture, which shows a menu on clicking to it
     *
     * @param icon            - The showable icon
     * @param widthMultiplier - The width multiplier of the picture
     * @param menu            - The generator of the context menu, which should be shown when the user clicks to the picture
     * @return The created picture
     */
    protected Pane createImageMenu(Icons icon, double widthMultiplier, Consumer<Consumer<ContextMenu>> menu) {
        Pane wrapper = createAutoResizingPicture(icon, widthMultiplier);
        wrapper.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY)
                menu.accept((m) -> m.show(wrapper, event.getScreenX(), event.getScreenY() - 10));
        });
        return wrapper;
    }

    /**
     * Gets the observable width of the parent window used for making auto resizing objects
     *
     * @return The observable width of the parent window
     */
    public abstract Observable<Double> getScreenWidth();

    /**
     * Makes a column constraints with the given percent width
     *
     * @param percent - The percent width of the creatable column constraints
     * @return The created column constraints
     */
    protected ColumnConstraints pctCol(double percent) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(percent);
        return col;
    }

    /**
     * Makes a row constraints with the given percent height
     *
     * @param percent - The percent height of the creatable row constraints
     * @return The created row constraints
     */
    protected RowConstraints pctRow(double percent) {
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(percent);
        return row;
    }

    /**
     * Renders an auto updating date, stored in a observable UTC format.
     * Shows it's time and date information.
     *
     * @param date - The renderable date
     * @return The rendered date
     */
    protected Label renderDate(Observable<Long> date) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        label.setAlignment(Pos.BOTTOM_RIGHT);
        attach(date, () -> label.setText(formatTime(date.getData())));
        return label;
    }

    /**
     * Renders the given text with the given font size
     *
     * @param fontSize - The font size
     * @param text     - The renderable text
     * @return The rendered text
     */
    protected Label renderText(int fontSize, Observable<String> text) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        label.setFont(Font.font(fontSize));
        attach(text, () -> label.setText(text.getData()));
        return label;
    }
}
