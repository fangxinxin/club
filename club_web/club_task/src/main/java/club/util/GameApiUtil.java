package club.util;

import com.google.common.collect.Maps;
import dsqp.db.util.DateUtils;
import dsqp.db_club_dict.dao.DictGameDbDao;
import dsqp.db_club_dict.model.DictGameDbModel;
import dsqp.util.CommonUtils;
import dsqp.util.EncodingUtils;
import dsqp.util.HttpUtils;
import dsqp.util.SignUtil;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by ds on 2018/1/22.
 */
public class GameApiUtil {

    /**
     *     http://192.168.8.90:35442/api/mail/?act=newusermails&userIds=&title=&content=&reward=&startTime=&endTime=
     *     userIds: , 分隔
     *     content: 4096
     *     reward:  10008:1 | 10009:2(1.钻石，2.兑换券)
     */

    public static void sendMails(int gameId,
                                 String userIds,
                                 String title,
                                 String content,
                                 String reward,
                                 Date startTime,
                                 Date endTime)
    {
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        if (dictDb == null) {
            System.out.println("DictGameDb游戏库配置地址未配置");
            return;
        }

        Map<String, String> _maps = Maps.newHashMap();
        _maps.put("userIds",   userIds);
        _maps.put("title",     title);
        _maps.put("content",   content);
        _maps.put("reward",    CommonUtils.getStringValue(reward));
        _maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(startTime)));
        _maps.put("endTime",   EncodingUtils.urlEncode(DateUtils.DateTime2String(endTime)));

        //Start: 推送通知
        String url = "http://" + dictDb.getWebDomain() + ":" + dictDb.getWebPort() + "/api";
        try {
            String sendUrl = url + "/mail/?" + SignUtil.getParamsStrFromMap(_maps) + "&act=newusermails";
            HttpUtils.get(sendUrl);
            System.out.println(sendUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendMails(int gameId,
                                 String userIds,
                                 String title,
                                 String content,
                                 String reward,
                                 Date startTime)
    {
        DictGameDbModel dictDb = DictGameDbDao.getByGameId(gameId, true);
        if (dictDb == null) {
            System.out.println("DictGameDb游戏库配置地址未配置");
            return;
        }

        Map<String, String> _maps = Maps.newHashMap();
        _maps.put("userIds",   userIds);
        _maps.put("title",     title);
        _maps.put("content",   content);
        _maps.put("reward",    CommonUtils.getStringValue(reward));
        _maps.put("startTime", EncodingUtils.urlEncode(DateUtils.DateTime2String(startTime)));

        //Start: 推送通知
        String url = "http://" + dictDb.getWebDomain() + ":" + dictDb.getWebPort() + "/api";
        try {
            String sendUrl = url + "/mail/?" + SignUtil.getParamsStrFromMap(_maps) + "&act=newusermails";
            HttpUtils.get(sendUrl);
            System.out.println(sendUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
