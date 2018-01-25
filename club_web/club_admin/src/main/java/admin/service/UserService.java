package admin.service;


public interface UserService extends BaseService {

    int updatePassword(String userName, String md5Password);

    boolean login(String userName, String password);
}
