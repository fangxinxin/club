package dsqp.db_club.dao;

import dsqp.db.model.DataTable;
import dsqp.db_club.impl.NoticeDetailDaoImpl;
import dsqp.db_club.model.NoticeDetailModel;

import java.util.List;

/**
 * Created by jeremy on 2017/7/21.
 */
public class NoticeDetailDao {
    private static final NoticeDetailDaoImpl impl = new NoticeDetailDaoImpl();

    public static int add(NoticeDetailModel model) {
        return impl.add(model);
    }

    public static NoticeDetailModel getOne(long id) {
        return impl.getOne(id);
    }

    public static DataTable getList() {
        return impl.getList();
    }

    public static int deleteById(long id){
        return impl.deleteById(id);
    }

    public static int updateIsReadById(long id){
        return impl.updateIsReadById(id);
    }

    public static DataTable getNoticeIsReadInfo(long promoterId,int isRead){
        return impl.getNoticeIsReadInfo(promoterId,isRead);
    }

    public static DataTable getNoticeNumsById(long promoterId){
        return impl.getNoticeNumsById(promoterId);
    }

    public static DataTable getNoticeList2Delete(long promoterId,int index,int nums){
        return impl.getNoticeList2Delete(promoterId,index,nums);
    }

    public static int deleteNoticeDetailByListId(List listId){
        return impl.deleteNoticeDetailByListId(listId);
    }
}
