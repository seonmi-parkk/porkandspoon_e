package kr.co.porkandspoon.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import kr.co.porkandspoon.dto.FileDTO;

import javax.servlet.http.HttpServletResponse;

public class CommonUtil {
   
   @Value("${upload.path}") static String paths; 

   @Value("${uploadTem.path}")   static String pathTem;


    /**
     * author sm.park (24.12.13)
     * 접근 권한 없는 경우 메세지 URL 인코딩 후 history.back();
     *
     * @param response : HttpServletResponse
     * @param message : 전달할 String 메세지
     */
    public static void encodeAccessDeniedMessage(HttpServletResponse response, String message){
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<script>alert('" + message + "'); history.back();</script>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


   /**
    * author yh.kim (24.12.13) 
    * 문자열 날짜를 지정된 형식으로 변환
    * 
    * @param dateString   변환할 LocalDateTime 객체
    * @param inputFormat   입력 날짜 형식 (예: "yyyy-MM-dd")
    * @param outputFormat   출력 날짜 형식 (예: "MM/dd/yyyy")
    */
   public static String formatDate(String dateString, String inputFormat, String outputFormat) {
      DateTimeFormatter inputForamtter = DateTimeFormatter.ofPattern(inputFormat);
      DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
      
      LocalDate date = LocalDate.parse(dateString, inputForamtter);
      return date.format(outputFormatter);
   }
   
   /**
    * author yh.kim (24.12.13) 
    * LocalDateTime을 지정된 형식으로 변환
    * 
    * @param dateTime      변환할 LocalDateTime 객체
    * @param outputFormat   출력 날짜 형식 (예: "yyyy-MM-dd HH:mm:ss")
    */
   public static String formatDateTime(LocalDateTime dateTime, String outputFormat) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat);
      return dateTime.format(formatter);
   }
   
   /**
    * author yh.kim (24.12.13) 
    * 현재 날짜를 지정된 형식으로 변환
    * 
    * @param outputFormat   출력 날짜 형식 (예: "yyyy-MM-dd HH:mm:ss")
    */
   public static String gerCurrentDateTime(String outputFormat) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat);
      return LocalDateTime.now().format(formatter);
   }

   
   /**
    * author yh.kim (24.12.13) 
    * 객체 값 확인을 위한 toString 메소드
    * @param obj : Class, Map, List, Set, Arr, DTO 등
    */
   public static String toString(Object obj) {
      
      if(obj == null) {
         return "null";
      }
      
      if(obj instanceof String || obj instanceof Number || obj instanceof Boolean || obj instanceof Character) {
         return obj.toString();
      } else if(obj.getClass().isArray()) {
         int length = Array.getLength(obj);
         StringBuilder sb = new StringBuilder("[");
         for (int i = 0; i < length; i++) {
            sb.append(Array.get(obj, i)); 
            if(i < length -1) {
               sb.append(", ");
            }
            
         }
         sb.append("]");
         return sb.toString();
      } else if(obj instanceof Collection) {
         Collection<?> collection = (Collection<?>) obj;
         StringBuilder sb = new StringBuilder("[");
         for (Object item : collection) {
            sb.append(toString(item)).append(",");
         }
         
         if(!collection.isEmpty()) {
            sb.setLength(sb.length() -2); // 마지막 콤마 제거
         }
         
         sb.append("]");
         return sb.toString();
         
      } else if(obj instanceof Map) {
         Map<?, ?> map = (Map<?, ?>) obj;
         StringBuilder sb = new StringBuilder("{");
         for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append(toString(entry.getKey()))
            .append(" : ")
            .append(toString(entry.getValue()))
            .append(", ");
         }
         
         if(!map.isEmpty()) {
            sb.setLength(sb.length() -2);
         }
         sb.append("}");
         return sb.toString();
      } else {
         return dtoToString(obj);
      }
         
   }
   
   /**
    * author yh.kim (24.12.14) 
    * DTO 형태 toString 기능 제공
    */
    public static String dtoToString(Object obj) {
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = obj.getClass();
        sb.append(clazz.getSimpleName()).append(" {");

        // 모든 필드 가져오기
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // static 필드는 무시
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true); // private 필드 접근 가능하도록 설정
            try {
                Object value = field.get(obj); // 필드 값 가져오기
                sb.append(field.getName())
                  .append(" : ")
                  .append(value)
                  .append(", ");
            } catch (IllegalAccessException e) {
                sb.append(field.getName()).append("=ACCESS_ERROR, ");
            }
        }

        if (fields.length > 0) {
            sb.setLength(sb.length() - 2); // 마지막 콤마 제거
        }

        sb.append("}");
        return sb.toString();
    }
    
    /**
     * author yh.kim (24.12.14)
     * 지정된 경로에 파일 저장, 파일 정보 {@link FileDTO} 리스트로 반환
     *
    * @param files 업로드할 파일 배열
    * @return 저장된 파일 정보를 담은 {@link FileDTO} 리스트
    * @throws IllegalArgumentException 파일 배열이 비어 있는 경우
    * @throws RuntimeException 파일 저장 중 오류가 발생한 경우
     */
    public static List<FileDTO> uploadFiles(MultipartFile... files){
       
       if(files == null || files.length == 0) {
          throw new IllegalArgumentException("업로드된 파일이 없습니다.");
       }
       
       List<FileDTO> results = new ArrayList<FileDTO>();
       
       for (MultipartFile file : files) {
         String ori_filename = file.getOriginalFilename();
         String type = ori_filename.substring(ori_filename.lastIndexOf("."));
         String new_filename =  UUID.randomUUID().toString() + type;
         
         try {

            Path path = Paths.get("/usr/local/tomcat/webapps/photo/", new_filename);

            Files.write(path, file.getBytes());
            
            results.add(new FileDTO(ori_filename, new_filename, type));
         } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + new_filename, e);
         }   
      }
       return results;
    }
    
    /**
     * 단일 파일 업로드를 위한 메서드 (배열 형태로 위임)
     *
     * @param file 업로드할 단일 파일
     * @return 저장된 파일 정보를 담은 {@link FileDTO}
     * @throws IllegalArgumentException 파일이 없는 경우
     */
    public static FileDTO uploadSingleFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
        }

        // 단일 파일 처리 결과를 반환
        return uploadFiles(file).get(0);
    }

    /**
     * author yh.kim (24.12.13) 
     * 파일 다운로드
     * 
     * @param paths         파일 경로
     * @param ori_filename   기존 파일명
     * @param new_filename   UUID
     */
    public static ResponseEntity<Resource> download(String paths, String ori_filename, String new_filename) {
       try {
          // 파일 경로 생성
          Path filePath = Paths.get(paths).resolve(new_filename).normalize();
          Resource resource = new UrlResource(filePath.toUri());
          
          if (!resource.exists() || !resource.isReadable()) {
             throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + new_filename);
          }
          
          // 한글 파일 이름 URL 인코딩
          String encodedFilename = URLEncoder.encode(ori_filename, StandardCharsets.UTF_8.toString()).replace("+", "%20"); 
          
          // 파일 다운로드 응답 생성
          return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                .body(resource);
          
       } catch (Exception e) {
          throw new RuntimeException("파일 다운로드 중 문제가 발생했습니다.", e);
       }
    }
    
    
    /**
     * author yh.kim (24.12.14) 
     * 임시 저장 폴더에서 최종 파일 폴더로 저장
     * 
     * @param fileNames 파일명(new_filename + type)
     * @return
     */
    public static boolean moveFiles(List<String> fileNames) {
       
       boolean allSuccess = false;
       
       for (String fileName : fileNames) {

         File srcFile = new File("/usr/local/tomcat/webapps/photoTem/" + fileName);
         File destFile = new File("/usr/local/tomcat/webapps/photo/" + fileName);
   
         if(srcFile.exists()) {
            try {
               FileUtils.copyFile(srcFile, destFile);
            } catch (IOException e) {
               System.out.println("복사 실패: " + srcFile.getAbsolutePath());
               e.printStackTrace();
            }
            allSuccess = true;
         } else {
            System.out.println("파일 없음: " + srcFile.getAbsolutePath());
         }
      }
       return allSuccess;
    }
    
    /**
     * author yh.kim (24.12.16) 
     * 숫자를 원하는 형태로 변환하는 메서드
     * 
     * @param number      변환하고자 하는 숫자
     * @param outputFormat   변환하고자 하는 유형 (ex.#,### 000.000 등..)
     */
    public static String addCommaToNumber(double number, String outputFormat) {
        try {
            DecimalFormat formatter = new DecimalFormat(outputFormat);
            return formatter.format(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("유효한 숫자가 아닙니다: " + number);
        }
    }
    
}

