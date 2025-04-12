package kr.co.porkandspoon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.porkandspoon.dao.ApprovalDAO;
import kr.co.porkandspoon.dto.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

	Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	
	private final ApprovalDAO approvalDAO;
	private final AlarmService alarmService;

	public ApprovalService(ApprovalDAO approvalDAO, AlarmService alarmService) {
		this.approvalDAO = approvalDAO;
		this.alarmService = alarmService;
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

			// 게시글 이미지 옮겨 저장
			saveEditorImg(approvalDTO);
			// 로고파일 저장
			saveFiles(logoFile, draftIdx, true);
			// 첨부파일 저장
			saveFiles(attachedFiles, draftIdx, false);
		}else{
			updateDraft(approvalDTO, attachedFiles, logoFile, "false");
		}
        
        // 재상신의 경우
        if(new_filename != null) {
        	for (String filename : new_filename) {
				// 기존파일 저장
        		approvalDAO.saveExistingFiles(filename, draftIdx);
			}
        }

		// 임시저장이 아닌 상신의 경우
        if(status.equals("sd")) {
        	// 알림 요청
        	NoticeDTO noticedto = new NoticeDTO();
        	noticedto.setFrom_idx(draftIdx);
    		noticedto.setUsername(approvalDTO.getUsername());
    		noticedto.setCode_name("ml007");
    		alarmService.saveAlarm(noticedto);
        }
        return draftIdx;
	}

	@Transactional
	public void updateDraft(ApprovalDTO approvalDTO, MultipartFile[] newFiles, MultipartFile[] logoFile, String reapproval) {
		String draftIdx = approvalDTO.getDraft_idx();
		String status = reapproval.equals("true") ? "sd" : "sv";
		approvalDTO.setStatus(status);
		// draft 테이블에 업데이트
		approvalDAO.updateDraft(approvalDTO);
		// 결재라인 업데이트
		updateApprovalLines(approvalDTO);
		// 게시글 이미지 옮겨 저장
		saveEditorImg(approvalDTO);
		// 로고파일 업데이트
		//updateFile(logoFile, draftIdx, true);
		saveFiles(logoFile, draftIdx, true);
		// 첨부파일 업데이트
		//updateFile(newFiles, draftIdx, false);
		saveFiles(newFiles, draftIdx, false);
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

		// 기존 결재라인 삭제
		approvalDAO.deleteApprovalLines(draftIdx);

		// 새로운 결재라인 리스트 생성
		List<ApprovalDTO> newApprovalLines = new ArrayList<>();
		for (int i = 0; i < appr_user.size(); i++) {
			ApprovalDTO line = new ApprovalDTO();
			line.setDraft_idx(draftIdx);
			line.setUsername(appr_user.get(i));
			line.setOrder_num(String.valueOf(i));
			newApprovalLines.add(line);
		}

		if(!newApprovalLines.isEmpty()) {
			approvalDAO.batchInsertApprovalLines(newApprovalLines);

		}
	}

