package models;

import java.io.Serializable;

import api.categoryApi;
import interfaces.GetCollectionCallback;

public class Category extends BaseObject implements Serializable {
    private String categoryName;
    private api.categoryApi categoryApi;


    public Category(String categoryID, String categoryName) {
        super.baseId = categoryID;
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



    @Override
    public String getBaseId() {
        return super.baseId;
    }

    @Override
    public void setBaseId(String categoryId) {
        super.baseId = categoryId;

    }

    public void getCategoryCollection(GetCollectionCallback<Category> callback){
       categoryApi.getAllCategoryApi(callback);
    }


}
