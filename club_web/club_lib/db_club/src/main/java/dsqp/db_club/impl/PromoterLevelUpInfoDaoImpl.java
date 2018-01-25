package dsqp.db_club.impl;

import com.google.common.base.Strings;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db_club.model.PromoterLevelUpInfoModel;

/**
 * Created by jeremy on 2017/7/27.
 */
public class PromoterLevelUpInfoDaoImpl implements BaseDao<PromoterLevelUpInfoModel> {

    private static final String CONNECTION = "club";

    public int add(PromoterLevelUpInfoModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO promoter_levelup_info (" +
                "  gameId," +
                "  promoterId," +
                "  pLevel," +
                "  levelUpType," +
                "  createTime" +
                ") VALUES(?,?,?,?,?)";

        db.createCommand(sql, DBCommandType.Text, true);
        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(PromoterLevelUpInfoModel promoterLevelUpInfoModel) {
        return 0;
    }

    public PromoterLevelUpInfoModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getLatestLevelUpTimeByPromoterIdAndType(long promoterId,int levelUpType){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM promoter_levelup_info WHERE promoterId =? and levelUpType = ? ORDER BY createTime DESC LIMIT ?,?;");

        db.addParameter(promoterId);
        db.addParameter(levelUpType);
        db.addParameter(0);
        db.addParameter(1);
        return db.executeQuery();
    }

}
