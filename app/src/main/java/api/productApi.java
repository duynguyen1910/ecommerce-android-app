package api;

import static android.content.ContentValues.TAG;
import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.collectionName.STORE_COLLECTION;
import static constants.collectionName.USER_COLLECTION;
import static constants.keyName.STORE_PRODUCTS;

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

import java.util.Map;

import constants.toastMessage;
import interfaces.CreateProductCallback;
import interfaces.CreateStoreCallback;
import interfaces.GetProductDataCallback;
import interfaces.GetStoreDataCallback;
import interfaces.UpdateUserCallback;

public class productApi {
    private FirebaseFirestore db;

    public productApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createProductApi(Map<String, Object> productData, String storeId, CreateProductCallback callback) {
        // Sản phẩm nằm trong Store
        // Tìm reference của Collection Store
        DocumentReference storeRef = db.collection(STORE_COLLECTION).document(storeId);

        // Vào trường storeProducts của CollectionStore,đây là 1 subCollection, add productData vào subCollection này
        storeRef.collection(STORE_PRODUCTS)
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

    public void getProductDetailDataApi(String storeId, String productId, GetProductDataCallback callback) {
        DocumentReference productRef = db.collection(STORE_COLLECTION).document(storeId).collection(STORE_PRODUCTS).document(productId);
        productRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        callback.onGetDataSuccess(document.getData());
                    } else {
                        callback.onGetDataFailure(toastMessage.GET_PRODUCT_FAILED);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
