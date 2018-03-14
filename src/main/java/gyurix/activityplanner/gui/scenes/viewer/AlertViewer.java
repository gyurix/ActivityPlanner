package gyurix.activityplanner.gui.scenes.viewer;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.data.observation.ListObserver;
import gyurix.activityplanner.core.data.observation.Observable;
import gyurix.activityplanner.gui.renderers.ElementRenderer;
import gyurix.activityplanner.gui.scenes.InfoScreen;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradient;

public class AlertViewer extends InfoScreen<Alert> {
    private GridPane elements;
    private ScrollPane elementsWrapper;
    private GridPane grid = new GridPane();

    public AlertViewer(Alert info, Stage stage) {
        super(info, stage);
    }

    @Override
    public void addNodesToGrid() {

    }

    public GridPane createElementsGrid() {
        GridPane grid = new GridPane();
        ElementRenderer er = new ElementRenderer(grid);
        info.getElements().forEach((e) -> e.getData().accept(er));
        return grid;
    }

    @Override
    public void createNodes() {
        elements = createElementsGrid();
        elementsWrapper = new ScrollPane(elements);
        info.getElements().attach(new ListObserver<Observable<Element>>() {
            @Override
            public void onAdd(Observable<Element> observable) {
                elements = createElementsGrid();
                elementsWrapper.setContent(elements);
            }

            @Override
            public void onRemove(Observable<Element> observable) {
                elements = createElementsGrid();
                elementsWrapper.setContent(elements);
            }
        });
    }

    public void makeDynamicBackground(GridPane grid, Observable<String> obs) {
        attach(obs, () -> grid.setBackground(bgColorGradient(Color.web("#" + obs.getData()))));
    }

    @Override
    public void makeGrid() {
        grid.setVgap(5);
        grid.setHgap(5);
        makeDynamicBackground(grid, info.getColor());
        makeGridColumns();
        makeGridRows();
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
