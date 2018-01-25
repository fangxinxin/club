package api.controller;

import api.service.LogCardService;
import api.vo.MsgVO;
import dsqp.util.JsonUtils;
import dsqp.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ds on 2017/12/22.
 */
@Controller("log")
@RequestMapping("log")
public class LogController {

    @Autowired
    private LogCardService logCardService;

    @RequestMapping("test")
    public String test() {
        return "test_log";
    }

    /**
     * 俱乐部代开房钻石流水
     * @param request
     *     参数： gameId, clubId, roomId, cardConsume, gameUserNum, roomRound, gameUserInfo, createTime
     * @param response
     * By lyf
     */
    @RequestMapping(value = "/roomCardConsume")
    @ResponseBody
    public void roomCardConsume(HttpServletRequest request, HttpServletResponse response){
        MsgVO vo = logCardService.add(request);

        String result = JsonUtils.getJson(vo);
        RequestUtils.write(response, result);
    }

}
