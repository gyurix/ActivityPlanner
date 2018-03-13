package gyurix.activityplanner.core.data.observation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();
    @Getter
    private T data;

    public Observable() {
    }

    public Observable(T data) {
        this.data = data;
    }

    public void attach(Observer<T> observer) {
        observers.add(observer);
    }

    public void detach(Observer<T> observer) {
        observers.remove(observer);
    }

    public void setData(T data) {
        this.data = data;
        observers.forEach(Observer::update);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
