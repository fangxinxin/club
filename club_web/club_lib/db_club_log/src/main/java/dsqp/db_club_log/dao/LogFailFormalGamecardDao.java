package dsqp.db_club_log.dao;

import dsqp.db.db.DBHelper;
import dsqp.db_club_log.model.LogFailFormalGamecardModel;

/**
 * Created by ds on 2018/1/22.
 */

public class LogFailFormalGamecardDao {
    private static final LogFailFormalGamecardDaoImpl impl = new LogFailFormalGamecardDaoImpl();


    public static int add(LogFailFormalGamecardModel model) {
        return impl.add(model);
    }

}

class LogFailFormalGamecardDaoImpl {

    private static final String CONNECTION = "club_log";

    public int add(LogFailFormalGamecardModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("INSERT INTO log_fail_formal_gamecard" +
                " (gameId, cellphone, gameUserId, gameNickName, gameCard, createTime, createDate ) " +
                " VALUES(?,?,?,?,?,?,?);");
        db.addParameter(model.getGameId());
        db.addParameter(model.getCellphone());
        db.addParameter(model.getGameUserId());
        db.addParameter(model.getGameNickName());
        db.addParameter(model.getGameCard());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());
        return db.executeNonQuery();
    }


}
