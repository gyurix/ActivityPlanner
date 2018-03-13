package gyurix.activityplanner.gui.scenes;

import javafx.stage.Stage;

public abstract class InfoScreen<T> extends AbstractScreen {
    protected final T info;

    public InfoScreen(T info, Stage stage) {
        super(stage);
        this.info = info;
    }
}
