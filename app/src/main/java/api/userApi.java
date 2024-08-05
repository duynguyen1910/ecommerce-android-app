package api;

import static constants.collectionName.USER_COLLECTION;
import static constants.keyName.PASSWORD;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.USER_IMAGE_URL;
import static constants.toastMessage.UPDATE_SUCCESSFULLY;
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
import interfaces.StatusCallback;
import interfaces.UserCallback;
import models.User;
import interfaces.UpdateDocumentCallback;
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

    public void createUserApi(Map<String, Object> newUser, final StatusCallback callback) {
        db.collection(USER_COLLECTION)
                .add(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onSuccess(toastMessage.REGISTER_SUCCESSFULLY);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(toastMessage.REGISTER_FAILED);
                    }
                });

    }

    public void saveImageUriApi(String downloadUri, String userId) {
        db.collection(USER_COLLECTION).document(userId)
                .update(USER_IMAGE_URL, downloadUri)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void updateUserApi(Map<String, Object> updateData, String userId,
                              UpdateDocumentCallback callback) {
        DocumentReference userRef = db.collection(USER_COLLECTION).document(userId);
        userRef.update(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onUpdateSuccess(UPDATE_SUCCESSFULLY);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onUpdateFailure(e.getMessage());
                    }
                });
    };

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
                            callback.getUserInfoSuccess(user);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.getUserInfoFailure(e.getMessage());
                    }
                });
    }

    public void updateUserInfoApi(String userId, Map<String, Object> userUpdates,
                                  final StatusCallback callback) {
        db.collection(USER_COLLECTION).document(userId)
                .update(userUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess(toastMessage.UPDATE_SUCCESSFULLY);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                });


    }
}
