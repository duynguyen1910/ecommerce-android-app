package interfaces;

import models.User;

public interface UserCallback {
    void onGetUserInfoSuccess(User user);
    void onGetUserInfoFailure(String errorMessage);
}
