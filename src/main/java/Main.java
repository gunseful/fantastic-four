import app.entities.products.Product;
import app.model.controller.Repository;

//import app.entities.products.Order;
//import app.entities.user.User;
//import app.model.controller.Repository;
//
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //Тестирую многопоточность

            //Semaphore:
//        new Thread() {
//            @Override
//            public void run() {
//                Repository repository = new Repository();
//                System.out.println(repository.getListSemaphore());
//            }
//        }.start();
//
//        new Thread() {
//            @Override
//            public void run() {
//                Repository repository = new Repository();
//                System.out.println(repository.getListSemaphore());
//            }
//        }.start();

            //BlockingQueue
        new Thread(){
            @Override
            public void run() {
                Repository repository = new Repository();
                System.out.println(repository.getList());
            }
        }.start();


        new Thread(){
            @Override
            public void run() {
                Repository repository = new Repository();
                repository.add(new Product("Хрень", 100));
            }
        }.start();


        new Thread(){
            @Override
            public void run() {
                Repository repository = new Repository();
                System.out.println(repository.getList());
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                Repository repository = new Repository();
                repository.add(new Product("Хрень", 100));
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                Repository repository = new Repository();
                repository.add(new Product("Хрень", 100));
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                Repository repository = new Repository();
                System.out.println(repository.getList());
            }
        }.start();
    }
}
