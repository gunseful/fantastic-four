package app.model.products;

import app.enums.States;
import app.model.user.User;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private int id;
    private Date creationDate;
    private List<Product> products;
    private int customerId;
    private User user;
    private String state;


    public Order() {
    }

    public int getId() {
        return id;
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

    public boolean isPaid() {
        return state.equals(States.PAID.name());
    }

    public int totalPrice(){
        int total = 0;
        for(Product product : this.products){
            total += product.getPrice()*product.getCount();
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                customerId == order.customerId &&
                Objects.equals(creationDate, order.creationDate) &&
                Objects.equals(products, order.products) &&
                Objects.equals(user, order.user) &&
                Objects.equals(state, order.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, products, customerId, user, state);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Product product : products){
            sb.append("<br>").append(product.toString());
        }
        String s;
        if(state.equals(States.PAID.name())){
            s = "Оплачено";
        }else{
            s = "Неоплачено";
        }
        return "Заказ №"+ id +", создан " + creationDate.toString() +
                "<br> Товары:" + sb.toString()+"<br><br>Итоговая сумма - "+totalPrice()+" тенге.<br><br>"+s+"<br>";
    }

}
