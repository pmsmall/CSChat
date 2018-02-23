package org.onlineChat.model;

import java.util.concurrent.ConcurrentHashMap;

import org.onlineChat.model.DataTimer;
import org.onlineChat.pojo.Table;
import org.onlineChat.service.DatabaseService;

/**
 * 记录缓存的数据结构
 * 
 * @author Frank
 *
 * @param <T>
 *            主码的类型
 * @param <F>
 *            对应主码类型的Table类型
 */
public class DataMap<T, F extends Table<T>> {
	/**
	 * 数据存储的主体，使用线程安全的ConcurrentHashMap
	 */
	private volatile ConcurrentHashMap<T, DataTimer<F>> map;
	private volatile int tableMapSize = 0;
	private int maxSize;

	/**
	 * 底层服务接口
	 */
	private DatabaseService<F, T> database;

	/**
	 * 构造函数，设置缓存大小
	 * 
	 * @param maxSize
	 *            初始化缓存大小
	 */
	public DataMap(int maxSize) {
		this.maxSize = maxSize;
		map = new ConcurrentHashMap<>(maxSize);
	}

	/**
	 * 设置底层数据接口
	 * 
	 * @param database
	 *            底层数据接口的实现实例
	 */
	public void setDataBase(DatabaseService<F, T> database) {
		this.database = database;
		new Thread(updateThread).start();
	}

	/**
	 * 插入数据，会现在缓存里面检查，如果记录以及存在直接返回插入失败
	 * 
	 * @param table
	 *            需要插入的数据table
	 * @return 插入成功返回true，失败返回false
	 */
	public boolean insert(F table) {
		if (map.containsKey(table.getPrimaryKey()))
			return false;
		boolean result = database.insert(table);
		if (result)
			insertIntoBuff(table);
		return result;
	}

	/**
	 * 通过id检索所有的
	 * 
	 * @param id
	 *            主码
	 * @return 返回检索到的记录
	 */
	public F selectAllById(T id) {
		DataTimer<F> data = map.get(id);
		if (data != null) {
			for (DataTimer<F> tmp : map.values()) {
				tmp.timeUp();
			}
			data.resetTimer();
			return (F) data.data;
		} else {
			F result = selectAllByIdInDatabase(id);
			if (result != null) {
				for (DataTimer<F> tmp : map.values()) {
					tmp.timeUp();
				}
				insertIntoBuff(result);
				return (F) result;
			}
		}
		return null;
	}

	/**
	 * 通过id直接检索数据库中的
	 * 
	 * @param id
	 *            主码
	 * @return 检索结果
	 */
	public F selectAllByIdInDatabase(T id) {
		return database.selectUserById(id);
	}

	/**
	 * 将记录插入缓存
	 * 
	 * @param table
	 *            需要插入缓存的记录
	 * @return 插入结果，成功返回true，失败返回false
	 */
	public boolean insertIntoBuff(F table) {
		if (table == null)
			return false;
		DataTimer<F> data = map.get(table.getPrimaryKey());
		if (data != null) {
			return data.updateData(table);
		} else {
			if (tableMapSize >= maxSize) {
				int maxTimer = -1;
				DataTimer<F> maxtable = null;
				for (DataTimer<F> tmp : map.values()) {
					if (tmp.time > maxTimer) {
						maxTimer = tmp.time;
						maxtable = tmp;
					}
				}
				map.remove(maxtable.data.getPrimaryKey());
				if (maxtable.needWriteToDataBase()) {
					database.update(maxtable.data);
				}
				maxtable.dispose();
			}
			map.put((T) table.getPrimaryKey(), new DataTimer<F>((F) table));
			return true;
		}
	}

	/**
	 * 更新记录，会先在缓存里面更新，包含缓存的更新和写标记
	 * 
	 * @param table
	 *            更新后的记录
	 * @return 更新结果，成功返回true，失败返回false
	 */
	public boolean update(F table) {
		DataTimer<F> tmp = map.get(table.getPrimaryKey());
		boolean result;
		if (tmp != null) {
			result = tmp.updateData(table);
			if (result) {
				for (DataTimer<F> tmp0 : map.values()) {
					tmp0.timeUp();
				}
				tmp.resetTimer();
			}
		} else {
			result = database.update(table);
			if (result) {
				insertIntoBuff(table);
			}
		}
		return result;
	}

	/**
	 * 动态刷新线程，用于定时的动态刷新，防止系统崩溃导致数据损失
	 */
	private Runnable updateThread = new Runnable() {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(2000);
					for (DataTimer<F> tmp : map.values()) {
						if (tmp.needWriteToDataBase()) {
							database.update(tmp.data);
							tmp.updated();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	/**
	 * 将未写入数据库的记录修改全部写回数据库
	 * 
	 * @param id
	 *            需要执行该操作的记录的主码
	 * @return 成功返回true，失败返回false
	 */
	public boolean flush(T id) {
		DataTimer<F> tmp = map.get(id);
		if (tmp != null) {
			if (tmp.needWriteToDataBase()) {
				database.update(tmp.data);
				tmp.updated();
				return true;
			}
		}
		return false;
	}

	/**
	 * 清除缓存中某记录，如果该记录以及被修改过，会写回数据库
	 * 
	 * @param id
	 *            需要清除的记录的主码
	 * @return 成功返回true，失败返回fasle
	 */
	public boolean clear(T id) {
		DataTimer<F> tmp = map.get(id);
		if (tmp != null) {
			if (tmp.needWriteToDataBase()) {
				database.update(tmp.data);
			}
			map.remove(id);
			return true;
		}
		return false;
	}
}
