package interfaces;

import java.util.ArrayList;

import models.Category;

public interface GetCollectionCallback<T> {
    void onGetDataSuccess(ArrayList<T> items);
    void onGetDataFailure(String errorMessage);
}
