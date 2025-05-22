package kr.co.porkandspoon.service;

import kr.co.porkandspoon.dao.ApprovalDAO;
import kr.co.porkandspoon.dto.*;
import kr.co.porkandspoon.enums.*;
import kr.co.porkandspoon.util.JsonUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ApprovalService {

	Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	
	private final ApprovalDAO approvalDAO;
	private final AlarmService alarmService;
	private final FileService fileService;

	public ApprovalService(ApprovalDAO approvalDAO, AlarmService alarmService, FileService fileService) {
		this.approvalDAO = approvalDAO;
		this.alarmService = alarmService;
		this.fileService = fileService;
	}

    @Value("${upload.path}") String paths;
    @Value("${uploadTem.path}") String tem_path;
	
	public UserDTO getUserInfo(String userId) {
		return approvalDAO.getUserInfo(userId);
	}
	
	public List<DeptDTO> getDeptList() {
		return approvalDAO.getDeptList();
	}

	@Transactional
	public String saveDraft(ApprovalDTO approvalDTO, MultipartFile[] attachedFiles, MultipartFile[] logoFile, String status, String[] new_filename) {
		// draft_idx 가져오기
		String draftIdx = approvalDTO.getDraft_idx();
		approvalDTO.setStatus(status);

		if(draftIdx.isEmpty()) {
			/*처음 저장하는 경우*/
			// 문서번호 생성
			approvalDTO.setDocument_number(generateDocumentNumber(approvalDTO.getTarget_type()));

			// draft 테이블에 저장
			approvalDAO.saveDraft(approvalDTO);
			draftIdx = approvalDTO.getDraft_idx();

			// 결재라인 테이블 저장
			approvalDAO.saveApprovalLine(approvalDTO,status);

			// 게시글 이미지 파일 복사 저장 (임시저장 폴더 -> 저장 폴더)
			fileService.moveFiles(approvalDTO.getFileList());
			// 로고파일 저장
			fileService.saveFiles(draftIdx, FileCodeName.BRAND_LOGO.getCode(), logoFile);
			// 첨부파일 저장
			fileService.saveFiles(draftIdx, FileCodeName.DRAFT.getCode(), attachedFiles);
		}else{
			updateDraft(approvalDTO, attachedFiles, logoFile, "false");
		}

		// 임시저장이 아닌 상신의 경우
        if(status.equals(DraftStatus.SUBMITTED.getCode())) {
        	// 알림 요청
        	NoticeDTO noticedto = new NoticeDTO(approvalDTO.getUsername(),draftIdx,AlarmType.APPROVAL_REQUEST.getCode());
    		alarmService.saveAlarm(noticedto);
        }
        return draftIdx;
	}

	@Transactional
	public void updateDraft(ApprovalDTO approvalDTO, MultipartFile[] newFiles, MultipartFile[] logoFile, String reapproval) {
		String draftIdx = approvalDTO.getDraft_idx();
		String status = reapproval.equals("true") ? DraftStatus.SUBMITTED.getCode() : DraftStatus.SAVED.getCode();
		approvalDTO.setStatus(status);
		// draft 테이블에 업데이트
		approvalDAO.updateDraft(approvalDTO);
		// 결재라인 업데이트
		updateApprovalLines(approvalDTO);
		// 게시글 이미지 파일 복사 저장 (임시저장 폴더 -> 저장 폴더)
		fileService.moveFiles(approvalDTO.getFileList());

		// 로고파일 업데이트
		if(logoFile != null) {
			fileService.saveFiles(draftIdx, FileCodeName.BRAND_LOGO.getCode(), logoFile);
		}
		// 첨부파일 업데이트
		if(newFiles != null) {
			fileService.saveFiles(draftIdx, FileCodeName.DRAFT.getCode(), newFiles);
		}
	}

	public ApprovalDTO getDraftInfo(String draft_idx) {
		return approvalDAO.getDraftInfo(draft_idx);
	}

	public List<ApprovalDTO> getApprLine(String draft_idx) {
		List<ApprovalDTO> apprLineList = approvalDAO.getApprLine(draft_idx);
		for (ApprovalDTO approvalDTO : apprLineList) {
			String appr_date = approvalDTO.getApproval_date();
			if(appr_date != null) {
				appr_date = appr_date.substring(0,10);
			}
			approvalDTO.setApproval_date(appr_date);
		}
		return apprLineList;
	}

	public List<FileDTO> getAttachedFiles(String draft_idx) {
		return approvalDAO.getAttachedFiles(draft_idx);
	}

	@Transactional
	public void updateApprovalLines(ApprovalDTO approvalDTO) {
		String draftIdx = approvalDTO.getDraft_idx();
		List<String> appr_user = approvalDTO.getAppr_user();

		//approvalDAO.deleteApprovalLines(draftIdx);

		// 새로운 결재라인 리스트 생성
		List<ApprovalDTO> newApprovalLines = new ArrayList<>();
		for (int i = 0; i < appr_user.size(); i++) {
			ApprovalDTO line = new ApprovalDTO();
			if(i == 0 && approvalDTO.getStatus().equals(DraftStatus.SUBMITTED.getCode())) {
				// 상신 상태이며 기안자인 경우 결재 처리
				line.setStatus(ApprovalStatus.COMPLETED.getCode());
				line.setApproval_date(LocalDateTime.now().toString());
			}else {
				line.setStatus(ApprovalStatus.UNCHECKED.getCode());
			}
			line.setDraft_idx(draftIdx);
			line.setUsername(appr_user.get(i));
			line.setOrder_num(i);
			newApprovalLines.add(line);
		}
		if(!newApprovalLines.isEmpty()) {
			approvalDAO.batchUpdateApprovalLines(newApprovalLines);
		}
	}

	// 문서번호 생성
	@Transactional
	public String generateDocumentNumber(String target_type) {
		String date = new SimpleDateFormat("yyyy").format(new Date());
		String prefix = "";
		if(target_type.equals(DraftTargetType.BRAND.getCode())) {
			prefix = "B";
		}else if(target_type.equals(DraftTargetType.DIRECT_STORE.getCode())) {
			prefix = "P";
		}
		Integer maxNumber = approvalDAO.getMaxNumberForDate(prefix+date);
		int newNumber = (maxNumber == null) ? 1 : maxNumber + 1;
		return prefix + date + String.format("%04d", newNumber);
	}

	public FileDTO getLogoFile(String draft_idx) {
		return approvalDAO.getLogoFile(draft_idx);
	}

	// 현재시간 가져오기(yyyy-MM-dd HH:mm:ss)
	public String CreateNowDateTime() {
		// 현재 시간을 LocalDateTime으로 가져옴
		LocalDateTime now = LocalDateTime.now();
		// DATETIME 형식으로 포맷
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// 포맷된 현재 시간
		String formattedDateTime = now.format(formatter);
		return formattedDateTime;
	}

	// 기안문 반려
	@Transactional
	public boolean returnDraft(ApprovalDTO approvalDTO) {
		approvalDTO.setApproval_date(CreateNowDateTime());
		int approvalRow = approvalDAO.changeApprovalLineToReturn(approvalDTO);
		int draftRow = approvalDAO.changeStatusToReturn(approvalDTO);

		// 결재 반려 알림
		NoticeDTO noticedto = new NoticeDTO(approvalDTO.getUsername(),approvalDTO.getDraft_idx(), AlarmType.APPROVAL_REFUSAL.getCode());
		alarmService.saveAlarm(noticedto);

		return approvalRow > 0 && draftRow > 0;
	}

	public boolean approvalRecall(String draft_idx, String loginId) {
		boolean result = false;
		return approvalDAO.approvalRecall(draft_idx) > 0;
	}

	public int changeStatusToApproved(String draft_idx) {
		return approvalDAO.changeStatusToApproved(draft_idx);
	}

	@Transactional
	public boolean changeStatusToSend(String draft_idx, String loginId) {
		boolean result = false;
		// 기안자여부
		ApprovalAuthDTO authDTO = getDraftAuthInfo(draft_idx);
		// 기안자여부
		boolean isDraftSender = isDraftSender(authDTO,loginId);
		if(isDraftSender) {
			int draftResult = approvalDAO.changeStatusToSend(draft_idx);
			int apprLineResult = approvalDAO.changeSenderStatus(draft_idx,loginId);
			result = draftResult > 0 && apprLineResult > 0;
		}
		return result;
	}

	public boolean changeStatusToDelete(String draft_idx, String loginId) {
		boolean result = false;
		// 기안자여부
		ApprovalAuthDTO authDTO = getDraftAuthInfo(draft_idx);
		boolean isDraftSender = isDraftSender(authDTO,loginId);
		// 기안문 상태 확인(임시저장 or 회수)
		String draftStatus = getDraftStatus(authDTO);
		boolean deletable = draftStatus.equals(DraftStatus.SAVED.getCode()) || draftStatus.equals(DraftStatus.RECALLED.getCode());
		if(isDraftSender && deletable) {
			approvalDAO.changeStatusToDelete(draft_idx);
			result = true;
		}
		return result;
	}


	public Object getApprovalMyListData(Map<String, Object> params) {
		int page_ = Integer.parseInt((String)params.get("page"));
		int cnt_ = Integer.parseInt((String)params.get("cnt"));
		int limit = cnt_;
		int offset = (page_ - 1) * cnt_;
		params.put("limit", limit);
		params.put("offset", offset);

		return approvalDAO.getApprovalMyListData(params);
	}

	@Transactional
	public Map<String, Object> getDraftUpdateViewData(String draftIdx, String loginId, boolean reapproval) {
		ApprovalAuthDTO authDTO = getDraftAuthInfo(draftIdx);
		// 기안자여부
		boolean isDraftSender = isDraftSender(authDTO,loginId);
		boolean permission = false;
		String message = "";
		// 전송 메세지
		if (!isDraftSender) {
			message = "기안문 수정권한이 없습니다.";
		}
		// 수정인 경우(재기안x)
		if (!reapproval) {
			// 임시저장 상태인지 체크
			boolean isSaved = getDraftStatus(authDTO).equals(DraftStatus.SAVED.getCode());
			permission = isDraftSender && isSaved;
			// 상신인 경우
			if (!isSaved) {
				message = "상신된 기안문은 수정할 수 없습니다.";
			}
		} else {
		// 재기안인 경우
			permission = isDraftSender;
		}

		Map<String, Object> result = new HashMap<>();
		result.put("permission", permission);
		result.put("message", message);

		return result;
	}

	@Transactional
	public String draftUpdate(String deletedFilesJson, MultipartFile[] logoFile, MultipartFile[] newFiles, String imgsJson, ApprovalDTO approvalDTO, String reapproval) {
		String draftIdx = approvalDTO.getDraft_idx();

		/* 새 로고파일 첨부시 */
		if (logoFile != null && logoFile.length > 0) {
			// 기존 로고 삭제
			FileDTO logoFileDto = getLogoFile(draftIdx);
			fileService.deleteFiles(draftIdx, FileCodeName.BRAND_LOGO.getCode(), logoFileDto);
		}

		/* 일반 첨부파일 */
		// 삭제할 기존 첨부파일 (json -> List<String> 변환)
		List<String> deletedFiles = JsonUtil.jsonToList(deletedFilesJson, String.class);
		// 삭제 처리
		if(deletedFiles != null ) {
			for (String file : deletedFiles) {
				FileDTO fileDto = new FileDTO();
				fileDto.setNew_filename(file);
				fileService.deleteFiles(draftIdx, FileCodeName.DRAFT.getCode(), fileDto);
			}
		}

		// 새로 업로드된 파일 저장, db 데이터 저장
		updateDraft(approvalDTO, newFiles, logoFile, reapproval);

		return approvalDTO.getDraft_idx();
	}

	// 기안문 열람 권한 체크
	public DraftPermissionResultDTO checkPermission(String draftIdx, String loginId) {
		// 기안자여부
		ApprovalAuthDTO authDTO = getDraftAuthInfo(draftIdx);
		boolean isDraftSender = isDraftSender(authDTO,loginId);
		// 로그인 유저 결재 상태
		String approverStatus = approverStatus(draftIdx, loginId).getStatus();

		// 결재 순서 체크
		boolean approverTurn = isCurrentAndLastApprover(draftIdx, loginId);

		// 로그인 유저의 부서정보
		String userDept = getUserDept(loginId);
		// 협력부서 여부
		boolean isCooperDept = isCooperDept(draftIdx,userDept);
		// 기안부서 여부
		boolean isApproveDept = isApproveDept(draftIdx,userDept);
		// 기안문 삭제 여부
		boolean isDeleted = getDraftStatus(authDTO).equals(DraftStatus.DELELTED.getCode());
		boolean permitted = (isDraftSender || approverStatus != null || isCooperDept || isApproveDept) && !isDeleted;

		String message = "";
		//전송 메세지 셋팅
		if(!permitted) {
			message = "해당 기안문의 열람권한이 없습니다.";
		}
		if(isDeleted) {
			message = "삭제된 기안문입니다.";
		}

		DraftPermissionResultDTO result = new DraftPermissionResultDTO(permitted, message, isDraftSender, approverStatus, approverTurn);
		return result;
	}

	public boolean isCurrentAndLastApprover(String draftIdx, String loginId) {
		boolean approverTurn = false;
		ApprovalDTO userApproverInfo = approverStatus(draftIdx,loginId);

		// 결재자 여부
		if(userApproverInfo != null){
			// 이전 결재자들의 결재상태 (내 순서인지 체크)
			approverTurn = true;
			List<String> otherApproversStatus = otherApproversStatus(draftIdx,loginId);

			boolean lastOrder = userApproverInfo.getOrder_num() == otherApproversStatus.size();

			for (String status : otherApproversStatus) {
				if(!status.equals(ApprovalStatus.COMPLETED.getCode())) {
					approverTurn = false;
					break;
				}
			}
		}
		return approverTurn;
	}

	@Transactional
	public boolean approvalDraft(ApprovalDTO approvalDTO) {
		boolean result = false;
		// 결재라인 테이블 결재자 상태코드 변경
		// 기안문 결재
		approvalDTO.setApproval_date(CreateNowDateTime());
		int approvalRow = approvalDAO.ApprovalDraft(approvalDTO);

		// 마지막 결재자인 경우 기안문테이블 상태코드 변경(결재완료)
		ApprovalDTO approvalInfo = userApprovalInfo(approvalDTO);
		//결재자 결재 순서
		int orderNum = approvalInfo.getOrder_num();
		//총 결재자 수
		int totalCount = approvalInfo.getApproval_line_count();
		if(orderNum == (totalCount-1)) {
			// 마지막 결재자인 경우
			changeStatusToApproved(approvalDTO.getDraft_idx());
		}else {
			// 마지막 결재자가 아닌경우
			// 다음 사람에게 요청 알림
			NoticeDTO noticedto = new NoticeDTO(approvalDTO.getUsername(), approvalDTO.getDraft_idx(), AlarmType.APPROVAL_REQUEST.getCode());
			alarmService.saveAlarm(noticedto);

			// 기안자에게 승인 알림
			NoticeDTO noticedto2 = new NoticeDTO(approvalDTO.getUsername(), approvalDTO.getDraft_idx(), AlarmType.APPROVAL_COMPLETED.getCode());
			alarmService.saveAlarm(noticedto2);
		}

		if(approvalRow > 0) {
			result = true;
		}
		return result;
	}


	@Transactional
	public boolean setApprLineBookmark(Map<String, Object> params) {
		String bookmarkIdx = "";
		if(params.get("line_idx") != null && !params.get("line_idx").equals("")) {
			bookmarkIdx = (String) params.get("line_idx");
		}else {
			bookmarkIdx = approvalDAO.getMaxBookmarkIdx();
		}
		params.put("bookmarkIdx", bookmarkIdx);

		List<String> approvalLines = (List<String>) params.get("approvalLines");
		for (String line : approvalLines) {
			System.out.println("!!!!!!Approval Line: " + line);
		}
		return approvalDAO.setApprLineBookmark(params) > 0;
	}

	public List<ApprovalDTO> getLineBookmark(Map<String, Object> params) {
		if(params.get("page")!=null && params.get("cnt") !=null) {
			int page_ = Integer.parseInt((String) params.get("page"));
			int cnt_ = Integer.parseInt((String) params.get("cnt"));
			int limit = cnt_;
			int offset = (page_ - 1) * cnt_;
			params.put("limit", limit);
			params.put("offset", offset);
		}
		return approvalDAO.getLineBookmark(params);
	}

	//북마크 삭제
	public boolean deleteBookmark(String lineIdx, String loginId) {
		return approvalDAO.deleteBookmark(lineIdx, loginId) > 0;
	}

	public ApprovalAuthDTO getDraftAuthInfo(String draftIdx) {
		return approvalDAO.getDraftAuthInfo(draftIdx);
	}

	//결재할 기안문 수
	public int haveToApproveCount(String loginId) {
		return approvalDAO.haveToApproveCount(loginId);
	}

	public ApprovalDTO userApprovalInfo(ApprovalDTO approvalDTO) {
		return approvalDAO.userApprovalInfo(approvalDTO);
	}

	// 로그인유저 부서정보
	public String getUserDept(String loginId) {
		return approvalDAO.getUserDept(loginId);
	}

	// 열람권한체크
	public boolean isDraftSender(ApprovalAuthDTO authDTO, String loginId) {
		return authDTO.getUsername().equals(loginId);
	}
	// 열람권한체크
	public ApprovalDTO approverStatus(String draft_idx, String loginId) {
		return approvalDAO.approverStatus(draft_idx,loginId);
	}
	// 열람권한체크
	public boolean isCooperDept(String draft_idx, String userDept) {
		return approvalDAO.isCooperDept(draft_idx,userDept) != null;
	}
	// 열람권한체크
	public boolean isApproveDept(String draft_idx, String userDept) {
		return approvalDAO.isApproveDept(draft_idx,userDept) != null;
	}

	public String getDraftStatus(ApprovalAuthDTO authDTO) {
		return authDTO.getStatus();
	}

	public int changeStatusToRead(String loginId, String draft_idx) {
		return approvalDAO.changeStatusToRead(loginId, draft_idx);
	}

	public List<String> otherApproversStatus(String draft_idx, String loginId) {
		return approvalDAO.otherApproversStatus(draft_idx,loginId);
	}

}
