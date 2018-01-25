package club.service;

import dsqp.db.model.DataTable;

/**
 * Created by jeremy on 2017/7/21.
 */
public interface NoticeService {

    DataTable findNoticeListByGameIdAndPromoterId(long gameId,long promoterId);
}
