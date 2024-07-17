package interfaces;

import android.net.Uri;

public interface UpdateCallback {
    void updateSuccess(String successMessage);
    void updateFailure(String errorMessage);
}
