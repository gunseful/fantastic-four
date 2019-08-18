import app.entities.products.Product;
import app.model.controller.Repository;

//import app.entities.products.Order;
//import app.entities.user.User;
//import app.model.controller.Repository;
//
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //Тестирую многопоточность

        new Thread(){
            @Override
            public void run() {
                Repository hello = new Repository();
                System.out.println(hello.getList());
            }
        }.start();


        new Thread(){
            @Override
            public void run() {
                Repository hello = new Repository();
                Product product = new Product("Хрень", 100);
                hello.add(product);
            }
        }.start();


        new Thread(){
            @Override
            public void run() {
                Repository hello = new Repository();
                System.out.println(hello.getList());
            }
        }.start();




        new Thread(){
            @Override
            public void run() {
                Repository hello = new Repository();
                Product product = new Product("Хрень", 100);
                hello.add(product);
            }
        }.start();



        new Thread(){
            @Override
            public void run() {
                Repository hello = new Repository();
                Product product = new Product("Хрень", 100);
                hello.add(product);
            }
        }.start();


        new Thread(){
            @Override
            public void run() {
                Repository hello = new Repository();
                System.out.println(hello.getList());
            }
        }.start();



    }
}
