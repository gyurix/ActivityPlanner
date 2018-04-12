package gyurix.activityplanner.core.data.content.properties;

import gyurix.activityplanner.core.observation.Observable;

/**
 * Interface for objects having numeric ID
 */
public interface Identifiable {
    /**
     * Get the numeric ID of the Object
     *
     * @return The numeric ID of the Object
     */
    Observable<Integer> getId();
}
