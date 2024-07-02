package Models;

import java.io.Serializable;
import java.util.ArrayList;

public class CartItem implements Serializable {

    private String storeName;
    ArrayList<Product> listProducts;
//    Product(String title, String description, ArrayList<String> picUrl, double price, double oldPrice, double rating, int sold, int saleoff, int numberInCart, int storeID)

    public CartItem(String storeName, ArrayList<Product> listProducts) {
        this.storeName = storeName;
        this.listProducts = listProducts;
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
