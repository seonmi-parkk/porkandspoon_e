package kr.co.porkandspoon.service;

import kr.co.porkandspoon.dao.MailDAO;
import kr.co.porkandspoon.dto.FileDTO;
import kr.co.porkandspoon.dto.MailDTO;
import kr.co.porkandspoon.dto.NoticeDTO;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MailService {

	Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
	private final MailDAO mailDAO;
	private final AlarmService alarmService;

	public MailService(MailDAO mailDAO, AlarmService alarmService) {
		this.mailDAO = mailDAO;
		this.alarmService = alarmService;
	}

	@Value("${upload.path}") String paths;
	@Value("${uploadTem.path}") String tem_path;

	public List<Map<String, String>> getUserList() {
		return mailDAO.getUserList();
	}

	@Transactional
	public String saveMail(HashSet<String> username, MailDTO mailDTO, MultipartFile[] attachedFiles, List<String> existingFileIds , String originalIdx, String status) {
		// summernote 이미지 서버 저장 (이미지가 있을 경우 반복문 사용)
        List<FileDTO> imgs = mailDTO.getFileList();
        if (imgs != null && !imgs.isEmpty()) {
            for (FileDTO img : imgs) {
                fileWrite(img); // 게시글 이미지 파일 복사 저장
            }
        }
        
        // idx 가져오고 없으면 생성
        String mailIdx = mailDTO.getIdx();
        if(mailIdx == null || mailIdx.isEmpty()) {
        	mailIdx = mailDAO.getmailIdx();
 			mailDTO.setIdx(mailIdx);
        }
        mailDAO.saveMail(mailDTO);
        
		//기존 메일수신 정보 삭제
		if(status.equals("sv")) {
			mailDAO.removeMailReceiver(mailIdx);
		}
		// 메일수신 정보저장
	    mailDAO.saveMailReceiver(mailIdx, username);

        // 첨부파일 저장
        saveFile(attachedFiles, mailIdx);
        
        // 전달의 경우 기존 첨부파일
        if(existingFileIds != null) {
        	for (String fileId : existingFileIds) {
				mailDAO.setDeleveryExistingImage(mailIdx, fileId, originalIdx);
			}
        }
        
        // 메일 수신 알림
        if(status.equals("sd")) {
        	NoticeDTO dto = new NoticeDTO();
        	for(String user : username) {
                dto.setUsername(user);
                dto.setFrom_id(mailDTO.getSender());
                dto.setFrom_idx(mailDTO.getIdx());
                dto.setSubject(mailDTO.getTitle());
                dto.setCode_name("ml002");
                alarmService.saveAlarm(dto);
        	}
        }
        return mailIdx;
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

	private void saveFile(MultipartFile[] attachedFiles, String mailIdx) {
		if(attachedFiles != null) {
			for(MultipartFile file : attachedFiles) {
				try {
					if(!file.isEmpty()) {
						String ori_filename = file.getOriginalFilename();
						String ext = ori_filename.substring(ori_filename.lastIndexOf("."));
						String new_filename = UUID.randomUUID()+ext;

						int existingFile = mailDAO.checkExistingFile(mailIdx, ori_filename);
						if (existingFile == 0) {
							// db에 저장
							FileDTO fileDto = new FileDTO();
							fileDto.setOri_filename(ori_filename);
							fileDto.setNew_filename(new_filename);
							fileDto.setCode_name("ma001");
							fileDto.setPk_idx(mailIdx);
							fileDto.setType(file.getContentType());
							mailDAO.fileSave(fileDto);

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

	public MailDTO getMailInfo(String idx) {
		return mailDAO.getMailInfo(idx);
	}

	public boolean isBookmarked(String idx, String loginId) {
		return mailDAO.isBookmarked(idx,loginId) > 0;
	}

	public List<FileDTO> getAttachedFiles(String idx) {
		return mailDAO.getAttachedFiles(idx);
	}

	public boolean updateBookmark(Map<String, String> params) {
		int result = 0;
		if(params.get("isBookmarked").equals("true")) {
			result = mailDAO.deleteBookmark(params);
		}else {
			result = mailDAO.setBookmark(params);
		}
		return result > 0;
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
