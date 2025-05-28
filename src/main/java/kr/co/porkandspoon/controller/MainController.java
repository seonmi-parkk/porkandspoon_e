package kr.co.porkandspoon.controller;

import kr.co.porkandspoon.dao.UserDAO;
import kr.co.porkandspoon.dto.MainDto;
import kr.co.porkandspoon.dto.MenuDTO;
import kr.co.porkandspoon.dto.UserDTO;
import kr.co.porkandspoon.service.ApprovalService;
import kr.co.porkandspoon.service.MailService;
import kr.co.porkandspoon.service.MainService;
import kr.co.porkandspoon.service.ResevationService;
import kr.co.porkandspoon.util.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

	Logger logger = LoggerFactory.getLogger(getClass());

	private final MainService mainService;
	private final UserDAO userDao;

	public MainController(MainService mainService, MailService mailService, ApprovalService approvalService, UserDAO userDao, ResevationService reservationService) {
		this.mainService = mainService;
		this.userDao = userDao;
	}

	@Value("${upload.path}") String paths;

	@GetMapping(value="/main")
	public ModelAndView mainView(HttpSession session) {
		ModelAndView mav = new ModelAndView("/main");
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		MainDto mainDto = mainService.getMainViewData(userDetails);
		mav.addObject("name", mainDto.getName());
		mav.addObject("userInfo", mainDto.getUserInfo());
		mav.addObject("unreadMail", mainDto.getUnreadMail());
		mav.addObject("haveToApprove", mainDto.getHaveToApprove());
		mav.addObject("reservationCount", mainDto.getReservationCount());
		return mav;
	}
	
	@GetMapping(value="/sidebar")
	public Map<String, Object> getMenu(@AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> result = new HashMap<String, Object>();
		String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
		List<MenuDTO> menuList = mainService.getMenu(role);
		result.put("menuList", menuList);
		return result;
	}

	@GetMapping(value="/header")
	public Map<String, Object> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> result = new HashMap<String, Object>();
		String loginId = userDetails.getUsername();
		// 유저정보
		result.put("userInfo",userDao.userDetail(loginId)); 
		return result;
	}

}
