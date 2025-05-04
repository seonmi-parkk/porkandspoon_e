package kr.co.porkandspoon.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuDTO {
  
	private int idx;
	private String name;
	private String url;
	private String role;
	private String icon;
    private List<MenuDTO> childMenus = new ArrayList<MenuDTO>();

	public MenuDTO() {}

	// Depth1 생성
	public static MenuDTO createDepth1(Integer idx, String name, String url, String role, String icon) {
		MenuDTO menu = new MenuDTO();
		menu.idx = idx;
		menu.name = name;
		menu.url = url;
		menu.role = role;
		menu.icon = icon;
		return menu;
	}

	// Depth2 생성
	public static MenuDTO createDepth2(Integer idx, String name, String url, String role) {
		MenuDTO menu = new MenuDTO();
		menu.idx = idx;
		menu.name = name;
		menu.url = url;
		menu.role = role;
		return menu;
	}

	public int getIdx() { return idx; }

	public String getName() {
		return name;
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
		this.idx = idx;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setChildMenus(List<MenuDTO> childMenus) {
		this.childMenus = childMenus;
	}



	
}
