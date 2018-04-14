package gyurix.activityplanner.core;

/**
 * Interface used for future tasks having return value
 *
 * @param <T> - The type of return value
 */
public interface Callable<T> {
    /**
     * Calls the future task
     *
     * @return The return value of the future task
     */
    T call();
}
