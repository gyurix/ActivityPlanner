package gyurix.activityplanner.core.observation;

import java.util.HashMap;

public class ObserverContainer implements Destroyable {
    private HashMap<Observable, Observer> registeredObservers = new HashMap<>();

    public <T> void attach(Observable<T> observable, Observer observer) {
        observer.update();
        registeredObservers.put(observable, observer);
        observable.attach(observer);
    }

    public <T> void attachLater(Observable<T> observable, Observer observer) {
        registeredObservers.put(observable, observer);
        observable.attach(observer);
    }

    @Override
    public void destroy() {
        registeredObservers.forEach(Observable::detach);
        registeredObservers = null;
    }
}
