package models;

import static constants.keyName.USER_ROLE;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import api.userApi;
import constants.toastMessage;
import enums.UserRole;
import interfaces.LoginCallback;
import interfaces.RegisterCallback;

public class User extends BaseObject {
    private String phoneNumber;
    private String password;
    private String fullname;
    private String imageUrl;
    private UserRole role;
    private userApi userApi;

    public User() {
        userApi  = new userApi();
    }

    public User(String phoneNumber, String password, String fullname, String imageUrl, UserRole role) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullname = fullname;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    @Override
    public String getBaseId() {
        return super.baseId;
    }

    @Override
    public void setBaseId(String userId) {
        validateBaseId(userId);

        super.baseId = userId;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = UserRole.fromInt(role);
    }

    public void onLogin(String phoneNumber, String password, final LoginCallback callback) {
        userApi.checkUserCredentialsApi(phoneNumber, password, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        String documentId = document.getId();
                        user.setBaseId(documentId);
                        int roleValue = document.getLong(USER_ROLE).intValue();
                        user.setRole(roleValue);

                        callback.onLoginSuccess(user);
                    }
                } else {
                    callback.onLoginFailure(toastMessage.LOGIN_FAILED);
                }
            }
        });
    }

    public void onRegister(Map<String, Object> newUser, final RegisterCallback callback) {
        userApi.createUserApi(newUser, callback);
    }

}