package interfaces;

import android.net.Uri;

import models.User;

public interface UploadCallback {
    void uploadSuccess(Uri downloadUri);
    void uploadFailure(String errorMessage);
}
