package org.onlineChat.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.onlineChat.constants.Constants;
import org.onlineChat.database.utils.XmlHelper;
import org.onlineChat.pojo.Message;
import org.onlineChat.pojo.User;
import org.onlineChat.service.IUserService;
import org.onlineChat.utils.CommonDate;
import org.onlineChat.websocket.message.MessageHandler;
import org.onlineChat.websocket.message.NullMesssageHandler;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * buffer层的业务构件，由于使用其他构件为上层提供服务
 * 
 * @author Frank
 *
 */
@Repository
@Service(value = "data")
public class Data {
	/**
	 * 用户缓存
	 */
	private volatile DataMap<String, User> userMap;

	/**
	 * 消息缓存
	 */
	private volatile DataMap<String, Message> messageMap;

	/**
	 * 用户状态与会话表
	 */
	private volatile ConcurrentHashMap<String, MessageHandler> userState;

	/**
	 * 在线用户列表
	 */
	private volatile List<String> userOnline;

	/**
	 * 用户数据库的service，使用依赖注入，实例化之后自动注入
	 */
	@Resource
	private IUserService userService;

	/**
	 * 开始标记
	 */
	boolean start = false;

	/**
	 * 最大容量
	 */
	public final int maxSize;

	public Data() {
		maxSize = XmlHelper.getBufferMaxSize();
		init(maxSize);
	}

	void init(int maxSize) {
		userMap = new DataMap<>(maxSize);
		userState = new ConcurrentHashMap<>();
		userOnline = new LinkedList<>();
		// DataManager.initData(this);
	}

	/**
	 * 检查数据库service是否到位
	 */
	public void checkForDatabase() {
		if (!start) {
			userMap.setDataBase(userService);
			start = true;
		}
	}

	/**
	 * 提供给上层的登录服务
	 * 
	 * @param userid
	 *            用户id
	 * @param password
	 *            密码
	 * @param date
	 *            登录时间
	 * @return 状态
	 *         Constants.NOT_FOUND、Constants.DISABLE、Constants.SUCCUESSFULLY、Constants.ERROR分别表示用户名没有找到，用户禁止登录，登录成功和密码错误
	 */
	public int userLogin(String userid, String password, CommonDate date) {
		checkForDatabase();
		User user = userMap.selectAllById(userid);
		if (user == null)
			return Constants.NOT_FOUND;
		else if (user.getStatus() != 1) {
			return Constants.DISABLE;
		} else if (user.getPassword().equals(password)) {
			MessageHandler state = userState.get(userid);
			if (state != null)
				return Constants.DISABLE;
			if (date != null) {
				user.setLasttime(date.getTime24ToString());
				userMap.update(user);
				userState.put(userid, new NullMesssageHandler(userid));
			}
			return Constants.SUCCUESSFULLY;
		} else
			return Constants.ERROR;
	}

	/**
	 * 提供给上层的注册前判断id是否可用的功能
	 * 
	 * @param userid
	 *            需要判断的用户id
	 * @return 返回注册结果
	 */
	public boolean userRegister(String userid) {
		if (userMap.selectAllById(userid) != null)
			return false;
		return true;
	}

	/**
	 * 提供给上层的登出函数
	 * 
	 * @param userid
	 *            需要登出的用户id
	 * @return 登出结果
	 */
	public boolean logout(String userid) {
		MessageHandler state = userState.get(userid);
		if (state == null)
			return false;
		userMap.flush(userid);
		userOnline.remove(userid);
		return true;
	}

	/**
	 * 注册web socket给指定用户
	 * 
	 * @param userid
	 *            指定用户的id
	 * @param handler
	 *            web socket的MessageHandler
	 * @return 注册成功返回true，失败返回false
	 */
	public boolean registerWebSocket(String userid, MessageHandler handler) {
		if (userState.replace(userid, handler) == null)
			return false;
		userOnline.add(userid);
		return true;
	}

	/**
	 * 提供给上层的用户注册功能
	 * 
	 * @param user
	 *            注册用户的信息
	 * @return 注册成功返回true，失败返回false
	 */
	public boolean userRegister(User user) {
		return userMap.insert(user);
	}

	/**
	 * 拉取在线用户列表
	 * 
	 * @return
	 */
	public List<String> getUserOnline() {
		return userOnline;
	}

	/**
	 * 提供给上层的广播功能
	 * 
	 * @param message
	 *            广播的消息
	 */
	public void broadcast(String message) {
		LinkedList<Object> keys = new LinkedList<>();
		for (MessageHandler handler : userState.values()) {

			boolean result = handler.sendText(message);
			System.out.println(result);
			if (!result)
				keys.add(handler.getKey());
		}
		for (Object k : keys) {
			userState.remove(k);
		}
	}

	/**
	 * 给上层提供的私聊功能
	 * 
	 * @param userid
	 *            目标用户的id
	 * @param message
	 *            要发送的消息
	 */
	public void sendMessage(String userid, String message) {
		MessageHandler handler = userState.get(userid);
		boolean result;
		if (handler != null) {
			result = handler.sendText(message);
			System.out.println(result);
			if (!result)
				userState.remove(handler.getKey());
		}
	}

	public static void main(String[] args) {
		// User u1 = new User("00", "", "", 0, 0, "", "", "", "", 0);
		// User u2 = new User("01", "", "", 0, 0, "", "", "", "", 0);
		// UserData data = new UserData(u1);
		// data.setDataProperty("userid", "01");
		// System.out.println(u1.equals(u2));
		// User u3 = new User("00", "", "", 0, 0, "", "", "", "", 0);
		// System.out.println(u3.equals(u1));
	}
}
