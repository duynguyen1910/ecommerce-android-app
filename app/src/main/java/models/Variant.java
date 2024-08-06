package models;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.io.Serializable;

public class Variant extends BaseObject implements Serializable {
    private String variantName;
    private double oldPrice;
    private double newPrice;
    private int inStock;
    private String variantImageUrl;
    private String productID;

    public Variant(String variantName, double oldPrice, double newPrice, int inStock,
                   String variantImageUrl, String productID) {
        this.variantName = variantName;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.inStock = inStock;
        this.variantImageUrl = variantImageUrl;
        this.productID = productID;
    }

    public Variant() {
    }

    @Override
    protected String getBaseID() {
        return super.baseID;
    }

    @Override
    public void setBaseID(String variantID) {
        super.baseID = variantID;
    }


    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public String getVariantImageUrl() {
        return variantImageUrl;
    }

    public void setVariantImageUrl(String variantImageUrl) {
        this.variantImageUrl = variantImageUrl;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
