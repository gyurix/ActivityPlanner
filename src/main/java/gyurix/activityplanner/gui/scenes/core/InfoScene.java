package gyurix.activityplanner.gui.scenes.core;

import javafx.stage.Stage;
import lombok.Getter;

/**
 * InfoScene extends the AbstractScene, by adding an info parameter
 *
 * @param <T> - Type of the info parameter
 */
public abstract class InfoScene<T> extends AbstractScene {
    /**
     * The info parameter
     */
    @Getter
    protected final T info;

    /**
     * Creates a new InfoScene from the given info and stage parameters
     *
     * @param info  - The info parameter
     * @param stage - The stage of the window
     */
    public InfoScene(T info, Stage stage) {
        super(stage);
        this.info = info;
    }
}
