package Models;

import java.io.Serializable;

public class Invoice implements Serializable {
    private int orderID;
    private String deliveryAddress;
    private String createdDate;
    private String paidDate;
    private String giveToDeliveryDate;
    private String completedDate;
    private double totalPayment;
    private String note;
    private int paymentMethod; // 0: Thanh toán khi nhận hàng
    private int orderStatus; // 0: Chờ xác nhận
    private CartItem cartItem;
    private int customerID;

    public Invoice(String deliveryAddress, String createdDate, String paidDate, String giveToDeliveryDate, String completedDate, double totalPayment, String note, int paymentMethod, int orderStatus, CartItem cartItem, int customerID) {
        this.deliveryAddress = deliveryAddress;
        this.createdDate = createdDate;
        this.paidDate = paidDate;
        this.giveToDeliveryDate = giveToDeliveryDate;
        this.completedDate = completedDate;
        this.totalPayment = totalPayment;
        this.note = note;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.cartItem = cartItem;
        this.customerID = customerID;

    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
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

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getGiveToDeliveryDate() {
        return giveToDeliveryDate;
    }

    public void setGiveToDeliveryDate(String giveToDeliveryDate) {
        this.giveToDeliveryDate = giveToDeliveryDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }



}
