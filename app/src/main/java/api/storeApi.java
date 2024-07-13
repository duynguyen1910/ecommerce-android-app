package api;

import static constants.collectionName.STORE_COLLECTION;
import static constants.collectionName.USER_COLLECTION;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import constants.toastMessage;
import interfaces.RegisterCallback;

public class storeApi {
    private FirebaseFirestore db;
    public storeApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createStoreApi(Map<String, Object> newStore, final RegisterCallback callback) {
        db.collection(STORE_COLLECTION)
                .add(newStore)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onRegisterSuccess(toastMessage.REGISTER_SUCCESSFULLY);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onRegisterFailure(toastMessage.REGISTER_FAILED);
                    }
                });

    }
}
