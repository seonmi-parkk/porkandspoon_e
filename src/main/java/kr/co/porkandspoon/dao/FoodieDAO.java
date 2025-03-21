package kr.co.porkandspoon.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.porkandspoon.dto.FoodieDTO;

@Mapper
public interface FoodieDAO {

	// 매장 위도, 경도 중복검사
	int getoverlapStore(FoodieDTO dto);
	
	int setStoreWrite(FoodieDTO dto);

	int setReviewWrirte(FoodieDTO dto);

	List<FoodieDTO> getFoodieList(Map<String, Object> params);

	List<FoodieDTO> getReviewList(FoodieDTO fdto);

	int getstoreidx(FoodieDTO dto);

	int setDeleteR(FoodieDTO dto);

	int getStoreidx(FoodieDTO dto);


}
