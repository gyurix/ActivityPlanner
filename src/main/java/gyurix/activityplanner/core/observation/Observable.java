package gyurix.activityplanner.core.observation;

import gyurix.activityplanner.core.WrappedData;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable extends WrappedData with the list of observers, which are notified when the wrapped data is replaced
 *
 * @param <T> - Type of the wrapped data
 */
public class Observable<T> extends WrappedData<T> {
    /**
     * List of observers watching for WrappedData replacements
     */
    protected final List<Observer> observers = new ArrayList<>();

    /**
     * Constructs an Observable without providing any default value
     */
    public Observable() {
        super(null);
    }

    /**
     * Constructs an Observable and sets it's wrapped data to the given value
     *
     * @param data - The value of the wrapped data
     */
    public Observable(T data) {
        super(data);
    }

    /**
     * Adds an observer to the Observer list
     *
     * @param observer - The attachable observer
     */
    void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the Observer list
     *
     * @param observer - The detachable observer
     */
    void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Gets the wrapped data
     *
     * @return - The wrapped data
     */
    public T getData() {
        return wrappedData;
    }

    /**
     * Set the value of the WrappedData and notify observers about the change
     *
     * @param data - The new value of the WrappedData
     */
    public void setData(T data) {
        wrappedData = data;
        new ArrayList<>(observers).forEach(Observer::update);
    }
}
