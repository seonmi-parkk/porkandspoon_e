package kr.co.porkandspoon.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CommonService {
	Logger logger = LoggerFactory.getLogger(CommonService.class);
	
	@Value("${upload.path}") String paths; // 사용자 지정
	
	@Value("${uploadTem.path}") String pathsTem;
	
	/*
	 * author yh.kim (24.12.5) 
	 * 텍스트 에디터 사진 미리 보기
	 */
	public Map<String, Object> textImage(MultipartFile file) throws IllegalStateException {
		
		Map<String, Object> resultFileMap = new HashMap<String, Object>();

		// 파일이 비어있는지 확인 (제거)
		if (file == null || file.isEmpty()) {
			logger.error("업로드된 파일이 없습니다.");
			return Collections.singletonMap("error", "파일이 비어 있습니다.");
		}

		String uploadDir = pathsTem;
		logger.info("uploadDir 경로: {}", uploadDir);
		File dir = new File(uploadDir);
		
		// 폴더 없을 경우 생성
		if(!dir.exists()) {
			dir.mkdirs();
		}

		try {
			String ori_filename = file.getOriginalFilename();
			String ext = ori_filename.substring(ori_filename.lastIndexOf("."));
			String new_filename = UUID.randomUUID().toString() + ext;

			resultFileMap.put("ori_filename", ori_filename);
			resultFileMap.put("new_filename", "/photoTem/" + new_filename);

			File targetFile = new File(uploadDir + new_filename);
			file.transferTo(targetFile);


			logger.info("파일 저장 성공: {}", targetFile.getAbsolutePath());
			logger.info("ori_filename: {}, new_filename: {}", ori_filename, new_filename);
		} catch (IOException e) {
			logger.error("파일 저장 실패: {}", e.getMessage());
			resultFileMap.put("error", "파일 저장 중 오류 발생");
		}

		return resultFileMap;
	}


}
