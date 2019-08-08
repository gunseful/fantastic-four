package app.entities.products;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    private List<Product> basket;

    public Basket() {
        this.basket = new ArrayList<>();
    }

    public Basket(List<Product> basket) {
        this.basket = basket;
    }

    public List<Product> getList() {
        return basket;
    }

    public void setBasket(List<Product> basket) {
        this.basket = basket;
    }

    public void add(Product product){
        this.basket.add(product);
    }

    @Override
    public String toString() {
        String s="";
        for(Product product : basket){
            s+=product.getId()+" ";
        }
        return s;
    }
}
