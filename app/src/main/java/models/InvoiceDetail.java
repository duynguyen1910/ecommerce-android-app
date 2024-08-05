package models;


import java.util.ArrayList;

import api.invoiceApi;
import interfaces.StatusCallback;

public class InvoiceDetail {
    private String invoiceID;
    private String variantID;
    private String productName;
    private double newPrice;
    private double oldPrice;
    private int quantity;
    private String storeID;

    private ArrayList<String> productImages;
    public static int ITEM_TO_DISPLAY = 1;

    public InvoiceDetail() {
    }

    public InvoiceDetail(String invoiceID, String variantID, int quantity) {
        this.invoiceID = invoiceID;
        this.variantID = variantID;
        this.quantity = quantity;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getVariantID() {
        return variantID;
    }

    public void setVariantID(String variantID) {
        this.variantID = variantID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(ArrayList<String> productImages) {
        this.productImages = productImages;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }
}