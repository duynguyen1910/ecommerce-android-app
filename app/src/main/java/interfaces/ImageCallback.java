package interfaces;

public interface ImageCallback {
    void getImageSuccess(String downloadUri);
    void getImageFailure(String errorMessage);
}
