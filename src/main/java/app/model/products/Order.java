package app.model.products;

import app.model.user.User;

import java.sql.Date;
import java.util.List;

public class Order {
    private int id;
    private Date creationDate;
    private List<Product> products;
    private boolean isPaid=false;
    private int customerId;
    private User user;
    private String state;


    public Order() {
    }

    public Order(int id, Date creationDate, List<Product> products, boolean isPaid, User user) {
        this.id = id;
        this.creationDate = creationDate;
        this.products = products;
        this.isPaid = isPaid;
        this.user=user;
    }

    public Order(int id, List<Product> products) {
        this.id = id;
        this.products = products;
    }

    public Order(Date creationDate, List<Product> products) {
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Product product : products){
            sb.append("<br>").append(product.toString());
        }
        String s ="";
        if(state.equals("PAID")){
            s = "Оплачено";
        }else{
            s = "Неоплачено";
        }
        return "Заказ №"+ id +", создан " + creationDate.toString() +
                "<br> Товары:" + sb.toString()+"<br><br>Итоговая сумма - "+totalPrice()+" тенге.<br><br>"+s+"<br>";
    }

    public int totalPrice(){
        if(state.equals("PAID")){
            isPaid=true;}
        int total = 0;
        for(Product product : this.products){
            total += product.getPrice()*product.getCount();
        }
        return total;
    }
}
