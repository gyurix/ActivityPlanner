package gyurix.activityplanner.gui.renderers;

import gyurix.activityplanner.core.data.element.ChatMessage;
import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.data.user.Lector;
import gyurix.activityplanner.core.data.user.Student;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.DataStorage;
import gyurix.activityplanner.gui.scenes.main.UserScene;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static gyurix.activityplanner.gui.assets.Icons.*;
import static gyurix.activityplanner.gui.scenes.SceneUtils.*;

/**
 * Renders the chat panel of the main GUI
 */
public class ChatRenderer extends DataRenderer {
    /**
     * Size multiplier of the 4 receiver group selector icons
     */
    private static final double CHAT_ICON_SIZE = 0.06;

    /**
     * Main background of the chat pane
     */
    private static final Background MAIN_BG = bgColor(Color.web("#aaf"));

    /**
     * Background of other users messages
     */
    private static final Background OTHERS_BG = bgColorGradient(Color.web("#aaa"), new CornerRadii(5));

    /**
     * Background of own chat messages
     */
    private static final Background OWN_BG = bgColorGradient(Color.web("#ffa"), new CornerRadii(5));

    /**
     * Renderer for elements appearing in chat messages
     */
    private final ElementRenderer elementRenderer;

    /**
     * Holder of the whole chat pane, including message sending box and the receiver selector icons
     */
    private GridPane chat;

    /**
     * Holder of all the chat messages
     */
    private GridPane chatMessages = new GridPane();

    /**
     * Title of the chat, including the receiving user/groups name
     */
    private Label chatTitle = new Label("Chat with yourself");

    /**
     * Messages of the currently selected chat channel
     */
    private Observable<ObservableList<ChatMessage>> currentChannel = new Observable<>();

    /**
     * The parent UserScene containing this chat pane
     */
    private UserScene parent;

    /**
     * Generates the context menu items of groups (lectures + students together) for Students.
     * For lectures, this method opens the group chat instead of showing the context menu.
     */
    private Consumer<Consumer<ContextMenu>> groupBoth = (consumer) -> {
        User user = parent.getInfo();
        if (user instanceof Student) {
            DataStorage ds = DataStorage.getInstance();
            ContextMenu menu = new ContextMenu();
            javafx.collections.ObservableList<MenuItem> items = menu.getItems();
            ((Student) user).getLectores().forEach(
                    (name) -> ds.getUser(name,
                            (l) -> items.add(createGroupMenuItem((Lector) l, true))));
            consumer.accept(menu);
            return;
        }
        showGroupChat((Lector) user, true);
    };

    /**
     * Generates the context menu items of groups (students without lectors) for Students.
     * For lectures, this method shows a alert about that they can not access this channel.
     */
    private Consumer<Consumer<ContextMenu>> groupStudents = (consumer) -> {
        User user = parent.getInfo();
        if (user instanceof Student) {
            DataStorage ds = DataStorage.getInstance();
            ContextMenu menu = new ContextMenu();
            javafx.collections.ObservableList<MenuItem> items = menu.getItems();
            ((Student) user).getLectores().forEach(
                    (name) -> ds.getUser(name,
                            (l) -> items.add(createGroupMenuItem((Lector) l, false))));
            consumer.accept(menu);
            return;
        }
        makeAlert("Only students", "This channel is only accessible by students").showAndWait();
    };

    /**
     * Generates the context menu items of lectors.
     * Students can only see their lectors.
     * Lectors can see every other lector.
     */
    private Consumer<Consumer<ContextMenu>> invLector = (consumer) -> {
        DataStorage ds = DataStorage.getInstance();
        ContextMenu menu = new ContextMenu();
        User user = parent.getInfo();
        javafx.collections.ObservableList<MenuItem> items = menu.getItems();
        if (user instanceof Lector) {
            ds.getLectures((lectures -> lectures.forEach(
                    (l) -> items.add(createInvidualMenuItem(l)))));
            consumer.accept(menu);
            return;
        }
        ((Student) user).getLectores().forEach((name) -> ds.getUser(name, (u) -> items.add(createInvidualMenuItem(u))));
        consumer.accept(menu);
    };

    /**
     * Generates the context menu items of students.
     * Students can see their every lectors students.
     * Lectors can see their own students only.
     */
    private Consumer<Consumer<ContextMenu>> invStudent = (consumer) -> {
        DataStorage ds = DataStorage.getInstance();
        ContextMenu menu = new ContextMenu();
        javafx.collections.ObservableList<MenuItem> items = menu.getItems();
        if (parent.getInfo() instanceof Lector) {
            ((Lector) parent.getInfo()).getAssignedStudents().forEach(
                    (name) -> ds.getUser(name,
                            (u) -> items.add(createInvidualMenuItem(u))));
            consumer.accept(menu);
            return;
        }
        ((Student) parent.getInfo()).getLectores().forEach(
                (name) -> ds.getUser(name,
                        (u) -> items.add(createStudentListMenu((Lector) u))));
        consumer.accept(menu);
    };

