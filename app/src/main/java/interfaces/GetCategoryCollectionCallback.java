package interfaces;

import java.util.ArrayList;

import models.Category;

public interface GetCategoryCollectionCallback {
    void onGetDataSuccess(ArrayList<Category> categories);
    void onGetDataFailure(String errorMessage);
}
