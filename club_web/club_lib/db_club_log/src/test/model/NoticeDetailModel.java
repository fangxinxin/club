package dsqp.db_club.model;

import java.util.Date;

/**
 * Created by jeremy on 2017/7/21.
 */
public class NoticeDetailModel {
    private long id;
    private int gameId;
    private long noticeId;
    private long promoterId;
    private Boolean isRead;
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long  noticeId) {
        this.noticeId = noticeId;
    }

    public long getPromoterId() {
        return promoterId;
    }

    public void setPromoterId(long promoterId) {
        this.promoterId = promoterId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
