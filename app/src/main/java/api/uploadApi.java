package api;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import constants.toastMessage;
import interfaces.GetCollectionCallback;
import interfaces.UploadCallback;

public class uploadApi {
    FirebaseStorage storage;

    public uploadApi() { storage = FirebaseStorage.getInstance();}

    public void uploadImageToStorageApi(Uri imageUri, final UploadCallback callback) {
        if (imageUri != null) {
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            callback.uploadSuccess(downloadUri);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    callback.uploadFailure(toastMessage.UPLOAD_FAILED);
                }
            });
        }
    }

    public void uploadMultiImageToStorageApi(ArrayList<Uri> imagesUriList,
                                             final GetCollectionCallback callback) {
        ArrayList<String> imagesUrl = new ArrayList<>();
        StorageReference storageRef = storage.getReference();

        for (Uri uri : imagesUriList) {
            if (uri != null) {
                StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

                imageRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String url = task.getResult().toString();
                            imagesUrl.add(url);

                            if (imagesUrl.size() == imagesUriList.size()) {
                                callback.onGetListSuccess(imagesUrl);
                            }
                        } else {
                            callback.onGetListFailure(task.getException().getMessage());
                        }
                    });
                }).addOnFailureListener(e -> callback.onGetListFailure(e.getMessage()));
            }
        }
    }
}
