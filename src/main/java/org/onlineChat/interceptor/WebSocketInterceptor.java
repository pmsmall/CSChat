package org.onlineChat.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.onlineChat.constants.Constants;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		System.out.println("有人进来了");
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			if (session != null) {
				// 使用userName区分WebSocketHandler，以便定向发送消息
				String userName = (String) session.getAttribute(Constants.SESSION_USERNAME);
				if (userName == null) {
					userName = "default-system";
				}
				attributes.put(Constants.WEBSOCKET_USERNAME, userName);

			}
		}
		return super.beforeHandshake(request, response, wsHandler, attributes);
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		System.out.println("After Handshake");
		super.afterHandshake(request, response, wsHandler, exception);
	}

}
