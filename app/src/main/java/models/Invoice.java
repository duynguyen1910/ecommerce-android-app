package models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import api.invoiceApi;
import enums.OrderStatus;
import interfaces.UpdateDocumentCallback;

public class Invoice extends BaseObject implements Serializable {
    private String customerID;
    private String detailedAddress;
    private String deliveryAddress;
    private double total;
    private OrderStatus status;
    private Timestamp createdAt;
    private Timestamp confirmedAt;
    private Timestamp shippedAt;
    private Timestamp deliveredAt;
    private Timestamp cancelledAt;
    private String cancelledBy;

    private String note;
    private String cancelledReason;
    private String storeID;
    private ArrayList<InvoiceDetail> invoiceItems;
    private invoiceApi invoiceApi;

    public Invoice() {
        this.invoiceApi = new invoiceApi();
    }

    public Invoice(String customerID, String deliveryAddress, double total, OrderStatus status,
                   Timestamp createdAt, String note) {
        this.customerID = customerID;
        this.deliveryAddress = deliveryAddress;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
        this.note = note;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    @Override
    public String getBaseID() {
        return super.baseID;
    }

    @Override
    public void setBaseID(String userID) {
        validateBaseID(userID);

        super.baseID = userID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = OrderStatus.fromInt(status);
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Timestamp confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Timestamp getShippedAt() {
        return shippedAt;
    }

    public void setShippedAt(Timestamp shippedAt) {
        this.shippedAt = shippedAt;
    }

    public Timestamp getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Timestamp deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Timestamp getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Timestamp cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public api.invoiceApi getInvoiceApi() {
        return invoiceApi;
    }

    public void setInvoiceApi(api.invoiceApi invoiceApi) {
        this.invoiceApi = invoiceApi;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<InvoiceDetail> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(ArrayList<InvoiceDetail> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public void updateInvoice(String invoiceID, Map<String, Object> invoiceUpdate,
                              final UpdateDocumentCallback callback) {
        invoiceApi.updateStatusInvoiceApi(invoiceID, invoiceUpdate, callback);
    }
}