import app.entities.products.Order;
import app.entities.user.User;
import app.model.repository.Repository;

import java.util.List;

//import app.entities.products.Order;
//import app.entities.user.User;
//import app.model.controller.Repository;
//
public class Main {

    public static void main(String[] args) throws InterruptedException {
        //Тестирую многопоточность
        Repository r = new Repository();
                        User user = new User();
                user.setId(2);
        System.out.println(r.getOrders(user).get(0).totalPrice());




            //BlockingQueue
//        new Thread(){
//            @Override
//            public void run() {
//                User user = new User();
//                user.setId(2);
//                Repository repository = new Repository();
//                System.out.println(repository.updateBasket(user, 34,true));
//
//            }
//        }.start();
    }

    public boolean containsOrder(final List<Order> list, final int id){
        return list.stream().filter(o -> o.getId()==id).findFirst().isPresent();
    }
}
