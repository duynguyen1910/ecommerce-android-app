package api;

import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.STORE_ID;
import static constants.toastMessage.INTERNET_ERROR;
import static constants.toastMessage.UPDATE_SUCCESSFULLY;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import constants.toastMessage;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetCountCallback;
import interfaces.GetDocumentCallback;
import interfaces.UpdateDocumentCallback;
import models.Product;

public class productApi implements Serializable {
    private final FirebaseFirestore db;

    public productApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createProductApi(Map<String, Object> newProduct, CreateDocumentCallback callback) {
        db.collection(PRODUCT_COLLECTION)
                .add(newProduct)
                .addOnSuccessListener(documentReference -> callback.onCreateSuccess(documentReference.getId()))
                .addOnFailureListener(e -> callback.onCreateFailure(toastMessage.CREATE_PRODUCT_FAILED));

    }

    public void getProductDetailApi(String productId, GetDocumentCallback callback) {
        db.collection(PRODUCT_COLLECTION)
                .document(productId)
                .get()
                .addOnSuccessListener(task -> {
                    callback.onGetDataSuccess(task.getData());

                }).addOnFailureListener(e -> {
                    callback.onGetDataFailure(INTERNET_ERROR);
                });
    }

    public void updateProductApi(Map<String, Object> updateData, String productId, UpdateDocumentCallback callback) {

        db.collection(PRODUCT_COLLECTION).document(productId)
                .update(updateData)
                .addOnSuccessListener(unused -> callback.onUpdateSuccess(UPDATE_SUCCESSFULLY))
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating user's STORE_ID.", e));
    }

    private void getProducts(Query query, int limit, final GetCollectionCallback<Product> callback) {
        query.limit(limit)
                .get()
                .addOnSuccessListener(task -> {
                    ArrayList<Product> products = new ArrayList<>();
                    for (DocumentSnapshot document : task.getDocuments()) {
                        Product product = document.toObject(Product.class);
                        product.setBaseID(document.getId());
                        products.add(product);
                    }
                    callback.onGetListSuccess(products);
                })
                .addOnFailureListener(e -> callback.onGetListFailure(INTERNET_ERROR));
    }


    public void getProductsByStoreIdApi(String storeId, GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION).whereEqualTo(STORE_ID, storeId);
        getProducts(query, 100, callback);
    }

    public void getProductsInStockByStoreIdApi(String storeId, GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereGreaterThan(PRODUCT_INSTOCK, 0.0)
                .orderBy(PRODUCT_INSTOCK, Query.Direction.ASCENDING);
        getProducts(query, 100, callback);
    }

    public void getProductsOutOfStockByStoreIdApi(String storeId, GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereEqualTo(PRODUCT_INSTOCK, 0.0);
        getProducts(query, 100, callback);
    }

    public void getAllProductApi(final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION).whereGreaterThan(PRODUCT_INSTOCK, 0);
        getProducts(query, 100, callback);
    }

    public void getAllProductAscendingByCategoryIdApi(String categoryId, final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(CATEGORY_ID, categoryId)
                .whereGreaterThan(PRODUCT_INSTOCK, 0)
                .orderBy(PRODUCT_INSTOCK, Query.Direction.ASCENDING);
        getProducts(query, 100, callback);
    }

    public void getAllProductDescendingByCategoryIdApi(String categoryId, final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(CATEGORY_ID, categoryId)
                .whereGreaterThan(PRODUCT_INSTOCK, 0)
                .orderBy(PRODUCT_INSTOCK, Query.Direction.DESCENDING);
        getProducts(query, 100, callback);
    }

    public void getAllProductByCategoryIdApi(String categoryId, final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(CATEGORY_ID, categoryId).whereGreaterThan(PRODUCT_INSTOCK, 0);
        getProducts(query, 100, callback);
    }

    public void getAllProductByStoreIdAndCategoryIdApi(String storeId, String categoryId, final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereEqualTo(CATEGORY_ID, categoryId);
        getProducts(query, 100, callback);
    }

    private void countProducts(Query query, final GetCountCallback<Product> callback) {
        query.get()
                .addOnSuccessListener(task -> {
                    int count = task.size();
                    callback.onGetCountSuccess(count);
                })
                .addOnFailureListener(e -> callback.onGetCountFailure(INTERNET_ERROR));
    }

    public void countProductsOutOfStockByStoreIdApi(String storeId, GetCountCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereEqualTo(PRODUCT_INSTOCK, 0.0);
        countProducts(query, callback);
    }

    public void countProductsInStockByStoreIdApi(String storeId, GetCountCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereGreaterThan(PRODUCT_INSTOCK, 0.0);
        countProducts(query, callback);
    }


}