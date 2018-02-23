package org.onlineChat.service;

import java.util.List;

import org.onlineChat.pojo.Table;

/**
 * 声明了Service层的统一格式接口，为泛型，其中Table是DAO层表数据格式
 * 
 * @author Frank
 *
 * @param <E>
 *            DAO层Table类型
 * @param <T>
 *            DAO层Table类型使用的泛型，是主码的类型
 */
public interface DatabaseService<E extends Table<T>, T> {
	/**
	 * 检索规定范围的记录
	 * 
	 * @param page
	 *            起始位置
	 * @param pageSize
	 *            检索深度
	 * @return 返回包含检索记录的List
	 */
	List<E> selectAll(int page, int pageSize);

	/**
	 * 通过主码检索指定记录
	 * 
	 * @param primaryKey
	 *            主码
	 * @return 检索结果
	 */
	E selectUserById(T primaryKey);

	/**
	 * 查找记录数
	 * 
	 * @param pageSize
	 *            指定检索深度
	 * @return 返回记录数
	 */
	int selectCount(int pageSize);

	/**
	 * 插入一条记录e
	 * 
	 * @param e
	 *            要查插入的数据
	 * @return 插入成功返回true，失败返回false
	 */
	boolean insert(E e);

	/**
	 * 更新一条记录
	 * 
	 * @param e
	 *            更新后的记录
	 * @return 更新成功返回true，失败返回false
	 */
	boolean update(E e);

	/**
	 * 删除一条记录
	 * 
	 * @param primaryKey
	 *            要删除的记录
	 * @return 删除成功返回true，失败返回false
	 */
	boolean delete(T primaryKey);
}
