package gyurix.activityplanner.core.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.content.Content;
import gyurix.activityplanner.core.data.element.ChatMessage;
import gyurix.activityplanner.core.data.user.Lecture;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.ObservableList;
import gyurix.activityplanner.core.storage.gson.TypeSelectorAdapter;
import gyurix.activityplanner.core.storage.gson.UnwrapperAdapter;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * DataStorage is a singleton class, used for managing the data loading and saving
 */
public class DataStorage extends StorableData {
    /**
     * The configured GSON toolkit for data serialization and deserialization
     */
    @Getter
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(new TypeSelectorAdapter())
            .registerTypeAdapterFactory(new UnwrapperAdapter()).create();
    /**
     * The instance of the singleton DataStorage object
     */
    @Getter
    private static DataStorage instance;
    /**
     * Map of loaded chat messages
     */
    private TreeMap<String, ObservableList<ChatMessage>> chatMessages = new TreeMap<>();
    /**
     * Map of loaded contents
     */
    private TreeMap<Integer, Content> contents = new TreeMap<>();
    /**
     * Map of loaded users
     */
    private TreeMap<String, User> users = new TreeMap<>();

    /**
     * Constructs the singleton DataStorage
     */
    public DataStorage() {
        instance = this;
    }

    /**
     * Loads everything from a file
     *
     * @param f - The file used for loading data
     */
    @SneakyThrows
    public static void load(File f) {
        FileReader fr = new FileReader(f);
        instance = gson.fromJson(fr, DataStorage.class);
        fr.close();
    }

    /**
     * Stores and gives identification number to the given content
     *
     * @param content - The stores content
     * @return - The id of the stored content
     */
    public int addContent(Content content) {
        Integer lastId = contents.floorKey(10000000);
        int contentId = lastId == null ? 1 : lastId + 1;
        content.getId().setData(contentId);
        contents.put(contentId, content);
        return contentId;
    }

    /**
     * Adds the given new user to the user storage
     *
     * @param user - The addable user
     * @return True if the user was added successfully
     */
    public boolean addUser(User user) {
        return users.put(user.getUsername().getData(), user) == null;
    }

    /**
     * Gets all the chat messages of a chat channel
     *
     * @param channel  - The channel
     * @param consumer - The consumer, which consumes the chat messages
     */
    public void getChatMessages(String channel, Consumer<ObservableList<ChatMessage>> consumer) {
        ObservableList<ChatMessage> cm = chatMessages.get(channel);
        if (cm == null) {
            cm = new ObservableList<>();
            chatMessages.put(channel, cm);
        }
        consumer.accept(cm);
    }

    /**
     * Gets the content having the given numeric id
     *
     * @param contentId - The requested contents numeric id
     * @param consumer  - The consumer, which consumes the content
     * @throws NoSuchElementException - When the requested content was not found
     */
    public void getContent(int contentId, Consumer<Content> consumer) throws NoSuchElementException {
        Content con = contents.get(contentId);
        if (con == null)
            throw new NoSuchElementException("Content " + contentId + " was not found.");
        consumer.accept(contents.get(contentId));
    }

    /**
     * Gets every lecture
     *
     * @param consumer - Consumer, which consumes the list of every lecture
     */
    public void getLectures(Consumer<List<Lecture>> consumer) {
        List list = users.values().stream().filter(Lecture.class::isInstance).collect(Collectors.toList());
        consumer.accept(list);
    }

    /**
     * Gets the requested user
     *
     * @param userName - Requested users username
     * @param consumer - Consumer, which consumes the requested user
     */
    public void getUser(String userName, Consumer<User> consumer) {
        consumer.accept(users.get(userName));
    }

    /**
     * Removes the given content of the given user
     *
     * @param user      - User who has edit permissions for the requested content
     * @param contentId - The removable content
     */
    public void removeContent(User user, int contentId) {
        if (user.removeContent(contentId)) {
            Content c = contents.remove(contentId);
            c.getId().setData(0);
        }
    }

    /**
     * Removes the given user
     *
     * @param userName - The removable users username
     * @param consumer - Consumer, which consumes the result of the removal process
     */
    public void removeUser(String userName, Consumer<Boolean> consumer) {
        consumer.accept(users.remove(userName) != null);
    }

    /**
     * Saves every data to the given File
     *
     * @param f - The File to which the data should be written
     */
    @SneakyThrows
    public void save(File f) {
        FileWriter fw = new FileWriter(f);
        fw.write(gson.toJson(this));
        fw.close();
    }
}
