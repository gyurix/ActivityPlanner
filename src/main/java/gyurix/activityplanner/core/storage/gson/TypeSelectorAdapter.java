package gyurix.activityplanner.core.storage.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Modifier;

public class TypeSelectorAdapter implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        TypeAdapterFactory taf = this;
        if (!typeToken.getRawType().getName().startsWith("java.") &&
                (typeToken.getRawType().getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT) {
            TypeAdapter delegate = gson.getDelegateAdapter(this, typeToken);
            return new TypeAdapter<T>() {
                @Override
                public T read(JsonReader jsonReader) {
                    JsonObject jo = gson.fromJson(jsonReader, JsonObject.class);
                    String type = jo.getAsJsonPrimitive("type").getAsString();
                    String cn = typeToken.getRawType().getPackage().getName() + "." + type;
                    Class cl = null;
                    try {
                        cl = Class.forName(cn);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        return gson.fromJson(jo, (Class<T>) cl);
                    } catch (Throwable e) {
                        return null;
                    }
                }

                @Override
                public void write(JsonWriter jsonWriter, T t) throws IOException {
                    delegate.write(jsonWriter, t);
                }
            };
        }
        return gson.getDelegateAdapter(this, typeToken);
    }
}
