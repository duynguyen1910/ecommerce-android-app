package interfaces;

public interface StatusCallback {
    void onSuccess(String successMessage);
    void onFailure(String errorMessage);
}
