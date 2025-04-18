package kr.co.porkandspoon.service;

import kr.co.porkandspoon.dao.MailDAO;
import kr.co.porkandspoon.dto.FileDTO;
import kr.co.porkandspoon.dto.MailDTO;
import kr.co.porkandspoon.dto.NoticeDTO;
import kr.co.porkandspoon.util.CommonUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailService {

	private final FileService fileService;
	Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	private final MailDAO mailDAO;
	private final AlarmService alarmService;

	public MailService(MailDAO mailDAO, AlarmService alarmService, FileService fileService) {
		this.mailDAO = mailDAO;
		this.alarmService = alarmService;
		this.fileService = fileService;
	}

	@Value("${upload.path}") String paths;
	@Value("${uploadTem.path}") String tem_path;

	public List<Map<String, String>> getUserList() {
		return mailDAO.getUserList();
	}

	@Transactional
	public String saveMail(MailDTO mailDTO, HashSet<String> username, MultipartFile[] attachedFiles, List<String> existingFileNames , FileDTO[] deletedFiles, String status) {
		// summernote 이미지 파일 복사 저장 (임시저장 -> 저장 폴더)
        List<FileDTO> imgs = mailDTO.getFileList();
		fileService.moveFiles(imgs);
        
        // idx 가져오고 없으면 생성
        String mailIdx = mailDTO.getIdx();
        if(mailIdx == null || mailIdx.isEmpty()) {
			// 메일 저장
			mailDAO.saveMail(mailDTO);
			mailIdx = mailDTO.getIdx();
			// 메일수신 정보저장
			mailDAO.saveMailReceiver(mailIdx, username);
			// 첨부파일 저장
			fileService.saveFiles(mailIdx, "ma001", attachedFiles);
			// 전달의 경우 첨부 파일 처리
			if(mailDTO.getUpdateStatus() != null && mailDTO.getUpdateStatus().equals("delivery")) {
				deliverMail(existingFileNames, mailIdx, mailDTO.getOriginalIdx());
				// 삭제된 첨부파일 처리
				for(FileDTO fileDTO : deletedFiles) {
					fileDTO.setCode_name("ma001");
					fileDTO.setPk_idx(mailIdx);
					fileService.deleteFileFromDB(fileDTO);
				}
				fileService.deleteFiles(mailIdx, "ma001", deletedFiles);
			}
        }else{
			// 이미 임시저장된 경우
			updateMail(mailDTO, username, attachedFiles, existingFileNames, deletedFiles);
		}
        
        // 메일 수신 알림
        if(status.equals("sd")) {
        	for(String user : username) {
				NoticeDTO noticedto = new NoticeDTO(user, mailDTO.getSender(), mailDTO.getIdx(), "ml002", mailDTO.getTitle());
				alarmService.saveAlarm(noticedto);
        	}
        }
        return mailIdx;
	}

	public void deliverMail(List<String> existingFileNames, String mailIdx, String OriginalMailIdx) {
		// 전달의 경우 기존 첨부파일 처리
		if(existingFileNames != null) {
			for (String fileName : existingFileNames) {
				String cleanedFileName = fileName.replaceAll("[\\[\\]\"]", "").trim();
				mailDAO.setDeleveryExistingImage(mailIdx, cleanedFileName, OriginalMailIdx);
			}
		}
	}

	private void updateMail(MailDTO mailDTO, HashSet<String> username, MultipartFile[] attachedFiles, List<String> existingFileNames, FileDTO[] deletedFiles) {
		String mailIdx = mailDTO.getIdx();
		// 메일 저장
		mailDAO.updateMail(mailDTO);
		//기존 메일수신 정보 삭제
		mailDAO.removeMailReceiver(mailIdx);
		// 메일수신 정보저장
		mailDAO.saveMailReceiver(mailIdx, username);
		// 전달의 경우 첨부 파일 처리
		if(mailDTO.getUpdateStatus().equals("delivery")) {
			deliverMail(existingFileNames, mailIdx, mailDTO.getOriginalIdx());
		}
		// 삭제된 첨부파일 처리
		fileService.deleteFiles(mailIdx, "ma001", deletedFiles);
		// 첨부파일 저장
		fileService.saveFiles(mailIdx, "ma001", attachedFiles);
	}

	public Map<String, Object> mailDetailView(String idx, String loginId) {
		Map<String, Object> result = new HashMap<>();
		MailDTO mailInfo = getMailInfo(idx);

		// 권한체크
		// 받는사람 여부 확인
		boolean isReceiver = false;
		// 받는사람 리스트 <> 괄호 안의 값을 추출
		String regex = "<([^>]+)>";
		Pattern pattern = Pattern.compile(regex);
		if(mailInfo.getUsername() != null) {
			Matcher matcher = pattern.matcher(mailInfo.getUsername());
			List<String> usernames = new ArrayList<>();
			while (matcher.find()) {
				usernames.add(matcher.group(1));
			}

			for (String user : usernames) {
				if(user.equals(loginId)) {
					isReceiver = true;
					break;
				}
			}
		}

		// 보낸사람
		boolean isSender = mailInfo.getSender().equals(loginId);
		result.put("isReceiver", isReceiver);
		result.put("isSender", isSender);

		if(isReceiver || isSender) {
			if(isReceiver) {
				//즐겨찾기 여부
				result.put("is_bookmark", getReceivedMailBookmark(idx, loginId));
				//삭제(휴지통)일자
				result.put("use_from_date", getReceivedMailUseFromDate(idx, loginId));
				// 읽음 처리
				List<String> idxList = new ArrayList<String>();
				idxList.add(idx);
				changeToRead(idxList, loginId);
			} else if (isSender) {
				result.put("is_bookmark", getSentMailBookmark(idx, loginId));
				result.put("use_from_date", getSentMailUseFromDate(idx, loginId));
			}

			// update시 Filepond 초기값(기존업로드 파일) 설정
			List<FileDTO> fileList = getUploadedFiles(idx);
			result.put("fileList", fileList);

			// 전송일시
			LocalDateTime sendDate = mailInfo.getSend_date();
			// 요일 표시 형식 변경
			String dayOfWeek = sendDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
			String send_date = CommonUtil.formatDateTime(sendDate, "yyyy년 MM월 dd일 (" + dayOfWeek + ") a hh:mm");
			mailInfo.setSend_date_str(send_date);

			result.put("mailInfo", mailInfo);
		}
		return result;
	}

	// update시 Filepond 초기값(기존업로드 파일) 설정
	private List<FileDTO> getUploadedFiles(String idx) {
		// 첨부파일 가져오기
		List<FileDTO> fileList = getAttachedFiles(idx);
		// 파일 크기
		for (FileDTO fileDTO : fileList) {
			String fileName = fileDTO.getNew_filename();
			File file = new File(paths + fileName);
			if (file.exists()) {
				long fileSize = file.length();
				fileDTO.setFile_size(fileSize);
			}
		}
		return fileList;
	}

	public boolean updateBookmark(Map<String, String> params) {
		boolean result = false;
		// 보낸 메일인지 확인
		boolean isSender = isSender(params);
		if (isSender) {
			toggleSentMailBookmark(params);
			result = true;
		}
		// 받은 메일인지 확인
		boolean isReceiver = isReceiver(params);
		if (isReceiver) {
			toggleReceivedMailBookmark(params);
			result = true;
		}
		return result;
	}

	public List<MailDTO> getMailListData(Map<String, Object> params) {
		int page_ = Integer.parseInt((String)params.get("page"));
		int cnt_ = Integer.parseInt((String)params.get("cnt"));
		int limit = cnt_;
		int offset = (page_ - 1) * cnt_;
		params.put("limit", limit);
		params.put("offset", offset);

		List<MailDTO> result = new ArrayList<MailDTO>();
		switch (params.get("listType").toString()) {
		case "sd":
			result = mailDAO.getSendList(params);
			break;
		case "recv":
			result = mailDAO.getReceiveList(params);
			break;
		case "sv":
			result = mailDAO.getSaveList(params);
			break;
		case "bk":
			result = mailDAO.getBookMark(params);
			break;
		case "del":
			result = mailDAO.getDeleteList(params);
			break;
		}

		for (MailDTO mailDTO : result) {
			LocalDateTime sendDate = mailDTO.getSend_date();

			// 현재 날짜
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
			String formattedDate = "";
			// 날짜가 오늘이면 시간만 출력
			if (sendDate.toLocalDate().equals(now.toLocalDate())) {
				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
				formattedDate = sendDate.format(timeFormatter);
			} else {
				// 오늘이 아닌 경우 날짜+시간 출력
				formattedDate = sendDate.format(dateFormatter);
			}
			mailDTO.setSend_date_str(formattedDate);
		}
		return result;
	}



	public boolean moveToTrash(String loginId, Map<String, List<String>> params) {
		boolean result = false;
		List<String> idxList = params.get("idxList");

		Map<String, String> data = new HashMap<>();
		data.put("username", loginId);
		for (String idx : idxList) {
			data.put("idx", idx);
			if(isReceiver(data)) {
				result = moveReceivedToTrash(idx,loginId);
			}
			if(isSender(data)) {
				result = moveSentToTrash(idx,loginId);
			}
		}

		return result;
	}

	public boolean completeDelete(String loginId, Map<String, List<String>> params) {
		boolean result = false;
		List<String> idxList = params.get("idxList");
		Map<String, String> data = new HashMap<>();
		data.put("username", loginId);
		for (String idx : idxList) {
			data.put("idx", idx);
			if(isReceiver(data)) {
				result = receivedCompleteDelete(idx,loginId);
			}
			if(isSender(data)) {
				result = sentCompleteDelete(idx,loginId);
			}
		}
		return result;
	}

	public boolean restoreFromTrash(String loginId, Map<String, List<String>> params) {
		boolean result = false;
		List<String> idxList = params.get("idxList");

		Map<String, String> data = new HashMap<>();
		data.put("username", loginId);
		for (String idx : idxList) {
			data.put("idx", idx);
			if(isReceiver(data)) {
				result = receivedRestoreFromTrash(idx,loginId);
			}
			if(isSender(data)) {
				result = sentRestoreFromTrash(idx,loginId);
			}
		}

		return result;
	}

	public boolean toggleBookmark(String loginId, Map<String, List<Map<String, String>>> params) {
		boolean result = false;
		List<Map<String, String>> checkedList = params.get("checkedList");

		for (Map<String, String> checkedItem : checkedList) {
			checkedItem.put("username", loginId);
			checkedItem.put("is_bookmark", checkedItem.get("is_bookmark").equals("Y") ? "N" : "Y");

			// 보낸 메일인지 확인
			boolean isSender = isSender(checkedItem);
			if (isSender) {
				toggleSentMailBookmark(checkedItem);
				result = true;
			}

			// 받은 메일인지 확인
			boolean isReceiver = isReceiver(checkedItem);
			if (isReceiver) {
				toggleReceivedMailBookmark(checkedItem);
				result = true;
			}
		}
		return result;
	}

	public Map<String, Object> deliverMail(Map<String, String> params) {
		Map<String, Object> result = new HashMap<>();
		// 권한 체크
		boolean isSender = isSender(params);
		boolean isReceiver = isReceiver(params);
		result.put("isSender", isSender);
		result.put("isReceiver", isReceiver);

		if (isSender || isReceiver) {
			// 메일 정보
            result.put("mailInfo", getMailInfo(params.get("idx")));
            // 첨부파일 정보
			result.put("attachedFiles", fileService.getAttachedFiles(params.get("idx"),"ma001"));
			//임시보관 메일 수
			result.put("savedMailCount", savedMailCount(params.get("loginId")));
		}

		return result;
	}

	public boolean resend(Map<String, List<String>> params) {
		boolean result = false;
		for (String idx : params.get("idxList")) {
			MailDTO mailDTO = new MailDTO();
			mailDTO.setIdx(idx);

			// mail 테이블 해당 idx row 복사
			int mailResult = copyMailRow(mailDTO);
			String newIdx = mailDTO.getIdx();
			// mail 수신자 테이블 해당 idx row 복사
			int mailReceiveResult = copyMailReceiverRow(newIdx,idx);

			result = mailResult > 0 && mailReceiveResult > 0;
		}
		return result;
	}

	public MailDTO getMailInfo(String idx) {
		return mailDAO.getMailInfo(idx);
	}

	public boolean isBookmarked(String idx, String loginId) {
		return mailDAO.isBookmarked(idx,loginId) > 0;
	}

	public List<FileDTO> getAttachedFiles(String idx) {
		return mailDAO.getAttachedFiles(idx);
	}

	public boolean changeToRead(List<String> idxList, String loginId) {
		return mailDAO.changeToRead(idxList, loginId) > 0;
	}

	public boolean isSender(Map<String, String> params) {
		return mailDAO.isSender(params)  > 0;
	}

	public boolean isReceiver(Map<String, String> params) {
		return mailDAO.isReceiver(params)  > 0;
	}

	public int toggleSentMailBookmark(Map<String, String> params) {
		return mailDAO.toggleSentMailBookmark(params);
	}

	public int toggleReceivedMailBookmark(Map<String, String> params) {
		return mailDAO.toggleReceivedMailBookmark(params);
	}

	public String getSentMailBookmark(String idx, String loginId) {
		return mailDAO.getSentMailBookmark(idx,loginId);
	}

	public String getReceivedMailBookmark(String idx, String loginId) {
		return mailDAO.getReceivedMailBookmark(idx,loginId);
	}

	public int savedMailCount(String loginId) {
		return mailDAO.savedMailCount(loginId);
	}

	public boolean moveSentToTrash(String idx, String loginId) {
		return  mailDAO.moveSentToTrash(idx,loginId) > 0;
	}

	public boolean moveReceivedToTrash(String idx, String loginId) {
		return  mailDAO.moveReceivedToTrash(idx,loginId) > 0;
	}

	public List<MailDTO> getSenderReceivers(String idx) {
		return mailDAO.getSenderReceivers(idx);
	}

	public int copyMailRow(MailDTO mailDTO) {
		return mailDAO.copyMailRow(mailDTO);
	}

	public int copyMailReceiverRow(String newIdx, String idx) {
		return mailDAO.copyMailReceiverRow(newIdx,idx);
	}

	//안읽은 메일수
	public int unreadMailCount(String loginId) {
		return mailDAO.unreadMailCount(loginId);
	}

	public boolean receivedCompleteDelete(String idx, String loginId) {
		return mailDAO.receivedCompleteDelete(idx,loginId) > 0;
	}

	public boolean sentCompleteDelete(String idx, String loginId) {
		return mailDAO.sentCompleteDelete(idx,loginId) > 0;
	}

	public boolean receivedRestoreFromTrash(String idx, String loginId) {
		return mailDAO.receivedRestoreFromTrash(idx,loginId) > 0;
	}

	public boolean sentRestoreFromTrash(String idx, String loginId) {
		return mailDAO.sentRestoreFromTrash(idx,loginId) > 0;
	}

	public String getReceivedMailUseFromDate(String idx, String loginId) {
		return mailDAO.getReceivedMailUseFromDate(idx,loginId);
	}

	public String getSentMailUseFromDate(String idx, String loginId) {
		return mailDAO.getSentMailUseFromDate(idx,loginId);
	}

	public boolean changeToUnread(String idx, String loginId) {
		return mailDAO.changeToUnread(idx,loginId);
	}

}
