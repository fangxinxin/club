package club.main;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Created by ds on 2017/12/8.
 */
public class JswForClub {


    public static void main(String[] args) {
        init();
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        //注册钩子函数
        Runtime.getRuntime().addShutdownHook(new ShutDownHook(context));

        //游戏列表
//        DataTable gameDt = DictGameDao.getList();
//
//        Date now = new Date();
//        Date start = DateUtils.String2Date(TimeUtil.getBegin(now));  //本周一
//        Date end = DateUtils.String2Date(TimeUtil.getEnd(now));      //本周日
//
//        int gameId;
//        long clubId;
//        long promoterId;
//        long gameUserId;
//        String gameNickName;
//        int pyjRoomNum;
//
//        DictGameDbModel dictDb;
//        LogWeekPyjUserRecordModel model;
//
//        for (DataRow gameRow: gameDt.rows) {
//            gameId = CommonUtils.getIntValue(gameRow.getColumnValue("gameId"));
//            dictDb = DictGameDbDao.getByGameId(gameId, true);
//            if (dictDb == null) {//游戏库地址为空
//                continue;
//            }
//
//            //俱乐部列表
//            DataTable clubDt = ClubDao.listByGameId(gameId);
//            for (DataRow clubRow: clubDt.rows) {
//                clubId = CommonUtils.getLongValue(clubRow.getColumnValue("id"));
//                DataTable clubUserDt = ClubUserDao.getListByClubId(clubId);
//
//                for (DataRow userRow: clubUserDt.rows) {
//                    promoterId = CommonUtils.getLongValue(userRow.getColumnValue("promoterId"));
//                    gameUserId = CommonUtils.getLongValue(userRow.getColumnValue("gameUserId"));
//                    gameNickName = userRow.getColumnValue("gameNickName");
//
//                    pyjRoomNum = UPyjUserRecordDao.getClubUserPyjRoomNum(dictDb, clubId, gameUserId, start, end);
//
//                    model = new LogWeekPyjUserRecordModel();
//
//                    model.setGameId(gameId);
//                    model.setClubId(clubId);
//                    model.setPromoterId(promoterId);
//                    model.setGameUserId(gameUserId);
//                    model.setGameNickName(gameNickName);
//                    model.setPyjRoomNum(pyjRoomNum);
//                    model.setYear(DateUtils.getYear());
//                    model.setWeek(DateUtils.getWeekOfYear(now));
//                    model.setCreateTime(new Date());
//
//                    LogWeekPyjUserRecordDao.addOnDuplicate(model);
//
//                }
//
//            }
//
//        }

    }

    public static void init() {
        InputStream in = JswForClub.class.getResourceAsStream("/proxool.xml");
        Reader reader = null;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JAXPConfigurator.configure(reader, false);
        } catch (ProxoolException e) {
            e.printStackTrace();
        }

    }
}
