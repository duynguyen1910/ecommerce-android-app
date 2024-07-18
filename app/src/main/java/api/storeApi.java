package api;

import static android.content.ContentValues.TAG;
import static constants.collectionName.STORE_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import constants.toastMessage;
import interfaces.CreateDocumentCallback;
import interfaces.GetDocumentCallback;

public class storeApi {
    private FirebaseFirestore db;
    public storeApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createStoreApi(Map<String, Object> newStore, final CreateDocumentCallback callback) {
        db.collection(STORE_COLLECTION)
                .add(newStore)
                .addOnSuccessListener(documentReference -> {

                    // documentReference.getId() trong trường hợp này là storeId
                    callback.onCreateSuccess(documentReference.getId());
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCreateFailure(toastMessage.REGISTER_FAILED);
                    }
                });

    }

    public void getStoreDataApi(String storeId, GetDocumentCallback callback){
        DocumentReference docRef = db.collection(STORE_COLLECTION).document(storeId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    callback.onGetDataSuccess(document.getData());
                } else {
                    callback.onGetDataFailure(toastMessage.REGISTER_FAILED);
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }
}
