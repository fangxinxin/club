package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.NoticeDaoImpl;
import dsqp.db_club.model.NoticeModel;

import java.util.List;

/**
 * Created by jeremy on 2017/7/21.
 */
public class NoticeDao {
    private static final NoticeDaoImpl impl = new NoticeDaoImpl();

    public static int add(NoticeModel model) {
        return impl.add(model);
    }

//    public static int update(NoticeModel model) {
//        return impl.update(model);
//    }

    public static NoticeModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static boolean hasNewNotice(long promoterId, boolean isRead){
        return  impl.hasNewNotice(promoterId, isRead);
    }

    public static DataTable findNoticeListByGameIdAndPromoterId(long gameId,long promoterId){
        return  impl.findNoticeListByGameIdAndPromoterId(gameId,promoterId);
    }

    public static int deleteById(long id){
        return impl.deleteById(id);
    }

    public static int deleteNoticeByListId(List listId){
        return impl.deleteNoticeByListId(listId);
    }

}
