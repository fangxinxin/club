package pay.controller;

import com.google.common.base.Strings;
import dsqp.db_club_dict.model.DictPayGatewayModel;
import dsqp.util.RequestUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pay.service.LogService;
import pay.service.gateway.PayGateway;
import pay.service.gateway.PayGatewayFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 支付宝前端控制支付成功跳转
 * Created by Aris on 2017/3/13.
 */
@Controller
public class SyncController {

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/{className}/sync")
    public void Notify(HttpServletRequest request, HttpServletResponse response
            , @PathVariable("className") String className){
        long logID = logService.addRequestLog(request);

        if (Strings.isNullOrEmpty(className)){
            fail(response,logID,"支付类名缺失");
            return;
        }

        //根据className生成具体的支付网关
        PayGateway payGateway = PayGatewayFactory.create(className);
        if (payGateway == null){
            fail(response,logID,"初始化失败");
            return;
        }

        Map<String,String> notifyMap = payGateway.getNotifyParams(request);
        if (MapUtils.isEmpty(notifyMap)){
            fail(response,logID,"获取参数失败");
            return;
        }

        //获取支付网关配置信息
        DictPayGatewayModel dictPayGateway = dsqp.db_club_dict.dao.DictPayGatewayDao.getByClassName(className);
        if (dictPayGateway == null) {
            fail(response,logID,"配置参数缺失");
            return;
        }

        payGateway.validateSync(notifyMap,dictPayGateway,response);
    }


    /**------------------------------------私有方法-----------------------------------------**/
    private void fail(HttpServletResponse response,long logID,String msg){
        logService.updateLog(logID,msg);
        RequestUtils.write(response, msg);
    }

}
