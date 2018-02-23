package org.onlineChat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.onlineChat.constants.Constants;
import org.onlineChat.websocket.message.ChatMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

@Controller
public class WebSocketController {

	@Bean // 这个注解会从Spring容器拿出Bean
	public ChatMessageHandler infoHandler() {
		return new ChatMessageHandler();
	}

	@RequestMapping("/websocket/login")
	public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("进入web socket");
		String username = request.getParameter("username");
		HttpSession session = request.getSession(false);
		session.setAttribute(Constants.SESSION_USERNAME, username);

		// response.sendRedirect("/ship/websocket/ws.jsp");
	}

	@RequestMapping("/websocket/send")
	@ResponseBody
	public String send(HttpServletRequest request) {

		String username = request.getParameter("username");
		infoHandler().sendMessageToUser(username, new TextMessage("你好，测试！！！！"));

		return null;
	}

}
