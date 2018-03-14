package gyurix.activityplanner.core;

import lombok.Getter;
import lombok.Setter;

public abstract class WrappedData<T> {
    @Getter
    @Setter
    protected T wrappedData;

    public WrappedData() {

    }

    public WrappedData(T data) {
        wrappedData = data;
    }
}
