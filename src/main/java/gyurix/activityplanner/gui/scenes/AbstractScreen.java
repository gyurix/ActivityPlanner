package gyurix.activityplanner.gui.scenes;

import javafx.stage.Stage;

public abstract class AbstractScreen {
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
