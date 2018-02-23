package org.onlineChat.dao;

import org.apache.ibatis.annotations.Param;
import org.onlineChat.pojo.Log;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 封装了Log表的基本操作
 * @author Frank
 *
 */
@Service(value = "logDao")
public interface ILogDao {
    List<Log> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    List<Log> selectLogByUserid(@Param("userid") String userid, @Param("offset") int offset, @Param("limit") int limit);

    Log selectCount();

    Log selectCountByUserid(@Param("userid") String userid);

    boolean insert(Log log);

    boolean delete(String id);

    boolean deleteThisUser(String userid);

    boolean deleteAll();
}
