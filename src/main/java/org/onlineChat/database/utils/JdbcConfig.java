package org.onlineChat.database.utils;

public class JdbcConfig {

	public final String name;
	public final String password;
	public final String ip;
	public final String port;
	public final String database;
	public final String driver;

	public JdbcConfig(String name, String password, String ip, String port, String database, String driver) {
		this.name = name;
		this.password = password;
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.driver = driver;
	}
}
