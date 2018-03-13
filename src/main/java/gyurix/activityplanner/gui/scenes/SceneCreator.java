package gyurix.activityplanner.gui.scenes;

import javafx.stage.Stage;

public interface SceneCreator {
    void addNodesToGrid();

    default void apply(Stage stage) {
        createNodes();
        makeGrid();
        addNodesToGrid();
        prepareScene(stage);
    }

    void createNodes();

    void makeGrid();

    void makeGridColumns();

    void makeGridRows();

    void prepareScene(Stage stage);
}
