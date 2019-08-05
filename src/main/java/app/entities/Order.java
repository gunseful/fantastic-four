package app.entities;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private int id;
    private LocalDate localDate;
    private List<Product> products;
    private boolean isPaid=false;


    public Order(int id, LocalDate localDate, List<Product> products) {
        this.id = id;
        this.localDate = localDate;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Product product : products){
            sb.append("<br>"+product.toString());
        }
        String s ="";
        if(isPaid){
            s = "Оплачено";
        }else{
            s = "Неоплачено";

        }
        return "Заказ №"+ id +", создан " + localDate.toString() +
                "<br> Товары:" + sb.toString()+"<br>"+s;
    }
}
