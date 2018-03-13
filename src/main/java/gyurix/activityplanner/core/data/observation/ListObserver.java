package gyurix.activityplanner.core.data.observation;


public interface ListObserver<T> {
    void onAdd(T observable);

    void onRemove(T observable);
}
