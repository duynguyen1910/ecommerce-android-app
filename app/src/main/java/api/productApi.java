package api;

import static constants.collectionName.INVOICE_COLLECTION;
import static constants.collectionName.INVOICE_DETAIL_COLLECTION;
import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.collectionName.VARIANT_COLLECTION;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.INSTOCK;
import static constants.keyName.PRODUCT_ID;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_SOLD;
import static constants.keyName.STATUS;
import static constants.keyName.STORE_ID;
import static constants.toastMessage.INTERNET_ERROR;
import static constants.toastMessage.UPDATE_SUCCESSFULLY;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import constants.toastMessage;
import enums.OrderStatus;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetAggregate.GetAggregateCallback;
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

    public void updateProductWhenCancelInvoice(ArrayList<InvoiceDetail> invoiceDetails, StatusCallback callback) {
        WriteBatch batch = db.batch();  // Khởi tạo WriteBatch

        // Tạo một map để lưu tổng số lượng tồn kho cần cập nhật cho mỗi sản phẩm có variant
        Map<String, Integer> productSoldMap = new HashMap<>();

        for (InvoiceDetail detail : invoiceDetails) {
            // Khi hủy đơn thì cần tăng tồn kho của sản phẩm lên, giảm số lượng đã bán


            if (detail.getVariantID() != null) {
                // Cập nhật tồn kho cho variant
                DocumentReference variantRef = db.collection(VARIANT_COLLECTION).document(detail.getVariantID());
                batch.update(variantRef, INSTOCK, FieldValue.increment(detail.getQuantity()));

                // Cộng tổng số lượng cho sản phẩm liên quan
                String productID = detail.getProductID();
                productSoldMap.put(productID, productSoldMap.getOrDefault(productID, 0) + detail.getQuantity());
            } else {
                // Cập nhật tồn kho cho product không có variant
                DocumentReference productRef = db.collection(PRODUCT_COLLECTION).document(detail.getProductID());
                batch.update(productRef, INSTOCK, FieldValue.increment(detail.getQuantity()));
                batch.update(productRef, PRODUCT_SOLD, FieldValue.increment(-detail.getQuantity()));
            }
        }

        // Cập nhật số lượng tồn kho cho mỗi sản phẩm có variant
        for (Map.Entry<String, Integer> entry : productSoldMap.entrySet()) {
            String productID = entry.getKey();
            int totalSold = entry.getValue();

            DocumentReference productRef = db.collection(PRODUCT_COLLECTION).document(productID);
            batch.update(productRef, INSTOCK, FieldValue.increment(totalSold));
            batch.update(productRef, PRODUCT_SOLD, FieldValue.increment(-totalSold));

        }

        // Commit batch update
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess("Cập nhật tồn kho thành công");
                } else {
                    callback.onFailure("Failed to update inventory: " + task.getException().getMessage());
                }
            }
        });
    }

    public void updateProductWhenBuying(ArrayList<InvoiceDetail> invoiceDetails, StatusCallback callback) {
        WriteBatch batch = db.batch();  // Khởi tạo WriteBatch

        // Tạo một map để lưu tổng số lượng tồn kho cần cập nhật cho mỗi sản phẩm có variant
        Map<String, Integer> productSoldMap = new HashMap<>();

        for (InvoiceDetail detail : invoiceDetails) {
            // Khi khách hàng đặt mua sản phẩm thì giảm tồn kho xuống, tăng số lượng bán lên


            if (detail.getVariantID() != null) {
                // Cập nhật tồn kho cho variant
                DocumentReference variantRef = db.collection(VARIANT_COLLECTION).document(detail.getVariantID());
                batch.update(variantRef, INSTOCK, FieldValue.increment(-detail.getQuantity()));

                // Cộng tổng số lượng cho sản phẩm liên quan
                String productID = detail.getProductID();
                productSoldMap.put(productID, productSoldMap.getOrDefault(productID, 0) + detail.getQuantity());
            } else {
                // Cập nhật tồn kho, sold cho product không có variant
                DocumentReference productRef = db.collection(PRODUCT_COLLECTION).document(detail.getProductID());
                batch.update(productRef, INSTOCK, FieldValue.increment(-detail.getQuantity()));
                batch.update(productRef, PRODUCT_SOLD, FieldValue.increment(detail.getQuantity()));

            }
        }

        // Cập nhật số lượng tồn kho, số lượng đã bán cho mỗi sản phẩm có variant
        for (Map.Entry<String, Integer> entry : productSoldMap.entrySet()) {
            String productID = entry.getKey();
            int totalSold = entry.getValue();

            DocumentReference productRef = db.collection(PRODUCT_COLLECTION).document(productID);
            batch.update(productRef, INSTOCK, FieldValue.increment(-totalSold));
            batch.update(productRef, PRODUCT_SOLD, FieldValue.increment(totalSold));

        }

        // Commit batch update
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess("Cập nhật sản phẩm thành công");
                } else {
                    callback.onFailure("Failed to update inventory: " + task.getException().getMessage());
                }
            }
        });
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
                .orderBy(PRODUCT_NEW_PRICE, Query.Direction.ASCENDING);
        getProducts(query, 100, callback);
    }

    public void getAllProductDescendingByCategoryIdApi(String categoryId, final GetCollectionCallback<Product> callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(CATEGORY_ID, categoryId)
                .whereGreaterThan(PRODUCT_INSTOCK, 0)
                .orderBy(PRODUCT_NEW_PRICE, Query.Direction.DESCENDING);
        getProducts(query, 100, callback);
    }

    public void getTopBestSellerByStoreID(String storeID, int limit, final GetCollectionCallback<Product> callback) {
        db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeID)
                .orderBy(PRODUCT_SOLD, Query.Direction.DESCENDING)
                .limit(limit)
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


    public Task<Void> getProductRevenueTask(Product product) {
        final double[] productRevenue = {0};
        CollectionReference invoiceDetailRef = db.collection(INVOICE_DETAIL_COLLECTION);
        HashSet<String> checkedInvoiceIDSet = new HashSet<>();
        // Set này chứa các invoiceID đã được kiểm tra và là invoiceID của 1 hóa đơn không bị hủy

        long startTime = System.currentTimeMillis();

        return invoiceDetailRef.whereEqualTo(PRODUCT_ID, product.getBaseID())
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        List<Task<DocumentSnapshot>> documentTasks = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            InvoiceDetail invoiceDetail = document.toObject(InvoiceDetail.class);
                            if (invoiceDetail != null) {
                                String invoiceID = invoiceDetail.getInvoiceID();
                                if (!checkedInvoiceIDSet.contains(invoiceID)) {
                                    documentTasks.add(getInvoiceTask(checkedInvoiceIDSet, invoiceID, productRevenue, invoiceDetail));
                                } else {
                                    productRevenue[0] += invoiceDetail.getNewPrice() * invoiceDetail.getQuantity();
                                }
                            }
                        }
                        return Tasks.whenAllSuccess(documentTasks);
                    } else {
                        throw Objects.requireNonNull(task.getException());
                    }
                })
                .addOnSuccessListener(aVoid -> {
                    product.setProductRevenue(productRevenue[0]);

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    Log.d("Performance", "getProductRevenueTask duration: " + duration + "ms");
                })
                .continueWith(task -> null);
    }

    public Task<DocumentSnapshot> getInvoiceTask(HashSet<String> checkedInvoiceIDSet, String invoiceID, double[] productRevenue, InvoiceDetail invoiceDetail) {
        return db.collection(INVOICE_COLLECTION)
                .document(invoiceID)
                .get()
                .addOnSuccessListener(document -> {
                    int status = Objects.requireNonNull(document.getLong(STATUS)).intValue();
                    if (status != OrderStatus.CANCELLED.getOrderStatusValue()) {
                        checkedInvoiceIDSet.add(invoiceID);
                        productRevenue[0] += invoiceDetail.getNewPrice() * invoiceDetail.getQuantity();
                    }
                });
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

    private void countProducts(Query query, final GetAggregateCallback callback) {
        query.get()
                .addOnSuccessListener(task -> {
                    int count = task.size();
                    callback.onSuccess(count);
                })
                .addOnFailureListener(e -> callback.onFailure(INTERNET_ERROR));
    }

    public void countProductsOutOfStockByStoreIdApi(String storeId, GetAggregateCallback callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereEqualTo(PRODUCT_INSTOCK, 0.0);
        countProducts(query, callback);
    }

    public void countProductsInStockByStoreIdApi(String storeId, GetAggregateCallback callback) {
        Query query = db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereGreaterThan(PRODUCT_INSTOCK, 0.0);
        countProducts(query, callback);
    }


}