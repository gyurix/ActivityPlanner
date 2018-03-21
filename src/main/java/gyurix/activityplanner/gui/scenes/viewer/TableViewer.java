package gyurix.activityplanner.gui.scenes.viewer;

import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.gui.scenes.core.ElementHolderScene;
import javafx.stage.Stage;

public class TableViewer extends ElementHolderScene<Table> {
    public TableViewer(Table info, Stage stage) {
        super(info, stage);
    }

    @Override
    public void addNodesToGrid() {

    }

    @Override
    public void createNodes() {

    }

    @Override
    public void createScene() {
        createResizableScene(0.5, "Table Viewer");
    }

    @Override
    public void makeGrid() {

    }

    @Override
    public void makeGridColumns() {

    }

    @Override
    public void makeGridRows() {

    }

    @Override
    public void prepareScene() {

    }
}
