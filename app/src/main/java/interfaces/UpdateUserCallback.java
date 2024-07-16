package interfaces;

public interface UpdateUserCallback {
    void onUpdateSuccess();
    void onUpdateFailure(String errorMessage);
}
