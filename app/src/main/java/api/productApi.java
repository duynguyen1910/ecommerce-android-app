package api;

import static android.content.ContentValues.TAG;
import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.collectionName.STORE_COLLECTION;
import static constants.keyName.PRODUCTS;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.STORE_ID;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import constants.toastMessage;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import interfaces.UpdateDocumentCallback;
import models.InvoiceDetail;
import models.Product;

public class productApi implements Serializable {
    private FirebaseFirestore db;

    public productApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createProductApi(Map<String, Object> newProduct, CreateDocumentCallback callback) {
        db.collection(PRODUCT_COLLECTION)
                .add(newProduct)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onCreateSuccess(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCreateFailure(toastMessage.CREATE_PRODUCT_FAILED);
                    }
                });

    }

    public void getProductDetailApi(String productId, GetDocumentCallback callback) {
        DocumentReference productRef = db.collection(PRODUCT_COLLECTION).document(productId);
        productRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        callback.onGetDataSuccess(document.getData());
                    } else {
                        callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    public void getProductsByStoreIdApi(String storeId, GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                String id = document.getId();
                                product.setBaseID(id);
                                products.add(product);
                            }
                            callback.onGetListSuccess(products);
                        } else {
                            Log.e("Firestore Query", "Lỗi khi lấy tài liệu: ", task.getException());
                            callback.onGetListFailure("Lấy thông tin sản phẩm thất bại");
                        }
                    }
                });
    }

    public void getProductsOutOfStockByStoreIdApi(String storeId, GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereEqualTo(PRODUCT_INSTOCK, 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                String id = document.getId();
                                product.setBaseID(id);
                                products.add(product);
                            }
                            callback.onGetListSuccess(products);
                        } else {
                            callback.onGetListFailure("Lấy thông tin sản phẩm thất bại");
                        }
                    }
                });
    }

    public void updateProductApi(Map<String, Object> updateData, String storeId, String productId, UpdateDocumentCallback callback) {
        DocumentReference productRef = db.collection(STORE_COLLECTION).document(storeId).collection(PRODUCTS).document(productId);
        productRef.update(updateData)
                .addOnSuccessListener(unused -> callback.onUpdateSuccess())
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating user's STORE_ID.", e));
    }

    public void getAllProductApi(final GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product product = document.toObject(Product.class);
                        String id = document.getId();
                        product.setBaseID(id);
                        products.add(product);
                    }
                    callback.onGetListSuccess(products);
                } else {
                    callback.onGetListFailure("Lấy thông tin sản phẩm thất bại");
                }
            }
        });
    }

}
