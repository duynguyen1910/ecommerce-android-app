package api;

import static android.content.ContentValues.TAG;
import static constants.collectionName.STORE_COLLECTION;
import static constants.keyName.PRODUCTS;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import constants.toastMessage;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import models.Product;

public class productApi {
    private FirebaseFirestore db;

    public productApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createProductApi(Map<String, Object> productData, String storeId, CreateDocumentCallback callback) {
        // Sản phẩm nằm trong Store
        // Tìm reference của Collection Store
        DocumentReference storeRef = db.collection(STORE_COLLECTION).document(storeId);

        // Vào trường storeProducts của CollectionStore,đây là 1 subCollection, add productData vào subCollection này
        storeRef.collection(PRODUCTS)
                .add(productData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onCreateSuccess(toastMessage.CREATE_PRODUCT_SUCCESSFULLY);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCreateFailure(toastMessage.CREATE_PRODUCT_FAILED);
                    }
                });

    }

    public void getProductDetailApi(String storeId, String productId, GetDocumentCallback callback) {
        DocumentReference productRef = db.collection(STORE_COLLECTION).document(storeId).collection(PRODUCTS).document(productId);
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

    public void getProductsCollectionApi(String storeId, final GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        CollectionReference productRef = db.collection(STORE_COLLECTION).document(storeId).collection(PRODUCTS);
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product category = document.toObject(Product.class);
                        products.add(category);
                    }
                    callback.onGetDataSuccess(products);
                } else {
                    callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
                }
            }
        });

    }
}
