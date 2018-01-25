package club.controller.withdraw;

import dsqp.common_const.club.DepositSource;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.dao.WithdrawRequestDao;
import dsqp.db_club.model.PromoterModel;
import dsqp.db_club_log.dao.LogDepositDao;
import dsqp.util.CommonUtils;
import dsqp.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by mj on 2017/7/24.
 */

@Controller
public class WithdrawRequestController {

    @RequestMapping("addWithdrawRequest")
    @ResponseBody
    public void sellRecordGi(@RequestParam(value = "withdraw", defaultValue = "") double withdrawAmount
            , HttpServletResponse response) {

        long id = CommonUtils.getLongValue(SecurityUtils.getSubject().getSession().getAttribute("id"));
        PromoterModel promoter = PromoterDao.getOne(id);
        int requestTimes = WithdrawRequestDao.queryRequestTimes(id);
        if (promoter.getDeposit() < withdrawAmount) {
            RequestUtils.write(response, "金额输入错误，请重新输入！");
        } else if (requestTimes > 0) {
            RequestUtils.write(response, "本周已提现，请下周再申请提现！");//一周只能提现一次
        } else {
            int i = WithdrawRequestDao.addRequest(promoter.getGameId(), id, promoter.getpLevel(), System.currentTimeMillis() + "" + id, "", promoter.getDeposit(), promoter.getDeposit() - withdrawAmount, withdrawAmount, promoter.getBankAccount(), "");
            LogDepositDao.add(promoter.getGameId(), promoter.getId(), promoter.getNickName(), DepositSource.WithDraw.getType(), 0 - withdrawAmount, promoter.getDeposit(), promoter.getDeposit() - withdrawAmount);
            if (i > 0) {
                PromoterDao.updateDeposit(promoter.getDeposit() - withdrawAmount, promoter.getId());
                RequestUtils.write(response, "OK");
            } else {
                RequestUtils.write(response, "失败，请重试！");
            }
        }
    }
}
