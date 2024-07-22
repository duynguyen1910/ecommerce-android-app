package interfaces;

public interface CreateDocumentCallback {
    void onCreateSuccess(String documentId);
    void onCreateFailure(String errorMessage);
}
