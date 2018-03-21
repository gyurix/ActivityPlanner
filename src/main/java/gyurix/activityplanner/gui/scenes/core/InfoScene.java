package gyurix.activityplanner.gui.scenes.core;

import javafx.stage.Stage;
import lombok.Getter;

public abstract class InfoScene<T> extends AbstractScene {
    @Getter
    protected final T info;

    public InfoScene(T info, Stage stage) {
        super(stage);
        this.info = info;
    }
}
