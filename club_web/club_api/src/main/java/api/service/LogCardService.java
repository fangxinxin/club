package api.service;


import api.vo.MsgVO;

import javax.servlet.http.HttpServletRequest;

/**
* Created by ds on 2017/3/11.
*/
public interface LogCardService {

    MsgVO add(HttpServletRequest request);


}
