package kr.co.porkandspoon.service;

import kr.co.porkandspoon.dao.MainDAO;
import kr.co.porkandspoon.dto.MenuDTO;
import kr.co.porkandspoon.dto.MenuDepth2DTO;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MainService {

	Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	private final MainDAO menuDAO;

	public MainService(MainDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	public List<MenuDTO> getMenu() {
		// 로그인 사용자 권한 얻기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		if (authorities.isEmpty()) {
			throw new IllegalStateException("권한이 없는 사용자입니다.");
		}
		String userRole = authorities.iterator().next().getAuthority();

		List<MenuDTO> menuList = new ArrayList<>();
		List<Map<String, Object>> rawData = menuDAO.getMenu();
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
				depth1Menu = new MenuDTO();
				depth1Menu.setDepth1_idx(depth1Idx);
				depth1Menu.setDepth1_name((String) row.get("depth1_name"));
				depth1Menu.setDepth1_url((String) row.get("depth1_url"));
				depth1Menu.setDepth1_role(depth1Role);
				depth1Menu.setDepth1_icon((String) row.get("depth1_icon"));
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

				MenuDepth2DTO depth2Menu = new MenuDepth2DTO();
				depth2Menu.setDepth2_idx(depth2Idx);
				depth2Menu.setDepth2_name((String) row.get("depth2_name"));
				depth2Menu.setDepth2_url((String) row.get("depth2_url"));
				depth2Menu.setDepth2_role(depth2Role);
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
		String normalizedUserRole = userRole.replace("ROLE_", "").toLowerCase();

		return Arrays.asList(menuRoles.split(",")).contains(normalizedUserRole);
	}

}
