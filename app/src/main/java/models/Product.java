package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import api.productApi;
import api.storeApi;
import interfaces.CreateProductCallback;
import interfaces.CreateStoreCallback;
import interfaces.GetProductDataCallback;
import interfaces.GetStoreDataCallback;

public class Product implements Serializable {
    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private double oldPrice;
    private int review;
    private double rating;
    private int numberInCart;
    private int sold;
    private int saleoff;
    private int storeID;
    private boolean checkedStatus;
    private int inStock;
    private int likes;
    private int views;
    private api.productApi productApi;

    private boolean hiddenStatus;

    public Product() {
        productApi = new productApi();
    }


    public Product(String title, String description, ArrayList<String> picUrl, double price, double oldPrice, double rating, int sold, int saleoff, int storeID) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.oldPrice = oldPrice;
        this.rating = rating;
        this.sold = sold;
        this.saleoff = saleoff;
        this.storeID = storeID;
    }

    public Product(String title, String description, ArrayList<String> picUrl, double price, double oldPrice, double rating, int sold, int saleoff, int numberInCart, int storeID, boolean checkedStatus) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.oldPrice = oldPrice;
        this.rating = rating;
        this.sold = sold;
        this.saleoff = saleoff;
        this.numberInCart = numberInCart;
        this.storeID = storeID;
        this.checkedStatus = checkedStatus;
    }



    // constructor for MyProduct Item
    public Product(String title, String description, ArrayList<String> picUrl, double oldPrice, int sold, int inStock, int likes, int views, boolean hiddenStatus, int storeID) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.oldPrice = oldPrice;
        this.sold = sold;
        this.inStock = inStock;
        this.likes = likes;
        this.views = views;
        this.hiddenStatus = hiddenStatus;
        this.storeID = storeID;
    }

    public boolean isHiddenStatus() {
        return hiddenStatus;
    }

    public void setHiddenStatus(boolean hiddenStatus) {
        this.hiddenStatus = hiddenStatus;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public int getSaleoff() {
        return saleoff;
    }

    public void setSaleoff(int saleoff) {
        this.saleoff = saleoff;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }


    public void onCreateProduct(Map<String, Object> productData, String storeId, final CreateProductCallback callback) {
        productApi.createProductApi(productData, storeId, callback);
    }

    public void getProductDetailData(String storeId, String productId, GetProductDataCallback callback){
        productApi.getProductDetailDataApi(storeId, productId, callback);
    }
}
