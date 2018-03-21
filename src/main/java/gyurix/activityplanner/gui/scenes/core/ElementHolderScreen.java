package gyurix.activityplanner.gui.scenes.core;

import gyurix.activityplanner.core.data.content.ElementHolder;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.renderers.ElementRenderer;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradientInv;

@Getter
public abstract class ElementHolderScreen<T extends ElementHolder> extends InfoScreen<T> {
    protected ElementRenderer elementRenderer;
    protected ScrollHandler elementScroller = new ScrollHandler();
    protected GridPane elements;
    protected ScrollPane elementsWrapper = new ScrollPane();
    protected Scene scene;
    protected Observable<Double> screenWidth = new Observable<>();

    public ElementHolderScreen(T info, Stage stage) {
        super(info, stage);
        elementsWrapper.setFitToWidth(true);
    }

    public void createElementsGrid() {
        elements = new GridPane();
        ColumnConstraints full = new ColumnConstraints();
        full.setPercentWidth(100);
        elements.getColumnConstraints().add(full);
        if (elementRenderer != null)
            elementRenderer.destroy();
        elementRenderer = new ElementRenderer(this);
        info.getElements().forEach((e) -> e.getData().accept(elementRenderer));
        elements.setOnScroll(elementScroller);
        elementsWrapper.setContent(elements);
    }

    public void createResizableScene(double multiplier) {
        Screen screen = Screen.getPrimary();

        Rectangle2D bounds = screen.getVisualBounds();
        double maxx = bounds.getWidth();
        double maxy = bounds.getHeight();
        scene = new Scene(grid, maxx * multiplier, maxy * multiplier);
        screenWidth.setData(multiplier * maxx);
        stage.setResizable(true);
        stage.setMinWidth(0.6 * multiplier * maxx);
        stage.setMinHeight(0.6 * multiplier * maxy);
        stage.setX(multiplier / 2 * maxx);
        stage.setY(multiplier / 2 * maxy);
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

    @Override
    public void prepareScene() {
        stage.setScene(scene);
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
                destroy();
            }
        });
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            screenWidth.setData(newVal.doubleValue());
        });
        stage.show();
    }

    @Override
    public void start() {
        super.start();
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
