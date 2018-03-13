package gyurix.activityplanner.gui.scenes;

import javafx.stage.Stage;

public abstract class InfoScreen<T> implements SceneCreator {
    protected final T info;
    protected final Stage stage;

    public InfoScreen(T info, Stage stage) {
        this.info = info;
        this.stage = stage;
    }

    @Override
    public void start() {
        createNodes();
        makeGrid();
        addNodesToGrid();
        prepareScene();
    }
}
