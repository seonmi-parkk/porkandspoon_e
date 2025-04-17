package kr.co.porkandspoon.dao;

import kr.co.porkandspoon.dto.ApprovalDTO;
import kr.co.porkandspoon.dto.DeptDTO;
import kr.co.porkandspoon.dto.FileDTO;
import kr.co.porkandspoon.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileDAO {

	int saveFile(FileDTO fileDto);

	int deleteFile(FileDTO file);
}
