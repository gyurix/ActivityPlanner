package gyurix.activityplanner.gui.scenes.core;

import gyurix.activityplanner.core.data.content.properties.ElementHolder;
import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.gui.renderers.ElementRenderer;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradientInv;

@Getter
public abstract class ElementHolderScene<T extends ElementHolder> extends InfoScene<T> {
    protected ElementRenderer elementRenderer;
    protected ScrollHandler elementScroller = new ScrollHandler();
    protected GridPane elements;
    protected ScrollPane elementsWrapper = new ScrollPane();
    protected UserScene userScene;

    public ElementHolderScene(UserScene userScene, T info, Stage stage) {
        super(info, stage);
        this.userScene = userScene;
        elementsWrapper.setFitToWidth(true);
        attachLater(info.getId(), () -> {
            stage.hide();
            destroy();
        });
    }

    public void createElementsGrid() {
        elements = new GridPane();
        ColumnConstraints full = new ColumnConstraints();
        full.setPercentWidth(100);
        elements.getColumnConstraints().add(full);
        if (elementRenderer != null)
            elementRenderer.destroy();
        elementRenderer = new ElementRenderer(this);
        ObservableList<Observable<Element>> elementList = info.getElements();
        elementList.forEach((e) -> e.getData().accept(elementRenderer));
        elementRenderer.createAddButtons();
        elements.setOnScroll(elementScroller);
        elementsWrapper.setContent(elements);
    }

    public void makeDynamicBackground(GridPane grid, Observable<String> obs) {
        attach(obs, () -> {
            Color c = Color.web("#" + obs.getData());
            grid.setBackground(bgColorGradientInv(c));
        });
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
        ColumnConstraints side = new ColumnConstraints();
        ColumnConstraints center = new ColumnConstraints();
        side.setPercentWidth(4);
        center.setPercentWidth(92);
        grid.getColumnConstraints().addAll(side, center, side);
    }

    public class ScrollHandler implements EventHandler<ScrollEvent> {
        @Override
        public void handle(ScrollEvent e) {
            double deltaY = e.getDeltaY() * 3;
            double width = elements.getBoundsInLocal().getHeight();
            double vvalue = elementsWrapper.getVvalue();
            elementsWrapper.setVvalue(vvalue - deltaY / width);
        }
    }
}
