package api;
import static constants.collectionName.CATEGORY_COLLECTION;
import static constants.collectionName.PRODUCT_COLLECTION;
import static constants.keyName.PRODUCT_INSTOCK;
import static constants.keyName.STORE_ID;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

import interfaces.GetCollectionCallback;
import models.Category;
import models.Product;

public class categoryApi {
    private FirebaseFirestore db;

    public categoryApi() {
        db = FirebaseFirestore.getInstance();
    }


    public void getAllCategoryApi(final GetCollectionCallback<Category> callback) {
        ArrayList<Category> categories = new ArrayList<>();
        CollectionReference categoryRef = db.collection(CATEGORY_COLLECTION);
        categoryRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Category category = document.toObject(Category.class);
                        category.setBaseID(document.getId());
                        categories.add(category);
                    }
                    callback.onGetListSuccess(categories);
                } else {
                    callback.onGetListFailure("Lấy thông tin sản phẩm thất bại");
                }
            }
        });

    }





}