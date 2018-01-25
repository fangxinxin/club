package club.service;

import club.vo.PaginationVO;
import dsqp.db_club.model.PromoterPayModel;

import java.util.Date;

/**
 * Created by Aris on 2017/7/26.
 */
public interface PayService {
//    MsgVO buy(long promoterId, int goodId);

    PaginationVO getTotalPayByDate(long promoterId, int pLevel, Date startDate, Date endDate, int pageNum, int pageSize);

    boolean setOrderSuccess(PromoterPayModel order, double totalFee, String payOrderId, int rebate);
}
