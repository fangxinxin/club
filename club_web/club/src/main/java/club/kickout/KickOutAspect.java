package club.kickout;

import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.PromoterModel;
import dsqp.util.CommonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by jeremy on 2017/8/23.
 */
@Aspect
@Component
public class KickOutAspect {

    //切controller层
    @Pointcut("execution (* club.controller..*.*(..))")
    public  void kickOutAspect(){

    }


    /**
     * 前置通知   用于拦截controller层所有的方法  如果处于封停状态则踢出
     */
    @Before("kickOutAspect()")
    public  void before() {
        Subject subject = SecurityUtils.getSubject();
        long promoterId = CommonUtils.getLongValue(subject.getSession().getAttribute("id"));
        PromoterModel promoterModel = PromoterDao.getOne(promoterId);
        if(promoterModel!=null){
            int loginStatus = promoterModel.getLoginStatus();
            if(loginStatus==2){
                subject.logout();
            }
        }

    }




}
