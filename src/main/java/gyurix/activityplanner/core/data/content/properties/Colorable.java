package gyurix.activityplanner.core.data.content.properties;

import gyurix.activityplanner.core.observation.Observable;

public interface Colorable extends Identifiable {
    Observable<String> getColor();
}
