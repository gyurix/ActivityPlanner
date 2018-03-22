package gyurix.activityplanner.core.storage.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gyurix.activityplanner.core.WrappedData;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class UnwrapperAdapter implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (WrappedData.class.isAssignableFrom(typeToken.getRawType())) {
            Type t = findWrappedDataType(typeToken.getType());
            TypeAdapter delegate = gson.getDelegateAdapter(this, TypeToken.get(t));
            return new TypeAdapter<T>() {
                @SuppressWarnings("unchecked")
                @Override
                public T read(JsonReader reader) {
                    try {
                        WrappedData wd = (WrappedData) typeToken.getRawType().newInstance();
                        wd.setWrappedData(gson.getAdapter(TypeToken.get(t)).read(reader));
                        return (T) wd;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void write(JsonWriter writer, T t) throws IOException {
                    delegate.write(writer, ((WrappedData) t).getWrappedData());
                }
            };
        }
        return gson.getDelegateAdapter(this, typeToken);
    }

    public Type findWrappedDataType(Type main) {
        if (!(main instanceof ParameterizedType))
            return main;
        Type child = ((ParameterizedType) main).getActualTypeArguments()[0];
        while (getRawClass(main) != WrappedData.class) {
            main = ((Class) (((ParameterizedType) main).getRawType())).getGenericSuperclass();
            main = replaceParameterType(main, child);
            if (main instanceof ParameterizedType)
                child = ((ParameterizedType) main).getActualTypeArguments()[0];
        }
        return child;
    }

    public Class getRawClass(Type t) {
        return (Class) (t instanceof ParameterizedType ? ((ParameterizedType) t).getRawType() : t);
    }

    public Type replaceParameterType(Type pt, Type repl) {
        if (pt instanceof ParameterizedType) {
            return new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{replaceParameterType(((ParameterizedType) pt).getActualTypeArguments()[0], repl)};
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }

                @Override
                public Type getRawType() {
                    return ((ParameterizedType) pt).getRawType();
                }

                @Override
                public String toString() {
                    return getRawType().getTypeName() + "<" + getActualTypeArguments()[0].getTypeName() + ">";
                }
            };
        }
        return repl;
    }
}
