package api;

import static constants.collectionName.USER_COLLECTION;
import static constants.keyName.PASSWORD;
import static constants.keyName.PHONE_NUMBER;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import interfaces.LoginCallback;
import interfaces.RegisterCallback;
import models.User;
import constants.toastMessage;

public class userApi {

    private FirebaseFirestore db;

    public userApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void checkUserCredentialsApi(String phoneNumber, String password, OnCompleteListener<QuerySnapshot> listener) {
        db.collection(USER_COLLECTION)
                .whereEqualTo(PHONE_NUMBER, phoneNumber)
                .whereEqualTo(PASSWORD, password)
                .get()
                .addOnCompleteListener(listener);
    }

    public void createUserApi(Map<String, Object> newUser, final RegisterCallback callback) {
        db.collection(USER_COLLECTION)
                .add(newUser)
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
