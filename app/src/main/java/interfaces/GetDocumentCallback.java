package interfaces;

import java.util.Map;

public interface GetDocumentCallback {
    void onGetDataSuccess(Map<String, Object> data);
    void onGetDataFailure(String errorMessage);
}
