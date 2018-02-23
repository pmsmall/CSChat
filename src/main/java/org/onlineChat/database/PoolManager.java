package org.onlineChat.database;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.junit.Test;
import org.onlineChat.database.utils.JdbcConfig;
import org.onlineChat.database.utils.XmlHelper;

public class PoolManager {
	public final static String driver;

	public final static String name;
	public final static String password;
	public final static String ip;
	public final static String port;
	public final static String database;
	public final static String url;

	private static Class driverClass = null;
	private static ObjectPool connectionPool = null;

	/**
	 * 装配配置文件
	 */
	static {
		JdbcConfig config = null;
		config = XmlHelper.getJdbcConfig();
		name = config.name;
		password = config.password;
		ip = config.ip;
		port = config.port;
		database = config.database;
		driver = config.driver;

		url = "jdbc:oracle:thin://" + ip + ":" + port + "/" + database;
	}

	public static boolean saveConfiguration() {
		File file = new File(XmlHelper.configurationName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			XmlHelper.saveConfigure(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public PoolManager() {
		System.out.println("use pool manager instance");
		new Exception().printStackTrace(System.out);
	}

	/**
	 * 初始化数据源
	 */
	private static synchronized void initDataSource() {
		if (driverClass == null) {
			try {
				driverClass = Class.forName(driver);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 连接池启动
	 */
	private static void startPool() {
		initDataSource();
		if (connectionPool != null) {
			destroyPool();
		}
		try {
			connectionPool = new GenericObjectPool(null);
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, name, password);
			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,
					connectionPool, null, null, false, true);
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			driver.registerPool("dbpool", connectionPool);
			System.out.println("装配连接池OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭连接池
	 */
	private static void destroyPool() {
		try {
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			driver.closePool("dbpool");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得连接池中的连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		if (connectionPool == null)
			startPool();
		try {
			conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:dbpool");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 获取连接 getConnection
	 * 
	 * @param name
	 * @return
	 */
	public static Connection getConnection(String name) {
		return getConnection();
	}

	/**
	 * 释放连接 freeConnection
	 * 
	 * @param conn
	 */
	private static void freeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放连接 freeConnection
	 * 
	 * @param name
	 * @param con
	 */
	public static void freeConnection(String name, Connection con) {
		freeConnection(con);
	}

	@Test
	public void test() {
		try {
			Connection conn = PoolManager.getConnection();
			if (conn != null) {
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery("select * from log");
				int c = rs.getMetaData().getColumnCount();
				while (rs.next()) {
					System.out.println();
					for (int i = 1; i <= c; i++) {
						System.out.print(rs.getObject(i));
					}
				}
				rs.close();
			}
			PoolManager.freeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(driver);
		System.out.println(saveConfiguration());
	}
}