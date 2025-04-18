package kr.co.porkandspoon.dto;

import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class MailDTO {
	// 메일 테이블
	private String idx;
	private String sender;
	private String title;
	private String content;
	private LocalDateTime send_date;
	private String send_date_str;
	private String send_status;
	private String use_yn;
	private String use_from_date;
	private String is_bookmark;
	private String originalIdx;

	// 메일 수신테이블
	private String username;
	private String is_read;
	private String read_date;

	private String updateStatus;
	
	// 이름
	private String name;
	// 부서명
	private String dept_name;
	// 직급명
	private String position_name;
	
	 // 이미지 리스트
	private List<FileDTO> fileList;

	// 첨부파일 여부
	private String fileYn;

	// 메일타입 (수신/발신)
	private String mail_type;
	
	// 리스트 글 개수 카운트
    private int total_count;
    
	public String getIdx() {
		return idx;
	}
	public String getSender() {
		return sender;
	}
	public String getUsername() {
		return username;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public LocalDateTime getSend_date() {
		return send_date;
	}
	public String getSend_status() {
		return send_status;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public String getUse_from_date() {
		return use_from_date;
	}
	public String getIs_bookmark() {
		return is_bookmark;
	}
	public String getIs_read() {
		return is_read;
	}
	public String getRead_date() {
		return read_date;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setSend_date(LocalDateTime send_date) {
		this.send_date = send_date;
	}
	public void setSend_status(String send_status) {
		this.send_status = send_status;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public void setUse_from_date(String use_from_date) {
		this.use_from_date = use_from_date;
	}
	public void setIs_bookmark(String is_bookmark) {
		this.is_bookmark = is_bookmark;
	}
	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}
	public void setRead_date(String read_date) {
		this.read_date = read_date;
	}
	public List<FileDTO> getFileList() {
		return fileList;
	}
	public void setFileList(List<FileDTO> fileList) {
		this.fileList = fileList;
	}
	public String getName() {
		return name;
	}
	public String getDept_name() {
		return dept_name;
	}
	public String getPosition_name() {
		return position_name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}
    public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public String getFileYn() {
		return fileYn;
	}
	public void setFileYn(String fileYn) {
		this.fileYn = fileYn;
	}
	public String getMail_type() {
		return mail_type;
	}
	public void setMail_type(String mail_type) {
		this.mail_type = mail_type;
	}
	public String getSend_date_str() {
		return send_date_str;
	}
	public void setSend_date_str(String send_date_str) {
		this.send_date_str = send_date_str;
	}

    public String getOriginalIdx() {
        return originalIdx;
    }

    public void setOriginalIdx(String originalIdx) {
        this.originalIdx = originalIdx;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }
};
