package gyurix.activityplanner.core.observation;

/**
 * Interface, implemented by objects, which should self handle their destruction
 */
public interface Destroyable {
    /**
     * Invoked, when the object is no longer needed
     */
    void destroy();
}
