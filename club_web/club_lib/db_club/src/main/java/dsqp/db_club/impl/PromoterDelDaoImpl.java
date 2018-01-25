package dsqp.db_club.impl;

import dsqp.db.db.DBHelper;
import dsqp.db_club.model.PromoterModel;

/**
 * Created by ds on 2017/10/13.
 */
public class PromoterDelDaoImpl {

    private static final String CONNECTION = "club";

    public int add(PromoterModel model, long parentCellPhone) {
        DBHelper db = new DBHelper(CONNECTION);
        db.createCommand(
                "INSERT INTO promoter_del (" +
                            "gameId, cellPhone, parentCellPhone, loginPassword, realName, nickName, IDCard, bankArea, bankAccount, pLevel, gameCard, gameCardTotal, " +
                            "gameCardSellTotal, deposit, depositTotal, totalPay, gameUserId, editAdmin, editAdminId, promoterTime, promoterDate, createTime, createDate" +
                        ") " + 
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now());");
        db.addParameter(model.getGameId());
        db.addParameter(model.getCellPhone());
        db.addParameter(parentCellPhone);
        db.addParameter(model.getLoginPassword());
        db.addParameter(model.getRealName());
        db.addParameter(model.getNickName());
        db.addParameter(model.getIDCard());
        db.addParameter(model.getBankArea());
        db.addParameter(model.getBankAccount());
        db.addParameter(model.getpLevel());
        db.addParameter(model.getGameCard());
        db.addParameter(model.getGameCardTotal());
        db.addParameter(model.getGameCardSellTotal());
        db.addParameter(model.getDeposit());
        db.addParameter(model.getDepositTotal());
        db.addParameter(model.getTotalPay());
        db.addParameter(model.getGameUserId());
        db.addParameter(model.getEditAdmin());
        db.addParameter(model.getEditAdminId());
        db.addParameter(model.getCreateTime());
        db.addParameter(model.getCreateDate());

        return db.executeNonQuery();
    }
}
