package gyurix.activityplanner.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public abstract class WrappedData<T> {
    @Getter
    @Setter
    protected T wrappedData;

    public WrappedData() {

    }

    public WrappedData(T data) {
        wrappedData = data;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(obj, wrappedData) ||
                obj instanceof WrappedData && Objects.equals(wrappedData, ((WrappedData) obj).getWrappedData());
    }

    @Override
    public String toString() {
        return wrappedData.toString();
    }
}
