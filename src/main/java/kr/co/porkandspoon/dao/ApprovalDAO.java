package kr.co.porkandspoon.dao;

import kr.co.porkandspoon.dto.ApprovalDTO;
import kr.co.porkandspoon.dto.DeptDTO;
import kr.co.porkandspoon.dto.FileDTO;
import kr.co.porkandspoon.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApprovalDAO {

	UserDTO getUserInfo(String userId);

	int saveDraft(ApprovalDTO approvalDTO);

	Integer getMaxNumberForDate(String prefixDate);

	List<DeptDTO> getDeptList();

	int fileSave(FileDTO fileDto);

	int getDraftIdx();

	int saveApprovalLine(String draftIdx, String[] appr_user, String status);

	ApprovalDTO getDraftInfo(String draft_idx);

	List<ApprovalDTO> getApprLine(String draft_idx);

	List<FileDTO> getAttachedFiles(String draft_idx);

	int updateDraft(ApprovalDTO approvalDTO);

	int removeApprovalLine(String draftIdx);

	int deleteFiles(FileDTO file);

	int checkExistingFile(String draftIdx, String ori_filename);

	FileDTO getLogoFile(String draft_idx);

	int changeApprovalLineToReturn(ApprovalDTO approvalDTO);

	String getUserDept(String loginId);

	String isDraftSender(String draft_idx, String loginId);

	ApprovalDTO approverStatus(String draft_idx, String loginId);

	String isCooperDept(String draft_idx, String userDept);

	String isApproveDept(String draft_idx, String userDept);

	String getDraftStatus(String draft_idx);

	int changeStatusToRead(String loginId, String draft_idx);

	List<String> otherApproversStatus(String draft_idx, String loginId);

	int ApprovalDraft(ApprovalDTO approvalDTO);

	int approvalRecall(String draft_idx);

	int changeStatusToApproved(String draft_idx);

	int changeStatusToReturn(ApprovalDTO approvalDTO);

	int saveExistingFiles(String filename, String draftIdx);

	int changeStatusToSend(String draft_idx);

	int changeStatusToDelete(String draft_idx);

	List<ApprovalDTO> getApprovalMyListData(Map<String, Object> params);

	int setApprLineBookmark(Map<String, Object> params);

	String getMaxBookmarkIdx();

	List<ApprovalDTO> getLineBookmark(Map<String, Object> params);

	int deleteBookmark(String lineIdx, String loginId);

	int changeSenderStatus(String draft_idx, String loginId);

	int haveToApproveCount(String loginId);

	ApprovalDTO userApprovalInfo(ApprovalDTO approvalDTO);

}
