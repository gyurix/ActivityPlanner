package gyurix.activityplanner.gui.scenes;

import gyurix.activityplanner.core.observation.ObserverContainer;
import javafx.stage.Stage;

public abstract class AbstractScreen extends ObserverContainer {
    protected final Stage stage;

    public AbstractScreen(Stage stage) {
        this.stage = stage;
    }

    public abstract void addNodesToGrid();

    public abstract void createNodes();

    public abstract void makeGrid();

    public abstract void makeGridColumns();

    public abstract void makeGridRows();

    public abstract void prepareScene();

    public void start() {
        createNodes();
        makeGrid();
        addNodesToGrid();
        prepareScene();
    }
}
