package models;

import java.io.Serializable;

import api.categoryApi;
import interfaces.GetCollectionCallback;

public class Category extends BaseObject implements Serializable {
    private String categoryName;
    private categoryApi categoryApi;

    @Override
    public String getBaseID() {
        return super.baseID;
    }

    @Override
    public void setBaseID(String categoryID) {
        super.validateBaseID(categoryID);
        super.baseID = categoryID;
    }

    public Category(String categoryID, String categoryName) {
        super.baseID = categoryID;
        this.categoryName = categoryName;
    }
    public Category() {
        categoryApi = new categoryApi();
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public void getCategoryCollection(GetCollectionCallback<Category> callback){
       categoryApi.getAllCategoryApi(callback);
    }
}
