package org.onlineChat.websocket.message;

public class NullMesssageHandler implements MessageHandler<String> {

	private String id;

	public NullMesssageHandler(String key) {
		id = key;
	}

	@Override
	public boolean sendText(String message) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getKey() {
		return id;
	}

}
