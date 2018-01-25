package pay.util.alipay.util;

import dsqp.util.SignUtil;
import org.dom4j.DocumentException;
import pay.util.alipay.config.AlipayConfig;
import pay.util.alipay.sign.MD5;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;


/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipaySubmit {
    
    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara,String key) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(AlipayConfig.sign_type.equals("MD5") ) {
        	mysign = MD5.sign(prestr, key, AlipayConfig.input_charset);
        }
        return mysign;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp,String key) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara,key);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayConfig.sign_type);

        return sPara;
    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp,String key) {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp,key);
        return ALIPAY_GATEWAY_NEW + SignUtil.getParamsStrFromMap(sPara);
    }
    
 
    
    /**
     * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数
     * 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
     * @return 时间戳字符串
     * @throws IOException
     * @throws DocumentException
     * @throws MalformedURLException
     */
//	public static String query_timestamp() throws MalformedURLException,
//                                                        DocumentException, IOException {
//
//        //构造访问query_timestamp接口的URL串
//        String strUrl = ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner=" + AlipayConfig.partner + "&_input_charset" +AlipayConfig.input_charset;
//        StringBuffer result = new StringBuffer();
//
//        SAXReader reader = new SAXReader();
//        Document doc = reader.read(new URL(strUrl).openStream());
//
//        List<Node> nodeList = doc.selectNodes("//alipay/*");
//
//        for (Node node : nodeList) {
//            // 截取部分不需要解析的信息
//            if (node.getName().equals("is_success") && node.getText().equals("T")) {
//                // 判断是否有成功标示
//                List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
//                for (Node node1 : nodeList1) {
//                    result.append(node1.getText());
//                }
//            }
//        }
//
//        return result.toString();
//    }
}
