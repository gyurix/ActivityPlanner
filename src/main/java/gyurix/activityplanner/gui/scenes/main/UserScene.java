package gyurix.activityplanner.gui.scenes.main;

import gyurix.activityplanner.core.data.content.Alert;
import gyurix.activityplanner.core.data.content.Table;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.storage.ConfigUtils;
import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.assets.Icons;
import gyurix.activityplanner.gui.renderers.ChatRenderer;
import gyurix.activityplanner.gui.renderers.ContentRenderer;
import gyurix.activityplanner.gui.scenes.SceneUtils;
import gyurix.activityplanner.gui.scenes.core.InfoScene;
import gyurix.activityplanner.gui.scenes.viewer.ContentViewer;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

import static gyurix.activityplanner.gui.scenes.SceneUtils.*;

/**
 * UserScene is used for rendering the whole user dashboard
 */
@Getter
public class UserScene extends InfoScene<User> {
    /**
     * Icon width multiplier for create table and create alert buttons
     */
    private static final double ICON_SIZE = 0.025;

    /**
     * Background of the alerts part of the screen
     */
    private static final Color alertBackground = Color.web("#ff7070");

    /**
     * Background of the chat part of the screen
     */
    private static final Color chatBackground = Color.web("#a0a0ff");

    /**
     * Main background of the screen
     */
    private static final Color mainBackground = Color.SILVER;

    /**
     * Background of the table part of the screen
     */
    private static final Color tableBackground = Color.web("#a07000");

    /**
     * Holder for rendered alerts
     */
    private final GridPane alerts = new GridPane();

    /**
     * Holder for chat
     */
    private final GridPane chat = new GridPane();

    /**
     * Logout button
     */
    private final Button logoutButton = new Button("Logout");

    /**
     * Holder for rendered tables
     */
    private final GridPane tables = new GridPane();

    /**
     * Wrapper for making alerts background colorable
     */
    private GridPane alertsWrapper;

    /**
     * Renderer of the chat part of the screen
     */
    private ChatRenderer chatRenderer;

    /**
     * Renderer of the contents parts of the screen (Alerts + Tables)
     */
    private ContentRenderer renderer;

    /**
     * Wrapper for making tables background colorable
     */
    private GridPane tableWrapper;

    /**
     * Informative username label
     */
    private Label usernameLabel;

    /**
     * Constructs a new UserScene
     *
     * @param info  - The User
     * @param stage - The Stage of the main window
     */
    public UserScene(User info, Stage stage) {
        super(info, stage);
    }


    @Override
    protected void addNodesToGrid() {
        chatRenderer.render();
        addNodesToMainGrid();
    }

    @Override
    protected void createNodes() {
        chatRenderer = new ChatRenderer(this);
        Observable<String> un = info.getUsername();
        usernameLabel = new Label();
        logoutButton.setOnAction((e) -> {
            disable();
            new LoginScene(stage).start();
        });
        chat.setBackground(bgColorGradient(chatBackground));

        attach(info.getCreatedContents(), () -> info.visitCreatedContents(renderer = new ContentRenderer(this)));
        alertsWrapper = createWrapper(alerts, bgColorGradient(alertBackground),
                createClickablePicture(Icons.ADD, ICON_SIZE, () -> {
                    Alert a = new Alert(System.currentTimeMillis(),
                            "Alert Title",
                            "Alert Subtitle",
                            colorToHex(getRandomColor()).substring(1));
                    DataStorage ds = DataStorage.getInstance();
                    getInfo().getCreatedContents().add(ds.addContent(a));
                    new ContentViewer(this, a, new Stage()).start();
                }));
        tableWrapper = createWrapper(tables, bgColorGradient(tableBackground),
                createClickablePicture(Icons.ADD, ICON_SIZE, () -> {
                    Table t = new Table("Table Title",
                            "Table Subtitle",
                            colorToHex(getRandomColor()).substring(1));
                    DataStorage ds = DataStorage.getInstance();
                    info.getCreatedContents().add(ds.addContent(t));
                    new ContentViewer(this, t, new Stage()).start();
                }));
        attach(un, () -> usernameLabel.setText(un.getData()));
    }

