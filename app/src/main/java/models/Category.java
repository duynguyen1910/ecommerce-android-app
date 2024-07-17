package models;

import java.io.Serializable;

public class Category extends BaseObject implements Serializable {
    private String categoryName;

    public Category() {
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
}
