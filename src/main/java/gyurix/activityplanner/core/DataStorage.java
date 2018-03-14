package gyurix.activityplanner.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gyurix.activityplanner.core.data.content.Content;
import gyurix.activityplanner.core.data.user.User;
import gyurix.activityplanner.core.observation.Observable;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.util.TreeMap;

public class DataStorage {
    private static TreeMap<Integer, Content> contents = new TreeMap<>();
    @Getter
    private static Gson gson = new GsonBuilder().registerTypeAdapterFactory(new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getType() instanceof ParameterizedType) {
                ParameterizedType type = (ParameterizedType) typeToken.getType();
                if (type.getRawType() == Observable.class) {
                    return new TypeAdapter() {
                        @Override
                        public Object read(JsonReader jsonReader) {
                            Observable out = new Observable();
                            out.setData(gson.fromJson(jsonReader, type.getActualTypeArguments()[0]));
                            return out;
                        }

                        @Override
                        public void write(JsonWriter jsonWriter, Object t) {
                            gson.toJson(((Observable) t).getData(), type.getActualTypeArguments()[0], jsonWriter);
                        }
                    };
                }
            }
            return gson.getDelegateAdapter(this, typeToken);
        }
    }).create();
    private static TreeMap<String, User> users = new TreeMap<>();

    public static int addContent(Content content) {
        Integer lastId = contents.floorKey(10000000);
        int contentId = lastId == null ? 1 : lastId + 1;
        content.getId().setData(contentId);
        contents.put(contentId, content);
        return contentId;
    }

    public static boolean addUser(User user) {
        return users.put(user.getUsername().getData(), user) == null;
    }

    public static Content getContent(int contentId) {
        return contents.get(contentId);
    }

    public static User getUser(String userName) {
        return users.get(userName);
    }

    public static boolean removeUser(String userName) {
        return users.remove(userName) != null;
    }
}
