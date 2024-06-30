package Models;

import java.util.ArrayList;

public class CartItem {
    private int cartItemID;
    private String storeName;
    ArrayList<Product> listProducts;
//    Product(String title, String description, ArrayList<String> picUrl, double price, double oldPrice, double rating, int sold, int saleoff, int numberInCart, int storeID)
    public CartItem(int cartItemID, String storeName, ArrayList<Product> listProducts) {
        this.cartItemID = cartItemID;
        this.storeName = storeName;
        this.listProducts = listProducts;
    }

    public int getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public ArrayList<Product> getListProducts() {
        return listProducts;
    }

    public void setListProducts(ArrayList<Product> listProducts) {
        this.listProducts = listProducts;
    }
}
