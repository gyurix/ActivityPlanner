package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.data.element.ChatMessage;
import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.Student;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.assets.Icons;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.function.Consumer;

public class ChatRenderer extends DataRenderer {
    private static final double CHAT_ICON_SIZE = 0.06;
    private GridPane chat;
    private GridPane chatMessages = new GridPane();
    private Observable<ObservableList<ChatMessage>> currentChannel = new Observable<>();
    private UserScene parent;
    private TextField sendMessage;

    public ChatRenderer(UserScene parent) {
        this.parent = parent;
        chat = parent.getChat();
    }

    private void createBody() {
        chat.add(doubleWrap(chatMessages), 0, 1, 4, 1);
    }

    private void createBottomRow() {
        sendMessage = new TextField();
        sendMessage.setOnAction((e) -> sendMessage());
    }

    private MenuItem createInvidualMenuItem(User user) {
        MenuItem menuItem = new MenuItem(user.getUsername().getData());
        menuItem.setOnAction((e) -> showChat(user));
        return menuItem;
    }

    private void createNodes() {
        createTopRow();
        createBody();
        createBottomRow();
    }

    private void createTopRow() {
        chat.add(createImageMenu(Icons.CHAT_STUDENT, CHAT_ICON_SIZE, makeOneStudentMenu()), 1, 0);
        chat.add(createImageMenu(Icons.CHAT_STUDENTS, CHAT_ICON_SIZE, makeStudentsMenu()), 2, 0);
        chat.add(createImageMenu(Icons.CHAT_LECTOR, CHAT_ICON_SIZE, makeOneLectorMenu()), 3, 0);
        chat.add(createImageMenu(Icons.CHAT_BOTH, CHAT_ICON_SIZE, makeBothMenu()), 4, 0);
    }

    private ScrollPane doubleWrap(GridPane pane) {
        GridPane wrap = new GridPane();
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        wrap.getColumnConstraints().add(col);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        wrap.add(pane, 0, 0);
        ScrollPane scroll = new ScrollPane(wrap);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        return scroll;
    }

    @Override
    public Observable<Double> getScreenWidth() {
        return parent.getScreenWidth();
    }

    private Consumer<Consumer<ContextMenu>> makeBothMenu() {
        return (consumer) -> DataStorage.getInstance().getLectures((lectures -> {
            ContextMenu menu = new ContextMenu();
            MenuItem loading = new MenuItem("Loading both menu...");
            menu.getItems().add(loading);
            consumer.accept(menu);
        }));
    }

    private Consumer<Consumer<ContextMenu>> makeOneLectorMenu() {
        return (consumer) -> {
            DataStorage ds = DataStorage.getInstance();
            ContextMenu menu = new ContextMenu();
            if (parent.getInfo() instanceof Lecture) {
                ds.getLectures((lectures -> {
                    javafx.collections.ObservableList<MenuItem> items = menu.getItems();
                    lectures.forEach((l) -> items.add(createInvidualMenuItem(l)));
                    consumer.accept(menu);
                }));
                return;
            }
            ((Student) parent.getInfo()).getLectures().forEach((name) -> {
                javafx.collections.ObservableList<MenuItem> items = menu.getItems();
                ds.getUser(name, (u) -> items.add(createInvidualMenuItem(u)));
                consumer.accept(menu);
            });
        };
    }

    private Consumer<Consumer<ContextMenu>> makeOneStudentMenu() {
        return (consumer) -> DataStorage.getInstance().getLectures((lectures -> {
            ContextMenu menu = new ContextMenu();
            MenuItem loading = new MenuItem("Loading one student menu...");
            menu.getItems().add(loading);
            consumer.accept(menu);
        }));
    }

    private Consumer<Consumer<ContextMenu>> makeStudentsMenu() {
        return (consumer) -> {
            DataStorage.getInstance().getLectures((lectures -> {
                ContextMenu menu = new ContextMenu();
                MenuItem loading = new MenuItem("Loading students menu...");
                menu.getItems().add(loading);
                consumer.accept(menu);
            }));
        };
    }

    public void render() {
        createNodes();
        showChat(parent.getInfo());
    }

    public void renderCurrentChannel() {

    }

    public void sendMessage() {

    }

    public void showChat(User user) {
        String u1 = parent.getInfo().getUsername().getData();
        String u2 = user.getUsername().getData();
        StringBuilder channel = new StringBuilder("uu:");
        switch (u1.compareTo(u2)) {
            case -1:
                channel.append(u1).append(':').append(u2);
                break;
            case 0:
                channel.append(u1);
                break;
            case 1:
                channel.append(u2).append(':').append(u1);
                break;

        }
        DataStorage.getInstance().getChatMessages(channel.toString(), (cm) -> currentChannel.setData(cm));
    }

    public void showGroupChat(Lecture l, boolean includeLecture) {
        System.out.println("Show group chat - " + l.getUsername().getData() + " - " + includeLecture);
    }
}
