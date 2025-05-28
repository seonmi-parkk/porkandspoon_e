package kr.co.porkandspoon.dto;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class MenuDTO {
  
	private int menu_idx;
	private String menu_name;
	private String url;
	private String role;
	private String icon;
	private int depth;
	private int parent_idx;
    private List<MenuDTO> childMenus = new ArrayList<MenuDTO>();

	public MenuDTO() {}

	// Depth1 생성
	public static MenuDTO createDepth1(Integer idx, String name, String url, String role, String icon) {
		MenuDTO menu = new MenuDTO();
		menu.menu_idx = idx;
		menu.menu_name = name;
		menu.url = url;
		menu.role = role;
		menu.icon = icon;
		return menu;
	}

	// Depth2 생성
	public static MenuDTO createDepth2(Integer idx, String name, String url, String role) {
		MenuDTO menu = new MenuDTO();
		menu.menu_idx = idx;
		menu.menu_name = name;
		menu.url = url;
		menu.role = role;
		return menu;
	}


	public int getParent_idx() {
		return parent_idx;
	}

	public void setParent_idx(int parent_idx) {
		this.parent_idx = parent_idx;
	}

	public int getIdx() { return menu_idx; }

	public String getMenu_name() {
		return menu_name;
	}

	public String getUrl() {
		return url;
	}

	public String getRole() {
		return role;
	}

	public String getIcon() {
		return icon;
	}

	public List<MenuDTO> getChildMenus() {
		return childMenus;
	}

	public void setIdx(int idx) {
		this.menu_idx = idx;
	}

	public void setName(String menu_name) {
		this.menu_name = menu_name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	public int getMenu_idx() {
		return menu_idx;
	}

	public void setChildMenus(List<MenuDTO> childMenus) {
		this.childMenus = childMenus;
	}





	
}
