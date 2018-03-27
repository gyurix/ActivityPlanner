package gyurix.activityplanner.core.observation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ObservableList<T> extends Observable<List<T>> {

    public ObservableList() {
        super(new ArrayList<>());
    }

    public void add(T data) {
        wrappedData.add(data);
        observers.forEach(Observer::update);
    }

    @Override
    public List<T> getData() {
        throw new ObservationException(false);
    }

    @Override
    public void setData(List<T> data) {
        throw new ObservationException(false);
    }

    public void forEach(Consumer<T> consumer) {
        wrappedData.forEach(consumer);
    }

    public boolean remove(T data) {
        if (this.wrappedData.remove(data)) {
            observers.forEach(Observer::update);
            return true;
        }
        return false;
    }

    public int size() {
        return wrappedData.size();
    }
}
