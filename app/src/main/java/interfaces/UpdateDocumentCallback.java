package interfaces;

public interface UpdateDocumentCallback {
    void onUpdateSuccess(String successMessage);
    void onUpdateFailure(String errorMessage);
}
