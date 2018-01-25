package pay.service.gateway;

import dsqp.common_const.club.PayType;

/**
 * Created by Aris on 2017/3/10.
 */
public class PayGatewayFactory {
    private static final PayGateway ali = new PayGateway_Alipay();
    private static final PayGateway wx  = new PayGateway_Wxpay();

    public static PayGateway create (String className){
        if (className.contains(PayType.ALIPAY.getClassName())) {
            return ali;
        } else if (className.contains(PayType.WXPAY.getClassName())) {
            return wx;
        } else {
            return null;
        }
    }
}
