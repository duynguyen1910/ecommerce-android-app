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
import interfaces.CreateStoreCallback;
import interfaces.RegisterCallback;

public class storeApi {
    private FirebaseFirestore db;
    public storeApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createStoreApi(Map<String, Object> newStore, final CreateStoreCallback callback) {
        db.collection(STORE_COLLECTION)
                .add(newStore)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        // documentReference.getId() trong trường hợp này là storeId
                        callback.onCreateSuccess(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCreateFailure(toastMessage.REGISTER_FAILED);
                    }
                });

    }
}
