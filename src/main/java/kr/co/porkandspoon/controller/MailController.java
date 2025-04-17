package kr.co.porkandspoon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.porkandspoon.dto.FileDTO;
import kr.co.porkandspoon.dto.MailDTO;
import kr.co.porkandspoon.service.ApprovalService;
import kr.co.porkandspoon.service.MailService;
import kr.co.porkandspoon.util.CommonUtil;
import kr.co.porkandspoon.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/mail")
public class MailController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private final MailService mailService;
	private final ApprovalService approvalService;

	public MailController(MailService mailService, ApprovalService approvalService) {
		this.mailService = mailService;
		this.approvalService = approvalService;
	}

	@Value("${upload.path}") String paths;
	
	// 메일리스트 view
	@GetMapping(value="/listView/{listType}")
	public ModelAndView mailListView(@PathVariable String listType) {
		ModelAndView mav = new ModelAndView("/mail/mailList");  
		mav.addObject("listType", listType);
		return mav;
	}
	
	// 메일리스트 데이터가져오기
	@GetMapping(value="/list/{listType}")
	public Map<String,Object> getMailListData(@PathVariable String listType, @RequestParam Map<String, Object> params, @AuthenticationPrincipal UserDetails userDetails) {
		String loginId = userDetails.getUsername();
		Map<String,Object> result = new HashMap<String, Object>();
        params.put("loginId", loginId);
        params.put("listType", listType);
		result.put("mailList", mailService.getMailListData(params));
		return result;
	}

	// 메일작성 view
	@GetMapping(value="/write")
	public ModelAndView MailWriteView(@AuthenticationPrincipal UserDetails userDetails) {
		ModelAndView mav = new ModelAndView("/mail/mailWrite");
		String loginId = userDetails.getUsername();
		//임시보관 메일 수
        mav.addObject("savedMailCount", mailService.savedMailCount(loginId));
		// 로그인 유저 정보
        mav.addObject("userDTO", approvalService.getUserInfo(loginId));
		return mav;
	}

	// 메일 발송
	@Transactional
	@PostMapping(value="/write/{status}")
	public Map<String, Object> MailWrite(@PathVariable String status, @RequestPart(value="attachedFiles", required = false) MultipartFile[] attachedFiles, @RequestParam(value="existingFileIds", required = false) List<String> existingFileIds, @RequestParam("imgsJson") String imgsJson, @ModelAttribute MailDTO mailDTO, @RequestParam HashSet<String> username, @AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> result = new HashMap<String, Object>();
		mailDTO.setSender(userDetails.getUsername());
		mailDTO.setSend_status(status);

		// JSON 문자열을 FileDTO 리스트로 변환
        List<FileDTO> imgs = JsonUtil.jsonToList(imgsJson, FileDTO.class);
		mailDTO.setFileList(imgs);  // 변환한 이미지 리스트를 DTO에 설정

        String mailIdx = mailService.saveMail(mailDTO, username, attachedFiles, existingFileIds, status);
        result.put("mailIdx", mailIdx);
        result.put("status", status);
		return result;
	}
	
	// 메일 상세페이지 view
	@GetMapping(value="/detail/{idx}")
	public ModelAndView MailDetailView(@PathVariable String idx, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
		ModelAndView mav = null;
		String loginId = userDetails.getUsername();
		Map<String, Object> result = mailService.mailDetailView(idx, loginId);

		boolean isSender = (Boolean) result.get("isSender");
		boolean isReceiver = (Boolean) result.get("isReceiver");
		if(isSender || isReceiver) {
			mav = new ModelAndView("/mail/mailDetail");
			mav.addObject("mailInfo", result.get("mailInfo"));
			mav.addObject("fileList", result.get("fileList"));
			mav.addObject("isSender", isSender);
			mav.addObject("isReceiver", isReceiver);
			mav.addObject("is_bookmark", result.get("is_bookmark"));
			mav.addObject("use_from_date", result.get("use_from_date"));
		}else {
			// 접근 불가 메세지 인코딩
			CommonUtil.encodeAccessDeniedMessage(response, "열람 권한이 없습니다.");
		}

		return mav;
	}
	
	// 메일작성시 수신자 자동완성
	@GetMapping(value = "/autoComplete", produces="text/plain;charset=UTF-8")
	public String autoComplete(Locale locale, Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, String>> userList = mailService.getUserList();
		String userListStr = "";
		try {
			userListStr = objectMapper.writeValueAsString(userList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return userListStr;
	}
	
	// 즐겨찾기 상태 변경
	@PutMapping(value = "/bookmark")
	public Map<String, Object> updateBookmark(@RequestParam Map<String, String> params, @AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> result = new HashMap<String, Object>();
        String loginId = userDetails.getUsername();
		params.put("username", loginId);
		params.put("is_bookmark", params.get("is_bookmark").equals("Y") ? "N" : "Y");
	    result.put("success", mailService.updateBookmark(params));
	    return result;
	}
	
	//다중 읽음 처리 기능
	@PutMapping(value = "/changeToRead")
	public Map<String, Object> changeToRead(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails) {    
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		success = mailService.changeToRead(params.get("idxList"),loginId);
		result.put("success", success);
		return result;
	}
	
	//개별 안읽음 처리 기능
	@PutMapping(value = "/changeToUnread")
	public Map<String, Object> changeToUnread(String idx, @AuthenticationPrincipal UserDetails userDetails) {    
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		success = mailService.changeToUnread(idx,loginId);
		result.put("success", success);
		return result;
	}
	
	//다중 삭제 처리 기능
	@Transactional
	@PutMapping(value = "/moveToTrash")
	public Map<String, Object> moveToTrash(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails) {    
		Map<String, Object> result = new HashMap<String, Object>();
		String loginId = userDetails.getUsername();
		result.put("success", mailService.moveToTrash(loginId, params));
		return result;
	}
	
	//다중 영구삭제 기능
	@PutMapping(value = "/completeDelete")
	public Map<String, Object> completeDelete(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails) {    
		Map<String, Object> result = new HashMap<String, Object>();
		String loginId = userDetails.getUsername();
		result.put("success", mailService.completeDelete(loginId, params));
		return result;
	}
	
	//다중 삭제 취소 기능
	@PutMapping(value = "/restoreFromTrash")
	public Map<String, Object> restoreFromTrash(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails) {
		Map<String, Object> result = new HashMap<String, Object>();
		String loginId = userDetails.getUsername();
		result.put("success", mailService.restoreFromTrash(loginId, params));
		return result;
	}
	
	//다중 북마크 토글 기능
	@Transactional
	@PutMapping(value = "/toggleBookmark")
	public Map<String, Object> toggleBookmark(@RequestBody Map<String, List<Map<String, String>>> params, @AuthenticationPrincipal UserDetails userDetails) {    
		Map<String, Object> result = new HashMap<String, Object>();
		String loginId = userDetails.getUsername();
		result.put("success", mailService.toggleBookmark(loginId, params));
		return result;
	}
	
	// 전달/답장
	@GetMapping(value="/prepareMail/{status}/{idx}")
	public ModelAndView deliverMail(@PathVariable String status, @PathVariable String idx, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){
		ModelAndView mav = new ModelAndView("redirect:/");
		Map<String, String> params = new HashMap<String, String>();

		String loginId = userDetails.getUsername();
		params.put("username", loginId);
		params.put("idx", idx);
		Map<String,Object> result = mailService.deliverMail(params);

	    if ((boolean) result.get("isSender") || (boolean) result.get("isReceiver")) {
	    	mav = new ModelAndView("/mail/mailUpdate");
	    	mav.addObject("mailInfo", result.get("mailInfo"));
	    	mav.addObject("status", status);
	    	//임시보관 메일 수
			mav.addObject("savedMailCount", result.get("savedMailCount"));
	    }else {
			// 접근 불가 메세지 인코딩
			CommonUtil.encodeAccessDeniedMessage(response, "해당 메일에 접근 권한이 없습니다.");
	    }
		return mav;
	}

	// 다시보내기
	@Transactional
	@PostMapping(value="/resend")
	public Map<String, Object> resend(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", mailService.resend(params));
		return result;
	}
	
}
