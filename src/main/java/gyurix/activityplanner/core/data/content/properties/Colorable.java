package gyurix.activityplanner.core.data.content.properties;

import gyurix.activityplanner.core.observation.Observable;

/**
 * Interface implemented by contents having color
 */
public interface Colorable extends Identifiable {
    /**
     * Get the color of this Colorable content
     *
     * @return The color of this content in a RGB hex format without # prefix
     */
    Observable<String> getColor();
}
