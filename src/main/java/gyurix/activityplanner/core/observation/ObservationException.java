package gyurix.activityplanner.core.observation;

/**
 * ObservationException is thrown by ObservableList, when the client code tries to directly
 * get or set the internal list of the ObservableList
 */
public class ObservationException extends RuntimeException {
    /**
     * Constructs an ObservableException
     *
     * @param set - True if the not permitted operation was a setter operation, false if it was a getter one
     */
    public ObservationException(boolean set) {
        super("Can not directly " + (set ? "set" : "get") + " the wrapped data of a ObservableList");
    }
}
