package api;

import static constants.collectionName.INVOICE_DETAIL_COLLECTION;
import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.INVOICE_ID;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_SOLD;
import static constants.keyName.STORE_ID;
import static constants.toastMessage.INTERNET_ERROR;
import static constants.toastMessage.UPDATE_SUCCESSFULLY;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constants.toastMessage;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetCountCallback;
import interfaces.GetDocumentCallback;
import interfaces.StatusCallback;
import interfaces.UpdateDocumentCallback;
import models.InvoiceDetail;
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
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating product", e));
    }

    public void updateProductWhenConfirmInvoice(String invoiceID, final StatusCallback callback) {
        db.collection(INVOICE_DETAIL_COLLECTION)
                .whereEqualTo(INVOICE_ID, invoiceID)
                .get()
                .addOnSuccessListener(task -> {
                    Map<String, Integer> productMap = new HashMap<>();
                    for (DocumentSnapshot document : task.getDocuments()) {
                        InvoiceDetail invoiceDetail = document.toObject(InvoiceDetail.class);

                        productMap.put(invoiceDetail.getVariantID(), invoiceDetail.getQuantity());
                    }

                    List<Task<Void>> updateTasks = new ArrayList<>();

                    for (Map.Entry<String, Integer> entry : productMap.entrySet()) {
                        String productID = entry.getKey();
                        int quantity = entry.getValue();

                        // Tạo tác vụ đọc sản phẩm
                        Task<DocumentSnapshot> getProductTask = db.collection(PRODUCT_COLLECTION).document(productID).get();

                        // Tạo tác vụ cập nhật sản phẩm sau khi đọc xong
                        Task<Void> updateTask = getProductTask.continueWithTask(task1 -> {
                            if (task1.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task1.getResult();
                                Product product = documentSnapshot.toObject(Product.class);
                                int currentInStock = product.getInStock();

                                Map<String, Object> updateData = new HashMap<>();
                                updateData.put(PRODUCT_INSTOCK, currentInStock - quantity);
                                updateData.put(PRODUCT_SOLD, product.getSold() + quantity);

                                // Trả về tác vụ cập nhật sản phẩm
                                return db.collection(PRODUCT_COLLECTION)
                                        .document(productID)
                                        .update(updateData);
                            } else {
                                // Nếu có lỗi, trả về tác vụ lỗi
                                return Tasks.forException(task1.getException());
                            }
                        });

                        updateTasks.add(updateTask);
                    }

                    // Chờ tất cả các tác vụ cập nhật hoàn tất
                    Tasks.whenAll(updateTasks)
                            .addOnSuccessListener(aVoid -> {
                                callback.onSuccess("Đã xác nhận đơn hàng");
                            })
                            .addOnFailureListener(e -> {
                                callback.onFailure(INTERNET_ERROR);
                            });

                })
                .addOnFailureListener(e -> callback.onFailure(INTERNET_ERROR));
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