package dsqp.db_club_dict.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club_dict.impl.DictGameDaoImpl;
import dsqp.db_club_dict.model.DictGameModel;

import java.util.List;

/**
 * Created by mj on 2017/7/22.
 */
public class DictGameDao {
    private static final DictGameDaoImpl impl = new DictGameDaoImpl();

    public static void add(DictGameModel model) {
        impl.add(model);
    }

    public static void update(DictGameModel model) {
        impl.update(model);
    }

    public static DictGameModel getOne(int id) {
        return impl.getOne(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static DataTable queryByGameId(int gameId) {
        return impl.queryByGameId(gameId);
    }

    /** 获取游戏的【前缀名称: 66、来来】和【游戏信息】**/
    public static DataTable getByGameId(int gameId) {
        return impl.getByGameId(gameId);
    }

    /**获取游戏列表**/
    public static DataTable listByGameIds(List<Long> gameIds) {
        return impl.listByGameIds(gameIds);
    }

    //查游戏属于来来还是66
    public static String queryRemarkByGameId(int gameId) {
        return impl.queryRemarkByGameId(gameId);
    }

    public static DataTable getListByDepth(int depth, boolean isEnable) {
        return impl.getListByDepth(depth, isEnable);
    }

    //从合服的库读取数据
    public static DataTable getUserInfoByUserId(long userId){
        return impl.getUserInfoByUserId( userId);
    }

    public static DataTable getUserInfoByNickName(String nickName){
        return impl.getUserInfoByNickName(nickName);
    }

    //从合服的库读取钻石数据
    public static int getPointByUserId(long userId){
        return impl.getPointByUserId( userId);
    }

    public static DataTable getDTPointByUserId(long userId){
        return impl.getDTPointByUserId(userId);
    }

    public static int getPointByNickName(String nickName){
        return impl.getPointByNickName( nickName);
    }

    public static DataTable getDTPointByNickName(String nickName){
        return impl.getDTPointByNickName(nickName);
    }

    //从合服的库读取兑换券数据
    public static int getPaperByUserId(long userId){
        return impl.getPaperByUserId(userId);
    }

    public static DataTable getDTPaperByUserId(long userId){
        return impl.getDTPaperByUserId(userId);
    }

    public static int getPaperByNickName(String nickName){
        return impl.getPaperByNickName(nickName);
    }

    public static DataTable getDTPaperByNickName(String nickName){
        return impl.getDTPaperByNickName(nickName);
    }

    public static DataTable getPropsLogByUserId(long userId){
        return impl.getPropsLogByUserId(userId);
    }

    public static DataTable getPropsLogByNickName(String nickName){
        return impl.getPropsLogByNickName(nickName);
    }
}
