package gyurix.activityplanner.gui.scenes.viewer;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.renderers.ElementRenderer;
import gyurix.activityplanner.gui.scenes.InfoScreen;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradientInv;
import static gyurix.activityplanner.gui.scenes.SceneUtils.formatTime;
import static java.lang.Double.MAX_VALUE;

@Getter
public class AlertViewer extends InfoScreen<Alert> {
    private ElementRenderer elementRenderer;
    private GridPane elements;
    private ScrollPane elementsWrapper;
    private GridPane grid = new GridPane();
    private Scene scene;
    private Observable<Double> screenWidth = new Observable<>();
    private ScrollHandler scrollHandler = new ScrollHandler();
    private Label title, subtitle, date;

    public AlertViewer(Alert info, Stage stage) {
        super(info, stage);
    }

    @Override
    public void addNodesToGrid() {
        grid.add(title, 1, 1);
        grid.add(subtitle, 1, 2);
        grid.add(date, 1, 3);
        grid.add(elementsWrapper, 1, 4);
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
        elements.setOnScroll(scrollHandler);
        elementsWrapper.setContent(elements);
    }

    @Override
    public void createNodes() {
        Screen screen = Screen.getPrimary();

        Rectangle2D bounds = screen.getVisualBounds();
        double maxx = bounds.getWidth();
        double maxy = bounds.getHeight();
        scene = new Scene(grid, maxx * 0.5, maxy * 0.5);
        screenWidth.setData(0.5 * maxx);
        stage.setResizable(true);
        stage.setMinWidth(0.3 * maxx);
        stage.setMinHeight(0.3 * maxy);
        stage.setX(0.25 * maxx);
        stage.setY(0.25 * maxy);
        title = renderText(24, info.getTitle());
        title.setPrefWidth(MAX_VALUE);
        title.setAlignment(Pos.BOTTOM_CENTER);

        subtitle = renderText(16, info.getSubtitle());
        subtitle.setAlignment(Pos.TOP_LEFT);
        subtitle.setPrefWidth(MAX_VALUE);

        date = renderDate(info.getDueDate());
        date.setPrefWidth(MAX_VALUE);
        date.setAlignment(Pos.BOTTOM_RIGHT);

        elementsWrapper = new ScrollPane();
        elementsWrapper.setFitToWidth(true);
        createElementsGrid();
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
    public void makeGridRows() {
        RowConstraints top = new RowConstraints();
        RowConstraints title = new RowConstraints();
        RowConstraints subtitle = new RowConstraints();
        RowConstraints date = new RowConstraints();
        RowConstraints elements = new RowConstraints();
        RowConstraints bottom = new RowConstraints();
        date.setMinHeight(20);
        bottom.setMinHeight(5);
        grid.getRowConstraints().addAll(top, title, subtitle, date, elements, bottom);
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
    public void destroy() {
        super.destroy();
        elementRenderer.destroy();
    }

    public void makeDynamicBackground(GridPane grid, Observable<String> obs) {
        attach(obs, () -> {
            Color c = Color.web("#" + obs.getData());
            grid.setBackground(bgColorGradientInv(c));
        });
    }

    private Label renderDate(Observable<Long> obs) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        attach(obs, () -> label.setText(formatTime(obs.getData())));
        return label;
    }

    private Label renderText(int fontSize, Observable<String> obs) {
        Label label = new Label();
        label.setPrefWidth(MAX_VALUE);
        label.setFont(Font.font(fontSize));
        attach(obs, () -> label.setText(obs.getData()));
        return label;
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
