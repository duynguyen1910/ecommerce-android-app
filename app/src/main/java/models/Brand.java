package models;

import java.io.Serializable;

public class Brand extends BaseObject implements Serializable {
    private String brandName;
    private String picUrl;

    public Brand() {
    }

    public Brand(String brandName, String picUrl) {
        this.brandName = brandName;
        this.picUrl = picUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    protected String getBaseId() {
        return super.baseId;
    }

    @Override
    protected void setBaseId(String brandId) {
        super.baseId = brandId;
    }
}
