package gyurix.activityplanner.core;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gyurix.activityplanner.core.data.content.Content;
import gyurix.activityplanner.core.data.user.User;
import lombok.Getter;

import java.util.TreeMap;

public class DataStorage {
    private static TreeMap<Integer, Content> contents = new TreeMap<>();
    @Getter
    private static Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getName().equals("observers");
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
