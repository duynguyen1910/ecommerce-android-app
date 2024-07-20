package models;

import java.io.Serializable;
import java.util.Map;

import api.storeApi;
import interfaces.CreateStoreCallback;
import interfaces.GetStoreDataCallback;

public class Store extends BaseObject implements Serializable {
    private String storeName;
    private String storeAddress;
    private String storeImage;
    private int ownerID;
    private storeApi storeApi;

    public Store() {
        storeApi = new storeApi();
    }

    public Store(String storeID, String storeName, String storeAddress, String storeImage) {
        super.baseID = storeID;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeImage = storeImage;
    }

    @Override
    public String getBaseID() {
        return super.baseID;
    }

    @Override
    public void setBaseID(String storeID) {
        validateBaseID(storeID);

        super.baseID = storeID;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void onCreateStore(Map<String, Object> newStore, final CreateStoreCallback callback) {
        storeApi.createStoreApi(newStore, callback);
    }
    public void onGetStoreData(String storeId, GetStoreDataCallback callback){
        storeApi.getStoreDataApi(storeId, callback);
    }
}
