package interfaces;

import java.util.ArrayList;

public interface GetCountCallback<T> {
    void onGetCountSuccess(int count);
    void onGetCountFailure(String errorMessage);
}
