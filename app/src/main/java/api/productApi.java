package api;

import static android.content.ContentValues.TAG;
import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.collectionName.STORE_COLLECTION;
import static constants.collectionName.USER_COLLECTION;
import static constants.keyName.CATEGORY_ID;
import static constants.keyName.PASSWORD;
import static constants.keyName.PHONE_NUMBER;
import static constants.keyName.PRODUCTS;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.PRODUCT_NAME;
import static constants.keyName.PRODUCT_NAME_CHUNK;
import static constants.keyName.STORE_ID;

import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import constants.toastMessage;
import interfaces.CreateDocumentCallback;
import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import interfaces.UpdateDocumentCallback;
import models.Product;

public class productApi implements Serializable {
    private final FirebaseFirestore db;

    public productApi() {
        db = FirebaseFirestore.getInstance();
    }

    public void createProductApi(Map<String, Object> newProduct, CreateDocumentCallback callback) {
        db.collection(PRODUCT_COLLECTION)
                .add(newProduct)
                .addOnSuccessListener(documentReference -> callback.onCreateSuccess(documentReference.getId()))
                .addOnFailureListener(e -> callback.onCreateFailure(toastMessage.CREATE_PRODUCT_FAILED));

    }

    public void getProductDetailApi(String productId, GetDocumentCallback callback) {
        DocumentReference productRef = db.collection(PRODUCT_COLLECTION).document(productId);
        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    callback.onGetDataSuccess(document.getData());
                } else {
                    callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }

    public void getProductsByStoreIdApi(String storeId, GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            String id = document.getId();
                            product.setBaseId(id);
                            products.add(product);
                        }
                        callback.onGetDataSuccess(products);
                    } else {
                        Log.e("Firestore Query", "Lỗi khi lấy tài liệu: ", task.getException());
                        callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
                    }
                });
    }

    public void getProductsOutOfStockByStoreIdApi(String storeId, GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereEqualTo(PRODUCT_INSTOCK, 0)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            String id = document.getId();
                            product.setBaseId(id);
                            products.add(product);
                        }
                        callback.onGetDataSuccess(products);
                    } else {
                        callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
                    }
                });
    }

    public void updateProductApi(Map<String, Object> updateData, String storeId, String productId, UpdateDocumentCallback callback) {
        DocumentReference productRef = db.collection(STORE_COLLECTION).document(storeId).collection(PRODUCTS).document(productId);
        productRef.update(updateData)
                .addOnSuccessListener(unused -> callback.onUpdateSuccess())
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating user's STORE_ID.", e));
    }

    public void getAllProductApi(final GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    String id = document.getId();
                    product.setBaseId(id);
                    products.add(product);
                }
                callback.onGetDataSuccess(products);
            } else {
                callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
            }
        });
    }

    public void getAllProductByCategoryIdApi(String categoryId, final GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(CATEGORY_ID, categoryId)
                .get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            String id = document.getId();
                            product.setBaseId(id);
                            products.add(product);
                        }
                        callback.onGetDataSuccess(products);
                    } else {
                        callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
                    }
                });
    }

    public void getAllProductByStringQueryApi(String stringQuery, final GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION)
                .whereArrayContainsAny(PRODUCT_NAME_CHUNK, splitProductNameBySpace(stringQuery))
                .get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            String id = document.getId();
                            product.setBaseId(id);
                            products.add(product);
                        }
                        callback.onGetDataSuccess(products);
                    } else {
                        callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
                    }
                });
    }

    public ArrayList<String> splitProductNameBySpace(String productName) {
        productName = Normalizer.normalize(productName.toLowerCase(), Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        productName = pattern.matcher(productName).replaceAll("");
        String[] parts = productName.split("\\s+");
        return new ArrayList<>(Arrays.asList(parts));
    }


    public static ArrayList<String> chunk(String productName, int size) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        int count = 0;
        for (int i = 0; i < productName.length(); i++) {
            char currentChar = productName.charAt(i);
            if (currentChar != ' ') {
                temp.append(currentChar);
                count++;
                if (count == size) {
                    result.add(temp.toString());
                    temp.setLength(0);
                    count = 0;
                }
            }
        }

        // Thêm chuỗi còn lại nếu có
        if (temp.length() != 0) {
            result.add(temp.toString());
        }

        return result;
    }

    public void getAllProductByStoreIdAndCategoryIdApi(String storeId, String categoryId, final GetCollectionCallback<Product> callback) {
        ArrayList<Product> products = new ArrayList<>();
        db.collection(PRODUCT_COLLECTION)
                .whereEqualTo(STORE_ID, storeId)
                .whereEqualTo(CATEGORY_ID, categoryId)
                .get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            String id = document.getId();
                            product.setBaseId(id);
                            products.add(product);
                        }
                        callback.onGetDataSuccess(products);
                    } else {
                        callback.onGetDataFailure("Lấy thông tin sản phẩm thất bại");
                    }
                });
    }
}
