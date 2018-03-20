package gyurix.activityplanner.gui.scenes;

import javafx.stage.Stage;
import lombok.Getter;

public abstract class InfoScreen<T> extends AbstractScreen {
    @Getter
    protected final T info;

    public InfoScreen(T info, Stage stage) {
        super(stage);
        this.info = info;
    }
}
