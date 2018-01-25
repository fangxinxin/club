package pay.ajax;

import com.google.common.base.Strings;
import dsqp.util.MatrixToImageWriter;
import dsqp.util.RequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 生成二维码的URL
 * Created by Aris on 2017/3/10.
 */
@Controller
@RequestMapping("/ajax")
public class QRCodeController {

    @RequestMapping("create")
    public void create(HttpServletResponse response
            ,@RequestParam(value = "code_url",defaultValue = "") String code_url
    )
    {
        //将code_url转为二维码
        if (Strings.isNullOrEmpty(code_url))
            RequestUtils.write(response,"");

        try {
            MatrixToImageWriter.writeToStream(code_url,response.getOutputStream());
        } catch (IOException e) {
            RequestUtils.write(response,"");
        }
    }
}
