package org.onlineChat.pojo;

import java.lang.reflect.Field;

import org.springframework.stereotype.Repository;

/**
 * TODO :
 */
@Repository(value = "user")
public class User implements Table<String> {
	private String userid; // 用户名
	private String password; // 密码
	private String nickname; // 昵称
	private int sex; // 性别
	private int age; // 年龄
	private String profilehead; // 头像
	private String profile; // 简介
	private String firsttime; // 注册时间
	private String lasttime; // 最后登录时间
	private int status; // 账号状态(1正常 0禁用)

	public User() {
	}

	public User(String userid, String password, String nickname, int sex, int age, String profilehead, String profile,
			String firsttime, String lasttime, int status) {
		this.userid = userid;
		this.password = password;
		this.nickname = nickname;
		this.sex = sex;
		this.age = age;
		this.profilehead = profilehead;
		this.profile = profile;
		this.firsttime = firsttime;
		this.lasttime = lasttime;
		this.status = status;
	}

	/**
	 * getter&setter
	 * 
	 * @return
	 */
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirsttime() {
		return firsttime;
	}

	public void setFirsttime(String firsttime) {
		this.firsttime = firsttime;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getProfilehead() {
		return profilehead;
	}

	public void setProfilehead(String profilehead) {
		this.profilehead = profilehead;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User)
			return equals((User) obj);
		return false;
	}

	/**
	 * 利用反射判断两个用户是否一样
	 * 
	 * @param user
	 *            需要比较的另外一个用户
	 * @return 如果和需要比较的用户是一样的，则返回true，否则返回false
	 */
	public boolean equals(User user) {
		Field fields[] = this.getClass().getDeclaredFields();
		boolean result = true;
		try {
			for (Field f : fields) {
				f.setAccessible(true);
				if (!f.get(this).equals(f.get(user))) {
					result = false;
					break;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public static void main(String[] args) {
		User u1 = new User("00", "", "", 0, 0, "", "", "", "", 0);
		User u2 = new User("01", "", "", 0, 0, "", "", "", "", 0);
		System.out.println(u1.equals(u2));
		User u3 = new User("00", "", "", 0, 0, "", "", "", "", 0);
		System.out.println(u3.equals(u1));
	}

	@Override
	public String getPrimaryKey() {
		return userid;
	}
}
