package app.model.daoFake.add;

import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;

public class AddUserToBlackList extends AbstractDao {

    public AddUserToBlackList(User user) {
        sql = "UPDATE Users SET IS_BLOCKED = TRUE WHERE id = " + user.getId();
    }
}
