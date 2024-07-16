package models;

public abstract class BaseObject {
    protected String baseId;

    /**
     * Retrieves the unique identifier of the object.
     *
     * @return the unique identifier of the object.
     */
    protected abstract String getBaseId();


    /**
     * Sets the unique identifier of the object.
     * Throws IllegalArgumentException if the baseId is null or empty.
     *
     * @param baseId the unique identifier to set.
     */
    protected abstract void setBaseId(String baseId);


    /**
     * Validates the baseId to ensure it's not null or empty.
     *
     * @param baseId the unique identifier to validate.
     * @throws IllegalArgumentException if the baseId is null or empty.
     */
    protected void validateBaseId(String baseId) {
        if (baseId == null || baseId.isEmpty()) {
            throw new IllegalArgumentException("'baseId' cannot be null or empty");
        }
    }
}