    /**
     * TextField for writing the sendable message
     */
    private TextField sendMessage;

    /**
     * Constructs a new ChatRenderer from the parent UserScene
     *
     * @param parent - The parent UserScene
     */
    public ChatRenderer(UserScene parent) {
        this.parent = parent;
        elementRenderer = new ElementRenderer(parent);
        showChat(parent.getInfo());
        attach(currentChannel, this::renderCurrentChannel);
        chat = parent.getChat();
    }

    /**
     * Creates the body of the chat pane, including the scrollable wrapper of the actual chat messages
     */
    private void createBody() {
        chatMessages.setBackground(MAIN_BG);
        chatMessages.getColumnConstraints().addAll(pctCol(3), pctCol(94), pctCol(3));
        chat.add(doubleWrap(chatMessages), 0, 2, 6, 1);
    }

    /**
     * Creates the bottom row of the chat pane, including the message sending TextField
     */
    private void createBottomRow() {
        sendMessage = new TextField();
        sendMessage.setOnAction((e) -> sendMessage());
        chat.add(sendMessage, 1, 3, 4, 1);
    }

    /**
     * Creates a context menu item which shows the group chat on click
     *
     * @param lector        - The lector, whose group should be shown
     * @param includeLector - True if the lector should be included in the chat, false otherwise
     * @return The created menu item
     */
    private MenuItem createGroupMenuItem(Lector lector, boolean includeLector) {
        MenuItem menuItem = new MenuItem(lector.getUsername().getData());
        menuItem.setOnAction((e) -> showGroupChat(lector, includeLector));
        return menuItem;
    }

    /**
     * Creates a context menu item which shows an invidual chat channel with a user
     *
     * @param user - The invidual user with who you would like to chat
     * @return The created menu item
     */
    private MenuItem createInvidualMenuItem(User user) {
        MenuItem menuItem = new MenuItem(user.getUsername().getData());
        menuItem.setOnAction((e) -> showChat(user));
        return menuItem;
    }

    /**
     * Creates all the nodes shown inside the chat pane
     */
    private void createNodes() {
        chat.setPrefHeight(Double.MAX_VALUE);
        chat.getRowConstraints().addAll(pctRow(15), pctRow(5), pctRow(70), pctRow(10));
        createTopRow();
        createBody();
        createBottomRow();
    }

    /**
     * Creates a context menu, containing all the students of a lector.
     * Clicking to a student opens individual chat with him.
     *
     * @param lector - The lector, whose students should be added to the context menu
     * @return The created context menu
     */
    private Menu createStudentListMenu(Lector lector) {
        Menu menu = new Menu(lector.getUsername().getData());
        javafx.collections.ObservableList<MenuItem> items = menu.getItems();
        DataStorage ds = DataStorage.getInstance();
        lector.getAssignedStudents().forEach(
                (name) -> ds.getUser(name,
                        (s) -> items.add(createInvidualMenuItem(s))));

        return menu;
    }

    /**
     * Creates the top row of the chat pane, including the receiver selector icons
     */
    private void createTopRow() {
        chat.add(createImageMenu(CHAT_STUDENT, CHAT_ICON_SIZE, invStudent), 1, 0);
        chat.add(createImageMenu(CHAT_STUDENTS, CHAT_ICON_SIZE, groupStudents), 2, 0);
        chat.add(createImageMenu(CHAT_LECTOR, CHAT_ICON_SIZE, invLector), 3, 0);
        chat.add(createImageMenu(CHAT_BOTH, CHAT_ICON_SIZE, groupBoth), 4, 0);
        chatTitle.setAlignment(Pos.CENTER);
        chatTitle.setPrefWidth(Double.MAX_VALUE);
        chat.add(chatTitle, 0, 1, 6, 1);
    }

    /**
     * Double wraps a grid pane.
     * The first wrapping is done by another GridPane for keeping the original GripPanes background
     * The second wrapping is done by a ScrollPane for making the GridPane vertically scrollable
     *
     * @param pane - The double wrappable GridPane
     * @return The double wrapped GridPane
     */
    private ScrollPane doubleWrap(GridPane pane) {
        GridPane wrap = new GridPane();
        wrap.getColumnConstraints().add(pctCol(100));
        wrap.getRowConstraints().add(pctRow(100));
        wrap.add(pane, 0, 0);
        ScrollPane scroll = new ScrollPane(wrap);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        return scroll;
    }

