package gyurix.activityplanner.core.observation;

/**
 * Observers are classes used for detecting changes of the Observables
 */
public interface Observer {
    /**
     * Called when the Observables observed data changes
     */
    void update();
}
