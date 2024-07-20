package models;

import java.io.Serializable;

import api.categoryApi;
import interfaces.GetCategoryCollectionCallback;

public class Category extends BaseObject implements Serializable {
    private String categoryName;
    private api.categoryApi categoryApi;

    public Category() {
        categoryApi = new categoryApi();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    protected String getBaseID() {
        return super.baseID;
    }

    @Override
    protected void setBaseID(String categoryID) {
        super.baseID = categoryID;

    }

    public void getCategoryCollection(GetCategoryCollectionCallback callback){
       categoryApi.getCategoryCollectionApi(callback);
    }


}
