package kr.co.porkandspoon.dao;

import kr.co.porkandspoon.dto.MenuDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MainDAO {

	List<MenuDTO> getMenuList(String role);

}
