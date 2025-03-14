package kr.co.porkandspoon.service;

import kr.co.porkandspoon.dao.ApprovalDAO;
import kr.co.porkandspoon.dto.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	public String saveDraft(String[] appr_user, ApprovalDTO approvalDTO, MultipartFile[] attachedFiles, MultipartFile[] logoFile, String status, String[] new_filename) {
		// 문서번호 생성
		approvalDTO.setDocument_number(generateDocumentNumber(approvalDTO.getTarget_type()));
		
		// draft_idx 가져오기 
		String draftIdx = approvalDTO.getDraft_idx();
		// 새로 저장하는 경우
		if(draftIdx == null || draftIdx.isEmpty()) {
		    draftIdx = String.valueOf(approvalDAO.getDraftIdx());
			approvalDTO.setDraft_idx(draftIdx);
		}

		// draft 테이블에 저장
		int row = approvalDAO.saveDraft(approvalDTO);
		// 결재라인 삭제
		approvalDAO.removeApprovalLine(draftIdx);
		// 결재라인 저장
	    approvalDAO.saveApprovalLine(draftIdx, appr_user, status);

	    // 이미지 정보 저장 (이미지가 있을 경우 반복문 사용)
        List<FileDTO> imgs = approvalDTO.getFileList();
        if (imgs != null && !imgs.isEmpty()) {
            for (FileDTO img : imgs) {
                img.setPk_idx(draftIdx);
                img.setCode_name("draft");
                img.setType("img");
                fileWrite(img); // 게시글 이미지 파일 복사 저장
            }
        }
        
        // 로고파일 저장
        saveFile(logoFile, draftIdx, true);
		// 첨부파일 저장
        saveFile(attachedFiles, draftIdx, false);
        
        // 재상신의 경우
        if(new_filename != null) {
        	for (String filename : new_filename) {
        		approvalDAO.saveExistingFiles(filename, draftIdx);
			}
        }
        
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
	
	private void saveFile(MultipartFile[] files, String draftIdx, boolean logoYn) {
		if(files != null) {
			for(MultipartFile file : files) {
				try {
					if(!file.isEmpty()) {
						String ori_filename = file.getOriginalFilename();
						String ext = ori_filename.substring(ori_filename.lastIndexOf("."));
						String new_filename = UUID.randomUUID()+ext;
						
				        int existingFile = approvalDAO.checkExistingFile(draftIdx, ori_filename);
					    if (existingFile == 0) {
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
					    }
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 이미지 파일 복사 저장
    private void fileWrite(FileDTO img) {
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
	public void updateDraft(String[] appr_user, ApprovalDTO approvalDTO, MultipartFile[] attachedFiles, MultipartFile[] logoFile, String reapproval) {
		int row = approvalDAO.updateDraft(approvalDTO);
		String draftIdx = approvalDTO.getDraft_idx();
		approvalDAO.removeApprovalLine(draftIdx);

		String status = reapproval.equals("true") ? "sd" : "sv";
		approvalDAO.saveApprovalLine(draftIdx, appr_user, status);

		// 이미지 정보 저장 (이미지가 있을 경우 반복문 사용)
		List<FileDTO> imgs = approvalDTO.getFileList();
		if (imgs != null && !imgs.isEmpty()) {
			for (FileDTO img : imgs) {
				img.setPk_idx(draftIdx);
				img.setCode_name("draft");
				img.setType("img"); //이게맞나...?check!!!
				fileWrite(img); // 게시글 이미지 파일 복사 저장
			}
		}
		saveFile(logoFile, draftIdx, true);
		saveFile(attachedFiles, draftIdx, false);
	}

	public void deleteFiles(List<FileDTO> deleteFiles, String draftIdx) {
		for (FileDTO file : deleteFiles) {
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

	public FileDTO getLogoFile(String draft_idx) {
		return approvalDAO.getLogoFile(draft_idx);
	}

	// 현재시간 가져오기(yyyy-MM-dd HH:mm:ss)
	String CreateNowDateTime() {
		// 현재 시간을 LocalDateTime으로 가져옴
		LocalDateTime now = LocalDateTime.now();
		// DATETIME 형식으로 포맷
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// 포맷된 현재 시간
		String formattedDateTime = now.format(formatter);
		return formattedDateTime;
	}

	// 기안문 반려
	public boolean returnDraft(ApprovalDTO approvalDTO) {
		approvalDTO.setApproval_date(CreateNowDateTime());
		int approvalRow = approvalDAO.changeApprovalLineToReturn(approvalDTO);
		int draftRow = approvalDAO.changeStatusToReturn(approvalDTO);
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

}
