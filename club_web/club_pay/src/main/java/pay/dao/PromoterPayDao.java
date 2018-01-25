package pay.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Aris on 2016/11/16.
 */
@Repository
public interface PromoterPayDao {

    /**
     * 修改订单返回的信息
     * @param payOrderId 第三方支付订单号
     * @param id         订单id
     * @return
     */
    int updateSuccessById(
              @Param("payOrderId") String payOrderId
            , @Param("id")         long id);


}
