package dsqp.db_club_dict.dao;

import dsqp.db_club_dict.impl.DictPayGatewayDaoImpl;
import dsqp.db_club_dict.model.DictPayGatewayModel;

/**
 * Created by mj on 2017/7/22.
 */
public class DictPayGatewayDao {
    private static final DictPayGatewayDaoImpl impl = new DictPayGatewayDaoImpl();

    public static DictPayGatewayModel getByClassName(String gatewayClass) {
        return impl.getByClassName(gatewayClass);
    }
}
