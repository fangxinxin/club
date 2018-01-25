package admin.operation;

import admin.vo.NewAgentModel;
import db_admin.model.SysUserModel;
import dsqp.db.model.DataTable;
import dsqp.db_club.dao.PromoterDao;
import dsqp.db_club.model.ClubModel;
import dsqp.db_club.model.NoticeModel;
import dsqp.db_club.model.PromoterLockModel;
import dsqp.db_club_dict.model.*;
import dsqp.db_club_log.dao.LogRecordDao;
import dsqp.db_club_log.model.LogRecordModel;
import dsqp.util.CommonUtils;
import dsqp.util.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by jeremy on 2017/8/15.
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);


    //Service层切点
    @Pointcut("execution (* admin.service.impl..*.*(..))")
    public void serviceAspect() {
    }


    /**
     * 拼接操作内容
     *
     * @param arguments
     * @param recordType
     * @param typeName
     * @param methodName
     * @return
     */
    public String opContent(Object[] arguments,int recordType, String typeName, String methodName) {
        String content = "";

        //根据recordType进行匹配，拼接相应的内容（需要注意参数的列表排序）
        switch (recordType) {
            case 11://开通代理
                NewAgentModel agent = (NewAgentModel) arguments[0];
                //查询生成的代理商Id
                DataTable dt = PromoterDao.queryByGameIdAndUserId(agent.getGameId(), agent.getGameUserId(), true);
                long promoterId = 0;
                if (dt.rows.length > 0) {
                    promoterId = Long.parseLong(dt.rows[0].getColumnValue("id"));
                }
                //开通代理结果
                if (promoterId > 0) {
                    content = typeName + ": 代理商ID " + promoterId;
                } else {
                    content = typeName + ": 未成功";
                }
                break;
            case 12://赠送/删除钻石
                int nums = CommonUtils.getIntValue(arguments[0]);
                long promoterId5 = CommonUtils.getLongValue(arguments[1]);
                if (nums > 0) {
                    content = "赠送钻石：" + promoterId5 + " 赠送 " + nums;
                } else {
                    content = "删除钻石：" + promoterId5 + " 删除 " + Math.abs(nums);
                }
                break;
            case 14://补发钻石
                int num = CommonUtils.getIntValue(arguments[1]);
                long userId = CommonUtils.getLongValue(arguments[2]);
                content = typeName + ": 给 " + userId + " 补发 " + num;
                break;
            case 15://封停账号
                PromoterLockModel model = (PromoterLockModel) arguments[0];
                long promoterId1 = model.getPromoterId();
                content = typeName + ": 代理商ID " + promoterId1;
                break;
            case 16://解封账号
                long promoterId2 = CommonUtils.getLongValue(arguments[0]);
                content = typeName + ": 代理商ID " + promoterId2;
                break;
            case 17://延长转正
                long promoterId3 = CommonUtils.getLongValue(arguments[0]);
                int expireTime = CommonUtils.getIntValue(arguments[2]);
                content = typeName + ": 给代理商 " + promoterId3 + " 延长 " + expireTime + " 小时";
                break;
            case 18://解散俱乐部
                ClubModel clubModel = (ClubModel) arguments[0];
                content = typeName + ": 解散 " + clubModel.getClubName() + ",俱乐部ID " + clubModel.getId();
                System.out.println(typeName + ": 解散 " + clubModel.getClubName() + ",俱乐部ID " + clubModel.getId());
                break;
            case 19://取消转正
                long clubId = CommonUtils.getLongValue(arguments[1]);
                content = typeName + ": 取消俱乐部ID " + clubId;
                break;
            case 191://赠送/删除俱乐部专属钻石
                int gameCard = CommonUtils.getIntValue(arguments[0]);
                long promoterId4 = CommonUtils.getLongValue(arguments[1]);
                int type = CommonUtils.getIntValue(arguments[2]);
                String str = "";
                if (type == 1) {
                    str = "赠送";
                } else {
                    str = "删除";
                }
                content = typeName + ": " + str + "代理商（ID: " + promoterId4 + "）俱乐部开房专属钻石 " + gameCard;
                break;
            case 21://玩家解绑
                long gameUserId = CommonUtils.getLongValue(arguments[1]);
                String gameNickName = CommonUtils.getStringValue(arguments[2]);
                content = typeName + ": 玩家账号 " + gameUserId + ",玩家昵称 " + gameNickName;
                break;
            case 22://发布公告
                NoticeModel notice = (NoticeModel) arguments[0];
                content = typeName + ": 公告标题 " + notice.getTitle();
                break;
            case 31://升降级条件修改
                DictLevelUpModel dictLevelUpModel = (DictLevelUpModel) arguments[0];
                int levelUpType = dictLevelUpModel.getLevelUpType();
                if ("add".equals(methodName)) {
                    if (2 == levelUpType) {
                        content = "新增二级升一级条件: 充值金额 " + dictLevelUpModel.getTotalPay();
                    } else if (1 == levelUpType) {
                        content = "新增一级升特级条件: 需要代理商人数 " + dictLevelUpModel.getTotalPromoter() + ",需要代理商充值 " + dictLevelUpModel.getTotalPay();
                    } else if (-1 == levelUpType) {
                        content = "新增特级降一级条件: 充值金额 " + dictLevelUpModel.getTotalPay();
                    }
                } else if ("update".equals(methodName)) {
                    if (2 == levelUpType) {
                        content = "二级升一级条件修改：充值金额 " + dictLevelUpModel.getTotalPay();
                    } else if (1 == levelUpType) {
                        content = "一级升特级条件修改：需要代理商人数 " + dictLevelUpModel.getTotalPromoter() + ",需要代理商充值 " + dictLevelUpModel.getTotalPay();
                    } else if (-1 == levelUpType) {
                        content = "特级降一级条件修改：充值金额 " + dictLevelUpModel.getTotalPay();
                    }
                } else {
                    content = typeName;
                }
                break;
            case 32://钻石售价调整
                if ("add".equals(methodName)) {
                    DictGoodPriceModel dictGoodPriceModel = (DictGoodPriceModel) arguments[0];
                    content = "新增钻石售价项: 基本钻石数量 " + dictGoodPriceModel.getGoodNum() + ",赠送钻石数量 " + dictGoodPriceModel.getGiftNum() + ",现金价格 " + dictGoodPriceModel.getCashPrice();
                } else if ("update".equals(methodName)) {
                    DictGoodPriceModel dictGoodPriceModel = (DictGoodPriceModel) arguments[0];
                    content = "钻石售价调整: 基本钻石数量 " + dictGoodPriceModel.getGoodNum() + ",赠送钻石数量 " + dictGoodPriceModel.getGiftNum() + ",现金价格 " + dictGoodPriceModel.getCashPrice();
                } else if ("deleteById".equals(methodName)) {
                    DictGoodPriceModel dictGoodPriceModel = (DictGoodPriceModel) arguments[1];
                    content = "删除钻石售价项: 基本钻石数量 " + dictGoodPriceModel.getGoodNum() + ",赠送钻石数量 " + dictGoodPriceModel.getGiftNum() + ",现金价格 " + dictGoodPriceModel.getCashPrice();
                } else {
                    content = typeName;
                }
                break;
            case 33://返钻比例修改
                DictBonusModel dictBonusModel = (DictBonusModel) arguments[0];
                if ("add".equals(methodName)) {
                    content = "新增返钻比例: 一级代理商返钻 " + dictBonusModel.getDirectPercent() + ",一级代理商返钻 " + dictBonusModel.getNonDirectPercent();
                } else if ("update".equals(methodName)) {
                    content = "返钻比例修改: 一级代理商返钻 " + dictBonusModel.getDirectPercent() + ",一级代理商返钻 " + dictBonusModel.getNonDirectPercent();
                } else {
                    content = typeName;
                }
                break;
            case 34://转正条件修改
                DictFormalModel dictFormalModel = (DictFormalModel) arguments[0];
                if ("add".equals(methodName)) {
                    content = "新增转正条件: 转正过期时间" + dictFormalModel.getExpireDay() + " 天,新玩家人数 " + dictFormalModel.getNewPeopleNum() + " 人,俱乐部人数" + dictFormalModel.getPeopleNum() + " 人,代开房局数 " + dictFormalModel.getPyjRoomNum() + " 局,非代开房局数 " + dictFormalModel.getNonPyjRoomNum() + " 局,转正后赠送 " + dictFormalModel.getAward() + " 钻,刷新时间 " + dictFormalModel.getRefreshDay() + " 天";
                } else if ("update".equals(methodName)) {
                    content = "转正条件修改: 转正过期时间" + dictFormalModel.getExpireDay() + " 天,新玩家人数 " + dictFormalModel.getNewPeopleNum() + " 人,俱乐部人数" + dictFormalModel.getPeopleNum() + " 人,代开房局数 " + dictFormalModel.getPyjRoomNum() + " 局,非代开房局数 " + dictFormalModel.getNonPyjRoomNum() + " 局,转正后赠送 " + dictFormalModel.getAward() + " 钻,刷新时间 " + dictFormalModel.getRefreshDay() + " 天";
                } else {
                    content = typeName;
                }
                break;
            case 35://俱乐部配置修改
                DictClubModel dictClubModel = (DictClubModel) arguments[0];
                String flag = dictClubModel.getIsAllowSell() ? "允许" : "不允许";
                if ("add".equals(methodName)) {
                    content = "新增俱乐部配置: " + flag + "售钻给任意玩家";
                } else if ("update".equals(methodName)) {
                    content = "俱乐部配置修改: " + flag + "售钻给任意玩家";
                } else {
                    content = typeName;
                }
                break;
            case 36://修改代理商信息
                long arg1 = (long) arguments[0];
                String arg2 = CommonUtils.getStringValue(arguments[1]);
                int arg3 = (int) arguments[2];
                switch (arg3) {
                    case 1:
                        return content = "修改俱乐部名称： 俱乐部新名称 " + arg2 + "（注：该俱乐部ID为" + arg1 + "）";
                    case 2:
                        return content = "修改代理商手机号码: 旧手机号码 " + arg1 + "，新手机号码 " + arg2;
                    case 3:
                        return content = "修改代理商真实姓名： 真实姓名 " + arg2 + "（注：该代理商手机号为" + arg1 + "）";
                    case 4:
                        return content = "手动修改代理商密码： 新密码 " + arg2 + "（注：该代理商手机号为" + arg1 + "）";
                    case 5:
                        return content = "修改代理商备注： 新备注 " + arg2 + "（注：该代理商Id为" + arg1 + "）";
                    case 6:
                        return content = "修改员工微信号： 微信号 " + arg2 + "（注：该代理商Id为" + arg1 + "）";
                }
                break;
            case 37://立即转正
                long promoterId6 = CommonUtils.getLongValue(arguments[0]);
                content = typeName + ": 给代理商 " + promoterId6 + " 立即转正";
                break;
            default:
                content = "操作记录未录入系统，请与相关技术人员确认！";
        }

        return content;

    }

    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    public String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }


    /**
     * 后置通知 用于拦截Service层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @After("serviceAspect()")
    public void after_service(JoinPoint joinPoint) {

//        System.out.println("=====service后置通知开始=====");

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //游戏Id
        int gameId = CommonUtils.getIntValue(session.getAttribute("gameId"));
        //用户信息
        Subject subject = SecurityUtils.getSubject();
        SysUserModel admin = (SysUserModel) subject.getPrincipal();
        long editAdminId = admin.getId();
        String editAdmin = admin.getUserName();
        //请求的IP
//        String ip = request.getRemoteAddr();
        String ip = getRemortIP(request);
        //Date时间格式
        Date createDate = DateUtils.String2Date(DateUtils.Date2String(new Date()));

        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String menuItem = "";
            String menuName = "";
            int recordType = 0;
            String typeName = "";
            String content = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        menuItem = method.getAnnotation(OperationServiceLog.class).menuItem();
                        menuName = method.getAnnotation(OperationServiceLog.class).menuName();
                        recordType = method.getAnnotation(OperationServiceLog.class).recordType();
                        typeName = method.getAnnotation(OperationServiceLog.class).typeName();
                        break;
                    }
                }
            }

//            System.out.println("请求方法: " + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+menuItem);

            //*========数据库日志=========*//
            //操作内容
            content = opContent(arguments, recordType, typeName, methodName);

            //保存数据库
            LogRecordModel model = new LogRecordModel();
            model.setGameId(gameId);
            model.setMenuItem(menuItem);
            model.setMenuName(menuName);
            model.setRecordType(recordType);
            model.setTypeName(typeName);
            model.setContent(content);
            model.setRequestIp(ip);
            model.setEditAdmin(editAdmin);
            model.setEditAdminId(editAdminId);
            model.setCreateDate(createDate);
            model.setCreateTime(new Date());

            LogRecordDao.add(model);

//            System.out.println("=====service后置通知结束=====");
        } catch (Exception e) {
            //记录本地异常日志
            logger.error("==后置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }


}
