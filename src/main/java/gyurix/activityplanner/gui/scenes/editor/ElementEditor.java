package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.gui.scenes.core.AbstractScreen;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Stage;

public class ElementEditor extends AbstractScreen {

    public ElementEditor(Alert el, Stage stage) {
        super(stage);
    }

    public void addNodesToGrid() {
    }

    public void createNodes() {
    }

    public void makeGrid() {
        makeGridColumns();
        makeGridRows();
    }

    public void makeGridColumns() {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        column1.setHalignment(HPos.RIGHT);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.LEFT);
        grid.getColumnConstraints().addAll(column1, column2);
    }

    public void makeGridRows() {
    }

    public void prepareScene() {
        Scene scene = new Scene(grid, 320, 240);
        stage.setTitle("• ActivityPlanner - Login •");
        stage.setScene(scene);
        stage.show();
    }
}
