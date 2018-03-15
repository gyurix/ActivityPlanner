package gyurix.activityplanner.gui.scenes.viewer;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.observation.ListObserver;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.gui.renderers.ElementRenderer;
import gyurix.activityplanner.gui.scenes.InfoScreen;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static gyurix.activityplanner.gui.scenes.SceneUtils.bgColorGradient;
import static gyurix.activityplanner.gui.scenes.SceneUtils.formatTime;
import static java.lang.Double.MAX_VALUE;

public class AlertViewer extends InfoScreen<Alert> {
    private VBox elements;
    private ElementRenderer er;
    private ScrollPane elementsWrapper;
    private GridPane grid = new GridPane();
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

    @Override
    public void createNodes() {
        title = renderText(24, info.getTitle());
        title.setPrefWidth(MAX_VALUE);
        title.setAlignment(Pos.BOTTOM_CENTER);

        subtitle = renderText(16, info.getSubtitle());
        subtitle.setAlignment(Pos.TOP_LEFT);
        subtitle.setPrefWidth(MAX_VALUE);

        date = renderDate(info.getDueDate());
        date.setPrefWidth(MAX_VALUE);
        date.setAlignment(Pos.BOTTOM_RIGHT);

        elements = createElementsGrid();
        elementsWrapper = new ScrollPane(elements);
        elementsWrapper.setStyle("-fx-background: #" + info.getColor() + "ff");
        elementsWrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
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

    @Override
    public void makeGridColumns() {
        ColumnConstraints side = new ColumnConstraints();
        ColumnConstraints center = new ColumnConstraints();
        side.setPercentWidth(10);
        center.setPercentWidth(80);
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
        top.setPercentHeight(5);
        title.setPercentHeight(8);
        subtitle.setPercentHeight(5);
        date.setPercentHeight(4);
        elements.setPercentHeight(73);
        bottom.setPercentHeight(5);
        grid.getRowConstraints().addAll(top, title, subtitle, date, elements, bottom);
    }

    @Override
    public void prepareScene() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double maxx = bounds.getWidth();
        double maxy = bounds.getHeight();
        Scene scene = new Scene(grid, maxx * 0.4, maxy * 0.4);
        stage.setResizable(false);
        stage.setX(0.3 * maxx);
        stage.setY(0.3 * maxy);
        stage.setScene(scene);
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
                destroy();
            }
        });
        stage.show();
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

    public VBox createElementsGrid() {
        VBox box = new VBox();
        if (er != null)
            er.destroy();
        er = new ElementRenderer(box);
        info.getElements().forEach((e) -> e.getData().accept(er));
        return box;
    }

    @Override
    public void destroy() {
        System.out.println("Destroy alert viewer");
        super.destroy();
        er.destroy();
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
}
