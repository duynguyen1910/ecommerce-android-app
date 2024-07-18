package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import api.productApi;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;

public class Product extends BaseObject implements Serializable {
    private String productName;
    private String description;
    private double newPrice;
    private double oldPrice;
    private int inStock;
    private String categoryName;
    private String storeId;
    private int numberInCart;
    private boolean checkedStatus;
    private final api.productApi productApi;
    private ArrayList<String> productImages;


    public Product() {
        productApi = new productApi();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }


    public boolean getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(ArrayList<String> productImages) {
        this.productImages = productImages;
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

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }


    public void onCreateProduct(Map<String, Object> productData, String storeId, final CreateDocumentCallback callback) {
        productApi.createProductApi(productData, storeId, callback);
    }



    public void getProductDetail(String storeId, String productId, GetDocumentCallback callback){
        productApi.getProductDetailApi(storeId, productId, callback);
    }

    public void getProductsCollection(String storeId, final GetCollectionCallback<Product> callback){
        productApi.getProductsCollectionApi(storeId, callback);
    }

    @Override
    protected String getBaseId() {
        return super.baseId;
    }

    @Override
    protected void setBaseId(String productId) {
        super.baseId = productId;

    }
}
