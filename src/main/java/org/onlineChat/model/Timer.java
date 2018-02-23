package org.onlineChat.model;

/**
 * 带有计时器功能的数据记录项接口
 * 
 * @author Frank
 *
 */
public interface Timer {
	/**
	 * 计数器计数
	 */
	public void timeUp();

	/**
	 * 计数器重置
	 */
	public void resetTimer();

	/**
	 * 获得当前计数
	 * 
	 * @return 当前的计数
	 */
	public int time();

	/**
	 * 获得写操作标志位的值
	 * 
	 * @return true：则表示有过写操作，如果要换出或者刷新，应该进行数据库数据更新； false：则表示没有写操作，可以直接换出
	 */
	public boolean needWriteToDataBase();

	/**
	 * 释放资源，防止内存溢出
	 */
	public void dispose();
}
