package gyurix.activityplanner.core.observation;

import gyurix.activityplanner.core.WrappedData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ObservableList<T> extends WrappedData<List<T>> {
    private final List<ListObserver<T>> observers = new ArrayList<>();

    public ObservableList() {
        super(new ArrayList<>());
    }

    public void add(T data) {
        wrappedData.add(data);
        observers.forEach((o) -> o.onAdd(data));
    }

    public void attach(ListObserver<T> observer) {
        observers.add(observer);
    }

    public void detach(ListObserver<T> observer) {
        observers.remove(observer);
    }

    public void forEach(Consumer<T> consumer) {
        wrappedData.forEach(consumer);
    }

    public void remove(T data) {
        this.wrappedData.remove(data);
        observers.forEach((o) -> o.onRemove(data));
    }

    public int size() {
        return wrappedData.size();
    }
}
