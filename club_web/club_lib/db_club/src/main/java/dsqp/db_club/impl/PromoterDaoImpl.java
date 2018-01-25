package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db.model.DBCommandType;
import dsqp.db.model.DataTable;
import dsqp.db.model.ParameterDirection;
import dsqp.db.model.SplitPage;
import dsqp.db.service.BaseDao;
import dsqp.db.util.DBUtils;
import dsqp.db.util.SqlUtils;
import dsqp.db_club.model.PromoterModel;
import dsqp.util.CommonUtils;

import java.util.Date;
import java.util.List;

public class PromoterDaoImpl implements BaseDao<PromoterModel> {

    private static final String CONNECTION = "club";

    public int updateStaffWechat(long id, String staffWechat) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET staffWechat= ? WHERE id= ?");
        db.addParameter(staffWechat);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateRemark(long id, String remark) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET remark= ? WHERE id= ?");
        db.addParameter(remark);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateClubId(long id, long clubId) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET clubId= ? WHERE id= ?");
        db.addParameter(clubId);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateAccount(long bankAccount, long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET bankAccount= ? WHERE id= ? ");
        db.addParameter(bankAccount);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateRebateForPay(long id, int gameCard, int rebate, double price) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET gameCard = gameCard + ?, gameCardTotal = gameCardTotal + ?,rebate = rebate + ?,rebateTotal = rebateTotal + ?,totalPay = totalPay + ? WHERE id = ?");
        db.addParameter(gameCard);
        db.addParameter(gameCard);
        db.addParameter(rebate);
        db.addParameter(rebate);
        db.addParameter(price);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateAccountAndRealName(long bankAccount, String realName, long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET bankAccount= ?,realName=? WHERE id= ? ");
        db.addParameter(bankAccount);
        db.addParameter(realName);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateAccountAndBankArea(long bankAccount, String realName, String IDCard, String bankArea, long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET bankAccount= ? , realName=? ,IDCard= ? ,bankArea = ? WHERE id= ? ");
        db.addParameter(bankAccount);
        db.addParameter(realName);
        db.addParameter(IDCard);
        db.addParameter(bankArea);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateDeposit(double deposit, long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET deposit= ? WHERE id= ? ");
        db.addParameter(deposit);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public DataTable queryByCellPhone(long cellPhone) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from promoter where cellPhone = ? and isEnable = true");
        db.addParameter(cellPhone);
        return db.executeQuery();
    }

    public DataTable queryByGameIdAndCellPhone(int gameId, long cellPhone) {

        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from promoter where gameId = ? and cellPhone = ?");
        db.addParameter(gameId);
        db.addParameter(cellPhone);
        return db.executeQuery();
    }


    public DataTable queryByGameIdAndUserId(int gameId, long gameUserId, boolean isEnable) {

        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from promoter where gameId = ? and gameUserId = ? and isEnable= ? ");
        db.addParameter(gameId);
        db.addParameter(gameUserId);
        db.addParameter(isEnable);
        return db.executeQuery();
    }

    public int add(PromoterModel model) {
        return 0;
    }

    public int update(PromoterModel model) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_promoter_update", DBCommandType.Procedure);
        //id要填进去就设置为true
        DBUtils.addSpParameters(db, model, true);

        return db.executeNonQuery();
    }

    public DataTable getList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter");
        return db.executeQuery();
    }

    public PromoterModel getOne(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter where id = ?");
        db.addParameter(id);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(PromoterModel.class, dt.rows[0]) : null;
    }

    public PromoterModel getByClubId(long clubId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter where clubId = ?");
        db.addParameter(clubId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(PromoterModel.class, dt.rows[0]) : null;
    }

    public PromoterModel queryByIdAndGamId(long id, int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter where id = ? and gameId= ? and isEnable = true");
        db.addParameter(id);
        db.addParameter(gameId);

        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2Model(PromoterModel.class, dt.rows[0]) : null;
    }

    public DataTable getOne2(long id) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter where id = ?");
        db.addParameter(id);
        return db.executeQuery();
    }

    public int updatePass(long cellPhone, String pass) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update promoter set loginPassword = ? where cellPhone = ?");
        db.addParameter(pass);
        db.addParameter(cellPhone);
        return db.executeNonQuery();
    }

