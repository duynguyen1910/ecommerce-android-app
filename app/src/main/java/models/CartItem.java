package models;

import java.io.Serializable;
import java.util.ArrayList;

public class CartItem implements Serializable {
    private String storeID;
    private String storeName;
    private String note;
    ArrayList<Variant> listVariants;


    public CartItem(String storeID, String storeName, ArrayList<Variant> listVariants) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.listVariants = listVariants;
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

    public ArrayList<Variant> getListVariants() {
        return listVariants;
    }

    public void setListVariants(ArrayList<Variant> listVariants) {
        this.listVariants = listVariants;
    }
}