package gyurix.activityplanner.core.data;

import gyurix.activityplanner.core.storage.DataStorage;

/**
 * StorableData represents any kind of data, which should be stored using GSON serialization / deserialization
 * It overrides some default Java methods for fixing equality issues of physically different, but logically
 * equivalent objects
 */
public abstract class StorableData {
    /**
     * Every StorableData has a hashCode generated from their GSON serialized String
     *
     * @return The StorableDatas hashCode
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Two StorableData equals if they have the same class and can be represented by the same GSON serialized String
     *
     * @param obj - Checkable object
     * @return True if the given object logically equals to this StorableData
     */
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass() && obj.toString().equals(toString());
    }

    /**
     * The String value of a StorableData is it's GSON serialized represantation
     *
     * @return The GSON serialized represantation of this StorableData
     */
    @Override
    public String toString() {
        return DataStorage.getGson().toJson(this);
    }
}
