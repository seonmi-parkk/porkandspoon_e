package kr.co.porkandspoon.dao;

import kr.co.porkandspoon.dto.FileDTO;
import kr.co.porkandspoon.dto.MailDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Mapper
public interface MailDAO {

	List<Map<String, String>> getUserList();

	int saveMail(MailDTO mailDTO);

	int removeMailReceiver(String mailIdx);
	
	int saveMailReceiver(String mailIdx, HashSet<String> username);

	String getmailIdx();

	int checkExistingFile(String mailIdx, String ori_filename);

	int fileSave(FileDTO fileDto);

	MailDTO getMailInfo(String idx);

	int isBookmarked(String idx, String loginId);

	List<FileDTO> getAttachedFiles(String idx);

	int setBookmark(Map<String, String> params);

	int deleteBookmark(Map<String, String> params);

	List<MailDTO> getSendList(Map<String, Object> params);

	List<MailDTO> getReceiveList(Map<String, Object> params);

	List<MailDTO> getSaveList(Map<String, Object> params);

	List<MailDTO> getBookMark(Map<String, Object> params);

	List<MailDTO> getDeleteList(Map<String, Object> params);

	int changeToRead(List<String> idxList, String loginId);

	int isSender(Map<String, String> params);

	int isReceiver(Map<String, String> params);

	int toggleSentMailBookmark(Map<String, String> params);

	int toggleReceivedMailBookmark(Map<String, String> params);

	String getSentMailBookmark(String idx, String loginId);

	String getReceivedMailBookmark(String idx, String loginId);

	int setDeleveryExistingImage(String mailIdx, String fileId, String originalIdx);

	int savedMailCount(String loginId);

	int moveReceivedToTrash(String idx, String loginId);

	int moveSentToTrash(String idx, String loginId);

	List<MailDTO> getSenderReceivers(String idx);

	int copyMailRow(MailDTO mailDTO);

	int copyMailReceiverRow(String newIdx, String idx);

	int unreadMailCount(String loginId);

	int receivedCompleteDelete(String idx, String loginId);

	int sentCompleteDelete(String idx, String loginId);

	int receivedRestoreFromTrash(String idx, String loginId);

	int sentRestoreFromTrash(String idx, String loginId);

	String getReceivedMailUseFromDate(String idx, String loginId);

	String getSentMailUseFromDate(String idx, String loginId);

	boolean changeToUnread(String idx, String loginId);

	void updateMail(MailDTO mailDTO);
}
