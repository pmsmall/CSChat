package org.onlineChat.dao;

import org.springframework.stereotype.Service;
import org.onlineChat.pojo.User;
import org.springframework.data.repository.query.*;
import java.util.List;

/**
 * 封装了User表最基本的操作
 * @author Frank
 *
 */
@Service(value = "userDao")
public interface IUserDao {
    List<User> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    User selectUserByUserid(String userid);

    User selectCount();

    boolean insert(User user);

    boolean update(User user);

    boolean delete(String userid);
}


