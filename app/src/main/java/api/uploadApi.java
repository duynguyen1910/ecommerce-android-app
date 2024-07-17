package api;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import constants.toastMessage;
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
}
