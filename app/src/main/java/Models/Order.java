package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private int orderID;
    private String createdDate;
    private String note;
    private String deliveryAddress;
    private int paymentMethod;
    private int status;
    private int customerID;
    private ArrayList<CartItem> listProducts;

    public Order(int orderID, String createdDate, String note, String deliveryAddress, int paymentMethod, int status, int customerID, ArrayList<CartItem> listProducts) {
        this.orderID = orderID;
        this.createdDate = createdDate;
        this.note = note;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.customerID = customerID;
        this.listProducts = listProducts;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public ArrayList<CartItem> getListProducts() {
        return listProducts;
    }

    public void setListProducts(ArrayList<CartItem> listProducts) {
        this.listProducts = listProducts;
    }
}
