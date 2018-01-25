package club.service.impl;

import club.service.NoticeService;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.NoticeDao;
import org.springframework.stereotype.Service;

/**
 * Created by jeremy on 2017/7/21.
 */
@Service
public class NoticeServiceImpl implements NoticeService {


    @Override
    public DataTable findNoticeListByGameIdAndPromoterId(long gameId,long promoterId) {
        return NoticeDao.findNoticeListByGameIdAndPromoterId(gameId,promoterId);
    }



}
