package api;

import static android.content.ContentValues.TAG;
import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.collectionName.STORE_COLLECTION;
import static constants.collectionName.USER_COLLECTION;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.PASSWORD;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.PRODUCTS;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NAME;

import static constants.keyName.PRODUCT_NAME_SPLIT;
import static constants.keyName.STORE_ID;
import static constants.toastMessage.INTERNET_ERROR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
                .addOnSuccessListener(unused -> callback.onUpdateSuccess())
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating user's STORE_ID.", e));
    }

    private void getProducts(Query query, int limit, final GetCollectionCallback<Product> callback) {
        query.limit(limit)
                .get()
                .addOnSuccessListener(task -> {
                    ArrayList<Product> products = new ArrayList<>();
                    for (DocumentSnapshot document : task.getDocuments()) {
                        Product product = document.toObject(Product.class);
                        if (product != null) {
                            product.setBaseId(document.getId());
                            products.add(product);
                        }
                    }
                    callback.onGetDataSuccess(products);
                })
                .addOnFailureListener(e -> callback.onGetDataFailure(INTERNET_ERROR));
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
        Query query = db.collection(PRODUCT_COLLECTION);
        getProducts(query, 100, callback);
    }

    public void getAllProductAscendingByCategoryIdApi(String categoryId, final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(CATEGORY_ID, categoryId)
                .orderBy(PRODUCT_INSTOCK, Query.Direction.ASCENDING);
        getProducts(query, 100, callback);
    }

    public void getAllProductDescendingByCategoryIdApi(String categoryId, final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(CATEGORY_ID, categoryId)
                .orderBy(PRODUCT_INSTOCK, Query.Direction.DESCENDING);
        getProducts(query, 100, callback);
    }

    public void getAllProductByCategoryIdApi(String categoryId, final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(CATEGORY_ID, categoryId);
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
