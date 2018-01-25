package dsqp.db_club.dao;

import dsqp.db_club.impl.PromoterDelDaoImpl;
import dsqp.db_club.model.PromoterModel;

/**
 * Created by ds on 2017/10/13.
 */
public class PromoterDelDao {

    private static final PromoterDelDaoImpl impl = new PromoterDelDaoImpl();

    public static int add(PromoterModel model, long parentCellPhone) {
        return impl.add(model, parentCellPhone);
    }

}
