package gyurix.activityplanner.core.data.observation;

import gyurix.activityplanner.core.Destroyable;

import java.util.HashMap;

public class ObserverContainer implements Destroyable {
    private HashMap<Observable, Observer> registeredObservers = new HashMap<>();

    public <T> void attach(Observable<T> observable, Observer<T> observer) {
        observer.update();
        registeredObservers.put(observable, observer);
        observable.attach(observer);
    }

    @Override
    public void destroy() {
        registeredObservers.forEach(Observable::detach);
        registeredObservers = null;
    }
}
