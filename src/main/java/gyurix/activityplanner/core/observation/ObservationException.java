package gyurix.activityplanner.core.observation;

public class ObservationException extends RuntimeException {
    public ObservationException(boolean set) {
        super("Can not directly " + (set ? "set" : "get") + " the wrapped data of a ObservableList");
    }
}
