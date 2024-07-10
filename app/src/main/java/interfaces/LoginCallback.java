package interfaces;

import models.User;

public interface LoginCallback {
    void onLoginSuccess(User user);
    void onLoginFailure(String errorMessage);
}
