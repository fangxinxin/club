package admin.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Created by Aris on 2016/11/29.
 */
@ControllerAdvice
public class DefaultExceptionHandler{
    private String PATH1 = "exception/error_info1";

//    @ExceptionHandler({UnauthorizedException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ModelAndView processUnauthenticatedException(UnauthorizedException e) {
//        ModelAndView mv = new ModelAndView(PATH1);
//        mv.addObject("code", 401);
//        mv.addObject("msg", "当前页面需相应的权限方能访问，您没有权限访问本页面。");
//
//        return mv;
//    }
//
//    @ExceptionHandler({UnknownAccountException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView processUnknownAccountException(UnknownAccountException e) {
//        ModelAndView mv = new ModelAndView(PATH1);
//        mv.addObject("code", 500);
//        mv.addObject("msg", "当前账号异常。");
//
//        return mv;
//    }


}
