package gyurix.activityplanner.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * WrappedData wraps an Object, it's used by Observable
 *
 * @param <T> - Type of the data, which should be wrapped
 */
public abstract class WrappedData<T> {
    /**
     * The wrapped data
     */
    @Getter
    @Setter
    protected T wrappedData;

    /**
     * Empty constructor, for simplifying deserialization
     */
    public WrappedData() {

    }

    /**
     * Constructor for wrapping the given data
     *
     * @param data - Wrappable data
     */
    public WrappedData(T data) {
        wrappedData = data;
    }

    /**
     * The wrapper equals with it's wrapped data, and vice versa.
     * It also equals with a wrapper wrapping a logically equal data
     *
     * @param obj - Equality checkable object
     * @return True if the object logically equals with this WrappedData, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return Objects.equals(obj, wrappedData) ||
                obj instanceof WrappedData && Objects.equals(wrappedData, ((WrappedData) obj).getWrappedData());
    }

    /**
     * Returns the String representation of the wrapped data
     *
     * @return The String representation of the wrapped data
     */
    @Override
    public String toString() {
        return String.valueOf(wrappedData);
    }
}
