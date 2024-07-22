package models;

import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.STORE_ID;

import com.example.stores.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import api.productApi;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetCountCallback;
import interfaces.GetDocumentCallback;
import interfaces.UpdateDocumentCallback;

public class Product extends BaseObject implements Serializable {
    private String productName;
    private String description;
    private double newPrice;
    private double oldPrice;
    private int inStock;
    private String categoryName;
    private String categoryId;
    private String storeId;
    private int numberInCart;
    private boolean checkedStatus;
    private api.productApi productApi;
    private ArrayList<String> productImages;
    private ArrayList<String> productNameSplit;


    public Product(String productName, String description, double newPrice, double oldPrice, int inStock, String storeId, int numberInCart) {
        this.productName = productName;
        this.description = description;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.inStock = inStock;
        this.storeId = storeId;
        this.numberInCart = numberInCart;
    }

    public Product(String productName, String description, double newPrice, double oldPrice, int inStock, String categoryName, String storeId) {
        this.productName = productName;
        this.description = description;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.inStock = inStock;
        this.categoryName = categoryName;
        this.storeId = storeId;
    }

    public Product(String productName, String description,ArrayList<String> productImages, double newPrice, double oldPrice, int inStock, int numberInCart, String storeId,  boolean checkedStatus) {
        this.productName = productName;
        this.description = description;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.inStock = inStock;
        this.storeId = storeId;
        this.numberInCart = numberInCart;
        this.checkedStatus = checkedStatus;
        this.productImages = productImages;
    }



    public ArrayList<String> getProductNameSplit() {
        return productNameSplit;
    }

    public void setProductNameSplit(ArrayList<String> productNameSplit) {
        this.productNameSplit = productNameSplit;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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


    public void onCreateProduct(Map<String, Object> productData, final CreateDocumentCallback callback) {
        productApi.createProductApi(productData, callback);
    }


    public void getProductDetail(String productId, GetDocumentCallback callback) {
        productApi.getProductDetailApi(productId, callback);
    }


    public void updateProduct(Map<String, Object> updateData,String productId, UpdateDocumentCallback callback) {
        productApi.updateProductApi(updateData, productId, callback);
    }


    public void getAllProducts(final GetCollectionCallback<Product> callback) {
        productApi.getAllProductApi(callback);
    }

    public void getProductsByStoreId(String storeId, GetCollectionCallback<Product> callback) {
        productApi.getProductsByStoreIdApi(storeId, callback);
    }

    public void getProductsOutOfStockByStoreId(String storeId, GetCollectionCallback<Product> callback) {
        productApi.getProductsOutOfStockByStoreIdApi(storeId, callback);
    }

    public void getAllProductByCategoryId(String categoryId, final GetCollectionCallback<Product> callback) {
        productApi.getAllProductByCategoryIdApi(categoryId, callback);
    }

    public void getAllProductByStoreIdAndCategoryId(String storeId, String categoryId, final GetCollectionCallback<Product> callback) {
        productApi.getAllProductByStoreIdAndCategoryIdApi(storeId, categoryId, callback);
    }

    public void getAllProductByStringQueryApi(String stringQuery, final GetCollectionCallback<Product> callback) {
        productApi.getAllProductByStringQueryApi(stringQuery, callback);
    }

    public ArrayList<String> splitProductNameBySpace(String productName) {
        return productApi.splitProductNameBySpace(productName);
    }

    public void countProductsOutOfStockByStoreId(String storeId, GetCountCallback<Product> callback){
        productApi.countProductsOutOfStockByStoreIdApi(storeId, callback);
    }

    public void countProductsInStockByStoreId(String storeId, GetCountCallback<Product> callback){
        productApi.countProductsInStockByStoreIdApi(storeId, callback);
    }


    @Override
    public String getBaseId() {
        return super.baseId;
    }

    @Override
    public void setBaseId(String productId) {
        super.baseId = productId;

    }
}
