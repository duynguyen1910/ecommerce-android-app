package models;

public class InvoiceDetail {
    private String invoiceID;
    private String variantID;
    private String variantName;
    private String productName;
    private double newPrice;
    private double oldPrice;
    private int quantity;
    private String storeID;
    private String productImage;
    private String productID;
    public static int ITEM_TO_DISPLAY = 1;

    public InvoiceDetail() {
    }

    public InvoiceDetail(String invoiceID, String variantID, int quantity) {
        this.invoiceID = invoiceID;
        this.variantID = variantID;
        this.quantity = quantity;
    }

    public InvoiceDetail(String invoiceID, String variantID, String productID, String productName, int quantity) {
        this.invoiceID = invoiceID;
        this.variantID = variantID;
        this.quantity = quantity;
        this.productID = productID;
        this.productName = productName;
    }

    public InvoiceDetail(String invoiceID, String productID, String variantID, String productName, double newPrice, double oldPrice, int quantity) {
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.variantID = variantID;
        this.productName = productName;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }
}