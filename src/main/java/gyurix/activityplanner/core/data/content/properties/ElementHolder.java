package gyurix.activityplanner.core.data.content.properties;

import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;

/**
 * Interface, implemented by contents, which has elements
 */
public interface ElementHolder extends Colorable {
    /**
     * Get the elements of the content
     *
     * @return The elements of the content
     */
    ObservableList<Observable<Element>> getElements();
}
