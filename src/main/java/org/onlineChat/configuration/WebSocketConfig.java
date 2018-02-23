package org.onlineChat.configuration;

import org.onlineChat.interceptor.WebSocketInterceptor;
import org.onlineChat.websocket.message.ChatMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		System.out.println("123");
		registry.addHandler(chatMessageHandler(), "/websocket/chatMessageServer")
				.addInterceptors(new WebSocketInterceptor());
		registry.addHandler(chatMessageHandler(), "/sockjs/chatMessageServer")
				.addInterceptors(new WebSocketInterceptor()).withSockJS();
	}

	@Bean
	public TextWebSocketHandler chatMessageHandler() {
		return new ChatMessageHandler();
	}

}