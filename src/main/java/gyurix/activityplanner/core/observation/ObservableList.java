package gyurix.activityplanner.core.observation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * ObservableList is a special Observable, which notifies Observes when adding and removing an element from a list
 *
 * @param <T> - Type of the ObservableList
 */
public class ObservableList<T> extends Observable<List<T>> {

    /**
     * Constructs a new empty ObservableList
     */
    public ObservableList() {
        super(new ArrayList<>());
    }

    /**
     * Adds the given data to the ObservableList, notifies all the observers about this operation
     *
     * @param data - The addable data
     */
    public void add(T data) {
        wrappedData.add(data);
        observers.forEach(Observer::update);
    }

    /**
     * Loops through the stored list and passes all the elements of the list to the given consumer
     *
     * @param consumer - The consumer, which wants to consume all the elements of the list
     */
    public void forEach(Consumer<T> consumer) {
        wrappedData.forEach(consumer);
    }

    /**
     * Getting the internal representation of the stored list is not allowed,
     * so this method throws and ObservationException
     *
     * @return Nothing, always throws exception
     * @throws ObservationException for doing not permitted operation
     */
    @Override
    public List<T> getData() throws ObservationException {
        throw new ObservationException(false);
    }

    /**
     * Setting the internal representation of the stored list is not allowed,
     * so this method throws and ObservationException
     *
     * @throws ObservationException for doing not permitted operation
     */
    @Override
    public void setData(List<T> data) throws ObservationException {
        throw new ObservationException(true);
    }

    /**
     * Removes the given data from the ObservableList, notifies all the observers about this operation
     *
     * @param data - The removable data
     * @return True if the data was removed successfully, false if it was not found
     */
    public boolean remove(T data) {
        if (this.wrappedData.remove(data)) {
            observers.forEach(Observer::update);
            return true;
        }
        return false;
    }

    /**
     * Gets the count of the elements stored in the list
     *
     * @return The size of this list
     */
    public int size() {
        return wrappedData.size();
    }
}
