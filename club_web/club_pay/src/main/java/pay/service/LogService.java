package pay.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Aris on 2016/11/21.
 */
public interface LogService {

    long addRequestLog(HttpServletRequest request);

    long updateLog(long logID, String content);

}
