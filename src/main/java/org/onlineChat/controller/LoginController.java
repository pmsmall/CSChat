package org.onlineChat.controller;

import org.onlineChat.constants.Constants;
import org.onlineChat.model.Data;
import org.onlineChat.model.DataManager;
import org.onlineChat.service.ILogService;
import org.onlineChat.service.IUserService;
import org.onlineChat.utils.CommonDate;
import org.onlineChat.utils.LogUtil;
import org.onlineChat.utils.NetUtil;
import org.onlineChat.utils.WordDefined;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * TODO : 用户登录与注销
 */
@Controller
@RequestMapping(value = "/user")
public class LoginController {

	// @Resource
	// private IUserService userService;

	@Resource
	private ILogService logService;

	// @Resource
	// private Data data;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String userid, String password, HttpSession session, RedirectAttributes attributes,
			WordDefined defined, CommonDate date, LogUtil logUtil, NetUtil netUtil, HttpServletRequest request) {
		Data data = DataManager.data();
		int result = data.userLogin(userid, password, date);
		System.out.println("LoginController:"+session);

		switch (result) {
		case Constants.NOT_FOUND:
			attributes.addFlashAttribute("error", defined.LOGIN_USERID_ERROR);
			return "redirect:/user/login";

		case Constants.SUCCUESSFULLY:
			logService.insert(logUtil.setLog(userid, date.getTime24(), defined.LOG_TYPE_LOGIN,
					defined.LOG_DETAIL_USER_LOGIN, netUtil.getIpAddress(request)));
			System.out.println("LoginControllor:"+session.getAttribute("userid"));
			session.setAttribute("userid", userid);
			session.setAttribute("login_status", true);
			// user.setLasttime(date.getTime24ToString());
			// userService.update(user);
			System.out.println("LoginControllor:"+session.getAttribute("userid"));
			attributes.addFlashAttribute("message", defined.LOGIN_SUCCESS);
			return "redirect:/chat";
		case Constants.DISABLE:
			attributes.addFlashAttribute("error", defined.LOGIN_USERID_DISABLED);
			return "redirect:/user/login";
		case Constants.ERROR:
		default:
			attributes.addFlashAttribute("error", defined.LOGIN_PASSWORD_ERROR);
			return "redirect:/user/login";
		}
		// User user = userService.selectUserById(userid);
		// if (user == null) {
		// attributes.addFlashAttribute("error", defined.LOGIN_USERID_ERROR);
		// return "redirect:/user/login";
		// } else {
		// if (!user.getPassword().equals(password)) {
		// attributes.addFlashAttribute("error", defined.LOGIN_PASSWORD_ERROR);
		// return "redirect:/user/login";
		// } else {
		// if (user.getStatus() != 1) {
		// attributes.addFlashAttribute("error", defined.LOGIN_USERID_DISABLED);
		// return "redirect:/user/login";
		// } else {
		// logService.insert(logUtil.setLog(userid, date.getTime24(),
		// defined.LOG_TYPE_LOGIN,
		// defined.LOG_DETAIL_USER_LOGIN, netUtil.getIpAddress(request)));
		// session.setAttribute("userid", userid);
		// session.setAttribute("login_status", true);
		// user.setLasttime(date.getTime24ToString());
		// userService.update(user);
		// attributes.addFlashAttribute("message", defined.LOGIN_SUCCESS);
		// return "redirect:/chat";
		// }
		// }
		// }
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session, RedirectAttributes attributes, WordDefined defined) {
		session.removeAttribute("userid");
		session.removeAttribute("login_status");
		attributes.addFlashAttribute("message", defined.LOGOUT_SUCCESS);
		return "redirect:/user/login";
	}
}
