package pay.service;

import dsqp.db_club.model.PromoterModel;
import dsqp.db_club.model.PromoterPayModel;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付相关服务
 * Created by Aris on 2016/11/17.
 */
public interface PayService {
    @Transactional(rollbackFor = Exception.class)
    boolean setOrderSuccess(PromoterPayModel order, double totalFee, String payOrderId, PromoterModel promoter);
}
