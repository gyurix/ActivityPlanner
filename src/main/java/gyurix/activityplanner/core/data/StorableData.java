package gyurix.activityplanner.core.data;

import gyurix.activityplanner.core.DataStorage;

public abstract class StorableData {
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass() && obj.toString().equals(toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return DataStorage.getGson().toJson(this);
    }
}
