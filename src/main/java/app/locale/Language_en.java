package app.locale;

import java.util.ListResourceBundle;

public class Language_en extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {

        return resources;
    }

    private final Object[][] resources = {

            { "welcome", "Welcome to our cozy store White Dragon <br> to continue you have to log in " },
            { "loggin.title", "Entry the system" },
            { "listClient.productlist", "This is the product list" },
            { "Currency", "EUR" },
            { "log.in", "Log in" },
            { "registration", "Registration" },
            { "user", "User" },
            { "inblacklist", "was blocked cuz make order and didnt pay" },
            { "loggin", "Loggin" },
            { "password", "Password" },
            { "nickname", "Nickname" },
            { "name", "Name" },
            { "fail", "The entered data is incorrect or user with this nickname is already exist<br> Correct data - password from 6 to 15 symbols<br> Name and Nickname from 3 to 15 symbols" },
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










    };
}