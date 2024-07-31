package models;

public class UserAddress {
    private String addressID;
    private String userID;

    private String fullname;
    private String phoneNumber;
    private String detailedAddress;
    private String wardName;
    private String districtName;
    private String provinceName;

    public UserAddress() {
    }

    public UserAddress(String addressID, String detailedAddress, String wardName, String districtName, String provinceName) {
        this.addressID = addressID;
        this.detailedAddress = detailedAddress;
        this.wardName = wardName;
        this.districtName = districtName;
        this.provinceName = provinceName;
    }


    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
