package pay.dao;

import dsqp.db_club.model.BonusDetailModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Aris on 2017/11/28.
 */
@Repository
public interface BonusDetailDao {
    int add(@Param("obj") BonusDetailModel obj);
}
