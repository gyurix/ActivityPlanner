package gyurix.activityplanner.core.observation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ObservableList<T> {
    private final List<T> observables = new ArrayList<>();
    private final List<ListObserver<T>> observers = new ArrayList<>();

    public void add(T data) {
        observables.add(data);
        observers.forEach((o) -> o.onAdd(data));
    }

    public void attach(ListObserver<T> observer) {
        observers.add(observer);
    }

    public void detach(ListObserver<T> observer) {
        observers.remove(observer);
    }

    public void forEach(Consumer<T> consumer) {
        observables.forEach(consumer);
    }

    public void remove(T data) {
        observables.remove(data);
        observers.forEach((o) -> o.onRemove(data));
    }

    public int size() {
        return observables.size();
    }
}
