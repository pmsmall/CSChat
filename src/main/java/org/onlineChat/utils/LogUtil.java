package org.onlineChat.utils;

import java.sql.Timestamp;

import org.onlineChat.pojo.Log;

/**
 * TODO   :
 */
public class LogUtil {

    public Log setLog(String userid, Timestamp time, String type, String detail, String ip){
         Log log = new Log();
        log.setUserid(userid);
        log.setTime(time);
        log.setType(type);
        log.setDetail(detail);
        log.setId(ip);
        return log;
    }

}
