package interfaces;

import models.User;

public interface RegisterCallback {
    void onRegisterSuccess(String successMessage);
    void onRegisterFailure(String errorMessage);
}
