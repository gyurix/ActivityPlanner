package gyurix.activityplanner.core.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gyurix.activityplanner.core.data.StorableData;
import gyurix.activityplanner.core.data.content.Content;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.storage.gson.TypeSelectorAdapter;
import gyurix.activityplanner.core.storage.gson.UnwrapperAdapter;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.TreeMap;
import java.util.function.Consumer;

public class DataStorage extends StorableData {
    @Getter
    private static DataStorage instance;
    private static final Charset utf8 = Charset.forName("UTF-8");
    @Getter
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(new TypeSelectorAdapter())
            .registerTypeAdapterFactory(new UnwrapperAdapter()).create();
    private TreeMap<Integer, Content> contents = new TreeMap<>();
    private TreeMap<String, User> users = new TreeMap<>();

    public int addContent(Content content) {
        Integer lastId = contents.floorKey(10000000);
        int contentId = lastId == null ? 1 : lastId + 1;
        content.getId().setData(contentId);
        contents.put(contentId, content);
        return contentId;
    }

    public boolean addUser(User user) {
        return users.put(user.getUsername().getData(), user) == null;
    }

    public void getContent(int contentId, Consumer<Content> consumer) {
        consumer.accept(contents.get(contentId));
    }

    public void getUser(String userName, Consumer<User> consumer) {
        consumer.accept(users.get(userName));
    }

    public void removeUser(String userName, Consumer<Boolean> consumer) {
        consumer.accept(users.remove(userName) != null);
    }

    @SneakyThrows
    public static void load(File f) {
        FileReader fr = new FileReader(f);
        instance = gson.fromJson(fr, DataStorage.class);
        fr.close();
    }

    @SneakyThrows
    public void save(File f) {
        FileWriter fw = new FileWriter(f);
        fw.write(gson.toJson(this));
        fw.close();
    }
}