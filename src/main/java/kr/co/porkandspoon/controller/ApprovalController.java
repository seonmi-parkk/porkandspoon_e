package kr.co.porkandspoon.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.porkandspoon.dto.ApprovalDTO;
import kr.co.porkandspoon.dto.FileDTO;
import kr.co.porkandspoon.dto.NoticeDTO;
import kr.co.porkandspoon.dto.UserDTO;
import kr.co.porkandspoon.service.AlarmService;
import kr.co.porkandspoon.service.ApprovalService;

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
	public ModelAndView draftView(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
		String loginId = userDetails.getUsername();
		ModelAndView mav = new ModelAndView("/approval/draftWrite");

		mav.addObject("userDTO", approvalService.getUserInfo(loginId));
		mav.addObject("deptList", approvalService.getDeptList());
		return mav;
	}
	
	// 기안문 저장
	@PostMapping(value="/write/{status}")
	public Map<String, Object> draftWrite(@RequestPart("logoFile") MultipartFile[] logoFile, @RequestPart(value="attachedFiles", required = false) MultipartFile[] attachedFiles, String[] appr_user, @RequestParam("imgsJson") String imgsJson, @ModelAttribute ApprovalDTO approvalDTO, @PathVariable String status, String[] new_filename) {

		Map<String, Object> result = new HashMap<String, Object>();

		// JSON 문자열을 FileDTO 리스트로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<FileDTO> imgs = null;
        try {
            // TypeFactory를 사용하여 제네릭 타입을 처리하여 변환
            imgs = objectMapper.readValue(imgsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
            approvalDTO.setFileList(imgs);  // 변환한 이미지 리스트를 DTO에 설정
        } catch (Exception e) {
            logger.error("파싱 오류 : {}", e.getMessage());
            return Map.of("error", e.getMessage());
        }
        
        // 변환된 이미지 리스트 확인
//        if (imgs != null && !imgs.isEmpty()) {
//            for (FileDTO img : imgs) {
//                logger.info("Original Filename: " + img.getOri_filename());
//                logger.info("New Filename: " + img.getNew_filename());
//            }
//        }
        
        // 저장
        String draftIdx = approvalService.saveDraft(appr_user, approvalDTO, attachedFiles, logoFile, status, new_filename);

		result.put("success", true);
		result.put("draftIdx", draftIdx);
		return result;
	}
	
	@GetMapping(value="/updateView/{draft_idx}/{reapproval}")
	public ModelAndView draftUpdateView(@PathVariable String draft_idx, @PathVariable boolean reapproval, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/approval/draftUpdate");  

		String loginId = userDetails.getUsername();
		// 부서정보
		//String userDept = approvalService.getUserDept(loginId);
		// 기안자 여부
		boolean permission = false;
		boolean isDraftSender = approvalService.isDraftSender(draft_idx,loginId);
		// 전송 메세지
		String message = "";
		if(!isDraftSender) {
			message = "기안문 수정권한이 없습니다.";
		}
		
		// 수정인 경우(재기안x)
		if(!reapproval) {
			// 임시저장 상태인지 체크
			boolean isSavedDraft =  approvalService.getDraftStatus(draft_idx).equals("sv");
			permission = isDraftSender && isSavedDraft ? true : false;
			
			if(!isSavedDraft) {
				message = "상신된 기안문은 수정할 수 없습니다.";
			}
			mav.addObject("reapproval", false);
		}else {
			// 재기안의 경우
			permission = isDraftSender ? true : false;
			mav.addObject("reapproval", true);
		}
		
		// 기안 유저 정보
		mav.addObject("userDTO", approvalService.getUserInfo(loginId));
		
		// 상세페이지 리턴 여부
		boolean returnValue = checkDraftPermission(draft_idx, response, mav, permission, message);
		return returnValue ? mav : null;
	}
	
	@GetMapping(value="/detail/{draft_idx}")
	public ModelAndView draftDetailView(@PathVariable String draft_idx, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/approval/draftDetail");  
		
		
		String loginId = userDetails.getUsername();
		// 부서정보
		String userDept = approvalService.getUserDept(loginId);
		/* 권한체크*/
		// 기안자여부
		boolean isDraftSender = approvalService.isDraftSender(draft_idx,loginId);
		// 본인의 결재상태
		ApprovalDTO userApproverInfo = approvalService.approverStatus(draft_idx,loginId);
		String approverStatus = userApproverInfo.getStatus();
		String approverOrder = userApproverInfo.getOrder_num();
		logger.info("%%%approverStatus !!!!!!!!!: "+ approverStatus);
		// 이전 결재자들의 결재상태 (내 순서인지 체크)
		List<String> otherApproversStatus = approvalService.otherApproversStatus(draft_idx,loginId);
		logger.info("%%%otherApproversStatus !!!!!!! : "+otherApproversStatus);
		boolean approverTurn = true;
		for (String status : otherApproversStatus) {
			logger.info("%%%status: !!!! : "+status);
			if(!status.equals("ap004")) {
				approverTurn = false;
				break;
			}
		}
		logger.info("%%%approverTurn: !!!! : "+approverTurn);
		// 협력부서여부
		boolean isCooperDept = approvalService.isCooperDept(draft_idx,userDept);
		// 기안부서여부
		boolean isApproveDept = approvalService.isApproveDept(draft_idx,userDept);
		// 삭제여부
		boolean isDeleted = approvalService.getDraftStatus(draft_idx).equals("de");
				
		boolean permission = (isDraftSender || approverStatus != null || isCooperDept || isApproveDept) && !isDeleted ? true : false;
		
		mav.addObject("isDraftSender", isDraftSender);
		mav.addObject("approverStatus", approverStatus);
		mav.addObject("approverOrder", approverOrder);
		mav.addObject("approverTurn", approverTurn);
		
		String message = "";
		//전송메세지
		if(!permission) {			
			message = "해당 기안문의 열람권한이 없습니다.";
		}
		if(isDeleted) {
			message = "삭제된 기안문입니다.";
		}
		
		// 상세페이지 리턴 여부
		boolean returnValue = checkDraftPermission(draft_idx, response, mav, permission, message);
		return returnValue ? mav : null;
	}
	
	// 기안문 열람권한 체크후 이동
	boolean checkDraftPermission(String draft_idx, HttpServletResponse response, ModelAndView mav, boolean permission, String message) {

		boolean returnValue = false;
		if(permission) {
			getDetailInfo(draft_idx, mav);
			returnValue = true;
		}else {
			try {
				// 메시지를 URL 인코딩
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("<script>alert('" + message + "'); history.back();</script>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
	
	// 기안문 정보 가져오기
	private void getDetailInfo(String draft_idx, ModelAndView mav) {
		ApprovalDTO DraftInfo = approvalService.getDraftInfo(draft_idx);
		List<ApprovalDTO> ApprLine = approvalService.getApprLine(draft_idx);
		// ObjectMapper를 사용해 ApprLine을 JSON으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonApprLine = "";
        try {
			jsonApprLine = objectMapper.writeValueAsString(ApprLine);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        logger.info("%%%DraftInfo.status!!!!!: "+DraftInfo.getStatus());
		mav.addObject("DraftInfo", DraftInfo);
		mav.addObject("ApprLine", ApprLine);
		mav.addObject("jsonApprLine", jsonApprLine);
		mav.addObject("logoFile", approvalService.getLogoFile(draft_idx));
		mav.addObject("attachedFiles", approvalService.getAttachedFiles(draft_idx));
		mav.addObject("deptList", approvalService.getDeptList());
		//logger.info("ApprLine !!!!!!! : "+ApprLine.get(0));
		//ogger.info("ApprLine !!!!!!! : "+ApprLine.get(1));
		//logger.info("ApprLine !!!!!!! : "+ApprLine.get(1).getUsername());
		//logger.info("DraftInfo getCreate_date!!!! : "+DraftInfo.getCreate_date());
	}
	
	// 결재자 상태변경(결재중으로)
	@PutMapping(value="/changeStatusToRead/{draft_idx}")
	public Map<String, Object> changeStatusToRead(@PathVariable String draft_idx, @AuthenticationPrincipal UserDetails userDetails) {
		logger.info("결재자 상태변경(결재중으로)!!!!!!!!!!!!!!!!!!!!!!!!! ");
		boolean success = false;
		String loginId = userDetails.getUsername();
		logger.info("loginid : " + loginId);
		logger.info("draft_idx : " + draft_idx);
		if(approvalService.changeStatusToRead(loginId, draft_idx) > 0) {
			success = true;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", success);
		return result;
	}

	

	
	
	
	
	
	
//	// 기안문 저장
//		@PostMapping(value="/write/{status}")
//		public Map<String, Object> draftWrite(String[] appr_user, @RequestParam("imgsJson") String imgsJson, @ModelAttribute ApprovalDTO approvalDTO, MultipartFile[] files, @PathVariable String status) {
//			logger.info("appr_user : "+ appr_user);
//			logger.info("appr_user : "+ appr_user[0]);
//			
//			Map<String, Object> result = new HashMap<String, Object>();
//			
//			logger.info("fromdate체크: "+approvalDTO.getFrom_date());
//			
//			// JSON 문자열을 ImageDTO 리스트로 변환
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        List<FileDTO> imgs = null;
//	        try {
//	            // TypeFactory를 사용하여 제네릭 타입을 처리하여 변환
//	            imgs = objectMapper.readValue(imgsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
//	            approvalDTO.setFileList(imgs);  // 변환한 이미지 리스트를 RunBoardDTO에 설정
//	        } catch (Exception e) {
//	            logger.error("파싱 오류 : {}", e.getMessage());
//	            return Map.of("error", e.getMessage());
//	        }
//	        
//	        // 변환된 이미지 리스트 확인
//	        if (imgs != null && !imgs.isEmpty()) {
//	            for (FileDTO img : imgs) {
//	                logger.info("Original Filename: " + img.getOri_filename());
//	                logger.info("New Filename: " + img.getNew_filename());
//	            }
//	        }
//	        
//	       // logger.info("imgDTO : " + approvalDTO.toString());
//	       // logger.info("체크!! : " + approvalDTO.getFileList().get(0).getNew_filename());
//	       // logger.info("체크 getUsername!! : " + approvalDTO.getUsername());
//	        
//	        // 이거 살려 check!!!
//	        String draftIdx = approvalService.saveDraft(appr_user, approvalDTO, files, status);
//
//	        
//
//			logger.info("여기까지옴: " );
//			//approvalService.draftWrite();
//			result.put("success", true);
//			
//			 // 이거 살려 check!!!
//			result.put("draftIdx", draftIdx);
//			return result;
//		}
		
	@Transactional
	@PostMapping(value="/update/{reapproval}")
	public Map<String, Object> draftUpdate(@RequestPart("logoFile") MultipartFile[] logoFile, @RequestPart(value="attachedFiles",required=false) MultipartFile[] attachedFiles, String[] appr_user, @RequestParam("imgsJson") String imgsJson, @RequestParam("deleteFiles") String deleteFilesJson, @ModelAttribute ApprovalDTO approvalDTO, @PathVariable String reapproval) {
		logger.info("연결!!!!!!");
		//logger.info("deleteFiles : "+ deleteFiles);
		//logger.info("appr_user[0] : "+ appr_user[0]);
		//logger.info("appr_user[1] : "+ appr_user[1]);
		//logger.info("appr_user[1] : "+ appr_user[2]);
		//logger.info("appr_user[1] : "+ appr_user[3]);
		
		
		 // deleteFilesJson을 List<FileDTO>로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<FileDTO> deleteFiles = null;
		try {
			deleteFiles = objectMapper.readValue(deleteFilesJson, new TypeReference<List<FileDTO>>(){});
			// 받은 데이터 확인
			System.out.println("Deleted Files: " + deleteFiles);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		approvalService.deleteFiles(deleteFiles, approvalDTO.getDraft_idx());
		
		
		
		//logger.info("deletedFiles.get(0)"+deleteFiles.get(0));
		
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		logger.info("fromdate체크: "+approvalDTO.getFrom_date());
		
		// JSON 문자열을 ImageDTO 리스트로 변환

        List<FileDTO> imgs = null;
        try {
            // TypeFactory를 사용하여 제네릭 타입을 처리하여 변환
            imgs = objectMapper.readValue(imgsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
            approvalDTO.setFileList(imgs);  // 변환한 이미지 리스트를 RunBoardDTO에 설정
        } catch (Exception e) {
            logger.error("파싱 오류 : {}", e.getMessage());
            return Map.of("error", e.getMessage());
        }
        
        // 변환된 이미지 리스트 확인
        if (imgs != null && !imgs.isEmpty()) {
            for (FileDTO img : imgs) {
                logger.info("Original Filename: " + img.getOri_filename());
                logger.info("New Filename: " + img.getNew_filename());
            }
        }
        
       // logger.info("imgDTO : " + approvalDTO.toString());
       // logger.info("체크!! : " + approvalDTO.getFileList().get(0).getNew_filename());
       // logger.info("체크 getUsername!! : " + approvalDTO.getUsername());
        
        approvalService.updateDraft(appr_user, approvalDTO, attachedFiles, logoFile, reapproval);

        

		logger.info("여기까지옴: " );
		//approvalService.draftWrite();
		result.put("success", true);
		result.put("draftIdx", approvalDTO.getDraft_idx());

		return result;
	}
	
	@GetMapping(value="/listView/{listType}")
	public ModelAndView approvalMyListView(@PathVariable String listType) {
		ModelAndView mav = new ModelAndView("/approval/approvalList");  
		mav.addObject("listType", listType);
		return mav;
	}

	@GetMapping(value="/list/{listType}")
	public Map<String,Object> getApprovalMyListData(@PathVariable String listType, @RequestParam Map<String, Object> params, @AuthenticationPrincipal UserDetails userDetails) {
		//ModelAndView mav = new ModelAndView("/approval/approvalList");  
		logger.info("filter!!@@@@@@ : "+params.get("filter") );
		logger.info("filter!!@@@@@@ : "+params.get("listType") );
		String loginId = userDetails.getUsername();
		Map<String,Object> result = new HashMap<String, Object>();
        params.put("loginId", loginId);
        params.put("listType", listType); // check!!! 이거빼도 될듯 jsp에서 보내주는듯
		result.put("approvalList", approvalService.getApprovalMyListData(params));
			
		return result;
	}

	@GetMapping(value="/listView/line")
	public ModelAndView approvalLineListView(@AuthenticationPrincipal UserDetails userDetails) {
		ModelAndView mav = new ModelAndView("/approval/approvalLineList");  
		String loginId = userDetails.getUsername();
		UserDTO userDTO = approvalService.getUserInfo(loginId);
		mav.addObject("userDTO", userDTO);
		return mav;
	}

	@GetMapping(value="/list/line")
	public Map<String, Object> approvalLineList(@RequestParam Map<String, Object> params, @AuthenticationPrincipal UserDetails userDetails) {
		String loginId = userDetails.getUsername();
		params.put("loginId", loginId);
		List<ApprovalDTO> bookmarkList = approvalService.getLineBookmark(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bookmarkList", bookmarkList);
		return result;
	}
	
	@DeleteMapping(value="/DeleteBookmark/{lineIdx}")
	public Map<String, Object> deleteBookmark(@PathVariable String lineIdx, @AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> result = new HashMap<String, Object>();
		String loginId = userDetails.getUsername();
		result.put("success", approvalService.deleteBookmark(lineIdx, loginId));
		return result;
	}
	
	// 기안문 반려
	@PutMapping(value="/returnDraft")
	public Map<String, Object> returnDraft(@ModelAttribute ApprovalDTO approvalDTO, @AuthenticationPrincipal UserDetails userDetails) {
		logger.info("approvalDTO.getComment : "+approvalDTO.getComment());
		approvalDTO.setUsername(userDetails.getUsername());
		boolean success = approvalService.returnDraft(approvalDTO);
		
		// 결재 반려 알림
		NoticeDTO noticedto = new NoticeDTO();
		noticedto.setFrom_idx(approvalDTO.getDraft_idx());
		noticedto.setUsername(approvalDTO.getUsername());
		noticedto.setCode_name("ml009");
		alarmService.saveAlarm(noticedto);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success",success);
		return result;
	}

	// 기안문 승인
	@Transactional
	@PostMapping(value="/ApprovalDraft")
	public Map<String, Object> approvalDraft(@ModelAttribute ApprovalDTO approvalDTO, @AuthenticationPrincipal UserDetails userDetails) {
		boolean success = false;
		logger.info("approvalDTO.getComment : "+approvalDTO.getComment());
		approvalDTO.setUsername(userDetails.getUsername());
		// 결재라인 테이블 결재자 상태코드 변경
		int approvalRow = approvalService.ApprovalDraft(approvalDTO);
		String draftIdx = approvalDTO.getDraft_idx();
		
		// 마지막 결재자인 경우 기안문테이블 상태코드 변경(결재완료)
		ApprovalDTO approvalInfo = approvalService.userApprovalInfo(approvalDTO);
		//결재자 결재 순서
		String orderNum = approvalInfo.getOrder_num();
		//총 결재자 수
		int totalCount = approvalInfo.getApproval_line_count();
		logger.info("orderNum"+orderNum);
		logger.info("totalCount"+totalCount);
		if(Integer.parseInt(orderNum) == (totalCount-1)) {
			// 마지막 결재자인 경우
			logger.info("totalCount==totalCount");
			approvalService.changeStatusToApproved(approvalDTO.getDraft_idx());
		}else {
			// 마지막 결재자가 아닌경우
			// 다음 사람에게 요청 알림
			NoticeDTO noticedto = new NoticeDTO();
			noticedto.setFrom_idx(approvalDTO.getDraft_idx());
			noticedto.setUsername(approvalDTO.getUsername());
			noticedto.setCode_name("ml007");
			alarmService.saveAlarm(noticedto);
			
			// 기안자에게 승인 요청 알림
			NoticeDTO noticedto2 = new NoticeDTO();
			noticedto2.setFrom_idx(approvalDTO.getDraft_idx());
			noticedto2.setUsername(approvalDTO.getUsername());
			noticedto2.setCode_name("ml008");
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

	// 삭제
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
		logger.info("어디까지1");
		if(isDraftSender && deletable) {
			logger.info("어디까지2");
			approvalService.changeStatusToDelete(draft_idx);
			success = true;
			logger.info("어디까지3");
		}
		result.put("success", success);
		return result;
	}
	
	// 조직도 결재라인 설정 (선택한 사람 데이터 가져오기)
	@GetMapping(value="/getUserInfo/{userId}")
	public UserDTO getUserInfo (@PathVariable String userId){
		UserDTO userInfo = approvalService.getUserInfo(userId);
		return userInfo;
	}
	
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

        // 처리된 approvalLines 배열
//        for (String line : approvalLines) {
//            System.out.println("!!!!!!Approval Line: " + line);
//        }
        params.put("approvalLines", approvalLines);
        //params.put("approvalLines", approvalLinesJson);
        logger.info("approvalLinesJson::? "+approvalLinesJson);
        logger.info("approvalLines::? "+approvalLines);

        String loginId = userDetails.getUsername();
        params.put("loginId", loginId);
        
		Map<String, Object> result = new HashMap<String, Object>();
//		for (String line : approvalLines) {
//            System.out.println("Approval Line: " + line);
//        }
		//logger.info("approvalLines"+ params.get("approvalLines"));
		result.put("success", approvalService.setApprLineBookmark(params));
		return result;
	}

}
