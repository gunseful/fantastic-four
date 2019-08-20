import app.entities.user.User;
import app.model.repository.Repository;

//import app.entities.products.Order;
//import app.entities.user.User;
//import app.model.controller.Repository;
//
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //Тестирую многопоточность

            //BlockingQueue
        new Thread(){
            @Override
            public void run() {
                User user = new User();
                user.setId(2);
                Repository repository = new Repository();
                System.out.println(repository.updateBasket(user, 34,true));

            }
        }.start();
    }
}
