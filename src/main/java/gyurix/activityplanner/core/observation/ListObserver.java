package gyurix.activityplanner.core.observation;


public interface ListObserver<T> {
    void onAdd(T observable);

    void onRemove(T observable);
}
