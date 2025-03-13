package kr.co.porkandspoon.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.porkandspoon.dto.ApprovalDTO;
import kr.co.porkandspoon.dto.FileDTO;
import kr.co.porkandspoon.dto.MailDTO;
import kr.co.porkandspoon.dto.UserDTO;
import kr.co.porkandspoon.service.ApprovalService;
import kr.co.porkandspoon.service.MailService;
import kr.co.porkandspoon.util.CommonUtil;

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
		//ModelAndView mav = new ModelAndView("/approval/approvalList");  
		//logger.info("filter!!@@@@@@ : "+params.get("filter") );
		logger.info("filter!!@@@@@@ : "+params.get("listType") );
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
		int savedMailCount = mailService.savedMailCount(loginId);
		mav.addObject("savedMailCount", savedMailCount);
		UserDTO userDTO = approvalService.getUserInfo(loginId);
		mav.addObject("userDTO", userDTO);
		return mav;
	}

	@Transactional
	@PostMapping(value="/write/{status}")
	public Map<String, Object> MailWrite(@PathVariable String status, @AuthenticationPrincipal UserDetails userDetails, @RequestPart(value="attachedFiles", required = false) MultipartFile[] attachedFiles, @RequestParam(value="existingFileIds", required = false) List<String> existingFileIds, String originalIdx, @RequestParam("imgsJson") String imgsJson, @ModelAttribute MailDTO mailDTO, @RequestParam HashSet<String> username ) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		mailDTO.setSender(userDetails.getUsername());
		mailDTO.setSend_status(status);
		
		logger.info("getSender: "+mailDTO.getSender());
		for (String user : username) {
			logger.info("user::: "+user);
		}
		
		// JSON 문자열을 ImageDTO 리스트로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<FileDTO> imgs = null;
        try {
            // TypeFactory를 사용하여 제네릭 타입을 처리하여 변환
            imgs = objectMapper.readValue(imgsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
            mailDTO.setFileList(imgs);  // 변환한 이미지 리스트를 DTO에 설정
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
        
        String mailIdx = mailService.saveMail(username, mailDTO, attachedFiles, existingFileIds, originalIdx, status);
        logger.info("여기서도 idx가져오기 가능??? : "+ mailDTO.getIdx());
        result.put("mailIdx", mailIdx);
        result.put("status", status);
		return result;
	}
	
	// 메일 상세페이지 view
	@GetMapping(value="/detail/{idx}")
	public ModelAndView MailDetailView(@PathVariable String idx, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
		ModelAndView mav = null;
		String loginId = userDetails.getUsername();
		MailDTO mailInfo = mailService.getMailInfo(idx);
		
		// 권한체크
		// 받는사람
		boolean isReceiver = false;
		// 받는사람 <> 괄호 안의 값을 추출
		String regex = "<([^>]+)>";  
        Pattern pattern = Pattern.compile(regex);
        if(mailInfo.getUsername() != null) {
	        Matcher matcher = pattern.matcher(mailInfo.getUsername());
	
	        List<String> usernames = new ArrayList<>();
	        while (matcher.find()) {
	        	usernames.add(matcher.group(1));
	        }
			
			for (String user : usernames) {
				logger.info("user: {} / loginId: {}",user,loginId);
				if(user.equals(loginId)) {
					isReceiver = true;
					break;
				}
			}
        }
		
        // 보낸사람
		boolean isSender = mailInfo.getSender().equals(loginId);
		
		if(isReceiver || isSender) {
			List<FileDTO> fileList = mailService.getAttachedFiles(idx);
			// 파일 크기 가져오기
			for (FileDTO fileDTO : fileList) {
				String fileName = fileDTO.getNew_filename();
				
		        File file = new File(paths + fileName);
		        if (file.exists()) {
		            long fileSize = file.length();
		            fileDTO.setFile_size(fileSize);
				}
			}
			
			// 전송일시 
			LocalDateTime sendDate = mailInfo.getSend_date();
			 // 요일
	        String dayOfWeek = sendDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN); 
			String send_date = CommonUtil.formatDateTime(sendDate, "yyyy년 MM월 dd일 (" + dayOfWeek + ") a hh:mm");
			mailInfo.setSend_date_str(send_date);
			
			mav = new ModelAndView("/mail/mailDetail"); 
			mav.addObject("mailInfo", mailInfo);
			//mav.addObject("isBookmarked", mailService.isBookmarked(idx, loginId));
			mav.addObject("fileList", fileList);
			mav.addObject("isSender", isSender);
			mav.addObject("isReceiver", isReceiver);
			if(isReceiver) {
				//즐겨찾기
				mav.addObject("is_bookmark", mailService.getReceivedMailBookmark(idx, loginId));
				//삭제(휴지통)일자
				mav.addObject("use_from_date", mailService.getReceivedMailUseFromDate(idx, loginId));
			}else if (isSender) {
				mav.addObject("is_bookmark", mailService.getSentMailBookmark(idx, loginId));
				mav.addObject("use_from_date", mailService.getSentMailUseFromDate(idx, loginId));
			}
		}else {
			try {
				// 메시지를 URL 인코딩
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("<script>alert('열람 권한이 없습니다.'); history.back();</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(isReceiver) {
			// 읽음 처리
			List<String> idxList = new ArrayList<String>();
			idxList.add(idx);
			mailService.changeToRead(idxList, loginId);
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
		//Map<String, Object> result = new HashMap<String, Object>();
//		String loginId = userDetails.getUsername();
//		params.put("username", loginId);
//		String isBookmarked = params.get("isBookmarked");
//		logger.info("bookmark? : "+params.get("isBookmarked"));
//		logger.info("idx? : "+params.get("idx"));
//		boolean success = mailService.updateBookmark(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		params.put("username", loginId);
		
		params.put("is_bookmark", params.get("is_bookmark").equals("Y") ? "N" : "Y");
		// 보낸 메일인지 확인
	    boolean isSender = mailService.isSender(params);
	    if (isSender) {
	        mailService.toggleSentMailBookmark(params);
	        success = true;
	    }

	    // 받은 메일인지 확인
	    boolean isReceiver = mailService.isReceiver(params);
	    if (isReceiver) {
	    	mailService.toggleReceivedMailBookmark(params);
	    	success = true;
	    }
	    result.put("success", success);
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
		logger.info("idx::: "+idx);
		success = mailService.changeToUnread(idx,loginId);
		result.put("success", success);
		return result;
	}
	
	//다중 삭제 처리 기능
	@Transactional
	@PutMapping(value = "/moveToTrash")
	public Map<String, Object> moveToTrash(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails) {    
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		List<String> idxList = params.get("idxList");
		logger.info("idxList count :"+idxList.size());
		for (String idx : idxList) {
			List<MailDTO> senderReceivers = mailService.getSenderReceivers(idx);
			boolean isSender = senderReceivers.get(0).getSender().equals(loginId) ?  true : false;
			boolean isReceiver = false;
			for (MailDTO senderReceiver : senderReceivers) {
				logger.info("Receiver : {} == loginId : {} ", senderReceiver.getUsername(), loginId);
				if(senderReceiver.getUsername() == null) {
					break;
				}
				if(senderReceiver.getUsername().equals(loginId)) {
					isReceiver = true;
					break;
				}
			}
			logger.info("isSender ? : " + isSender);
			logger.info("isReceiver ? : " + isReceiver);
			
			if(isReceiver) {
				success = mailService.moveReceivedToTrash(idx,loginId);
			}
			if(isSender) {
				success = mailService.moveSentToTrash(idx,loginId);
			}
		}
		result.put("success", success);
		return result;
	}
	
	//다중 영구삭제 기능
	@PutMapping(value = "/completeDelete")
	public Map<String, Object> completeDelete(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails) {    
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		List<String> idxList = params.get("idxList");
		logger.info("idxList count :"+idxList.size());
		for (String idx : idxList) {
			List<MailDTO> senderReceivers = mailService.getSenderReceivers(idx);
			boolean isSender = senderReceivers.get(0).getSender().equals(loginId) ?  true : false;
			boolean isReceiver = false;
			for (MailDTO senderReceiver : senderReceivers) {
				logger.info("Receiver : {} == loginId : {} ", senderReceiver.getUsername(), loginId);
				if(senderReceiver.getUsername() == null) {
					break;
				}
				if(senderReceiver.getUsername().equals(loginId)) {
					isReceiver = true;
					break;
				}
			}
			logger.info("isSender ? : " + isSender);
			logger.info("isReceiver ? : " + isReceiver);
			
			if(isReceiver) {
				success = mailService.receivedCompleteDelete(idx,loginId);
			}
			if(isSender) {
				success = mailService.sentCompleteDelete(idx,loginId);
			}
		}
		result.put("success", success);
		return result;
	}
	
	//다중 삭제 취소 기능
		@PutMapping(value = "/restoreFromTrash")
		public Map<String, Object> restoreFromTrash(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails) {    
			Map<String, Object> result = new HashMap<String, Object>();
			boolean success = false;
			String loginId = userDetails.getUsername();
			List<String> idxList = params.get("idxList");
			logger.info("idxList count :"+idxList.size());
			for (String idx : idxList) {
				List<MailDTO> senderReceivers = mailService.getSenderReceivers(idx);
				boolean isSender = senderReceivers.get(0).getSender().equals(loginId) ?  true : false;
				boolean isReceiver = false;
				for (MailDTO senderReceiver : senderReceivers) {
					logger.info("Receiver : {} == loginId : {} ", senderReceiver.getUsername(), loginId);
					if(senderReceiver.getUsername() == null) {
						break;
					}
					if(senderReceiver.getUsername().equals(loginId)) {
						isReceiver = true;
						break;
					}
				}
				logger.info("isSender ? : " + isSender);
				logger.info("isReceiver ? : " + isReceiver);
				
				if(isReceiver) {
					success = mailService.receivedRestoreFromTrash(idx,loginId);
				}
				if(isSender) {
					success = mailService.sentRestoreFromTrash(idx,loginId);
				}
			}
			result.put("success", success);
			return result;
		}
	
	//다중 북마크 토글 기능
	@Transactional
	@PutMapping(value = "/toggleBookmark")
	public Map<String, Object> toggleBookmark(@RequestBody Map<String, List<Map<String, String>>> params, @AuthenticationPrincipal UserDetails userDetails) {    
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		List<Map<String, String>> checkedList = params.get("checkedList");
		
		
		
		
		
//		params.put("is_bookmark", params.get("is_bookmark").equals("Y") ? "N" : "Y");
//		// 보낸 메일인지 확인
//	    boolean isSender = mailService.isSender(params);
//	    if (isSender) {
//	        mailService.toggleSentMailBookmark(params);
//	        success = true;
//	    }
//
//	    // 받은 메일인지 확인
//	    boolean isReceiver = mailService.isReceiver(params);
//	    if (isReceiver) {
//	    	mailService.toggleReceivedMailBookmark(params);
//	    	success = true;
//	    }
//	    result.put("success", success);
	    
	    
	    
	    
	    
		
		for (Map<String, String> checkedItem : checkedList) {
			logger.info("checkedItem"+checkedItem);
			checkedItem.put("username", loginId);
			checkedItem.put("is_bookmark", checkedItem.get("is_bookmark").equals("Y") ? "N" : "Y");
			
			// 보낸 메일인지 확인
		    boolean isSender = mailService.isSender(checkedItem);
		    if (isSender) {
		        mailService.toggleSentMailBookmark(checkedItem);
		        success = true;
		    }

		    // 받은 메일인지 확인
		    boolean isReceiver = mailService.isReceiver(checkedItem);
		    if (isReceiver) {
		    	mailService.toggleReceivedMailBookmark(checkedItem);
		    	success = true;
		    }
		}
		result.put("success", success);
		return result;
	}
	
	// 전달/답장
	@GetMapping(value="/prepareMail/{status}/{idx}")
	public ModelAndView deliverMail(@PathVariable String status, @PathVariable String idx, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){
		ModelAndView mav = new ModelAndView("redirect:/");
		String loginId = userDetails.getUsername();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", loginId);
		params.put("idx", idx);
		
		// 권한 체크
	    boolean isSender = mailService.isSender(params);
	    boolean isReceiver = mailService.isReceiver(params);
	    if (isSender || isReceiver) {
	    	MailDTO mailInfo = mailService.getMailInfo(idx);
	    	mav = new ModelAndView("/mail/mailUpdate");
	    	mav.addObject("mailInfo", mailInfo);
	    	mav.addObject("status", status);
	    	//임시보관 메일 수
			int savedMailCount = mailService.savedMailCount(loginId);
			mav.addObject("savedMailCount", savedMailCount);
	    }else {
	    	try {
				// 메시지를 URL 인코딩
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("<script>alert('해당 메일에 접근 권한이 없습니다.'); history.back();</script>");
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		return mav;
	}
	
	// update시 Filepond 초기값 설정
	@GetMapping(value="/getUploadedFiles/{idx}")
	public List<FileDTO> getUploadedFiles(@PathVariable String idx){
		List<FileDTO> fileList = mailService.getAttachedFiles(idx);
		
		// 파일 크기 가져오기
		for (FileDTO fileDTO : fileList) {
			String fileName = fileDTO.getNew_filename();
			
	        File file = new File(paths + fileName);
	        if (file.exists()) {
	            long fileSize = file.length();
	            fileDTO.setFile_size(fileSize);
			}
		}
		
//		[
//		  {
//		    "id": "12345",
//		    "name": "example.pdf",
//		    "size": 3000000,
//		    "type": "application/pdf"
//		  },
//		  {
//		    "id": "67890",
//		    "name": "image.jpg",
//		    "size": 500000,
//		    "type": "image/jpeg"
//		  }
//		]

		return fileList;
	}
	
	
	// 다시보내기
	@Transactional
	@PostMapping(value="/resend")
	public Map<String, Object> resend(@RequestBody Map<String, List<String>> params, @AuthenticationPrincipal UserDetails userDetails){
		Map<String, Object> result = new HashMap<String, Object>();
		boolean success = false;
		String loginId = userDetails.getUsername();
		for (String idx : params.get("idxList")) {
			MailDTO mailDTO = new MailDTO();
			mailDTO.setIdx(idx);
			mailService.copyMailRow(mailDTO);
			String newIdx = mailDTO.getIdx();
			mailService.copyMailReceiverRow(newIdx,idx);
		}
		success = true;
		result.put("success", success);
		return result;
	}
	
}
