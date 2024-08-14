package api;

import static android.content.ContentValues.TAG;
import static constants.collectionName.STORE_COLLECTION;
import static constants.collectionName.USER_COLLECTION;
import static constants.keyName.USER_IMAGE_URL;
import static constants.toastMessage.INTERNET_ERROR;
import static constants.toastMessage.URL_NOT_FOUND;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import constants.toastMessage;
import interfaces.CreateDocumentCallback;
import interfaces.GetAggregate.GetAggregateCallback;
import interfaces.GetDocumentCallback;
import interfaces.ImageCallback;

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



    public void getStoreDetailApi(String storeID, GetDocumentCallback callback){
        if(storeID == null) return;

        DocumentReference docRef = db.collection(STORE_COLLECTION).document(storeID);
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
    public void getCountOfStores(GetAggregateCallback callback){
      db.collection(STORE_COLLECTION)
              .get()
              .addOnSuccessListener(task -> callback.onSuccess(task.size()))
              .addOnFailureListener(e -> callback.onFailure(INTERNET_ERROR));

    }



}
