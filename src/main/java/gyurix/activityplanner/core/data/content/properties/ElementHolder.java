package gyurix.activityplanner.core.data.content.properties;

import gyurix.activityplanner.core.data.element.Element;
import gyurix.activityplanner.core.observation.Observable;
import gyurix.activityplanner.core.observation.ObservableList;

public interface ElementHolder extends Colorable {
    ObservableList<Observable<Element>> getElements();
}
