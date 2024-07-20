package models;


import java.util.ArrayList;

import api.invoiceApi;
import interfaces.StatusCallback;

public class InvoiceDetail {
    private String invoiceID;
    private String variantID;
    private int quantity;

    public InvoiceDetail(String invoiceID, String variantID, int quantity) {


        this.invoiceID = invoiceID;
        this.variantID = variantID;
        this.quantity = quantity;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getVariantID() {
        return variantID;
    }

    public void setVariantID(String variantID) {
        this.variantID = variantID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}