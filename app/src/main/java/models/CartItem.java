package models;

import java.io.Serializable;
import java.util.ArrayList;

public class CartItem implements Serializable {
    private String storeName;
    private String note;
    ArrayList<Product> listProducts;


    public CartItem(String storeName, ArrayList<Product> listProducts) {
        this.storeName = storeName;
        this.listProducts = listProducts;
    }

    public CartItem() {
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