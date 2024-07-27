package interfaces;

import java.util.ArrayList;

public interface GetCollectionCallback<T> {
    void onGetListSuccess(ArrayList<T> list);
    void onGetListFailure(String errorMessage);
}
