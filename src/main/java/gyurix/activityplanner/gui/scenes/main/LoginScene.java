package gyurix.activityplanner.gui.scenes.main;

import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.scenes.SceneUtils;
import gyurix.activityplanner.gui.scenes.core.AbstractScene;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static gyurix.activityplanner.gui.scenes.SceneUtils.makeAlert;

/**
 * The renderer of the login window
 */
public class LoginScene extends AbstractScene {
    /**
     * The gradient background of the scene
     */
    private final Background background = SceneUtils.bgColorGradient(Color.web("#b08045"));

    /**
     * The login button
     */
    private final Button loginButton = new Button("Login");

    /**
     * The password field
     */
    private final PasswordField passwordField = new PasswordField();

    /**
     * The password label
     */
    private final Label passwordLabel = new Label("Password");

    /**
     * The username field
     */
    private final TextField usernameField = new TextField("l1");

    /**
     * The username label
     */
    private final Label usernameLabel = new Label("Username");

    /**
     * Constructs a new LoginScene
     *
     * @param stage - The main application windows stage
     */
    public LoginScene(Stage stage) {
        super(stage);
    }

    @Override
    protected void addNodesToGrid() {
        grid.add(usernameLabel, 0, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(usernameField, 1, 1);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);
    }

    @Override
    protected void createNodes() {
        loginButton.setOnAction(this::login);
        usernameField.setOnAction(this::login);
        passwordField.setOnAction(this::login);
    }

    @Override
    protected void createScene() {
        createResizableScene(0.3, "Login");
    }

    @Override
    protected void makeGrid() {
        grid.setBackground(background);
        grid.setHgap(10);
        grid.setVgap(10);
        makeGridColumns();
        makeGridRows();
    }

    @Override
    protected void makeGridColumns() {
        ColumnConstraints title = new ColumnConstraints();
        title.setPercentWidth(30);
        title.setHalignment(HPos.RIGHT);

        ColumnConstraints editor = new ColumnConstraints();
        editor.setHalignment(HPos.LEFT);

        grid.getColumnConstraints().addAll(title, editor);
    }

    @Override
    protected void makeGridRows() {
        RowConstraints sep = pctRow(11);
        RowConstraints main = new RowConstraints();
        grid.getRowConstraints().addAll(sep, main, sep, main);
    }

    @Override
    public void destroy() {
        disable();
        SceneUtils.getIoThread().shutdown();
    }

    /**
     * Disables the login scene, used at logging in
     */
    public void disable() {
        super.destroy();
    }

    /**
     * Performs pre login checks and logs in
     *
     * @param e - The action event triggering login
     */
    private void login(ActionEvent e) {
        DataStorage.getInstance().getUser(usernameField.getText(), (user) -> Platform.runLater(() -> {
            if (user == null) {
                makeAlert("Incorrect Username", "The entered username is incorrect").showAndWait();
                return;
            }
            if (!passwordField.getText().equals(user.getPassword().getData())) {
                makeAlert("Incorrect Password", "The entered password is incorrect").showAndWait();
                return;
            }
            disable();
            new UserScene(user, stage).start();
        }));
    }
}
