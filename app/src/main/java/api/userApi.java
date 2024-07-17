package api;

import static constants.collectionName.USER_COLLECTION;
import static constants.keyName.PASSWORD;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.USER_IMAGE_URL;
import static constants.toastMessage.URL_NOT_FOUND;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import interfaces.ImageCallback;
import interfaces.UpdateCallback;
import interfaces.UserCallback;
import interfaces.RegisterCallback;
import constants.toastMessage;
import models.User;

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

    public void saveImageUriApi(String downloadUri, String userId) {
        db.collection(USER_COLLECTION).document(userId)
                .update(USER_IMAGE_URL, downloadUri)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getContext(), "Image URL saved to Firestore", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Failed to save image URL to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getUserImageApi(String userId, final ImageCallback callback) {
        db.collection(USER_COLLECTION).document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String imageUrl = documentSnapshot.getString(USER_IMAGE_URL);
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                callback.getImageSuccess(imageUrl);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.getImageFailure(URL_NOT_FOUND);
                    }
                });
    }

    public void getUserInfoApi(String userId, final UserCallback callback) {
        db.collection(USER_COLLECTION).document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            callback.onGetUserInfoSuccess(user);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onGetUserInfoFailure(e.getMessage());
                    }
                });
    }

    public void updateUserInfoApi(String userId, Map<String, Object> userUpdates, final UpdateCallback callback) {
        db.collection(USER_COLLECTION).document(userId)
                .update(userUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.updateSuccess(toastMessage.UPDATE_SUCCESSFULLY);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.updateFailure(e.getMessage());
                    }
                });
    }
}
