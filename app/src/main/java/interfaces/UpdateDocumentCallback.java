package interfaces;

public interface UpdateDocumentCallback {
    void onUpdateSuccess();
    void onUpdateFailure(String errorMessage);
}
