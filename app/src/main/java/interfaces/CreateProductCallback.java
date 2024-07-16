package interfaces;

public interface CreateProductCallback {
    void onCreateSuccess(String successMessage);
    void onCreateFailure(String errorMessage);
}
