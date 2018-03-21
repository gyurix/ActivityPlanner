package gyurix.activityplanner.gui.scenes.core;

import gyurix.activityplanner.gui.renderers.DataRenderer;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public abstract class AbstractScreen extends DataRenderer {
    protected final Stage stage;
    protected GridPane grid = new GridPane();

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
        stage.setOnCloseRequest(event -> destroy());
    }
}
