package org.onlineChat.service;

import java.util.List;

import org.onlineChat.pojo.User;

/**
 * TODO   :
 */
public interface IUserService extends DatabaseService<User,String> {
    List<User> selectAll(int page, int pageSize);
    User selectUserById(String userid);
    int selectCount(int pageSize);
    boolean insert(User user);
    boolean update(User user);
    boolean delete(String userid);
}
