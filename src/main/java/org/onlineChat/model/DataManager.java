package org.onlineChat.model;

import javax.annotation.Resource;

import org.onlineChat.model.Data;
import org.springframework.stereotype.Repository;

@Repository
public class DataManager {
	@Resource
	private Data data;
	private static DataManager manager;

	public DataManager() {
		if(DataManager.manager==null)
			DataManager.manager = this;
	}

	public static Data data() {
		return manager.data;
	}
}
