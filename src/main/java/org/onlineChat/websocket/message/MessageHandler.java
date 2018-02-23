package org.onlineChat.websocket.message;

public interface MessageHandler<T> {
	public boolean sendText(String message);

	public T getKey();
}
