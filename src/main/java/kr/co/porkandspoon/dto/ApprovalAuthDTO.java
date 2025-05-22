package kr.co.porkandspoon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class ApprovalAuthDTO {
   // 기안문 테이블
   private String draft_idx;
   private String username; //사번
   private String cooper_dept_id;
   private String dept_id;
   private String status;

}