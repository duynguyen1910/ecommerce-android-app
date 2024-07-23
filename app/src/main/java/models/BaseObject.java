package models;

public abstract class BaseObject{
    protected String baseID;

    /**
     * Retrieves the unique identifier of the object.
     *
     * @return the unique identifier of the object.
     */
    protected abstract String getBaseID();


    /**
     * Sets the unique identifier of the object.
     * Throws IllegalArgumentException if the baseId is null or empty.
     *
     * @param baseID the unique identifier to set.
     */
    protected abstract void setBaseID(String baseID);


    /**
     * Validates the baseId to ensure it's not null or empty.
     *
     * @param baseID the unique identifier to validate.
     * @throws IllegalArgumentException if the baseId is null or empty.
     */
    protected void validateBaseId(String baseID) {
        if (baseID == null || baseID.isEmpty()) {
            throw new IllegalArgumentException("'baseId' cannot be null or empty");
        }
    }
}
