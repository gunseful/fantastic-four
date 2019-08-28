package app.controller.locale;

import java.util.ListResourceBundle;

public class Language_en extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {

        return resources;
    }

    private final Object[][] resources = {

            { "welcome", "Welcome to our cozy store White Dragon <br> to continue you have to log in " },
            { "loggin.title", "Entry the system" },
            { "list.client.productlist", "This is the product list" },
            { "log.in", "Log in" },
            { "registration", "Registration" },
            { "user", "User" },
            { "inblacklist", "was blocked cuz make order and didnt pay" },
            { "loggin", "Loggin" },
            { "password", "Password" },
            { "nickname", "Nickname" },
            { "name", "Name" },
            { "registration.fail", "The entered data is incorrect or user with this nickname is already exist<br> Correct data - password from 6 to 15 symbols<br> Name and Nickname from 3 to 15 symbols" },
            { "nodata", "The entered data is incorrect" },
            { "blacklist.title", "Black List" },
            { "entry", "You are logged in as" },
            { "admin", "You are Administrator" },
            { "blacklist.hint", "You can unblock users" },
            { "orders", "Orders" },
            { "tothestore", "To The Store" },
            { "out", "Out" },
            { "nulldata", "You chose nothing" },
            { "blacklist.delete", "Delete from BlackList" },
            { "blacklist.empty", "Black list is empty" },
            { "admin.list.title", "Product list" },
            { "admin.list.hint", "You can add or delete products to/from the list" },
            { "admin.list.no.products", "No products now" },
            { "admin.list.add.product", "Add new product to the list" },
            { "price", "Price" },
            { "nomination", "Nomination" },
            { "add", "Add" },
            { "delete", "Delete" },
            { "exchange.rates", 0.00256 },
            { "currency", "$" },
            { "basket.title", "Your basket" },
            { "basket.empty", "Your basket is empty right now" },
            { "basket.get.order", "Get Order" },
            { "add.to.the.basket", "Add to the Basket"},
            { "orders.hint", "You can delete orders and block the users"},
            { "orders.admin", "Orders"},
            { "orders.client", "Your orders"},
            { "order.admin", "Orders"},
            { "order.client", "Your orders"},
            { "orders.pay", "pay"},
            { "orders.client.title", "Client"},
            { "orders.client.blocked", "THIS CLIENT WAS BLOCKED"},
            { "orders.client.block", "Block this client"},
            { "orders.empty", "No orders"},
            { "orders.order", "Order №"},
            { "orders.creation", "created at"},
            { "orders.paid", "Paid"},
            { "orders.notpaid", "Not paid"},
            { "orders.total.price", "Total price - "},
            { "count", "Count: "},



































    };
}