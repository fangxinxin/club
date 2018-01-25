package dsqp.db_club_dict.impl;

import com.google.common.base.Strings;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictGoodPriceModel;

/**
 * Created by Aris on 2017/7/21.
 */
public class DictGoodPriceDaoImpl implements dsqp.db.service.BaseDao<DictGoodPriceModel> {

    private static final String CONNECTION = "club_dict";

    public int add(DictGoodPriceModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "INSERT INTO dict_good_price (" +
                "  gameId," +
                "  goodName," +
                "  cashPrice," +
                "  nonCashDiscount," +
                "  goodNum," +
                "  giftNum," +
                "  type," +
                "  isEnable," +
                "  remark" +
                ") " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        db.createCommand(sql, DBCommandType.Text, true);

        DBUtils.addSqlParameters(db, model);

        int result = db.executeNonQuery();
        model.setId(db.generatedValue);
        return result;
    }

    public int update(DictGoodPriceModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);

        if (Strings.isNullOrEmpty(model.getGoodName())){
            sb.append(",goodName=?");
            db.addParameter(model.getGoodName());
        }

        if (model.getCashPrice() != 0){
            sb.append(",cashPrice=?");
            db.addParameter(model.getCashPrice());
        }

        if (model.getNonCashDiscount() != 0){
            sb.append(",nonCashDiscount=?");
            db.addParameter(model.getNonCashDiscount());
        }

        if (model.getGoodNum() != 0){
            sb.append(",goodNum=?");
            db.addParameter(model.getGoodNum());
        }

        sb.append(",giftNum=?");
        db.addParameter(model.getGiftNum());

        if (model.getType() != 0){
            sb.append(",type=?");
            db.addParameter(model.getType());
        }

        if (!Strings.isNullOrEmpty(model.getRemark())){
            sb.append(",remark=?");
            db.addParameter(model.getRemark());
        }

        if (sb.length() == 0){
            return 0;
        }else{
            db.createCommand("UPDATE dict_good_price set " + sb.substring(1) + " where id=?");
            db.addParameter(model.getId());

            return db.executeNonQuery();
        }
    }

    public DictGoodPriceModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_good_price where id = ?");

        db.addParameter(id);
        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? (DBUtils.convert2Model(DictGoodPriceModel.class, dt.rows[0])) : null;
    }

    public DataTable getList() {
        return null;
    }

    public DataTable getListByGameId(int gameId,boolean isEnable,int type) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_good_price where gameId = ? and isEnable = ? and type = ?");

        db.addParameter(gameId);
        db.addParameter(isEnable);
        db.addParameter(type);

        return  db.executeQuery();
    }

    public DataTable getByGameId(int gameId, boolean isEnable,int type){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_good_price where gameId = ? and isEnable = ? and type = ?");

        db.addParameter(gameId);
        db.addParameter(isEnable);
        db.addParameter(type);

        return db.executeQuery();
    }


    public DictGoodPriceModel getByGameIdAndType(int gameId, boolean isEnable,int type){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_good_price where gameId = ? and isEnable = ? and type = ?");

        db.addParameter(gameId);
        db.addParameter(isEnable);
        db.addParameter(type);

        DataTable dt = db.executeQuery();
        return dt.rows.length>0 ? (DBUtils.convert2Model(DictGoodPriceModel.class, dt.rows[0])) : null;
    }

    public int deleteById(int id){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("delete from dict_good_price where id = ?");

        db.addParameter(id);

        return db.executeNonQuery();
    }



}
