package dsqp.db_club_dict.dao;


import dsqp.db.model.DataTable;
import dsqp.db_club_dict.impl.DictGoodPriceDaoImpl;
import dsqp.db_club_dict.model.DictGoodPriceModel;

/**
 * Created by Aris on 2017/7/21.
 */
public class DictGoodPriceDao {

    private static final DictGoodPriceDaoImpl impl = new DictGoodPriceDaoImpl();

    public static int add(DictGoodPriceModel model) {
        return impl.add(model);
    }

    public static int update(DictGoodPriceModel model) {
        return impl.update(model);
    }

    public static DictGoodPriceModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getListByGameId(int gameId,boolean isEnable,int type) {
        return impl.getListByGameId(gameId,isEnable,type);
    }

    public static DataTable getByGameId(int gameId, boolean isEnable,int type){
        return impl.getByGameId(gameId, isEnable, type);
    }

    public static DictGoodPriceModel getByGameIdAndType(int gameId, boolean isEnable,int type){
        return impl.getByGameIdAndType(gameId,isEnable,type);
    }

    public static int deleteById(int id) {
        return impl.deleteById(id);
    }

}