    /**
     * Fixes the comparation result, of 2 objects by forcing values being in interval -1;1
     * Negative numbers will be converted to -1.
     * 0 will remain 0.
     * Positive numbers will be converted to +1.
     *
     * @param result - The fixable comparation result
     * @return The fixed result number
     */
    public int fixCompare(int result) {
        return result == 0 ? 0 : result / Math.abs(result);
    }

    @Override
    public Observable<Double> getScreenWidth() {
        return parent.getScreenWidth();
    }

    /**
     * Renders a new ChatMessage, including all the required wrapping, padding and background coloring,
     * sent by the logged in User
     *
     * @param chatMessage - The renderable ChatMessage
     * @return The rendered ChatMessage
     */
    public GridPane makeChatLine(ChatMessage chatMessage) {
        GridPane grid = new GridPane();
        /* Columns of chat line holder grid:
           #0 - external left padding (2%)
           #1 - internal left padding (3%)
           #2 - left half of body (45%)
           #3 - right half of body (45%)
           #4 - internal right padding (3%)
           #5 - external right padding (2%)
         */
        grid.getColumnConstraints().addAll(pctCol(2), pctCol(3), pctCol(45), pctCol(45), pctCol(3), pctCol(2));

        //Date is written to the right half and the internal right padding of body columns
        grid.add(renderDate(chatMessage.getDate()), 3, 0, 2, 1);
        //Sender name is written to the internal left padding and left half of body columns
        grid.add(renderText(12, chatMessage.getSender()), 1, 0, 2, 1);

        //Set dynamic background
        attach(chatMessage.getSender(), () ->
                grid.setBackground(chatMessage.getSender().equals(parent.getInfo().getUsername()) ? OWN_BG : OTHERS_BG));

        //Render the new element
        SingleElementRenderer renderer = new SingleElementRenderer(elementRenderer,
                (newEl) -> grid.add(newEl, 2, 1, 2, 1));
        chatMessage.getMessage().getData().accept(renderer);
        Observable<Element> message = chatMessage.getMessage();
        attach(message, () -> message.getData().accept(renderer));
        return grid;
    }

    /**
     * Makes an empty line for separating 2 chat lines
     *
     * @return The empty line
     */
    public GridPane makeEmptyLine() {
        GridPane emptyLine = new GridPane();
        emptyLine.setMinHeight(12);
        return emptyLine;
    }

    /**
     * Creates the nodes and shows the individual chat with yourself
     */
    public void render() {
        createNodes();
        showChat(parent.getInfo());
    }

    /**
     * Renders the current chat channel
     */
    public void renderCurrentChannel() {
        AtomicInteger row = new AtomicInteger();
        chatMessages.getChildren().clear();
        chatMessages.add(makeEmptyLine(), 1, row.getAndIncrement());
        currentChannel.getData().forEach((cm) -> {
            chatMessages.add(makeChatLine(cm), 1, row.getAndIncrement());
            chatMessages.add(makeEmptyLine(), 1, row.getAndIncrement());
        });
    }

    /**
     * Sends the message written to the TextField
     */
    public void sendMessage() {
        currentChannel.getData().add(new ChatMessage(System.currentTimeMillis(),
                Element.of(sendMessage.getText()), parent.getInfo().getUsername().getData()));

        //New message appeared, so we need to notify every observer about it
        currentChannel.setData(currentChannel.getData());
    }

    /**
     * Shows the individual chat with the given receiver
     *
     * @param user - The receiver User
     */
    public void showChat(User user) {
        String u1 = parent.getInfo().getUsername().getData();
        String u2 = user.getUsername().getData();
        chatTitle.setText(u1.equals(u2) ? "Yourself" : user.getClass().getSimpleName() + " " + u2);
        StringBuilder channel = new StringBuilder("uu:");
        switch (fixCompare(u1.compareTo(u2))) {
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

    /**
     * Shows the group chat with the given lector
     *
     * @param lector        - The lector, whose group you would like to chat with
     * @param includeLector - True if the lector should be included, false otherwise
     */
    public void showGroupChat(Lector lector, boolean includeLector) {
        String lectureName = lector.getUsername().getData();
        if (lectureName.equals(parent.getInfo().getUsername().getData()))
            chatTitle.setText("Your group");
        else
            chatTitle.setText((includeLector ? "Group of lecture " : "Students of lecture ") + lectureName);
        String channel = (includeLector ? "l:" : "nl:") + lectureName;
        DataStorage.getInstance().getChatMessages(channel, (cm) -> currentChannel.setData(cm));
    }
}
