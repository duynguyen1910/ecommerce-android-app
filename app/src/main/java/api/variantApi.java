package api;

import static constants.collectionName.INVOICE_DETAIL_COLLECTION;
import static constants.collectionName.VARIANT_COLLECTION;
import static constants.toastMessage.CREATE_VARIANT_SUCCESSFULLY;
import static constants.toastMessage.ORDER_SUCCESSFULLY;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import interfaces.StatusCallback;
import models.InvoiceDetail;
import models.Variant;
import utils.Cart.CartUtils;

public class variantApi {
    private FirebaseFirestore db;

    public variantApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createVariantsApi(ArrayList<Variant> variantList, final StatusCallback callback) {
        WriteBatch batch = db.batch();
        for (Variant variant : variantList) {
            DocumentReference detailRef = db.collection(VARIANT_COLLECTION).document();
            Map<String, Object> newVariant = new HashMap<>();
            newVariant.put("variantName", variant.getVariantName());
            newVariant.put("variantImageUrl", variant.getVariantImageUrl());
            newVariant.put("inStock", variant.getInStock());
            newVariant.put("newPrice", variant.getNewPrice());
            newVariant.put("oldPrice", variant.getOldPrice());
            newVariant.put("productID", variant.getProductID());
            batch.set(detailRef, newVariant);
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess(CREATE_VARIANT_SUCCESSFULLY);
                } else {
                    callback.onFailure(task.getException().getMessage());
                }
            }
        });
    }

}