//	@Transactional
//	public void updateFile(MultipartFile[] files, String draftIdx, boolean logoYn) {
//		// 기존 파일 조회
//		List<String> originalFileNames = null;
//		if(logoYn) {
//			// 로고파일일 경우
//			originalFileNames = approvalDAO.getExistingLogoFile(draftIdx);
//		}else{
//			// 일반첨부파일일 경우
//			originalFileNames = approvalDAO.getExistingFile(draftIdx);
//		}
//
//		// 새로운 파일 리스트 생성
//		List<String> newFileNames = new ArrayList<>();
//
//		if(files != null && Arrays.stream(files).anyMatch(file -> !file.isEmpty())) {
//			logger.info("첨부 파일수:"+ files.length);
//			for(MultipartFile file : files) {
//				newFileNames.add(file.getOriginalFilename());
//			}
//		}
//
//		// 기존 파일명과 비교하여 INSERT / DELETE 결정
//		// insert
//		List<String> filesToInsert = new ArrayList<>(newFileNames);
//		filesToInsert.removeAll(originalFileNames); // 새 파일리스트에서 기존 파일 제외
//		// delete
//		List<String> filesToDelete = new ArrayList<>(originalFileNames);
//		filesToDelete.removeAll(newFileNames); // 기존 리스트에서 새 파일 제외
//
//		// 파일 변경 사항 적용
//		if (!filesToDelete.isEmpty()) {
//			for(String file : filesToDelete) {
//				// 파일 삭제
//				deleteFile(file, draftIdx, logoYn);
//			}
//		}
//		if (!filesToInsert.isEmpty()) {
//			for (MultipartFile file : files) {
//				if (filesToInsert.contains(file.getOriginalFilename())) {
//					// 파일 저장
//					saveFile(file, draftIdx, logoYn);
//				}
//			}
//		}
//	}


	@Transactional
	public void saveFiles(MultipartFile[] files, String draftIdx, boolean logoYn) {
		if(files != null) {
			for(MultipartFile file : files) {
				if (!file.isEmpty()) {
					saveFile(file, draftIdx, logoYn);
				}
			}
		}
	}

	@Transactional
	public void saveFile(MultipartFile file, String draftIdx, boolean logoYn) {
		try {
			String ori_filename = file.getOriginalFilename();
			String ext = ori_filename.substring(ori_filename.lastIndexOf("."));
			String new_filename = UUID.randomUUID()+ext;

			// db에 저장
			FileDTO fileDto = new FileDTO();
			fileDto.setOri_filename(ori_filename);
			fileDto.setNew_filename(new_filename);
			if(logoYn) {
				fileDto.setCode_name("bl001");
			}else {
				fileDto.setCode_name("df000");
			}
			fileDto.setPk_idx(draftIdx);
			fileDto.setType(file.getContentType());
			approvalDAO.fileSave(fileDto);

			byte[] arr = file.getBytes();
			// check!! 경로바꾸기
			Path path = Paths.get(paths+new_filename);
			Files.write(path, arr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void deleteFile(String filename, String draftIdx, boolean logoYn) {
		FileDTO fileDto = new FileDTO();
		fileDto.setNew_filename(filename);
		fileDto.setPk_idx(draftIdx);

		// newFileName 가져오기
		//String newFileName = approvalDAO.getNewFileName(fileDto,logoYn);
		//fileDto.setNew_filename(newFileName);
		// 데이터베이스에서 파일 정보 삭제
		approvalDAO.deleteFiles(fileDto);

		// 파일 삭제 (서버 폴더에서)
		try {
			File fileToDelete = new File(paths + filename);
			if (fileToDelete.exists()) {
				boolean deleted = fileToDelete.delete();  // 파일 삭제
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 게시글 이미지 파일 복사 저장 (임시저장 폴더 -> 저장 폴더)
	public void saveEditorImg(ApprovalDTO approvalDTO) {
		List<FileDTO> imgs = approvalDTO.getFileList();
		if (imgs != null && !imgs.isEmpty()) {
			for (FileDTO img : imgs) {
				img.setPk_idx(approvalDTO.getDraft_idx());
				img.setCode_name("draft");
				img.setType("img");

				// 복사할 파일
				File srcFile = new File(tem_path + img.getNew_filename());
				// 목적지 파일
				File descDir = new File(paths + img.getNew_filename());
				try {
					Path filePath = Paths.get(paths, img.getNew_filename());
					if(Files.exists(filePath)) {
						// 파일 복사
						Files.copy(srcFile.toPath(), descDir.toPath());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 문서번호 생성
	@Transactional
	public String generateDocumentNumber(String target_type) {
		String date = new SimpleDateFormat("yyyy").format(new Date());
		String prefix = "";
		if(target_type.equals("df001")) {
			prefix = "B";
		}else if(target_type.equals("df002")) {
			prefix = "P";
		}
		Integer maxNumber = approvalDAO.getMaxNumberForDate(prefix+date);
		int newNumber = (maxNumber == null) ? 1 : maxNumber + 1;
		return prefix + date + String.format("%04d", newNumber);
	}

	@Transactional
	public void deleteFiles(List<FileDTO> deleteFiles, String draftIdx) {
		for (FileDTO file : deleteFiles) {
			if(file != null) {
				String filePath = file.getNew_filename();
				// 파일 삭제 (서버 폴더에서)
				try {
					File fileToDelete = new File(paths + filePath);
					if (fileToDelete.exists()) {
						boolean deleted = fileToDelete.delete();  // 파일 삭제
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 데이터베이스에서 파일 정보 삭제
				file.setPk_idx(draftIdx);
				approvalDAO.deleteFiles(file);
			}
		}

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
		NoticeDTO noticedto = new NoticeDTO();
		noticedto.setFrom_idx(approvalDTO.getDraft_idx());
		noticedto.setUsername(approvalDTO.getUsername());
		noticedto.setCode_name("ml009");
		alarmService.saveAlarm(noticedto);

		return approvalRow > 0 && draftRow >0 ? true : false;
	}

	// 기안문 결재
	public int ApprovalDraft(ApprovalDTO approvalDTO) {
		approvalDTO.setApproval_date(CreateNowDateTime());
		return approvalDAO.ApprovalDraft(approvalDTO);
	}

	// 로그인유저 부서정보
	public String getUserDept(String loginId) {
		return approvalDAO.getUserDept(loginId);
	}

	// 열람권한체크
	public boolean isDraftSender(String draft_idx, String loginId) {
		return approvalDAO.isDraftSender(draft_idx,loginId) != null;
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

	public String getDraftStatus(String draft_idx) {
		return approvalDAO.getDraftStatus(draft_idx);
	}

	public int changeStatusToRead(String loginId, String draft_idx) {
		return approvalDAO.changeStatusToRead(loginId, draft_idx);
	}

	public List<String> otherApproversStatus(String draft_idx, String loginId) {
		return approvalDAO.otherApproversStatus(draft_idx,loginId);
	}

	public int approvalRecall(String draft_idx) {
		return approvalDAO.approvalRecall(draft_idx);
	}

	public int changeStatusToApproved(String draft_idx) {
		return approvalDAO.changeStatusToApproved(draft_idx);
	}

	@Transactional
	public boolean changeStatusToSend(String draft_idx, String loginId) {
		int result2 = approvalDAO.changeStatusToSend(draft_idx);
		int result = approvalDAO.changeSenderStatus(draft_idx,loginId);
		return result > 0 && result2 > 0;
	}

	public int changeStatusToDelete(String draft_idx) {
		return approvalDAO.changeStatusToDelete(draft_idx);
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

	//결재할 기안문 수
	public int haveToApproveCount(String loginId) {
		return approvalDAO.haveToApproveCount(loginId);
	}

	public ApprovalDTO userApprovalInfo(ApprovalDTO approvalDTO) {
		return approvalDAO.userApprovalInfo(approvalDTO);
	}

	@Transactional
	public Map<String, Object> getDraftUpdateViewData(String draftIdx, String loginId, boolean reapproval) {
		// 기안자 여부
		boolean isDraftSender = isDraftSender(draftIdx, loginId);
		boolean permission = false;
		String message = "";
		// 전송 메세지
		if (!isDraftSender) {
			message = "기안문 수정권한이 없습니다.";
		}
		// 수정인 경우(재기안x)
		if (!reapproval) {
			// 임시저장 상태인지 체크
			boolean isSaved = getDraftStatus(draftIdx).equals("sv");
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
		//logger.info("deletedFileIdsJson!! : " + deletedFilesJson);
		String draftIdx = approvalDTO.getDraft_idx();

		/* 새 로고파일 첨부시 */
		if (logoFile != null && logoFile.length > 0) {
			// 기존 로고 삭제
			List<FileDTO> logoDtoList = new ArrayList<>();
			FileDTO logoFileDto = getLogoFile(draftIdx);
			logoDtoList.add(logoFileDto);
			deleteFiles(logoDtoList, draftIdx);
		}

		/* 일반 첨부파일 */
		// 삭제할 기존 첨부파일 (json -> List<String> 변환)
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> deletedFiles = new ArrayList<>();
		try {
			deletedFiles = objectMapper.readValue(deletedFilesJson, new TypeReference<List<String>>() {});
		} catch (JsonProcessingException e) {
			throw new RuntimeException("파일 ID JSON 파싱 실패", e);
		}
		// 삭제 처리
		for (String file : deletedFiles) {
			deleteFile(file, draftIdx,false);
		}

		// 새로 업로드된 파일 저장, db 데이터 저장
		updateDraft(approvalDTO, newFiles, logoFile, reapproval);
		return approvalDTO.getDraft_idx();
	}
}
