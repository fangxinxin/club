package db_admin.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import db_admin.model.SysAreaModel;
import dsqp.db.db.DBHelper;
import dsqp.db.model.DataTable;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by ds on 2017/7/31.
 */
public class SysAreaDaoImpl {
    private static final String CONNECTION = "admin";

    public SysAreaModel getOne(int id, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from sys_area where id = ? and available = ?;");
        db.addParameter(id);
        db.addParameter(available);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(SysAreaModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public SysAreaModel getByGameId(int gameId, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from sys_area where gameId = ? and available = ?;");
        db.addParameter(gameId);
        db.addParameter(available);
        DataTable dt = db.executeQuery();
        if (dt.rows.length > 0) {
            return DBUtils.convert2Model(SysAreaModel.class, dt.rows[0]);
        } else {
            return null;
        }
    }

    public int add(String area, int gameId, int parentId, String name) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand(
                "INSERT INTO sys_area " +
                "(`area`, `gameId`, `parentId`, `parentIds`, `available`, `name`, `disabled`) " +
                "VALUES " +
                "(?,?,?,0,1,?,0)"
        );

        db.addParameter(area);
        db.addParameter(gameId);
        db.addParameter(parentId);
        db.addParameter(name);

        return db.executeNonQuery();
    }

    public int update(int id, String area, String name) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(64);
        sb.append("UPDATE sys_area ");
        if (!Strings.isNullOrEmpty(area)) {
            sb.append("SET area = ? ");
            db.addParameter(area);
        }
        if (!Strings.isNullOrEmpty(name)) {
            sb.append(", name = ? ");
            db.addParameter(name);
        }

        sb.append("WHERE id = ?;");
        db.addParameter(id);
        db.createCommand(sb.toString());

        return db.executeNonQuery();
    }

    public DataTable listAreas(Set<String> areaIds, boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuilder sb = new StringBuilder(128);
        sb.append("SELECT *, (SELECT name FROM sys_area WHERE id = t.parentId)preName FROM sys_area t WHERE available = ? AND id in ")
                .append(SqlUtils.buildInConditions(areaIds.size()));

        db.addParameter(available);
        for (String id : areaIds) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());

        return db.executeQuery();
    }

    public DataTable listAreas(boolean available) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT *, (SELECT name FROM sys_area WHERE id = t.parentId)preName FROM sys_area t WHERE available = ?;");
        db.addParameter(available);

        return db.executeQuery();
    }

    public int delete(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("DELETE FROM sys_area WHERE gameId = ?;");
        db.addParameter(gameId);

        return db.executeNonQuery();
    }

    public List<String> getAllIds(String areaIds) {
        DBHelper db = new DBHelper(CONNECTION);

        List<String> s = Lists.newArrayList(areaIds.split(","));

        StringBuilder sb = new StringBuilder(128);
        sb.append("SELECT DISTINCT parentIds FROM sys_area WHERE id in ")
                .append(SqlUtils.buildInConditions(s.size()));

        for (String id : s) {
            db.addParameter(id);
        }

        db.createCommand(sb.toString());
        DataTable dt = db.executeQuery();

        return dt.rows.length>0 ? DBUtils.convert2List(String.class, "parentIds", dt) : null;
    }
}
