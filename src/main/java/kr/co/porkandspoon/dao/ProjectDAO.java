package kr.co.porkandspoon.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.porkandspoon.dto.ProjectDTO;
import kr.co.porkandspoon.dto.UserDTO;

@Mapper
public interface ProjectDAO {

	UserDTO getUserInfo(String username);

	int setProject(Map<String, String> params);

	List<ProjectDTO> getProject(String loginId, boolean includeCompleted, boolean includeInProgress, boolean isOpen, String searchKeyword);

	List<ProjectDTO> getKanBanInfo(String project_idx);

	int setTask(Map<String, String> params);

	int editStatus(Map<String, String> params);

	ProjectDTO getProjectInfo(String project_idx);

	String getPercent(String loginId);

	int editPercent(String percent, String project_idx);

	void setProjectPeoloe(String project_idx, String loginId);

	void editProject(Map<String, String> params);

	void saveApprovalLine(String project_idx, String username);

	void deletePeople(String project_idx);

	List<UserDTO> getUsersInfo(String loginId, String project_idx);

	void delProject(String project_idx);

	List<ProjectDTO> getProjectScroll(String loginId, boolean includeCompleted, boolean includeInProgress,
			boolean isOpen, String searchKeyword, int offset, int pageSize);

	String getProjectPeople(String loginId, String project_idx);

	


}
