package interfaces;

public interface CreateCallback {
    void onCreateSuccess(String documentID);
    void onCreateFailure(String errorMessage);
}
