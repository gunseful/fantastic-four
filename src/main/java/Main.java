import app.entities.user.User;
import app.model.dao.check.CheckLogginAndPassword;

//import app.entities.products.Order;
//import app.entities.user.User;
//import app.model.controller.Repository;
//
public class Main {

    public static void main(String[] args) throws InterruptedException {
        //Тест
        User user = new User("Илья","GUNSEFUL","123321");
        System.out.println(new CheckLogginAndPassword(user).start());

//        AbstractDao getList = new GetList();
//        System.out.println(getList.start());




    }

}
