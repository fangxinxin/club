package club.vo;

/**
 * 验证码对象
 * Created by mj on 2017/10/21.
 */

public class VerifyCode {

    private String content;
    private long xpiration;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getXpiration() {
        return xpiration;
    }

    public void setXpiration(long xpiration) {
        this.xpiration = xpiration;
    }
}
