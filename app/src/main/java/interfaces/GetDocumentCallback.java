package interfaces;

import java.util.Map;

import models.UserAddress;

public interface GetDocumentCallback {
    void onGetDataSuccess(Map<String, Object> data);
    void onGetDataFailure(String errorMessage);
}
