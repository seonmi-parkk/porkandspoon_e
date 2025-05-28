package kr.co.porkandspoon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ApprovalLineDTO {
   private String draft_idx;
   private String line_idx;
   private String username;
   private String status;
   private Integer order_num;
   private String approval_date;
   private String comment;

}
