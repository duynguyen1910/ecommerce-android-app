package interfaces;

import java.util.Map;

public interface GetStoreDataCallback {
    void onGetDataSuccess(Map<String, Object> data);
    void onGetDataFailure(String errorMessage);
}
