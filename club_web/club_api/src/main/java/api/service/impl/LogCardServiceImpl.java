package api.service.impl;

import api.service.LogCardService;
import api.vo.MsgVO;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dsqp.db_club_dict.dao.DictApiKeyDao;
import dsqp.db_club_dict.model.DictApiKeyModel;
import dsqp.db_club_log.dao.LogClubRoomcardDao;
import dsqp.db_club_log.model.LogClubRoomcardModel;
import dsqp.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
* Created by ds on 2016/11/29.
*/
@Service
public class LogCardServiceImpl implements LogCardService {


    @Override
    public MsgVO add(HttpServletRequest request) {

        MsgVO vo = new MsgVO();

        Map<String,String> map = RequestUtils.getMapFromRequest(request);
        if (MapUtils.isEmpty(map)){
            vo.setMsg("request fail");
            return vo;
        }

        int gameId = CommonUtils.getIntValue(map.get("gameId"));
        long clubId = CommonUtils.getLongValue(map.get("clubId"));
        long roomId = CommonUtils.getLongValue(map.get("roomId"));
        if (gameId == 0 || clubId == 0 || roomId == 0) {
            vo.setMsg("无效俱乐部信息");
            return vo;
        }

        DictApiKeyModel dict = DictApiKeyDao.getByGameId(gameId);
        if (dict == null || dict != null && Strings.isNullOrEmpty(dict.getApiKey())) {
            vo.setMsg("apiKey miss");
            return vo;
        }

        int cardConsume = CommonUtils.getIntValue(map.get("cardConsume"));
        int gameUserNum = CommonUtils.getIntValue(map.get("gameUserNum"));
        int roomRound = CommonUtils.getIntValue(map.get("roomRound"));
        if (cardConsume == 0 || gameUserNum == 0 || roomRound == 0) {
            vo.setMsg("错误的对局信息");
            return vo;
        }

        String gameUserInfo = map.get("gameUserInfo");
        String date = map.get("createTime");
        Date createTime = DateUtils.String2DateTime(date);
        if (Strings.isNullOrEmpty(gameUserInfo) && Strings.isNullOrEmpty(date)) {
            vo.setMsg("玩家信息缺失");
            return vo;
        }

        String gameSign = map.get("sign");
        if (Strings.isNullOrEmpty(gameSign)) {
            vo.setMsg("sign miss");
            return vo;
        }

        Map<String,String> requestMap = Maps.newHashMap();

        requestMap.put("gameId",          map.get("gameId"));
        requestMap.put("clubId",          map.get("clubId"));
        requestMap.put("roomId",          map.get("roomId"));
        requestMap.put("cardConsume",     map.get("cardConsume"));
        requestMap.put("gameUserNum",     map.get("gameUserNum"));
        requestMap.put("roomRound",       map.get("roomRound"));
        requestMap.put("gameUserInfo",    gameUserInfo);
        requestMap.put("createTime",      date);

        //校验是否已写入日志
        boolean isExist = LogClubRoomcardDao.isExistLog(roomId, createTime);
        if (isExist) {
            vo.setResult(true);
            vo.setMsg("添加成功");
            return vo;
        }

        //生产签名
        String signText = SignUtil.getSignText(requestMap) + "&key=" + dict.getApiKey();
        String sign = DigestUtils.md5Hex(signText).toUpperCase();
        if (!sign.equals(gameSign)) {
            vo.setMsg("sign error");
            return vo;
        }

        LogClubRoomcardModel model = new LogClubRoomcardModel();
        model.setGameId(gameId);
        model.setClubId(clubId);
        model.setRoomId(roomId);
        model.setCardConsume(cardConsume);
        model.setGameUserNum(gameUserNum);
        model.setRoomRound(roomRound);
        model.setGameUserInfo(gameUserInfo);
        model.setCreateTime(createTime);
        model.setCreateDate(createTime);

        int r = LogClubRoomcardDao.add(model);
        if (r > 0) {
            vo.setResult(true);
            vo.setMsg("添加成功");
        } else {
            vo.setMsg("insert fail");
        }

        return vo;
    }

    public static void main(String[] args) {
        //http://192.168.7.105:8084/club_api/log/roomCardConsume?
        //gameId=4156&clubId=100000&roomId=481127&cardConsume=2&gameUserNum=2&roomRound=4
        //&gameUserInfo=&createTime=2018-01-12 15:32:45&sign=6E7FC050A5C4C3118E879E77D0BD44F9
        String gameUserInfo ="{owner:[{userid:1058061198,nickname:user1}],winner:[{userid:1061689142,nickname:user2}],userinfo:[{userid:1058061198,nickname:user1},{userid:1061689142,nickname:user2}]}";
//        String gameUserInfo ="%7Bowner:%7Buserid:1058061198,nickname:user1%7D,winner:%5B%7Buserid:1058061198,nickname:user1%7D%5D,userinfo:%5B%7Buserid:1058061198,nickname:user1%7D,%7Buserid:1061689142,nickname:user2%7D%5D%7D";
        Map<String, String> maps = Maps.newHashMap();
        maps.put("gameId", String.valueOf(4156));
        maps.put("clubId", "100000");
        maps.put("roomId", "599981");
        maps.put("cardConsume", "2");
        maps.put("gameUserNum", "2");
        maps.put("roomRound", "4");
        maps.put("gameUserInfo", "%7Bowner%3A%5B%7Buserid%3A1058061198%2Cnickname%3Auser1%7D%5D%2Cwinner%3A%5B%7Buserid%3A1061689142%2Cnickname%3Auser2%7D%5D%2Cuserinfo%3A%5B%7Buserid%3A1058061198%2Cnickname%3Auser1%7D%2C%7Buserid%3A1061689142%2Cnickname%3Auser2%7D%5D%7D");
        maps.put("roomRound", "4");
        maps.put("createTime", "2018-01-12+16%3A45%3A27");
        maps.put("sign", "4B4716526220C1522CFF35F91BBECC27");

        String url = "http://192.168.7.105:8084" +  "/club_api/log/roomCardConsume" + "?" + SignUtil.getParamsStrFromMap(maps);
        System.out.println(url);

        String signText = SignUtil.getSignText(maps) + "&key=3FdWbDVbJ2SKFqvBRX2FcdNZ85s3oTOh";
        String sign = DigestUtils.md5Hex(signText).toUpperCase();
        System.out.println(sign);

        try {
            System.out.println(HttpUtils.get(url+"&sign=4B4716526220C1522CFF35F91BBECC27"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