    @Override
    protected void createScene() {
        createResizableScene(0.8, "User Dashboard");
    }

    @Override
    protected void makeGrid() {
        setGap(grid, 10);
        setGap(chat, 10);
        setGap(tables, 10);
        setGap(alerts, 10);

        grid.setBackground(bgColorGradient(mainBackground));

        makeGridColumns();
        makeGridRows();
    }

    @Override
    protected void makeGridColumns() {
        makeChatGridColumns();
        makeContentGridColumns();
        makeMainGridColumns();
    }

    @Override
    protected void makeGridRows() {
        RowConstraints row0 = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        row0.setPercentHeight(10);
        row1.setPercentHeight(90);
        grid.getRowConstraints().addAll(row0, row1);
    }

    /**
     * Adds nodes to the main grid
     */
    private void addNodesToMainGrid() {
        grid.add(usernameLabel, 1, 0);
        grid.add(logoutButton, 2, 0);
        grid.add(doubleWrap(tableWrapper), 0, 1);
        grid.add(doubleWrap(alertsWrapper), 1, 1);
        grid.add(doubleWrap(chat), 2, 1);
    }

    /**
     * Creates a colored, scrollable wrapper for tables or alerts
     *
     * @param pane       - The wrappable pane
     * @param background - The background of the wrapped contents
     * @param addButton  - The add button used for adding new content
     * @return The created wrapper
     */
    private GridPane createWrapper(GridPane pane, Background background, Pane addButton) {
        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(pctCol(90), pctCol(10));

        RowConstraints topRow = new RowConstraints();
        topRow.setPrefHeight(48);
        RowConstraints bottomRow = new RowConstraints();
        bottomRow.setPrefHeight(24);

        grid.getRowConstraints().addAll(topRow, pctRow(85), bottomRow);
        grid.add(addButton, 1, 0);
        grid.add(pane, 0, 1, 2, 1);
        grid.setBackground(background);
        return grid;
    }

    @Override
    public void destroy() {
        disable();
        SceneUtils.getIoThread().shutdown();
    }

    /**
     * Disables the UserScene, used on logging out
     */
    private void disable() {
        super.destroy();
        ConfigUtils.getInstance().saveConfig();
        renderer.destroy();
    }

    /**
     * Double wraps a GridPane, for keeping it's color and making it scrollable
     *
     * @param content - The double wrappable contents wrapper grid
     * @return The wrapper ScrollPane
     */
    private ScrollPane doubleWrap(GridPane content) {
        GridPane grid = new GridPane();

        grid.getRowConstraints().add(pctRow(100));
        grid.getColumnConstraints().add(pctCol(100));
        grid.add(content, 0, 0);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        return scroll;
    }

    /**
     * Makes an aligned 33% wide column
     *
     * @param alignment - The alignment of the column
     * @return The aligned column
     */
    private ColumnConstraints makeAlignedColumn(HPos alignment) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(33);
        col.setHalignment(alignment);
        return col;
    }

    /**
     * Make the columns of the chat grid
     */
    private void makeChatGridColumns() {
        ColumnConstraints side = pctCol(6);
        ColumnConstraints body = pctCol(22);
        chat.getColumnConstraints().addAll(side, body, body, body, body, side);
    }

    /**
     * Makes the columns of the alerts and tables grid
     */
    private void makeContentGridColumns() {
        ColumnConstraints side = pctCol(5);
        ColumnConstraints main = pctCol(90);
        tables.getColumnConstraints().addAll(side, main, side);
        alerts.getColumnConstraints().addAll(side, main, side);
    }

    /**
     * Makes the columns of the main grid
     */
    private void makeMainGridColumns() {
        grid.getColumnConstraints().addAll(makeAlignedColumn(HPos.LEFT), makeAlignedColumn(HPos.CENTER), makeAlignedColumn(HPos.RIGHT));
    }

    /**
     * Sets the gap of the given grid
     *
     * @param grid - The changeable grid
     * @param gap  - The requested gap of the grid
     */
    private void setGap(GridPane grid, double gap) {
        grid.setHgap(gap);
        grid.setVgap(gap);
    }
}
