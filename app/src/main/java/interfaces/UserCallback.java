package interfaces;

import models.User;

public interface UserCallback {
    void getUserInfoSuccess(User user);
    void getUserInfoFailure(String errorMessage);
}
