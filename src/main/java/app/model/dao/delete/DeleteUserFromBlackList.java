package app.model.dao.delete;

import app.model.dao.abstraction.AbstractDao;

public class DeleteUserFromBlackList extends AbstractDao {

    public DeleteUserFromBlackList(int id) {
        sql = "UPDATE Users SET IS_BLOCKED = FALSE WHERE id = " + id;
    }
}
