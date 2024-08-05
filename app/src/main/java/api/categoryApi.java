package api;
import static constants.collectionName.CATEGORY_COLLECTION;
import static constants.toastMessage.INTERNET_ERROR;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import interfaces.GetCollectionCallback;
import interfaces.GetDocumentCallback;
import models.Category;

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
    public void getCategoriesByIDSetApi(Set<String> categoryIDSet, final GetCollectionCallback<Category> callback) {
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String categoryID : categoryIDSet){
            tasks.add(getCategoryDetailTask(categoryID));
        }

        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                ArrayList<Category> categories = new ArrayList<>();
                int tasksSize = tasks.size();
                for (int i = 0; i < tasksSize; i++){
                    Task<DocumentSnapshot> taskItem = tasks.get(i);
                    Category category = taskItem.getResult().toObject(Category.class);
                    category.setBaseID(taskItem.getResult().getId());
                    categories.add(category);
                }
                callback.onGetListSuccess(categories);
            }else {
                callback.onGetListFailure("Lấy thông tin danh mục thất bại");
            }
        });

    }


    public Task<DocumentSnapshot> getCategoryDetailTask(String categoryID) {
        return db.collection(CATEGORY_COLLECTION)
                .document(categoryID)
                .get();
    }

    public void getCategoryDetailApi(String categoryID, GetDocumentCallback callback) {
        db.collection(CATEGORY_COLLECTION)
                .document(categoryID)
                .get()
                .addOnSuccessListener(task -> {
                    callback.onGetDataSuccess(task.getData());

                }).addOnFailureListener(e -> {
                    callback.onGetDataFailure(INTERNET_ERROR);
                });
    }







}