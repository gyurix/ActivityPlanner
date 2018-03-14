package gyurix.activityplanner.core.observation;

import gyurix.activityplanner.core.WrappedData;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> extends WrappedData<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    public Observable() {
        super(null);
    }

    public Observable(T data) {
        super(data);
    }

    void attach(Observer<T> observer) {
        observers.add(observer);
    }

    void detach(Observer<T> observer) {
        observers.remove(observer);
    }

    public T getData() {
        return wrappedData;
    }

    public void setData(T data) {
        wrappedData = data;
        observers.forEach(Observer::update);
    }

    @Override
    public String toString() {
        return wrappedData.toString();
    }
}
