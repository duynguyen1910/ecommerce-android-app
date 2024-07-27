package models;

import java.io.Serializable;

public class Variant extends BaseObject implements Serializable {
    private String variantName;
    private double oldPrice;
    private double newPrice;
    private int inStock;
    private String variantImage;

    public Variant(String variantName, double oldPrice, double newPrice, int inStock, String variantImage) {
        this.variantName = variantName;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.inStock = inStock;
        this.variantImage = variantImage;
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

    public String getVariantImage() {
        return variantImage;
    }

    public void setVariantImage(String variantImage) {
        this.variantImage = variantImage;
    }

    @Override
    protected String getBaseID() {
        return super.baseID;
    }

    @Override
    protected void setBaseID(String variantID) {
        super.baseID = variantID;
    }
}
