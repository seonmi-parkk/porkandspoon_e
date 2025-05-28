package kr.co.porkandspoon.service;

import kr.co.porkandspoon.dao.MainDAO;
import kr.co.porkandspoon.dao.UserDAO;
import kr.co.porkandspoon.dto.MainDto;
import kr.co.porkandspoon.dto.MenuDTO;
import kr.co.porkandspoon.dto.UserDTO;
import kr.co.porkandspoon.util.security.CustomUserDetails;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {


	Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	private final UserDAO userDAO;
	private final MainDAO menuDAO;
	private final MailService mailService;
	private final ApprovalService approvalService;
	private final ResevationService reservationService;


	public MainService(MainDAO menuDAO, MailService mailService, ApprovalService approvalService, ResevationService reservationService, UserDAO userDAO) {
		this.menuDAO = menuDAO;
		this.mailService = mailService;
		this.approvalService = approvalService;
		this.reservationService = reservationService;
		this.userDAO = userDAO;
	}

	public List<MenuDTO> getMenu(String role) {
		List<MenuDTO> rawMenuList = menuDAO.getMenu(role);
		// 1depth 메뉴
		Map<Integer, MenuDTO> depth1Map = new LinkedHashMap<>();
		for (MenuDTO menu : rawMenuList) {
			if (menu.getDepth() == 1) {
				depth1Map.put(menu.getMenu_idx(), menu);
			}
		}

		// 2depth 메뉴를 1depth에 계층 구조로 붙이기
		for (MenuDTO menu : rawMenuList) {
			if (menu.getDepth() == 2) {
				MenuDTO parent = depth1Map.get(menu.getParent_idx());
				if (parent != null) {
					parent.getChildMenus().add(menu);
				}
			}
		}

		return new ArrayList<>(depth1Map.values());
	}

	// 메뉴 접근 권한 체크
	private boolean hasAccess(String userRole, String menuRoles) {
		// 전체 접근 가능한 메뉴의 경우
		if (menuRoles == null || menuRoles.isBlank()) return true;
		// ROLE_ 접두사 제거
		String UserRoleName = userRole.replace("ROLE_", "").toLowerCase();

		return Arrays.asList(menuRoles.split(",")).contains(UserRoleName);
	}

    public MainDto getMainViewData(CustomUserDetails userDetails) {
		String loginId = userDetails.getUsername();
		// 유저 이름
		String name = userDetails.getName();
		// 프로필이미지
		UserDTO userInfo = userDAO.userDetail(loginId);
		// 미확인 메일
		int unreadMail = mailService.unreadMailCount(loginId);
		// 결재할 문서
		int haveToApprove = approvalService.haveToApproveCount(loginId);
		// 예약 수
		int reservationCount = reservationService.resTotal(userDetails);

		return new MainDto(name,userInfo,unreadMail,haveToApprove,reservationCount);
    }
}
