package models;

import java.io.Serializable;
import java.util.ArrayList;

public class CartItem implements Serializable {
    private String storeID;
    private String storeName;
    private String note;
    ArrayList<Product> listProducts;


    public CartItem(String storeID, String storeName, ArrayList<Product> listProducts) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.listProducts = listProducts;
    }

    public CartItem() {
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<Product> getListProducts() {
        return listProducts;
    }

    public void setListProducts(ArrayList<Product> listProducts) {
        this.listProducts = listProducts;
    }
}