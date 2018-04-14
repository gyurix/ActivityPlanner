package gyurix.activityplanner.core.observation;

import java.util.HashMap;

/**
 * ObserverContainer is a helper class for tracking the attached observable-observer pairs
 */
public class ObserverContainer implements Destroyable {
    /**
     * Map of attached observable-observer pairs
     */
    private HashMap<Observable, Observer> registeredObservers = new HashMap<>();

    /**
     * Updates then attaches the given Observer to the given Observable
     *
     * @param observable - The Observable
     * @param observer   - The attachable Observer
     * @param <T>        - The type of Observable
     */
    public <T> void attach(Observable<T> observable, Observer observer) {
        observer.update();
        registeredObservers.put(observable, observer);
        observable.attach(observer);
    }

    /**
     * Attaches the given Observer to the given Observable
     *
     * @param observable - The Observable
     * @param observer   - The attachable Observer
     * @param <T>        - The type of Observable
     */
    public <T> void attachLater(Observable<T> observable, Observer observer) {
        registeredObservers.put(observable, observer);
        observable.attach(observer);
    }

    /**
     * This method should be invoked when the ObserverContainer is no longer needed.
     * Used for detaching all the registered observers.
     */
    @Override
    public void destroy() {
        registeredObservers.forEach(Observable::detach);
        registeredObservers = null;
    }
}
