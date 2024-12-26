package kr.co.porkandspoon.dto;

import java.time.LocalDateTime;

public class CalenderDTO {
	
	// 일정 테이블
	private String idx;
	private String username;
	private LocalDateTime start_date;
	private LocalDateTime end_date;
	private String updater;
	private String subject;
	private String content;
	private String type; // 캘린더 유형(전사P,팀T,개인S)
	private String reservation_type;
	private String no; // 물품,회의실 no
	private String personnel;
	private String id; // 부서,직영 코드
	private String name;
	private String parent;
	
	//물품등록 테이블
	private String item_name;
	private String model_name;
	private String is_item;
	private String use_yn;
	
	
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public String getIs_item() {
		return is_item;
	}
	public void setIs_item(String is_item) {
		this.is_item = is_item;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public LocalDateTime getStart_date() {
		return start_date;
	}
	public void setStart_date(LocalDateTime string) {
		this.start_date = string;
	}
	public LocalDateTime getEnd_date() {
		return end_date;
	}
	public void setEnd_date(LocalDateTime end_date) {
		this.end_date = end_date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReservation_type() {
		return reservation_type;
	}
	public void setReservation_type(String reservation_type) {
		this.reservation_type = reservation_type;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getPersonnel() {
		return personnel;
	}
	public void setPersonnel(String personnel) {
		this.personnel = personnel;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	

}
