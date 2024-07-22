package api;

import static android.content.ContentValues.TAG;
import static constants.collectionName.INVOICE_COLLECTION;
import static constants.collectionName.INVOICE_DETAIL_COLLECTION;
import static constants.collectionName.PRODUCT_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.StatusCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.Product;

public class invoiceApi {
    private FirebaseFirestore db;

    public invoiceApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createInvoiceApi(Map<String, Object> newInvoice, final CreateDocumentCallback callback) {
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

    public void getAllInvoices(final GetCollectionCallback<Invoice> callback) {
        db.collection(INVOICE_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Invoice> invoices = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Invoice invoice = document.toObject(Invoice.class);
                                invoice.setBaseID(document.getId());
                                invoice.setStatus(document.getLong("status").intValue());

                                invoices.add(invoice);
                            }

                            callback.onGetListSuccess(invoices);
                        } else {
                            callback.onGetListFailure("Failed to get invoices: " + task.getException().getMessage());
                        }
                    }
                });
    }
    
    public void getInvoiceDetail(String invoiceID, final GetCollectionCallback<InvoiceDetail> callback) {
        db.collection(INVOICE_DETAIL_COLLECTION)
                .whereEqualTo("invoiceID", invoiceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<InvoiceDetail> invoiceDetails = new ArrayList<>();
                            ArrayList<String> productIDs = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                InvoiceDetail detail = document.toObject(InvoiceDetail.class);
                                invoiceDetails.add(detail);
                                productIDs.add(detail.getVariantID());
                            }

                            getProductsByListIDs(invoiceDetails, productIDs, callback);
                        } else {
                            callback.onGetListFailure("Failed to get invoice details: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void getProductsByListIDs(final ArrayList<InvoiceDetail> invoiceDetails, ArrayList<String> productIDs, final GetCollectionCallback<InvoiceDetail> callback) {
        final Map<String, Product> productMap = new HashMap<>();
        final AtomicInteger pendingRequests = new AtomicInteger(productIDs.size());

        for (String productID : productIDs) {
            db.collection(PRODUCT_COLLECTION).document(productID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Product product = document.toObject(Product.class);
                                    productMap.put(productID, product);
                                }
                            }

                            if (pendingRequests.decrementAndGet() == 0) {
                                for (InvoiceDetail detail : invoiceDetails) {
                                    Product product = productMap.get(detail.getVariantID());
                                    if (product != null) {
                                        detail.setProductName(product.getProductName());
                                        detail.setNewPrice(product.getNewPrice());
                                        detail.setStoreID(product.getStoreId());
                                    }
                                }
                                callback.onGetListSuccess(invoiceDetails);
                            }
                        }
                    });
        }

    }

}
