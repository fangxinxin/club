package db_admin.dao;


import db_admin.impl.SysAreaDaoImpl;
import db_admin.model.SysAreaModel;
import dsqp.db.model.DataTable;

import java.util.List;
import java.util.Set;

/**
 * Created by ds on 2017/6/21.
 */
public class SysAreaDao {

    private static final SysAreaDaoImpl impl = new SysAreaDaoImpl();

    public static SysAreaModel getOne(int id, boolean available) {
        return impl.getOne(id, available);
    }

    public static SysAreaModel getByGameId(int gameId, boolean available) {
        return impl.getByGameId(gameId, available);
    }

    public static int add(String area, int gameId, int parentId, String name){
        return impl.add(area, gameId, parentId, name);
    }

    public static int update(int id, String area, String name){
        return impl.update(id, area, name);
    }

    public static int delete(int gameId) {
        return impl.delete(gameId);
    }

    public static DataTable listAreas(Set<String> areaIds, boolean available) {
        return impl.listAreas(areaIds, available);
    }

    public static DataTable listAreas(boolean available) {
        return impl.listAreas(available);
    }


    //获取父节点
    public static List<String> getAllIds(String menuIds) {
        return impl.getAllIds(menuIds);
    }



}
