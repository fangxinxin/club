package admin.service;


import dsqp.db.model.DataTable;

import java.util.List;

public interface SysAuthService extends BaseService {

    DataTable getGames(int roleId);

    String getRole(long userId);

    List<String> listPermissions(int roleId);

    List<String> listAreas(int roleId);

    DataTable listRootMenus(int roleId);

    DataTable listCurrentMenus(int roleId, String menu);

}
