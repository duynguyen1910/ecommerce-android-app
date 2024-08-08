package api;

import static constants.collectionName.ADDRESS_COLLECTION;
import static constants.collectionName.USER_ADDRESS_COLLECTION;
import static constants.keyName.ADDRESS_ID;
import static constants.keyName.USER_ID;
import static constants.toastMessage.INTERNET_ERROR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import constants.toastMessage;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import models.InvoiceDetail;
import models.Product;
import models.User;
import models.UserAddress;

public class addressApi {
    private  FirebaseFirestore db;

    public addressApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createAddressApi(Map<String, Object> newAddress, CreateDocumentCallback callback) {
        db.collection(ADDRESS_COLLECTION)
                .add(newAddress)
                .addOnSuccessListener(documentReference -> callback.onCreateSuccess(documentReference.getId()))
                .addOnFailureListener(e -> callback.onCreateFailure(toastMessage.CREATE_PRODUCT_FAILED));
    }

    public void createUserAddressApi(Map<String, Object> newUserAddress, CreateDocumentCallback callback) {
        db.collection(USER_ADDRESS_COLLECTION)
                .add(newUserAddress)
                .addOnSuccessListener(documentReference -> callback.onCreateSuccess(documentReference.getId()))
                .addOnFailureListener(e -> callback.onCreateFailure(toastMessage.CREATE_PRODUCT_FAILED));
    }

    public void getAddressByUserIDApi(String userID, GetCollectionCallback<UserAddress> callback) {
        db.collection(USER_ADDRESS_COLLECTION)
                .whereEqualTo(USER_ID, userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<UserAddress> userAddresses = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String addressID = document.getString(ADDRESS_ID);

                            db.collection(ADDRESS_COLLECTION)
                                    .document(addressID)
                                    .get()
                                    .addOnCompleteListener(addressTask -> {
                                        if (addressTask.isSuccessful()) {
                                            UserAddress userAddress = addressTask.getResult().toObject(UserAddress.class);
                                            userAddress.setAddressID(addressID);
                                            userAddresses.add(userAddress);

                                            if (userAddresses.size() == task.getResult().size()) {
                                                callback.onGetListSuccess(userAddresses);
                                            }
                                        }
                                    });
                        }
                    } else {
                        callback.onGetListFailure(task.getException().getMessage());
                    }
                });
    }


    public void getAddressDetailApi(String addressID, final GetDocumentCallback callback) {
        if(addressID != null) {
            db.collection(ADDRESS_COLLECTION)
                    .document(addressID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Map<String, Object> data = documentSnapshot.getData();
                                callback.onGetDataSuccess(data);
                            }
                        }
                    });
        }

    }


}
