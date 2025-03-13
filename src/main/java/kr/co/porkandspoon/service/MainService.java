package kr.co.porkandspoon.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.porkandspoon.dao.MainDAO;
import kr.co.porkandspoon.dto.MenuDTO;
import kr.co.porkandspoon.dto.MenuDepth2DTO;

@Service
public class MainService {

	Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	private final MainDAO menuDAO;

	public MainService(MainDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	public List<MenuDTO> getMenu() {
		List<MenuDTO> menuList = new ArrayList<>();
        List<Map<String, Object>> rawData = menuDAO.getMenu();

        Map<Integer, MenuDTO> depth1MenuMap = new HashMap<>();

        for (Map<String, Object> row : rawData) {
            int depth1Idx = (int) row.get("depth1_idx");
            MenuDTO depth1Menu = depth1MenuMap.get(depth1Idx);

            if (depth1Menu == null) {
            	depth1Menu = new MenuDTO();
            	depth1Menu.setDepth1_idx(depth1Idx);
            	depth1Menu.setDepth1_name((String) row.get("depth1_name"));
            	depth1Menu.setDepth1_url((String) row.get("depth1_url"));
            	depth1Menu.setDepth1_role((String) row.get("depth1_role"));   
            	depth1Menu.setDepth1_icon((String) row.get("depth1_icon"));   
            	depth1MenuMap.put(depth1Idx, depth1Menu);
                menuList.add(depth1Menu);
            }

            Integer depth2Idx = (Integer) row.get("depth2_idx");
            if (depth2Idx != null) {
            	MenuDepth2DTO depth2Menu = new MenuDepth2DTO();
            	depth2Menu.setDepth2_idx(depth2Idx);
            	depth2Menu.setDepth2_name((String) row.get("depth2_name"));
            	depth2Menu.setDepth2_url((String) row.get("depth2_url"));
            	depth2Menu.setDepth2_role((String) row.get("depth2_role"));
                depth1Menu.getChildMenus().add(depth2Menu);
            }
        }

        return menuList;
	}
	


}
