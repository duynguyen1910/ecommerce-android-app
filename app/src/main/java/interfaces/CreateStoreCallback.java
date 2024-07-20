package interfaces;

public interface CreateStoreCallback {
    void onCreateSuccess(String storeId);
    void onCreateFailure(String errorMessage);
}
