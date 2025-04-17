package kr.co.porkandspoon.dto;

import java.time.LocalDateTime;

public class FileDTO extends ResponseDTO{
	private int file_idx;
	private int idx;
	private String code_name;
	private String ori_filename;
	private String new_filename;
	private String type;
	private String pk_idx;
	private LocalDateTime create_date;
	private long file_size;
	
	
	// 생성자
	public FileDTO(String ori_filename, String new_filename, String type) {
		this.ori_filename = ori_filename;
		this.new_filename = new_filename;
		this.type = type;
	}
	public FileDTO(String ori_filename, String new_filename, String type, String code_name, String pk_idx) {
		this.ori_filename = ori_filename;
		this.new_filename = new_filename;
		this.type = type;
		this.code_name = code_name;
		this.pk_idx = pk_idx;
	}
	
	public FileDTO() {};
	
	
	public String getOri_filename() {
		return ori_filename;
	}
	public void setOri_filename(String ori_filename) {
		this.ori_filename = ori_filename;
	}
	public String getNew_filename() {
		return new_filename;
	}
	public void setNew_filename(String new_filename) {
		this.new_filename = new_filename;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public int getFile_idx() {
		return file_idx;
	}

	public void setFile_idx(int file_idx) {
		this.file_idx = file_idx;
	}

	public String getCode_name() {
		return code_name;
	}

	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}

	public String getPk_idx() {
		return pk_idx;
	}

	public void setPk_idx(String pk_idx) {
		this.pk_idx = pk_idx;
	}

	public long getFile_size() {
		return file_size;
	}

	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public LocalDateTime getCreate_date() {
		return create_date;
	}

	public void setCreate_date(LocalDateTime create_date) {
		this.create_date = create_date;
	}
	
	
	
}
