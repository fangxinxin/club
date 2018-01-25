package admin.service;


public interface SysCfgPowerService extends BaseService {

    int cfgPower(int roleId, String areaIds, String menuIds, String permissionIds);

    int delete(int roleId);

}
