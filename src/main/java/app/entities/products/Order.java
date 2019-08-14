package app.entities.products;

import app.entities.user.User;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private int id;
    private LocalDate creationDate;
    private List<Product> products;
    private boolean isPaid=false;
    private int customerID;
    private User user;

    public Order() {
    }

    public Order(int id, LocalDate creationDate, List<Product> products) {
        this.id = id;
        this.creationDate = creationDate;
        this.products = products;
    }

    public Order(LocalDate creationDate, List<Product> products) {
        this.creationDate = creationDate;
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

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        return "Заказ №"+ id +", создан " + creationDate.toString() +
                "<br> Товары:" + sb.toString()+"<br><br>Итоговая сумма - "+totalPrice()+" тенге.<br><br>"+s+"<br>";
    }

    public int totalPrice(){
        int total = 0;
        for(Product product : this.products){
            total += product.getPrice();
        }
        return total;
    }
}
