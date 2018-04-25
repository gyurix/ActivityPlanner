package gyurix.activityplanner.gui.scenes.editor;

import gyurix.activityplanner.core.data.content.properties.ElementHolder;
import gyurix.activityplanner.gui.scenes.core.AbstractScene;
import gyurix.activityplanner.gui.scenes.core.ElementHolderScene;
import javafx.stage.Stage;

/**
 * Editor containing utility methods for editing elements
 */
public abstract class Editor extends AbstractScene {
    /**
     * @param holder - The holder of the editable element
     * @param stage  - The stage used of the window
     */
    public Editor(ElementHolderScene<? extends ElementHolder> holder, Stage stage) {
        super(stage);
        //Auto close editor if the element holder is removed
        attachLater(holder.getInfo().getId(), () -> {
            destroy();
            stage.hide();
        });
    }

    @Override
    protected void makeGrid() {
        grid.setVgap(5);
        makeGridColumns();
        makeGridRows();
    }

    @Override
    protected void makeGridColumns() {
        grid.getColumnConstraints().addAll(pctCol(5), pctCol(90), pctCol(5));
    }

    @Override
    protected void makeGridRows() {
        grid.getRowConstraints().addAll(pctRow(5), pctRow(5), pctRow(85), pctRow(5));
    }
}
