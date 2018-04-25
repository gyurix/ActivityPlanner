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

/**
 * ElementHolderScene is an extension of an InfoScene.
 * It's used for rendering elements bellow each other.
 *
 * @param <T> - Type of the holdable Element
 */
@Getter
public abstract class ElementHolderScene<T extends ElementHolder> extends InfoScene<T> {

    /**
     * The Element renderer, used for rendering elements
     */
    protected ElementRenderer elementRenderer;

    /**
     * The Element scroller, used for making the list of elements scrollable
     */
    protected ScrollHandler elementScroller = new ScrollHandler();

    /**
     * The grid containing all the elements
     */
    protected GridPane elements;

    /**
     * The elements wrapper wraps the elements grid, for making it scrollable
     */
    protected ScrollPane elementsWrapper = new ScrollPane();

    /**
     * Parent UserScene
     */
    protected UserScene userScene;

    /**
     * Constructs a new ElementHolderScene
     *
     * @param userScene - The parent UserScene
     * @param info      - The renderable element holder
     * @param stage     - The stage of the window, where we would like to show the elements
     */
    public ElementHolderScene(UserScene userScene, T info, Stage stage) {
        super(info, stage);
        this.userScene = userScene;
        elementsWrapper.setFitToWidth(true);
        attachLater(info.getId(), () -> {
            stage.hide();
            destroy();
        });
    }

    /**
     * Creates the grid of elements and renders all the elements
     */
    public void createElementsGrid() {
        //Create the elements grid
        elements = new GridPane();
        elements.getColumnConstraints().add(pctCol(100));
        elements.setOnScroll(elementScroller);
        elementsWrapper.setContent(elements);

        //Remove old element renderer, if we had one
        if (elementRenderer != null)
            elementRenderer.destroy();

        //Create new ElementRenderer and render the elements in it
        elementRenderer = new ElementRenderer(this);
        ObservableList<Observable<Element>> elementList = info.getElements();
        elementList.forEach((e) -> e.getData().accept(elementRenderer));
        elementRenderer.createAddButtons();
    }

    @Override
    protected void makeGrid() {
        grid.setVgap(5);
        grid.setHgap(5);
        makeInvGradientBackground(grid, info.getColor());
        makeGridColumns();
        makeGridRows();
    }

    /**
     * Makes an inverted gradient dynamic background
     *
     * @param grid       - The grid, which background should be changed
     * @param background - The dynamically changeable background of the grid
     */
    public void makeInvGradientBackground(GridPane grid, Observable<String> background) {
        attach(background, () -> {
            Color c = Color.web("#" + background.getData());
            grid.setBackground(bgColorGradientInv(c));
        });
    }

    @Override
    protected void makeGridColumns() {
        ColumnConstraints side = new ColumnConstraints();
        ColumnConstraints center = new ColumnConstraints();
        side.setPercentWidth(4);
        center.setPercentWidth(92);
        grid.getColumnConstraints().addAll(side, center, side);
    }

    /**
     * ScrollHandler used for making element list scrollable with proper scrolling speed
     */
    public class ScrollHandler implements EventHandler<ScrollEvent> {
        @Override
        public void handle(ScrollEvent e) {
            double deltaY = e.getDeltaY() * 3;
            double height = elements.getBoundsInLocal().getHeight();
            double curY = elementsWrapper.getVvalue();
            elementsWrapper.setVvalue(curY - deltaY / height);
        }
    }
}
