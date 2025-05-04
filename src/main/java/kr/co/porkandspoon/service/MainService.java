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

	public List<MenuDTO> getMenu() {
		// 로그인 사용자 권한 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		if (authorities.isEmpty()) {
			throw new IllegalStateException("권한이 없는 사용자입니다.");
		}
		String userRole = authorities.iterator().next().getAuthority();

		// 최종 메뉴 리스트
		List<MenuDTO> menuList = new ArrayList<>();
		// db에서 메뉴 조회
		List<Map<String, Object>> rawData = menuDAO.getMenu();
		// depth1 메뉴 존재 여부(중복생성 방지)의 빠른 조회를 위해 List 대신 Map 사용
		Map<Integer, MenuDTO> depth1MenuMap = new HashMap<>();

		for (Map<String, Object> row : rawData) {
			int depth1Idx = (int) row.get("depth1_idx");
			String depth1Role = (String) row.get("depth1_role");

			// depth1 메뉴 접근 권한 확인
			if (!hasAccess(userRole, depth1Role)) {
				continue;
			}

			MenuDTO depth1Menu = depth1MenuMap.get(depth1Idx);
			if (depth1Menu == null) {
				depth1Menu = MenuDTO.createDepth1(
					depth1Idx,
					(String) row.get("depth1_name"),
					(String) row.get("depth1_url"),
					depth1Role,
					(String) row.get("depth1_icon")
				);
				depth1MenuMap.put(depth1Idx, depth1Menu);
				menuList.add(depth1Menu);
			}

			Integer depth2Idx = (Integer) row.get("depth2_idx");
			// depth2 메뉴인 경우에만 수행
			if (depth2Idx != null) {
				String depth2Role = (String) row.get("depth2_role");

				// depth2 메뉴 접근 권한 확인
				if (!hasAccess(userRole, depth2Role)) {
					continue;
				}

				MenuDTO depth2Menu = MenuDTO.createDepth2(
					depth2Idx,
					(String) row.get("depth2_name"),
					(String) row.get("depth2_url"),
					depth2Role
				);
				depth1Menu.getChildMenus().add(depth2Menu);
			}
		}
		return menuList;
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
