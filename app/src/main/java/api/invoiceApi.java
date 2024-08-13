package api;

import static constants.collectionName.INVOICE_COLLECTION;
import static constants.collectionName.INVOICE_DETAIL_COLLECTION;
import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.collectionName.STORE_COLLECTION;
import static constants.collectionName.VARIANT_COLLECTION;
import static constants.keyName.CREATE_AT;
import static constants.keyName.CUSTOMER_ID;
import static constants.keyName.INVOICE_ID;
import static constants.keyName.PRODUCT_ID;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NEW_PRICE;
import static constants.keyName.PRODUCT_OLD_PRICE;
import static constants.keyName.PRODUCT_SOLD;
import static constants.keyName.QUANTITY;
import static constants.keyName.STATUS;
import static constants.keyName.STORE_ID;
import static constants.keyName.VARIANT_ID;
import static constants.toastMessage.CONFIRMED_ORDER_SUCCESSFULLY;
import static constants.toastMessage.INTERNET_ERROR;
import static constants.toastMessage.ORDER_SUCCESSFULLY;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import interfaces.CreateDocumentCallback;
import interfaces.GetAggregate.GetAggregateCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetAggregate.GetManyAggregateCallback;
import interfaces.StatusCallback;
import interfaces.UpdateDocumentCallback;
import models.Invoice;
import models.InvoiceDetail;
import models.Product;
import models.Store;
import models.Variant;
import utils.Cart.CartUtils;
import utils.TimeUtils;

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


    public void createDetailInvoiceApi(ArrayList<InvoiceDetail> invoiceItems,
                                       final StatusCallback callback) {
        WriteBatch batch = db.batch();
        for (InvoiceDetail detail : invoiceItems) {
            DocumentReference detailRef = db.collection(INVOICE_DETAIL_COLLECTION).document();
            Map<String, Object> newDetail = new HashMap<>();
            newDetail.put(INVOICE_ID, detail.getInvoiceID());
            newDetail.put(QUANTITY, detail.getQuantity());
            newDetail.put(PRODUCT_NAME, detail.getProductName());
            newDetail.put(PRODUCT_NEW_PRICE, detail.getNewPrice());
            newDetail.put(PRODUCT_OLD_PRICE, detail.getOldPrice());
            newDetail.put(VARIANT_ID, detail.getVariantID());
            newDetail.put(PRODUCT_ID, detail.getProductID());

            batch.set(detailRef, newDetail);
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess(ORDER_SUCCESSFULLY);
                    CartUtils.clearMyCart();
                } else {
                    callback.onFailure("Failed to create invoice details: " + task.getException().getMessage());
                }
            }
        });
    }

    public void getTopBestSellerByStoreID(String storeID, int limit, final GetCollectionCallback<Product> callback) {
        db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeID)
                .orderBy(PRODUCT_SOLD, Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(task -> {
                    ArrayList<Product> products = new ArrayList<>();
                    for (DocumentSnapshot document : task.getDocuments()) {
                        Product product = document.toObject(Product.class);
                        product.setBaseID(document.getId());
                        products.add(product);
                    }
                    callback.onGetListSuccess(products);
                })
                .addOnFailureListener(e -> callback.onGetListFailure(INTERNET_ERROR));
    }


    public void getSpendingsInHaftYearByCustomerID(String customerID, int year, int currentMonth, GetCollectionCallback<Double> callback) {
        if (1 <= currentMonth && currentMonth <= 12) {
            List<Task<QuerySnapshot>> tasks = new ArrayList<>();
            if (currentMonth <= 6) {
                for (int month = 1; month <= currentMonth; month++) {
                    // Tạo các task cho từng tháng
                    tasks.add(getSpendingInAMonthTask(customerID, year, month));
                }
            } else {
                for (int month = currentMonth - 5; month <= currentMonth; month++) {
                    // Tạo các task cho từng tháng
                    tasks.add(getSpendingInAMonthTask(customerID, year, month));
                }
            }


            // Sử dụng Tasks.whenAll để thực hiện tất cả các task và nhận kết quả
            Tasks.whenAll(tasks).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ArrayList<Double> monthlySpendings = new ArrayList<>();
                    for (int i = 0; i < tasks.size(); i++) {
                        Task<QuerySnapshot> t = tasks.get(i);
                        double spending = 0;
                        try {
                            for (DocumentSnapshot document : t.getResult().getDocuments()) {
                                Invoice invoice = document.toObject(Invoice.class);
                                double total = invoice.getTotal();
                                spending += total;
                            }
                        } catch (Exception e) {
                            Log.d("getSpendingsInHaftYearByCustomerID", "Error processing month " + (i + 1) + ": " + e.getMessage());
                        }
                        monthlySpendings.add(spending);
                    }
                    callback.onGetListSuccess(monthlySpendings);
                } else {
                    callback.onGetListFailure(INTERNET_ERROR);
                }
            });
        }
    }

    public void getCustomerMonthCountInvoice(String customerID, int year, int month, GetManyAggregateCallback callback) {
        List<Integer> orderStatuses = Arrays.asList(1, 2, 3, 4);
        Timestamp[] dayRange = TimeUtils.getMonthRange(year, month);
        db.collection(INVOICE_COLLECTION)
                .whereEqualTo(CUSTOMER_ID, customerID)
                .whereIn(STATUS, orderStatuses)
                .whereGreaterThanOrEqualTo(CREATE_AT, dayRange[0])
                .whereLessThanOrEqualTo(CREATE_AT, dayRange[1])
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot tasks) {
                        double spending = 0;
                        double countMonthInvoice = tasks.size();
                        for (DocumentSnapshot document : tasks.getDocuments()) {
                            Invoice invoice = document.toObject(Invoice.class);
                            double total = invoice.getTotal();
                            spending += total;
                        }
                        callback.onSuccess(spending, countMonthInvoice);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure("getCountOfInvoiceInAMonth: " + e.getMessage());
                    }
                });
    }


    private Task<QuerySnapshot> getSpendingInAMonthTask(String customerID, int year, int month) {
        List<Integer> orderStatuses = Arrays.asList(1, 2, 3, 4);
        Timestamp[] dayRange = TimeUtils.getMonthRange(year, month);
        return db.collection(INVOICE_COLLECTION)
                .whereEqualTo(CUSTOMER_ID, customerID)
                .whereIn(STATUS, orderStatuses)
                .whereGreaterThanOrEqualTo(CREATE_AT, dayRange[0])
                .whereLessThanOrEqualTo(CREATE_AT, dayRange[1])
                .get();
    }


    public void getRevenueForAllMonthsByStoreID(String storeID, int year, GetCollectionCallback<Double> callback) {
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            // Tạo các task cho từng tháng
            tasks.add(getRevenueInAMonthTask(storeID, year, month));
        }


        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Double> monthlyRevenues = new ArrayList<>();
                for (int i = 0; i < tasks.size(); i++) {
                    Task<QuerySnapshot> t = tasks.get(i);
                    double revenue = 0;
                    try {
                        for (DocumentSnapshot document : t.getResult().getDocuments()) {
                            Invoice invoice = document.toObject(Invoice.class);
                            double total = invoice.getTotal();
                            revenue += total;
                        }
                    } catch (Exception e) {
                        Log.d("getRevenueForAllMonthsByStoreID", "Error processing month " + (i + 1) + ": " + e.getMessage());
                    }
                    monthlyRevenues.add(revenue);
                }
                callback.onGetListSuccess(monthlyRevenues);
            } else {
                callback.onGetListFailure(INTERNET_ERROR);
            }
        });
    }


    private Task<QuerySnapshot> getRevenueInAMonthTask(String storeID, int year, int month) {
        List<Integer> orderStatuses = Arrays.asList(1, 2, 3, 4);
        Timestamp[] dayRange = TimeUtils.getMonthRange(year, month);
        return db.collection(INVOICE_COLLECTION)
                .whereEqualTo(STORE_ID, storeID)
                .whereIn(STATUS, orderStatuses)
                .whereGreaterThanOrEqualTo(CREATE_AT, dayRange[0])
                .whereLessThanOrEqualTo(CREATE_AT, dayRange[1])
                .get();
    }

    public void getStoreMonthStatistics(String storeID, int year, int month, GetManyAggregateCallback callback) {
        List<Integer> orderStatuses = Arrays.asList(1, 2, 3, 4);
        Timestamp[] dayRange = TimeUtils.getMonthRange(year, month);
        db.collection(INVOICE_COLLECTION)
                .whereEqualTo(STORE_ID, storeID)
                .whereIn(STATUS, orderStatuses)
                .whereGreaterThanOrEqualTo(CREATE_AT, dayRange[0])
                .whereLessThanOrEqualTo(CREATE_AT, dayRange[1])
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot tasks) {
                        double revenue = 0;
                        double countMonthInvoice = tasks.size();
                        for (DocumentSnapshot document : tasks.getDocuments()) {
                            Invoice invoice = document.toObject(Invoice.class);
                            double total = invoice.getTotal();
                            revenue += total;
                        }
                        callback.onSuccess(revenue, countMonthInvoice);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure("getCountOfInvoiceInAMonth: " + e.getMessage());
                    }
                });
    }

    public void getAllStoreRevenueByYear(int year, GetCollectionCallback<Store> callback) {
        List<Integer> orderStatuses = Arrays.asList(1, 2, 3, 4);
        Timestamp[] yearRange = TimeUtils.getYearRange(year);

        HashMap<String, Store> storeRevenueMap = new HashMap<>();
        ArrayList<Task<DocumentSnapshot>> storeTasks = new ArrayList<>();

        db.collection(INVOICE_COLLECTION)
                .whereIn(STATUS, orderStatuses)
                .whereGreaterThanOrEqualTo(CREATE_AT, yearRange[0])
                .whereLessThanOrEqualTo(CREATE_AT, yearRange[1])
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Invoice invoice = document.toObject(Invoice.class);
                        if (invoice != null) {
                            String storeID = invoice.getStoreID();
                            Store store = storeRevenueMap.get(storeID);
                            if (store == null) {

                                store = new Store();
                                store.setStoreRevenue(invoice.getTotal());
                                store.setBaseID(invoice.getStoreID());
                                storeRevenueMap.put(storeID, store);
                                storeTasks.add(getStoreDetail(storeID, store));
                            } else {
                                store.setStoreRevenue(store.getStoreRevenue() + invoice.getTotal());
                            }
                        }
                    }

                    Tasks.whenAllSuccess(storeTasks)
                            .addOnSuccessListener(results -> {
                                ArrayList<Store> stores = new ArrayList<>(storeRevenueMap.values());
                                callback.onGetListSuccess(stores);
                            }).addOnFailureListener(e -> {
                                callback.onGetListFailure(INTERNET_ERROR);
                            });
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onGetListFailure(INTERNET_ERROR);
                    }
                });
    }

    private Task<DocumentSnapshot> getStoreDetail(String storeID, Store store) {
        return db.collection(STORE_COLLECTION)
                .document(storeID)
                .get()
                .addOnSuccessListener(document -> {
                    store.setStoreName(document.toObject(Store.class).getStoreName());
                });
    }


    public void getEcommerceMonthRevenueByYear(int year, GetCollectionCallback<Double> callback) {
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            // Tạo các task cho từng tháng
            tasks.add(getEcommerceMonthRevenueTask(year, month));
        }


        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Double> monthlyRevenues = new ArrayList<>();
                for (int i = 0; i < tasks.size(); i++) {
                    Task<QuerySnapshot> t = tasks.get(i);
                    double revenue = 0;
                    try {
                        for (DocumentSnapshot document : t.getResult().getDocuments()) {
                            Invoice invoice = document.toObject(Invoice.class);
                            double total = invoice.getTotal();
                            revenue += total;
                        }
                    } catch (Exception e) {
                        Log.d("getRevenueForAllMonthsByStoreID", "Error processing month " + (i + 1) + ": " + e.getMessage());
                    }
                    monthlyRevenues.add(revenue);
                }
                callback.onGetListSuccess(monthlyRevenues);
            } else {
                callback.onGetListFailure(INTERNET_ERROR);
            }
        });
    }

    private Task<QuerySnapshot> getEcommerceMonthRevenueTask(int year, int month) {
        List<Integer> orderStatuses = Arrays.asList(1, 2, 3, 4);
        Timestamp[] dayRange = TimeUtils.getMonthRange(year, month);
        return db.collection(INVOICE_COLLECTION)
                .whereIn(STATUS, orderStatuses)
                .whereGreaterThanOrEqualTo(CREATE_AT, dayRange[0])
                .whereLessThanOrEqualTo(CREATE_AT, dayRange[1])
                .get();
    }

    public void getInvoicesByStatusApi(String customerID, int invoiceStatus, final GetCollectionCallback<Invoice> callback) {
        db.collection(INVOICE_COLLECTION)
                .whereEqualTo(CUSTOMER_ID, customerID)
                .whereEqualTo(STATUS, invoiceStatus)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Invoice> invoices = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Invoice invoice = document.toObject(Invoice.class);
                                invoice.setBaseID(document.getId());
                                invoice.setStatus(document.getLong(STATUS).intValue());

                                invoices.add(invoice);
                            }

                            callback.onGetListSuccess(invoices);
                        } else {
                            callback.onGetListFailure("Failed to get invoices: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void getDeliveryInvoicesByStatusApi(int invoiceStatus, final GetCollectionCallback<Invoice> callback) {
        db.collection(INVOICE_COLLECTION)
                .whereEqualTo(STATUS, invoiceStatus)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Invoice> invoices = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Invoice invoice = document.toObject(Invoice.class);
                                invoice.setBaseID(document.getId());
                                invoice.setStatus(document.getLong(STATUS).intValue());

                                invoices.add(invoice);
                            }

                            callback.onGetListSuccess(invoices);
                        } else {
                            callback.onGetListFailure("Failed to get invoices: " + task.getException().getMessage());
                        }
                    }
                });
    }


    public void getInvoiceDetailApi(String invoiceID, final GetCollectionCallback<InvoiceDetail> callback) {
        // Tìm tất cả invoice Detail thỏa mãn ID hóa đơn muốn tìm

        db.collection(INVOICE_DETAIL_COLLECTION)
                .whereEqualTo(INVOICE_ID, invoiceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<InvoiceDetail> invoiceDetails = new ArrayList<>();
                            AtomicInteger remainingDetails = new AtomicInteger(task.getResult().size());

                            // Task lấy invoice Detail chia thành 2 hướng
                            // 1. Hướng lấy invoice detail đối với sản phẩm có variant
                            // 2. Hướng lấy invoice detail đối với sản phẩm không có variant

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                InvoiceDetail detail = document.toObject(InvoiceDetail.class);
                                String variantID = detail.getVariantID();

                                // Hướng sản phẩm CÓ variant
                                if (variantID != null) {
                                    fetchVariantDetails(variantID, detail, invoiceDetails, remainingDetails, callback);
                                }
                                // Hướng sản phẩm KHÔNG CÓ variant
                                else {
                                    String productID = detail.getProductID();
                                    if (productID != null) {
                                        fetchProductDetails(productID, detail, invoiceDetails, remainingDetails, callback);
                                    } else {
                                        remainingDetails.decrementAndGet();
                                    }
                                }
                            }

                            // Check if no pending requests and notify callback
                            if (remainingDetails.get() == 0) {
                                callback.onGetListSuccess(invoiceDetails);
                            }
                        } else {
                            callback.onGetListFailure("Failed to get invoice details: " + task.getException().getMessage());
                        }
                    }
                });
    }


    private void fetchVariantDetails(String variantID, final InvoiceDetail detail,
                                     final ArrayList<InvoiceDetail> invoiceDetails, final AtomicInteger remainingDetails, final GetCollectionCallback<InvoiceDetail> callback) {
        db.collection(VARIANT_COLLECTION)
                .document(variantID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Variant variant = documentSnapshot.toObject(Variant.class);
                        if (variant != null) {
                            detail.setProductImage(variant.getVariantImageUrl());
                            detail.setVariantName(variant.getVariantName());

                            invoiceDetails.add(detail);
                            if (remainingDetails.decrementAndGet() == 0) {
                                callback.onGetListSuccess(invoiceDetails);
                            }
                        }
                    }
                });
    }


    private void fetchProductDetails(String productID, final InvoiceDetail detail,
                                     final ArrayList<InvoiceDetail> invoiceDetails, final AtomicInteger remainingDetails, final GetCollectionCallback<InvoiceDetail> callback) {
        db.collection(PRODUCT_COLLECTION)
                .document(productID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Product product = documentSnapshot.toObject(Product.class);
                        if (product != null) {
                            detail.setProductImage(product.getProductImages().get(0));

                            invoiceDetails.add(detail);
                        }
                        if (remainingDetails.decrementAndGet() == 0) {
                            callback.onGetListSuccess(invoiceDetails);
                        }
                    }
                });
    }


    public void getInvoiceByStoreIDApi(String storeID, int invoiceStatus, final GetCollectionCallback<Invoice> callback) {
        db.collection(INVOICE_COLLECTION)
                .whereEqualTo(STORE_ID, storeID)
                .whereEqualTo(STATUS, invoiceStatus)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Invoice> invoices = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Invoice invoice = document.toObject(Invoice.class);
                                invoice.setBaseID(document.getId());
                                invoice.setStatus(document.getLong(STATUS).intValue());

                                invoices.add(invoice);
                            }

                            callback.onGetListSuccess(invoices);
                        } else {
                            callback.onGetListFailure("Failed to get invoices: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void updateStatusInvoiceApi(String invoiceID, Map<String, Object> invoiceUpdate, final UpdateDocumentCallback callback) {
        DocumentReference invoiceRef = db.collection(INVOICE_COLLECTION).document(invoiceID);
        invoiceRef.update(invoiceUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onUpdateSuccess(CONFIRMED_ORDER_SUCCESSFULLY);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onUpdateFailure(e.getMessage());
                    }
                });
    }


    public void countInvoices(Query query, final GetAggregateCallback callback) {
        query.get()
                .addOnSuccessListener(task -> {
                    int count = task.size();
                    callback.onSuccess(count);
                })
                .addOnFailureListener(e -> callback.onFailure(INTERNET_ERROR));
    }

    public void countRequestInvoicesByStoreIDAndStatus(String storeId, int status, final GetAggregateCallback callback) {
        Query query = db.collection(INVOICE_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereEqualTo(STATUS, status);
        countInvoices(query, callback);
    }

    public void countDeliveryInvoicesByStatus(int status, final GetAggregateCallback callback) {
        Query query = db.collection(INVOICE_COLLECTION)
                .whereEqualTo(STATUS, status);
        countInvoices(query, callback);
    }

}
