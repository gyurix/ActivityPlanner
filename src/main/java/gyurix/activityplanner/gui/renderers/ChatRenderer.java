package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.Student;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.assets.Icons;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class ChatRenderer extends DataRenderer {
    private static final double CHAT_ICON_SIZE = 0.06;
    private final ContextMenu loadingMenu = makeLoadingMenu();
    private GridPane chat;
    private UserScene parent;

    public ChatRenderer(UserScene parent) {
        this.parent = parent;
        chat = parent.getChat();
    }

    private void createIcons() {
        chat.add(createImageMenu(Icons.CHAT_STUDENT, CHAT_ICON_SIZE, makeOneStudentMenu()), 1, 0);
        chat.add(createImageMenu(Icons.CHAT_STUDENTS, CHAT_ICON_SIZE, makeStudentsMenu()), 2, 0);
        chat.add(createImageMenu(Icons.CHAT_LECTOR, CHAT_ICON_SIZE, makeOneLectorMenu()), 3, 0);
        chat.add(createImageMenu(Icons.CHAT_BOTH, CHAT_ICON_SIZE, makeBothMenu()), 4, 0);
    }

    private MenuItem createInvidualMenuItem(User user) {
        MenuItem menuItem = new MenuItem(user.getUsername().getData());
        menuItem.setOnAction((e) -> showChat(user));
        return menuItem;
    }

    @Override
    public Observable<Double> getScreenWidth() {
        return parent.getScreenWidth();
    }

    private Consumer<Consumer<ContextMenu>> makeBothMenu() {
        return (consumer) -> {
            DataStorage.getInstance().getLectures((lectures -> {
                ContextMenu menu = new ContextMenu();
                MenuItem loading = new MenuItem("Loading both menu...");
                menu.getItems().add(loading);
                consumer.accept(menu);
            }));
        };
    }

    private ContextMenu makeLoadingMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem item = new MenuItem("Loading... ");
        menu.getItems().add(item);
        return menu;
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
        return (consumer) -> {
            DataStorage.getInstance().getLectures((lectures -> {
                ContextMenu menu = new ContextMenu();
                MenuItem loading = new MenuItem("Loading one student menu...");
                menu.getItems().add(loading);
                consumer.accept(menu);
            }));
        };
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
        createIcons();
    }

    public void showChat(User user) {
        System.out.println("Show chat - " + user.getUsername().getData());
    }

    public void showGroupChat(Lecture l, boolean includeLecture) {
        System.out.println("Show group chat - " + l.getUsername().getData() + " - " + includeLecture);
    }
}