    public int updatePassById(long promoterId, String pass) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update promoter set loginPassword = ? where id = ?");
        db.addParameter(pass);
        db.addParameter(promoterId);
        return db.executeNonQuery();
    }

    public DataTable listByParentId(long parentId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select * from promoter where parentId = ?;");
        db.addParameter(parentId);
        return db.executeQuery();
    }

    public DataTable getDirectNumsByParentId(long parentId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select count(*) AS nums from promoter where parentId = ?");

        db.addParameter(parentId);

        return db.executeQuery();
    }

    public int updateLevelById(long promoterId, int pLevel) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update promoter set pLevel = ? where id = ?");

        db.addParameter(pLevel);

        db.addParameter(promoterId);

        return db.executeNonQuery();
    }

    public int updateLoginStatus(long cellPhone, int loginStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update promoter set loginStatus= ? where cellPhone = ?");
        db.addParameter(loginStatus);
        db.addParameter(cellPhone);
        return db.executeNonQuery();
    }

    public int updateLoginStatusById(long promoterId, int loginStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("update promoter set loginStatus= ? where id = ?");
        db.addParameter(loginStatus);
        db.addParameter(promoterId);
        return db.executeNonQuery();
    }

    public int updateLoginStatus2More(List<Long> promoterIds, int loginStatus) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("update promoter set loginStatus= ? where id in ")
                .append(SqlUtils.buildInConditions(promoterIds.size()));

        db.addParameter(loginStatus);
        for (long id : promoterIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeNonQuery();
    }

    public DataTable getListByPromoter(List<Long> promoterIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("select id AS promoterId , cellPhone , realName , nickName , createTime , deposit , IDCard , bankArea from promoter where id in ")
                .append(SqlUtils.buildInConditions(promoterIds.size()));

        for (long id : promoterIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeQuery();
    }

    public DataTable listByPromoterIds(List<Long> promoterIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("select * from promoter where id in ")
                .append(SqlUtils.buildInConditions(promoterIds.size()));

        for (long id : promoterIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeQuery();
    }

    public int updateGameCardById(int gameCard, long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET gameCard = gameCard+ ? WHERE id = ?");
        db.addParameter(gameCard);
        db.addParameter(id);
        return db.executeNonQuery();
    }

    public int updateGameCardByIdFromRebate(long id) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET gameCard = gameCard + rebate, rebate = 0 WHERE id = ?;");
        db.addParameter(id);
        return db.executeNonQuery();
    }


    //代理管理
    public DataTable getDirect(long parentId, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT *  FROM promoter WHERE parentId = ? AND id = ? AND loginStatus != 3;");
        db.addParameter(parentId);
        db.addParameter(promoterId);

        return db.executeQuery();
    }

    public DataTable getNonDirect(long parentId, long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand(
                "SELECT * " +
                        "FROM promoter " +
                        "WHERE parentId IN (SELECT id FROM promoter WHERE parentId = ?) AND id = ? AND loginStatus != 3;"
        );
        db.addParameter(parentId);
        db.addParameter(promoterId);

        return db.executeQuery();
    }

    public SplitPage getPageDirect(long parentId, int pageNum, int pageSize) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_promoter_getPage_direct", DBCommandType.Procedure);

        db.addParameter("IN_parentId", parentId);
        db.addParameter("IN_pageNum", pageNum);
        db.addParameter("IN_pageSize", pageSize);
        db.addParameter("OUT_total", ParameterDirection.Output);

        DataTable dt = db.executeQuery();
        //获取记录数
        int totalNum = Integer.parseInt(db.getParamValue("OUT_total"));
        return new SplitPage(pageNum, pageSize, totalNum, dt);
    }

    public SplitPage getPageAllDirect(long parentId, int pageNum, int pageSize) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("CP_promoter_getPage_allDirect", DBCommandType.Procedure);

        db.addParameter("IN_parentId", parentId);
        db.addParameter("IN_pageNum", pageNum);
        db.addParameter("IN_pageSize", pageSize);
        db.addParameter("OUT_total", ParameterDirection.Output);

        DataTable dt = db.executeQuery();
        //获取记录数
        int totalNum = Integer.parseInt(db.getParamValue("OUT_total"));
        return new SplitPage(pageNum, pageSize, totalNum, dt);
    }

    //查询直属代理商列表
    public static DataTable getDirectUnder(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT * ,id AS promoterId FROM promoter WHERE parentId = ? order by createTime desc";
        db.createCommand(sql);
        db.addParameter(promoterId);
        return db.executeQuery();
    }

    //查询直属和非直属代理商列表
    public DataTable getAllUnder(long promoterId) {
        DBHelper db = new DBHelper(CONNECTION);

        String sql = "SELECT t.* FROM (" +
                "SELECT * ,id AS promoterId FROM promoter WHERE parentId = ? " +
                "UNION ALL " +
                "SELECT * ,id AS promoterId FROM promoter WHERE parentId IN (SELECT id FROM promoter WHERE parentId = ? )" +
                ")t order by createTime desc";
        db.createCommand(sql);
        db.addParameter(promoterId);
        db.addParameter(promoterId);
        return db.executeQuery();
    }

    public long newAgent(int gameId, String realName, long cellPhone, String loginPassword, long parentId
            , String nickName, int pLevel, long gameUserId, int loginStatus, long clubId, String clubName, double expireDay, String mpClassName, String editAdmin, long editAdminId, int clubStatus) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("TRAN_newAgent", DBCommandType.Procedure);
        db.addParameter("IN_gameId", gameId);
        db.addParameter("IN_realName", realName);
        db.addParameter("IN_cellPhone", cellPhone);
        db.addParameter("IN_loginPassword", loginPassword);
        db.addParameter("IN_parentId", parentId);
        db.addParameter("IN_nickName", nickName);
        db.addParameter("IN_pLevel", pLevel);
        db.addParameter("IN_gameUserId", gameUserId);
        db.addParameter("IN_loginStatus", loginStatus);
        db.addParameter("IN_clubId", clubId);
        db.addParameter("IN_clubName", clubName);
        db.addParameter("IN_expireDay", expireDay);
        db.addParameter("IN_mpClassName", mpClassName);
        db.addParameter("IN_editAdmin", editAdmin);
        db.addParameter("IN_editAdminId", editAdminId);
        db.addParameter("IN_clubStatus", clubStatus);
        db.addParameter("OUT_newClubId", ParameterDirection.Output);
        db.executeNonQuery();
        long newCluId = Long.parseLong(db.getParamValue("OUT_newClubId"));
        return newCluId;
    }

    public long reopen(int gameId, long promoterId, String nickName, long gameUserId, long clubId, String clubName, String mpClassName, int expireDay) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("TRAN_reopen", DBCommandType.Procedure);
        db.addParameter("IN_gameId", gameId);
        db.addParameter("IN_promoterId", promoterId);
        db.addParameter("IN_nickName", nickName);
        db.addParameter("IN_gameUserId", gameUserId);
        db.addParameter("IN_clubId", clubId);
        db.addParameter("IN_clubName", clubName);
        db.addParameter("IN_mpClassName", mpClassName);
        db.addParameter("IN_expireDay", expireDay);
        db.addParameter("OUT_newClubId", ParameterDirection.Output);
        db.executeNonQuery();
        long newCluId = Long.parseLong(db.getParamValue("OUT_newClubId"));
        return newCluId;
    }

    public PromoterModel queryByNickName(String nickName) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("select * from promoter where nickName= ? ");
        db.addParameter(nickName);
        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? DBUtils.convert2Model(PromoterModel.class, dt.rows[0]) : null;
    }

    public List<Long> getPromoterIdListByGameId(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select id from promoter where gameId = ?");
        db.addParameter(gameId);
        DataTable dt = db.executeQuery();

        if (dt.rows.length > 0) {
            return DBUtils.convert2List(Long.class, "id", dt);
        } else {
            return null;
        }
    }

    public List<Long> getSuperPromoterIdList() {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select id from promoter where pLevel = ?");

        db.addParameter(-1);
        DataTable dt = db.executeQuery();

        if (dt.rows.length > 0) {
            return DBUtils.convert2List(Long.class, "id", dt);
        } else {
            return null;
        }
    }

    //获取下级代理人数
    public int getDirectNum(long parentId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("select count(*) AS peopleNum from promoter where parentId = ?");
        db.addParameter(parentId);

        DataTable dt = db.executeQuery();

        return dt.rows.length > 0 ? CommonUtils.getIntValue(dt.rows[0].getColumnValue("peopleNum")) : 0;
    }

    public DataTable getPeopleNum(int gameId, Date endDate) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT COUNT(0) promoterNum,createDate FROM promoter WHERE gameId= ? AND createDate <= ? GROUP BY createDate order by createDate DESC");

        db.addParameter(gameId);
        db.addParameter(endDate);
        return db.executeQuery();
    }

    public int updateRealName(String realName, long cellphone) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET realName=? WHERE cellphone= ? ");
        db.addParameter(realName);
        db.addParameter(cellphone);
        return db.executeNonQuery();
    }

    public int updateCellPhone(long newCellphone, long cellphone) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand("UPDATE promoter SET cellPhone=? WHERE cellphone= ? ");
        db.addParameter(newCellphone);
        db.addParameter(cellphone);
        return db.executeNonQuery();
    }

    public int removeByPromoterId(List<Long> promoterIds) {
        DBHelper db = new DBHelper(CONNECTION);

        StringBuffer sb = new StringBuffer(128);
        sb.append("DELETE FROM promoter WHERE id in ")
                .append(SqlUtils.buildInConditions(promoterIds.size()));

        for (long id : promoterIds) {
            db.addParameter(id);
        }
        db.createCommand(sb.toString());
        return db.executeNonQuery();
    }

    public List<Long> listPromoterIdByGameId(int gameId) {
        DBHelper db = new DBHelper(CONNECTION);

        db.createCommand("SELECT id FROM promoter WHERE gameId= ?;");

        db.addParameter(gameId);
        DataTable dt = db.executeQuery();
        return dt.rows.length > 0 ? DBUtils.convert2List(Long.class, "id", dt) : null;
    }
}
