package app.locale;

import java.util.ListResourceBundle;

public class Language_ru extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        
        return resources;
    }

    private final Object[][] resources = {

            { "welcome", "Добро пожаловать в наш уютный интернет-магазин White Dragon!<br>" +
                    "\n" +
                    "чтобы продолжить вам необходимо авторизоваться" },
            { "loggin.title", "Вход в систему" },
            { "listClient.productlist", "Это товары нашего магазина" },
            { "log.in", "Войти" },
            { "registration", "Регистрация" },
            { "user", "Пользователь" },
            { "inblacklist", "заблокирован, так как делал заказы и не оплачивал" },
            { "loggin", "Логин" },
            { "password", "Пароль" },
            { "nickname", "Никнейм" },
            { "name", "Имя" },

            { "nodata", "Введенные данные неверны" },
            { "registration.fail", "Введенные данные не верны, либо пользователь с таким ником уже существует <br> Правильно введеные данные - пароль от 6 до 15 символов<br> Имя и Никнейм от 3 до 15 символов" },
            { "blacklist.title", "Черный список" },
            { "entry", "Вы вошли как " },
            { "admin", "Вы Администратор" },
            { "blacklist.hint", "Вы можете разблокировать кого-нибудь из пользователей" },
            { "orders", "Заказы" },
            { "tothestore", "К магазину" },
            { "out", "Выход" },
            { "nulldata", "Вы ничего не выбрали" },
            { "blacklist.delete", "Удалить из черного списка" },
            { "blacklist.empty", "Черный список пуст" },

















    };
}