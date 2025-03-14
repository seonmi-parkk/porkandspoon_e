package kr.co.porkandspoon.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MainDAO {

	List<Map<String, Object>> getMenu();

}
