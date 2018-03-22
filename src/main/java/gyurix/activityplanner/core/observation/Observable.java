package gyurix.activityplanner.core.observation;

import gyurix.activityplanner.core.WrappedData;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> extends WrappedData<T> {
    protected final List<Observer> observers = new ArrayList<>();

    public Observable() {
        super(null);
    }

    public Observable(T data) {
        super(data);
    }

    void attach(Observer observer) {
        observers.add(observer);
    }

    void detach(Observer observer) {
        observers.remove(observer);
    }

    public T getData() {
        return wrappedData;
    }

    public void setData(T data) {
        wrappedData = data;
        observers.forEach(Observer::update);
    }
}
