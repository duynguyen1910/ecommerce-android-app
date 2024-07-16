package interfaces;

import java.util.Map;

public interface GetProductDataCallback {
    void onGetDataSuccess(Map<String, Object> data);
    void onGetDataFailure(String errorMessage);
}
