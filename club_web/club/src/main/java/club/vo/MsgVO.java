package club.vo;

/**
 * 前后端交互信息类，展示成功和失败
 * Created by Aris on 2017/7/25.
 */
public class MsgVO {
    private boolean isSuccess;
    private String  remark;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
