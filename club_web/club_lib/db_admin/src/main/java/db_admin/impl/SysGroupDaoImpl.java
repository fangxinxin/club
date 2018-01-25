package db_admin.impl;

import com.google.common.base.Strings;
import db_admin.model.SysGroupModel;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;

/**
 * Created by ds on 2017/9/19.
 */
public class SysGroupDaoImpl {
    private static final String CONNECTION = "admin";

    public SysGroupModel getOne(int id, boolean available){
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM sys_group WHERE id = ? AND available = ?;");
        db.addParameter(id);
        db.addParameter(available);

        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? DBUtils.convert2Model(SysGroupModel.class, dt.rows[0]) :null;
    }


    public DataTable listGroup(boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT * FROM sys_group WHERE available = ? ORDER BY priority;");
        db.addParameter(available);

        return db.executeQuery();
    }

    public int add(String name, int priority) {
        DBHelper db = new DBHelper(CONNECTION);

        SysGroupModel model = new SysGroupModel();
        model.setAvailable(true);
        model.setName(name);
        model.setPriority(priority);

        db.createCommand("INSERT INTO sys_group VALUES(null,?,?,?)");
        DBUtils.addSqlParameters(db, model);

        return db.executeNonQuery();
    }

    public int update(int id, String name, int priority) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(64);
        sb.append("UPDATE sys_group SET ");
        if (!Strings.isNullOrEmpty(name)) {
            sb.append("name = ? ");
            db.addParameter(name);
        }
        if (priority != 0) {
            if(!Strings.isNullOrEmpty(name)) {
                sb.append(", ");
            }
            sb.append("priority = ? ");
            db.addParameter(priority);
        }

        sb.append("WHERE id = ?;");
        db.addParameter(id);
        db.createCommand(sb.toString());

        return db.executeNonQuery();
    }

//    public int delete(long groupId) {
//        DBHelper db = new DBHelper(CONNECTION);
//
//        db.createCommand("delete from sys_group where id = ?;");
//        db.addParameter(groupId);
//
//        return db.executeNonQuery();
//    }

}
