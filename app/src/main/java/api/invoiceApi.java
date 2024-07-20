package api;

import static constants.collectionName.INVOICE_COLLECTION;
import static constants.collectionName.INVOICE_DETAIL_COLLECTION;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import interfaces.CreateCallback;
import interfaces.StatusCallback;
import models.InvoiceDetail;

public class invoiceApi {
    private FirebaseFirestore db;

    public invoiceApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createInvoiceApi(Map<String, Object> newInvoice, final CreateCallback callback) {
        db.collection(INVOICE_COLLECTION)
                .add(newInvoice)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onCreateSuccess(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCreateFailure("Failed to create invoice: " + e.getMessage());
                    }
                });
    }

    public void createDetailInvoice(ArrayList<InvoiceDetail> invoiceItems,
                                    final StatusCallback callback) {
        WriteBatch batch = db.batch();
        for (InvoiceDetail detail : invoiceItems) {
            DocumentReference detailRef = db.collection(INVOICE_DETAIL_COLLECTION).document();
            Map<String, Object> newDetail = new HashMap<>();
            newDetail.put("invoiceID", detail.getInvoiceID());
            newDetail.put("variantID", detail.getVariantID());
            newDetail.put("quantity", detail.getQuantity());
            batch.set(detailRef, newDetail);
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess("Invoice and details created successfully");
                } else {
                    callback.onFailure("Failed to create invoice details: " + task.getException().getMessage());
                }
            }
        });
    }

}
