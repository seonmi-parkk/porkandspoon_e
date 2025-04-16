package kr.co.porkandspoon.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.porkandspoon.dto.*;
import kr.co.porkandspoon.service.AlarmService;
import kr.co.porkandspoon.service.ApprovalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/approval")
public class ApprovalController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private final ApprovalService approvalService;
	private final AlarmService alarmService;

	public ApprovalController(ApprovalService approvalService, AlarmService alarmService) {
		this.approvalService = approvalService;
		this.alarmService = alarmService;
	}
	
	@Value("${upload.path}") String paths;
	
	// 기안문 작성페이지 뷰
	@GetMapping(value="/writeView")
	public ModelAndView draftView(@AuthenticationPrincipal UserDetails userDetails) {
		String loginId = userDetails.getUsername();
		ModelAndView mav = new ModelAndView("/approval/draftWrite");

		mav.addObject("userDTO", approvalService.getUserInfo(loginId));
		mav.addObject("deptList", approvalService.getDeptList());
		return mav;
	}
	
	// 기안문 저장
	@Transactional
	@PostMapping(value="/write/{status}")
	public Map<String, Object> saveDraft(@RequestPart("logoFile") MultipartFile[] logoFile, @RequestPart(value="attachedFiles", required = false) MultipartFile[] attachedFiles, @RequestParam("imgsJson") String imgsJson, @ModelAttribute ApprovalDTO approvalDTO, @PathVariable String status, String[] new_filename) {
		Map<String, Object> result = new HashMap<String, Object>();

		// JSON 문자열을 FileDTO 리스트로 변환
		List<FileDTO> fileList = jsonToFileDtoList(imgsJson);
		// 변환한 FileDTO 리스트를 approvalDTO에 설정
		approvalDTO.setFileList(fileList);
		// 저장
        String draftIdx = approvalService.saveDraft(approvalDTO, attachedFiles, logoFile, status, new_filename);
		result.put("success", true);
		result.put("draftIdx", draftIdx);
		return result;
	}

	// 기안문 수정 뷰
	@GetMapping(value="/updateView/{draft_idx}/{reapproval}")
	public ModelAndView draftUpdateView(@PathVariable String draft_idx, @PathVariable boolean reapproval, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
		ModelAndView mav = null;

		String loginId = userDetails.getUsername();
		// 수정 권한 및 전달 메세지 처리
		Map<String, Object> result = approvalService.getDraftUpdateViewData(draft_idx, loginId, reapproval);

		if(Boolean.TRUE.equals(result.get("permission"))) {
			mav = new ModelAndView("/approval/draftUpdate");
			// 재기안 or 임시저장 수정 여부
			mav.addObject("reapproval", reapproval);
			// 기안 유저 정보
			mav.addObject("userDTO", approvalService.getUserInfo(loginId));
			// 기안문 정보 mav에 담기
			getDetailInfo(draft_idx, mav);
		}else{
			encodeAccessDeniedMessage(response, (String) result.get("message"));
		}

		return mav;
	}

	// 기안문 수정
	@Transactional
	@PostMapping(value="/update/{reapproval}")
	public Map<String, Object> draftUpdate(@RequestParam("deletedFiles") String deletedFilesJson, @RequestPart(value="logoFile", required = false) MultipartFile[] logoFile, @RequestPart(value="newAttachedFiles",required=false) MultipartFile[] newFiles, @RequestParam("imgsJson") String imgsJson, @ModelAttribute ApprovalDTO approvalDTO, @PathVariable String reapproval) {
		/* 텍스트에디터 이미지 처리*/
		// JSON 문자열을 FileDTO 리스트로 변환
		List<FileDTO> fileList = jsonToFileDtoList(imgsJson);
		// 변환한 FileDTO 리스트를 approvalDTO에 설정
		approvalDTO.setFileList(fileList);
		String draftIdx = approvalService.draftUpdate(deletedFilesJson,logoFile,newFiles,imgsJson,approvalDTO,reapproval);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("draftIdx", draftIdx);
		return result;
	}


	// 기안 상세페이지 뷰
	@GetMapping(value="/detail/{draft_idx}")
	public ModelAndView draftDetailView(@PathVariable String draft_idx, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
		ModelAndView mav = null;

		String loginId = userDetails.getUsername();
		// 기안문 열람 권한 체크
		DraftPermissionResultDTO permissionResult = approvalService.checkPermission(draft_idx, loginId);

		if(permissionResult.isPermitted()){
			mav = new ModelAndView("/approval/draftDetail");
			mav.addObject("isDraftSender", permissionResult.isDraftSender());
			mav.addObject("approverStatus", permissionResult.getApproverStatus());
			mav.addObject("approverTurn", permissionResult.isApproverTurn());
			// 기안문 정보 mav에 담기
			getDetailInfo(draft_idx, mav);
		}else{
			// 접근 불가 메세지 인코딩
			encodeAccessDeniedMessage(response, permissionResult.getMessage());
		}
		return mav;
	}


	// 결재자 상태변경(결재중으로)
	@PutMapping(value="/changeStatusToRead/{draft_idx}")
	public Map<String, Object> changeStatusToRead(@PathVariable String draft_idx, @AuthenticationPrincipal UserDetails userDetails) {
		boolean success = false;
		String loginId = userDetails.getUsername();
		if(approvalService.changeStatusToRead(loginId, draft_idx) > 0) {
			success = true;
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", success);
		return result;
	}

	// 문서리스트 뷰 (나의문서함)
	@GetMapping(value="/listView/{listType}")
	public ModelAndView approvalMyListView(@PathVariable String listType) {
		ModelAndView mav = new ModelAndView("/approval/approvalList");  
		mav.addObject("listType", listType);
		return mav;
	}

	// 문서리스트 (나의문서함)
	@GetMapping(value="/list/{listType}")
	public Map<String,Object> getApprovalMyListData(@PathVariable String listType, @RequestParam Map<String, Object> params, @AuthenticationPrincipal UserDetails userDetails) {
		String loginId = userDetails.getUsername();
		Map<String,Object> result = new HashMap<String, Object>();
        params.put("loginId", loginId);
		result.put("approvalList", approvalService.getApprovalMyListData(params));
			
		return result;
	}

	// 나의 결재라인 리스트 뷰
	@GetMapping(value="/listView/line")
	public ModelAndView approvalLineListView(@AuthenticationPrincipal UserDetails userDetails) {
		ModelAndView mav = new ModelAndView("/approval/approvalLineList");  
		String loginId = userDetails.getUsername();
		UserDTO userDTO = approvalService.getUserInfo(loginId);
		mav.addObject("userDTO", userDTO);
		return mav;
	}

	// 나의 결재라인 리스트
	@GetMapping(value="/list/line")
	public Map<String, Object> approvalLineList(@RequestParam Map<String, Object> params, @AuthenticationPrincipal UserDetails userDetails) {
		String loginId = userDetails.getUsername();
		params.put("loginId", loginId);
		List<ApprovalDTO> bookmarkList = approvalService.getLineBookmark(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bookmarkList", bookmarkList);
		return result;
	}
	
	// 기안문 반려
	@PutMapping(value="/returnDraft")
	public Map<String, Object> returnDraft(@ModelAttribute ApprovalDTO approvalDTO, @AuthenticationPrincipal UserDetails userDetails) {
		approvalDTO.setUsername(userDetails.getUsername());
		boolean success = approvalService.returnDraft(approvalDTO);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success",success);
		return result;
	}

	// 기안문 결재(승인)
	@Transactional
	@PostMapping(value="/ApprovalDraft")
	public Map<String, Object> approvalDraft(@ModelAttribute ApprovalDTO approvalDTO, @AuthenticationPrincipal UserDetails userDetails) {
		boolean success = false;
		approvalDTO.setUsername(userDetails.getUsername());
		// 결재라인 테이블 결재자 상태코드 변경
		int approvalRow = approvalService.ApprovalDraft(approvalDTO);
		
		// 마지막 결재자인 경우 기안문테이블 상태코드 변경(결재완료)
		ApprovalDTO approvalInfo = approvalService.userApprovalInfo(approvalDTO);
		//결재자 결재 순서
		int orderNum = approvalInfo.getOrder_num();
		//총 결재자 수
		int totalCount = approvalInfo.getApproval_line_count();
		if(orderNum == (totalCount-1)) {
			// 마지막 결재자인 경우
			approvalService.changeStatusToApproved(approvalDTO.getDraft_idx());
		}else {
			// 마지막 결재자가 아닌경우
			// 다음 사람에게 요청 알림
			NoticeDTO noticedto = new NoticeDTO(approvalDTO.getUsername(), approvalDTO.getDraft_idx(), "ml007");
			alarmService.saveAlarm(noticedto);
			
			// 기안자에게 승인 요청 알림
			NoticeDTO noticedto2 = new NoticeDTO(approvalDTO.getUsername(), approvalDTO.getDraft_idx(), "ml008");
		    alarmService.saveAlarm(noticedto2);
		}
		
		if(approvalRow > 0) {
			success = true;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success",success);
		return result;
	}
	
	// 기안문 회수
	@PutMapping(value="/recall/{draft_idx}")
	public Map<String, Object> approvalRecall(@PathVariable String draft_idx, @AuthenticationPrincipal UserDetails userDetails){
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		// 기안자여부
		boolean isDraftSender = approvalService.isDraftSender(draft_idx,loginId);
		// 기안문 결재진행중 여부
		boolean ongoingApproval =  approvalService.getDraftStatus(draft_idx).equals("sd");
		if(isDraftSender && ongoingApproval) {
			approvalService.approvalRecall(draft_idx);
			success = true;
		}
		result.put("success", success);
		return result;
	}
	
	// 임시저장 -> 상신
	@PutMapping(value="/changeStatusToSend/{draft_idx}")
	public Map<String, Object> changeStatusToSend(@PathVariable String draft_idx, @AuthenticationPrincipal UserDetails userDetails){
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		// 기안자여부
		boolean isDraftSender = approvalService.isDraftSender(draft_idx,loginId);
		if(isDraftSender) {
			success = approvalService.changeStatusToSend(draft_idx,loginId);
		}
		result.put("success", success);
		return result;
	}

	// 기안문 삭제
	@PutMapping(value="/changeStatusToDelete/{draft_idx}")
	public Map<String, Object> changeStatusToDelete(@PathVariable String draft_idx, @AuthenticationPrincipal UserDetails userDetails){
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		// 기안자여부
		boolean isDraftSender = approvalService.isDraftSender(draft_idx,loginId);
		// 기안문 상태 확인(임시저장or회수)
		String draftStatus = approvalService.getDraftStatus(draft_idx);
		boolean deletable = draftStatus.equals("sv") || draftStatus.equals("ca");
		if(isDraftSender && deletable) {
			approvalService.changeStatusToDelete(draft_idx);
			success = true;
		}
		result.put("success", success);
		return result;
	}
	
	// 선택 유저 데이터 가져오기
	@GetMapping(value="/getUserInfo/{userId}")
	public UserDTO getUserInfo (@PathVariable String userId){
		UserDTO userInfo = approvalService.getUserInfo(userId);
		return userInfo;
	}

	// 조직도 결재라인 즐겨찾기 저장
	@PostMapping(value="/setApprLineBookmark")
	public Map<String, Object> setApprLineBookmark(
		@RequestParam Map<String, Object> params,
		@RequestParam("approvalLines") String approvalLinesJson,
		@AuthenticationPrincipal UserDetails userDetails){
		
		 // JSON 문자열을 List로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> approvalLines = new ArrayList<String>();
		try {
			approvalLines = objectMapper.readValue(approvalLinesJson, new TypeReference<List<String>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
        params.put("approvalLines", approvalLines);
        params.put("loginId", userDetails.getUsername());
        
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", approvalService.setApprLineBookmark(params));
		return result;
	}

	// 조직도 결재라인 삭제
	@DeleteMapping(value="/DeleteBookmark/{lineIdx}")
	public Map<String, Object> deleteBookmark(@PathVariable String lineIdx, @AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> result = new HashMap<String, Object>();
		String loginId = userDetails.getUsername();
		result.put("success", approvalService.deleteBookmark(lineIdx, loginId));
		return result;
	}

	// filePond 이미지 미리보기
	@GetMapping("/filepond/{new_filename}")
	public ResponseEntity<Resource> getFile(@PathVariable String new_filename) {
		logger.info("filePond 이미지 미리보기 요청: {}", new_filename);

		Path path = Paths.get(paths + "/" + new_filename);
		Resource resource = new FileSystemResource(path.toFile());

        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (contentType == null) contentType = "application/octet-stream"; // fallback

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);
	}


	// 접근 권한 없는 경우 메세지 URL 인코딩
	void encodeAccessDeniedMessage(HttpServletResponse response, String message){
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<script>alert('" + message + "'); history.back();</script>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// 기안문 정보 가져오기
	private void getDetailInfo(String draft_idx, ModelAndView mav) {
		ApprovalDTO DraftInfo = approvalService.getDraftInfo(draft_idx);
		List<ApprovalDTO> ApprLine = approvalService.getApprLine(draft_idx);

		mav.addObject("DraftInfo", DraftInfo);
		mav.addObject("ApprLine", ApprLine);
		mav.addObject("logoFile", approvalService.getLogoFile(draft_idx));
		mav.addObject("attachedFiles", approvalService.getAttachedFiles(draft_idx));
		mav.addObject("deptList", approvalService.getDeptList());
	}


	// JSON 문자열을 FileDTO 리스트로 변환
	public List<FileDTO> jsonToFileDtoList(String imgsJson) {
		ObjectMapper objectMapper = new ObjectMapper();
		List<FileDTO> fileDTOList = null;
		try {
			fileDTOList = objectMapper.readValue(imgsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
			//fileDTOList = objectMapper.readValue(imgsJson, new TypeReference<List<FileDTO>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("JSON 문자열을 FileDTO 리스트 변환 실패");
		}
		return fileDTOList;
	}

}
