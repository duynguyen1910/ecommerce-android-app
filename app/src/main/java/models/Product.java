package models;

import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.STORE_ID;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import api.productApi;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import interfaces.UpdateDocumentCallback;

public class Product extends BaseObject implements Serializable {
    private String productName;
    private String description;
    private double newPrice;
    private double oldPrice;
    private int inStock;
    private String categoryName;
    private String categoryID;
    private String storeID;
    private int numberInCart;
    private boolean checkedStatus;
    private api.productApi productApi;
    private ArrayList<String> productImages;

    public Product(String productName, String description, double newPrice, double oldPrice, int inStock, String storeID, int numberInCart) {
        this.productName = productName;
        this.description = description;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.inStock = inStock;
        this.storeID = storeID;
        this.numberInCart = numberInCart;
    }

    public Product(String productName, String description, double newPrice, double oldPrice, int inStock, String categoryName, String storeID) {
        this.productName = productName;
        this.description = description;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.inStock = inStock;
        this.categoryName = categoryName;
        this.storeID = storeID;
    }


    @Override
    public String getBaseID() {
        return super.baseID;
    }

    @Override
    public void setBaseID(String productID) {
        super.validateBaseID(productID);
        super.baseID = productID;

    }

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

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
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

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
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


    public void onCreateProduct(Map<String, Object> productData, final CreateDocumentCallback callback) {
        productApi.createProductApi(productData, callback);
    }



    public void getProductDetail(String productId, GetDocumentCallback callback){
        productApi.getProductDetailApi(productId, callback);
    }


    public void updateProduct(Map<String, Object> updateData, String storeId, String productId, UpdateDocumentCallback callback) {
       productApi.updateProductApi(updateData, storeId, productId, callback);
    }


    public void getAllProducts(final GetCollectionCallback<Product> callback){
        productApi.getAllProductApi(callback);
    }

    public void getProductsByStoreId(String storeId, GetCollectionCallback<Product> callback) {
        productApi.getProductsByStoreIdApi(storeId, callback);
    }

    public void getProductsOutOfStockByStoreId(String storeId, GetCollectionCallback<Product> callback) {
        productApi.getProductsOutOfStockByStoreIdApi(storeId, callback);
    }

}
