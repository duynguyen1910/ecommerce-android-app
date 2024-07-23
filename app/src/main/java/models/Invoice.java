package models;

import java.io.Serializable;

public class Invoice implements Serializable {
    private String invoiceID;
    private String deliveryAddress;
    private String createdDate;
    private String paidDate;
    private String giveToDeliveryDate;
    private String completedDate;
    private double total;
    private String note;
    private int paymentMethod; // 0: Thanh toán khi nhận hàng
    private int invoiceStatus; // 0: Chờ xác nhận
    private CartItem cartItem;
    private String customerID;
    private String customerName;

    public Invoice(String deliveryAddress, String createdDate, String paidDate, String giveToDeliveryDate, String completedDate, double total, String note, int paymentMethod, int invoiceStatus, CartItem cartItem, String customerID,String customerName) {
        this.deliveryAddress = deliveryAddress;
        this.createdDate = createdDate;
        this.paidDate = paidDate;
        this.giveToDeliveryDate = giveToDeliveryDate;
        this.completedDate = completedDate;
        this.total = total;
        this.note = note;
        this.paymentMethod = paymentMethod;
        this.invoiceStatus = invoiceStatus;
        this.cartItem = cartItem;
        this.customerID = customerID;

    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotalPayment(int totalPayment) {
        this.total = totalPayment;
    }

    public int getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(int invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }



}
