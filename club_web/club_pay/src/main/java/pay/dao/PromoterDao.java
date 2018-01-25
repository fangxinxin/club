package pay.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Aris on 2016/11/16.
 */
@Repository
public interface PromoterDao {
    /**
     * 代理商钻石充值成功后更新财产信息
     */
    int updateForPay(@Param("gameCard") int gameCard, @Param("price") double price, @Param("id") long id);

    /**
     * 更新返利信息
     */
    int updateForRebate(@Param("rebate") int rebate, @Param("id") long id);
}
