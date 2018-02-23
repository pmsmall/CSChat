package org.onlineChat.pojo;

import org.springframework.stereotype.Repository;

@Repository(value = "message")
public class Message implements Table<String> {

	@Override
	public String getPrimaryKey() {
		return null;
	}

}
