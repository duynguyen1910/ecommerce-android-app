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
    protected String getBaseId() {
        return super.baseId;
    }

    @Override
    protected void setBaseId(String categoryId) {
        super.baseId = categoryId;

    }

    public void getCategoryCollection(GetCategoryCollectionCallback callback){
       categoryApi.getCategoryCollectionApi(callback);
    }


}
