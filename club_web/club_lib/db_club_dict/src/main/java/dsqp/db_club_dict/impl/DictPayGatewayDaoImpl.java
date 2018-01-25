package dsqp.db_club_dict.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db_club_dict.model.DictPayGatewayModel;

/**
 * Created by mj on 2017/7/22.
 */
public class DictPayGatewayDaoImpl implements BaseDao<DictPayGatewayModel> {
    private static final String CONNECTION = "club_dict";

    public int add(DictPayGatewayModel model) {
        return 0;
    }

    public int update(DictPayGatewayModel model) {
        return 0;
    }

    public DictPayGatewayModel getOne(long id) {
        return null;
    }

    public DataTable getList() {
        return null;
    }

    public DictPayGatewayModel getByClassName(String gatewayClass) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from dict_pay_gateway where gatewayClass = ?");
        db.addParameter(gatewayClass);
        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? (DBUtils.convert2Model(DictPayGatewayModel.class, dt.rows[0])) : null;
    }
}
