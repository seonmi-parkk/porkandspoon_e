package kr.co.porkandspoon.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import kr.co.porkandspoon.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.porkandspoon.dao.UserDAO;
import kr.co.porkandspoon.util.CommonUtil;

@Service
public class UserService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired UserDAO userDao;
	
	private static final AtomicInteger counter = new AtomicInteger(1);
    @Autowired
    private UserDAO userDAO;

	/**
	 * author yh.kim (24.12.22)
	 * 아이디 찾기 시 사용자 검증
	 */
	public String findUserId(UserDTO dto) {
		return userDao.findUserId(dto);
	}


	/**
	 * author yh.kim (24.12.22)
	 * 인증 코드 생성 메서드
	 */
	public int randomAuthenticationCode(UserDTO dto) {
	
		// 1. 기존 코드 있는지 체크 (select - count)
		Integer codeCheck = userDao.randomCodeCheck(dto);

		logger.info(codeCheck + " ====> 기존에 있는지 카운트!! ");
		
		
		// 코드가 없을 경우 기존 레코드 업데이트
		if (codeCheck != null && codeCheck > 0) {
		    userDao.randomCodeUpdate(codeCheck);
		}
		
		// 2. 없을 경우 바로 or 있을 경우 update 후 insert
		userDao.randomCodeInsert(dto);
		
		// 3. pk 리턴
		return dto.getIdx();
		
	}
	/**
	 * author yh.kim (24.12.20) 
	 * 비밀번호 변경 메서드
	 */
	public boolean changePassword(UserDTO params) {
		return userDao.changePassword(params) > 0 ? true : false;
	}


	/**
	 * author yh.kim (24.12.22)
	 * 인증코드 검증 메서드
	 */
	public boolean chackAuthCode(UserDTO dto) {
		
		int resultRow = userDao.chackAuthCode(dto);
		logger.info("인증 번호 찾기 로우 => " + resultRow);
	
		if(resultRow == 0) {
			return false;
		}

		// 인증번호가 맞으면 기존 인증번호 N으로 업데이트
		int idx = dto.getIdx();
		userDao.randomCodeUpdate(idx);

		dto.setUsername(userDao.findUsername(dto));
		
		return true;
	}


	/**
	 * author yh.kim (24.12.22)
	 * 인증번호 생성 후 2분 경과 시 실행 메서드
	 */
	public void timeoutAction(UserDTO dto) {
		
		int idx = dto.getIdx();
		userDao.randomCodeUpdate(idx);
	}


	/**
	 * author yh.kim (24.12.22)
	 * 비밀번호 찾기 시 사용자 검증
	 */
	public String findUserPw(UserDTO dto) {
		return userDao.findUserPw(dto);
	}

	/**
	 * author yh.kim (24.12.23)
	 * 직원 아이디 중복 체크
	 */
	public boolean usernameOverlay(UserDTO dto) {
		return userDao.usernameOverlay(dto) ==  0 ? true : false;
	}

	/**
	 * author yh.kim (24.12.23)
	 * 부서 리스트 조회
	 */
	public List<DeptDTO> deptList() {
		
		
		List<DeptDTO> deptList = userDao.deptList();
		DeptDTO dto = new DeptDTO();
		
		if(deptList.size() < 1) {
			
			deptList = new ArrayList<DeptDTO>();
			
			
			dto.setStatus(400);
			dto.setMessage("부서가 존재하지 않습니다.");
			
			deptList.add(dto);
			
			return deptList;
		}
		
		dto.setStatus(205);
		
		return deptList;
	}

	/**
	 * author yh.kim (24.12.24)
	 * 직원등록 메서드 (직원 등록, 이력 등록, 파일 등록 진행)
	 */
	public UserDTO userWrite(UserDTO dto, MultipartFile file) {
		
		// 사번 생성 메서드
		String num = generateCompanyNumber(dto);
		dto.setPerson_num(num);
		
		// 권한 생성 메서드 (권한 A 로 시작하면 admin 권한 B로 시작하면 manager 권한 B에 직급이 6이면 user 권한)
		String role = authorityCreate(dto);
		dto.setRole(role);

		if(dto.getStoreId() != null && dto.getStoreId().isEmpty()) {
			// 부서 정보에 직영점 id 입력
			dto.setParent(dto.getStoreId());
		}

		int row = userDao.userWrite(dto);
		
		logger.info("직원 등록 row => " + row);

		if(row > 0 && dto.getStoreId() != null && dto.getStoreId().isEmpty()){
			int storeUpdateRow = userDao.userStoreUpdate(dto);
			logger.info("직영점 owner 업데이트 => " + storeUpdateRow);
		}
		
		if(row == 0) {
			dto.setStatus(500);
			dto.setMessage("직원등록에 실패했습니다.");
			return dto;
		}
		
		// UserDTO의 career 리스트에 username 설정
	    if (dto.getCareer() != null) { // career 리스트가 비어있지 않은 경우에만 처리
	        for (CareerDTO career : dto.getCareer()) {
	            career.setUsername(dto.getUsername()); // UserDTO의 username을 CareerDTO에 설정
	        }
	    }
		
		// 이력 등록 메서드
		int careerRow = userDao.userCareerWrite(dto);
				
		logger.info("이력 등록 로우 => " + careerRow);
		
	    // 프로필 이미지 처리
	    if (file != null && !file.isEmpty()) { // 파일이 null이 아니고 비어있지 않은 경우에만 처리
	        try {
	            FileDTO fileDto = CommonUtil.uploadSingleFile(file);
	            logger.info(CommonUtil.toString(fileDto));
	            fileDto.setCode_name("up100");
	            fileDto.setPk_idx(dto.getUsername());

	            int fileRow = userDao.userFileWriet(fileDto);
	            logger.info("업로드된 파일 로우 => " + fileRow);
	        } catch (Exception e) {
	            logger.error("파일 업로드 중 오류 발생", e);
	            dto.setStatus(500);
				dto.setMessage("직원등록에 실패했습니다.");
				return dto;
	        }
	    } else {
	        logger.warn("프로필 이미지 파일이 없습니다. 기본 프로필 이미지 사용");
	        // 기본 프로필 이미지 설정 로직이 필요하면 여기에 추가
	    }
	    
	    dto.setStatus(200);
	    dto.setMessage("직원 등록을 완료했습니다.");

		return dto;
	}

	/**
	 * author yh.kim (24.12.24)
	 * 사번 생성 메서드
	 */
	public String generateCompanyNumber(UserDTO dto) {
		
		if(dto.getParent() == null || dto.getParent().isEmpty()) {
			return "입력된 부서가 없습니다.";
		}
		
	    // 부서 번호를 알파벳으로 변환
	    String firstTwoChars = dto.getParent().substring(0, 2);
	    
	    int year = LocalDate.now().getYear();
        
        // 뒤의 2자리 숫자만 추출
        int lastTwoDigits = year % 100;
       
        // 고유번호 생성 (4자리)
        String uniqueNumber = String.format("%04d", counter.getAndIncrement());

        // 사번 형태 (부서코드 - 240001 : AD-240001
	    String companyNumber = firstTwoChars + "-" + lastTwoDigits + uniqueNumber;
	    logger.info("생성된 사번 => " + companyNumber);
		
		return companyNumber;
	}
	
	/**
	 * author yh.kim (24.12.24)
	 * 권한 생성 메서드
	 */
	private String authorityCreate(UserDTO dto) {
		
		String deptFirst = dto.getParent().substring(0,1);
		
		String role = "superadmin";
		
		if(deptFirst == null || deptFirst == "") {
			return "입력된 부서가 없어 권한 생성이 미완료되었습니다.";
		}
		
		if(deptFirst.equals("A")) {
			role = "admin";
			return role;
		}else if(deptFirst.equals("M")) {
			role = "manager";
			return role;
		}else if(deptFirst.equals("M") && dto.getPosition().equals("po7")) {
			role = "user";
			return role;
		}else {
			role = "user"; // 기타 부서
		}
		
		return role;
	}

	/**
	 * author yh.kim (24.12.24)
	 * 직원 정보 조회 
	 */
	public UserDTO userDetail(String username) {
		
		UserDTO dto = new UserDTO();
		
		dto = userDao.userDetail(username);
		
		logger.info(CommonUtil.toString(dto));
		
		if(dto == null) {
			dto.setStatus(400);
			dto.setMessage("직원 정보가 없습니다.");
		}
		
		// 직원 이력 조회
		dto.setCareer(userDao.userCareerDetail(username));
		
		logger.info(CommonUtil.toString(dto));
		
		return dto;
	}

	/**
	 * author yh.kim (24.12.24)
	 * 직원 정보 수정 
	 */
	public UserDTO userUpdate(UserDTO dto, MultipartFile file) {
		
		// 수정할 때 update 부서 x -> 사번 o 
		// 만약 이동되면 권한은 다시 검사!  일단 권한 x
		
		int row = userDao.userUpdate(dto);
		logger.info("직원 수정 로우 => " + row);
		
		if(row == 0) {
			dto.setStatus(500);
			dto.setMessage("직원등록에 실패했습니다.");
			return dto;
		}
		
		// 기존에 없던 데이터일 경우 insert 이니까 근데 그거 판단을 어려우니까 delete하고 insert? - 일단이건 가능
		// 기존 이력 제거 후 insert
		// 이력은 남기고 type중 create_date가 가장 빠른 일자로 select 할 수 있도록 조회 쿼리 수정
		
		if (dto.getCareer() != null) { // career 리스트가 비어있지 않은 경우에만 처리
	        for (CareerDTO career : dto.getCareer()) {
	            career.setUsername(dto.getUsername()); // UserDTO의 username을 CareerDTO에 설정
	        }
	    }
		
		int careerInsertRow = userDao.userCareerWrite(dto);
		
		logger.info("이력 삽입 로우 => " + careerInsertRow);
		
		 // 프로필 이미지 처리
	    if (file != null && !file.isEmpty()) { // 파일이 null이 아니고 비어있지 않은 경우에만 처리
	    	
	    	// 파일도 삭제 후 입력 진행
	    	int fileDeleteRow = userDao.userFileDelete(dto);
	    	
	    	logger.info("파일 삭제 로우 => " + fileDeleteRow);
	    	
	    	if(fileDeleteRow == 0) {
	    		logger.warn("삭제할 파일 정보가 없습니다.");
	    	}
	    	
	        try {
	            FileDTO fileDto = CommonUtil.uploadSingleFile(file);
	            logger.info(CommonUtil.toString(fileDto));
	            fileDto.setCode_name("up100");
	            fileDto.setPk_idx(dto.getUsername());

	            int fileRow = userDao.userFileWriet(fileDto);
	            logger.info("업로드된 파일 로우 => " + fileRow);
	        } catch (Exception e) {
	            logger.error("파일 업로드 중 오류 발생", e);
	            dto.setStatus(500);
				dto.setMessage("직원수정에 실패했습니다.");
				return dto;
	        }
	    } else {
	        logger.warn("프로필 이미지 파일이 없습니다. 기본 프로필 이미지 사용");
	    }
	    dto.setStatus(200);
	    dto.setMessage("직원 수정을 완료했습니다.");
		
		return dto;
	}

	/**
	 * author yh.kim (24.12.24)
	 * 직원 리스트 조회
	 */
	public List<UserDTO> userList(PagingDTO pagingDTO) {
		return userDao.userList(pagingDTO);
	}

	/**
	 * author yh.kim (24.12.26)
	 * 부서(브랜드) 등록
	 */
	public DeptDTO deptWrite(MultipartFile file, DeptDTO dto) {

		// 브랜드 등록
		int deptRow = userDao.deptWrite(dto);

		String[] usernameArr = dto.getUser_name().split(" ");
		UserDTO userDTO = new UserDTO();
		for (String username : usernameArr) {
			userDTO.setUsername(username);
			userDTO.setParent(dto.getId());
			String person_num = generateCompanyNumber(userDTO);
			userDTO.setPerson_num(person_num);
			int userUpdateRow = userDao.userDeptUpdate(userDTO);
			logger.info("직원 부서 업데이트 로우 => " + userUpdateRow);
		}



		logger.info("브랜드 생성 로우 => " + deptRow);

		List<FileDTO> imgs = dto.getImgs();
		if(imgs.size() > 0 || imgs != null) {

			// FileDTO에서 new_filename 값 추출
		    List<String> fileNames = imgs.stream()
		                                 .map(FileDTO::getNew_filename) // new_filename 추출
		                                 .filter(Objects::nonNull)      // null 값 필터링
		                                 .collect(Collectors.toList()); // List<String>으로 변환

		    // 파일 이동
		    boolean moveResult = CommonUtil.moveFiles(fileNames);
		    logger.info("파일 이동 결과: {}", moveResult);

			for (FileDTO img : imgs) {
				img.setPk_idx(dto.getId());
				img.setCode_name("bc100");

				String type = img.getOri_filename().substring(img.getOri_filename().lastIndexOf("."));
				img.setType(type);

				int contentImgRow = userDao.userFileWriet(img);
				logger.info("이미지 업로드 => ", contentImgRow);
			}
		}

		// 브랜드 로고 업로드
		if (file != null && !file.isEmpty()) { // 파일이 null이 아니고 비어있지 않은 경우에만 처리
	        try {
	            FileDTO fileDto = CommonUtil.uploadSingleFile(file);
	            logger.info(CommonUtil.toString(fileDto));
	            fileDto.setCode_name("bl001"); // 브랜드 로고
	            fileDto.setPk_idx(dto.getId()); // 브랜드 코드 (id) 로 구분

	            int fileRow = userDao.userFileWriet(fileDto);
	            logger.info("업로드된 파일 로우 => " + fileRow);
	        } catch (Exception e) {
	            logger.error("파일 업로드 중 오류 발생", e);
	        }
	    } else {
	        logger.warn("프로필 이미지 파일이 없습니다.");
			// 기존 로고 파일로 브랜드 파일 업로드
			int defaultRogoUploadRow = userDao.defaultRogoUpload(dto);
			logger.info("기존 로고 파일 업로드 => " + defaultRogoUploadRow);
	    }

		dto.setStatus(200);
		dto.setMessage("부서 등록이 완료되었습니다.");

		return dto;
	}



	/**
	 * author yh.kim (24.12.26)
	 * 부서 수정
	 */
	public DeptDTO deptUpdate(MultipartFile file, DeptDTO dto) {

		// 부서 비활성 시 선택 시 실행
		if(dto.getUse_yn().equals("N")){
			deptInactiveUpdate(dto);
		}

		int deptRow = userDao.deptUpdate(dto);
		
		logger.info("브랜드 수정 로우 => " + deptRow);

		String[] usernameArr = dto.getUser_name().split(" ");
		UserDTO userDTO = new UserDTO();
		for (String username : usernameArr) {
			userDTO.setUsername(username);
			userDTO.setParent(dto.getId());
			String person_num = generateCompanyNumber(userDTO);
			userDTO.setPerson_num(person_num);
			int userUpdateRow = userDao.userDeptUpdate(userDTO);
			logger.info("직원 부서 업데이트 로우 => " + userUpdateRow);
		}
		
		// 브랜드 비활성 시 직영점 업데이트
		if(dto.getUse_yn().equals("N")) {
			int storeRow = userDao.storeUseUpdate(dto);
			logger.info("직영점 비활성 => " + storeRow);


		}
		
		
		List<FileDTO> imgs = dto.getImgs();
		if(imgs.size() > 0 || imgs != null) {
			
			// FileDTO에서 new_filename 값 추출
		    List<String> fileNames = imgs.stream()
		                                 .map(FileDTO::getNew_filename) // new_filename 추출
		                                 .filter(Objects::nonNull)      // null 값 필터링
		                                 .collect(Collectors.toList()); // List<String>으로 변환

		    // 파일 이동
		    boolean moveResult = CommonUtil.moveFiles(fileNames);
		    logger.info("파일 이동 결과: {}", moveResult);
			
			for (FileDTO img : imgs) {
				img.setPk_idx(dto.getId());
				img.setCode_name("bc100");
				
				String type = img.getOri_filename().substring(img.getOri_filename().lastIndexOf("."));
				img.setType(type);
				
				int contentImgRow = userDao.userFileWriet(img);
				logger.info("이미지 업로드 => ", contentImgRow);
			}
		}
		
		// 브랜드 로고 업로드 
		if (file != null && !file.isEmpty()) { // 파일이 null이 아니고 비어있지 않은 경우에만 처리
			
			// new_filename + pk_idx 를 넘거야 함!! 
			String pk_idx = dto.getId();
			String code_name = "bl001";
	    	int fileDeleteRow = userDao.fileDelete(pk_idx, code_name);
	    	
	    	logger.info("파일 삭제 로우 => " + fileDeleteRow);
	    	
	    	if(fileDeleteRow == 0) {
	    		logger.warn("삭제할 파일 정보가 없습니다.");
	    	}
			
			    try {
			        FileDTO fileDto = CommonUtil.uploadSingleFile(file);
			        logger.info(CommonUtil.toString(fileDto));
			        fileDto.setCode_name("bl001"); // 브랜드 로고
			        fileDto.setPk_idx(dto.getId()); // 브랜드 코드 (id) 로 구분

			        int fileRow = userDao.userFileWriet(fileDto);
			        logger.info("업로드된 파일 로우 => " + fileRow);
			    } catch (Exception e) {
			        logger.error("파일 업로드 중 오류 발생", e);
			    }
			} else {
			    logger.warn("로고 이미지 파일이 없습니다.");
			}

		dto.setStatus(200);
		dto.setMessage("부서 수정이 완료되었습니다.");

		return dto;
	}

	/**
	 * author yh.kim (25.01.07)
	 * 부서 비활성화 시 직영점 비활성화 및 직원 미발령 이동
	 */
	private void deptInactiveUpdate(DeptDTO dto) {

		// 부서 직원 변경
		String[] usernameArr = dto.getUsername().split(",");
		UserDTO userDTO = new UserDTO();
		for (String username : usernameArr) {
			userDTO.setUsername(username);
			userDTO.setParent("NO1000");
			String person_num = generateCompanyNumber(userDTO);
			userDTO.setPerson_num(person_num);
			int userUpdateRow = userDao.userDeptUpdate(userDTO);
			logger.info("직원 부서 업데이트 로우 (비활성화) => " + userUpdateRow);
		}

		// 직영점 직원 변경
		int storeUserUpdateRow = userDao.storeUserUpdate(dto);
		logger.info("직영점 직원 변경 로우 => " + storeUserUpdateRow);

		if(storeUserUpdateRow > 0) {
			// 직영점 비활성화
			int storeInactiveRow = userDao.storeInactiveUpdate(dto);
			logger.info("직영점 비활성화 업데이트 => " + storeInactiveRow);
		}


	}

	/**
	 * author yh.kim (24.12.26)
	 * 브랜드 리스트 조회
	 */
	public List<DeptDTO> deptGetList(PagingDTO pagingDTO) {
		
		return userDao.deptGetList(pagingDTO);
	}

	/**
	 * author yh.kim (24.12.26)
	 * 브랜드 생성 요청 리스트 조회
	 */
	public List<ApprovalDTO> deptCreateList(PagingDTO pagingDTO) {
		
		return userDao.deptCreateList(pagingDTO);
		
	}

	/**
	 * author yh.kim (24.12.26)
	 * 브랜드 삭제 요청 리스트 조회
	 */
	public List<ApprovalDTO> deptDeleteList(PagingDTO pagingDTO) {
		return userDao.deptDeleteList(pagingDTO);
	}





	/**
	 * author yh.kim (24.12.27)
	 * 직영점 등록
	 */
	public DeptDTO storeWrite(DeptDTO dto) {
		
		int storeRow = userDao.storeWrite(dto);
		
		logger.info("브랜드 수정 로우 => " + storeRow);

		// 직원 테이블 update
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(dto.getUser_name());
		userDTO.setParent(dto.getId());
		String person_num = generateCompanyNumber(userDTO);
		userDTO.setPerson_num(person_num);
		int userUpdateRow = userDao.userDeptUpdate(userDTO);
		logger.info("직원 부서 업데이트 로우 => " + userUpdateRow);
		
		List<FileDTO> imgs = dto.getImgs();
		if(imgs.size() > 0 || imgs != null) {
			
			// FileDTO에서 new_filename 값 추출
		    List<String> fileNames = imgs.stream()
		                                 .map(FileDTO::getNew_filename) // new_filename 추출
		                                 .filter(Objects::nonNull)      // null 값 필터링
		                                 .collect(Collectors.toList()); // List<String>으로 변환

		    // 파일 이동
		    boolean moveResult = CommonUtil.moveFiles(fileNames);
		    logger.info("파일 이동 결과: {}", moveResult);
			
			for (FileDTO img : imgs) {
				img.setPk_idx(dto.getId());
				img.setCode_name("bc100");
				
				String type = img.getOri_filename().substring(img.getOri_filename().lastIndexOf("."));
				img.setType(type);
				
				int contentImgRow = userDao.userFileWriet(img);
				logger.info("이미지 업로드 => ", contentImgRow);
			}
		}

		dto.setStatus(200);
		dto.setMessage("직영점 등록이 완료되었습니다.");

		return dto;
	}


	/**
	 * author yh.kim (24.12.27)
	 * 직영점 수정
	 */
	public DeptDTO storeUpdate(DeptDTO dto) {
			
		int storeRow = userDao.storeUpdate(dto);
		
		logger.info("브랜드 수정 로우 => " + storeRow);
		
		List<FileDTO> imgs = dto.getImgs();
		if(imgs.size() > 0 || imgs != null) {
			
			// FileDTO에서 new_filename 값 추출
		    List<String> fileNames = imgs.stream()
		                                 .map(FileDTO::getNew_filename) // new_filename 추출
		                                 .filter(Objects::nonNull)      // null 값 필터링
		                                 .collect(Collectors.toList()); // List<String>으로 변환

		    // 파일 이동
		    boolean moveResult = CommonUtil.moveFiles(fileNames);
		    logger.info("파일 이동 결과: {}", moveResult);
			
			for (FileDTO img : imgs) {
				img.setPk_idx(dto.getId());
				img.setCode_name("bc100");
				
				String type = img.getOri_filename().substring(img.getOri_filename().lastIndexOf("."));
				img.setType(type);
				
				int contentImgRow = userDao.userFileWriet(img);
				logger.info("이미지 업로드 => ", contentImgRow);
			}
		}
		// 직영점 비활성화
		if(dto.getUse_yn().equals("N")){

			// 직영점 비활성화
			int storeUpdateRow = userDao.storeUseYnUpdate(dto);
			logger.info("직영점 비활성화 로우 => " + storeUpdateRow);

			// 직원 테이블 업데이트


			UserDTO user = new UserDTO();

			user.setParent(dto.getId());
			String num = generateCompanyNumber(user);
			user.setPerson_num(num);

			int userUpdateRow = userDao.storeUserUserUpdate(dto);
			logger.info("비활성 시 직원 업데이트 로우 => " + userUpdateRow);
		}

		dto.setStatus(200);
		dto.setMessage("직영점 수정이 완료되었습니다.");
		
		return dto;
	}

	/**
	 * author yh.kim (24.12.28)
	 * 직영점 리스트
	 */
	public List<DeptDTO> storeList(PagingDTO pagingDTO) {
		
		return userDao.storeList(pagingDTO);
	}

	/**
	 * author yh.kim (24.12.29)
	 * 직영점 생성 요청 리스트 
	 */
	public List<ApprovalDTO> ceateStoreList(PagingDTO pagingDTO) {
		
		return userDao.ceateStoreList(pagingDTO);
	}

	/**
	 * author yh.kim (24.12.29)
	 * 직영점 폐점 요청 리스트
	 */
	public List<ApprovalDTO> deleteStoreList(PagingDTO pagingDTO) {
		
		return userDao.deleteStoreList(pagingDTO);
	}

	/**
	 * author yh.kim (24.12.31)
	 * 인사이동 리스트 조회
	 */
	public List<UserDTO> employeeTransferList
		(PagingDTO pagingDTO) {
		
		List<UserDTO> result = userDao.employeeTransferList(pagingDTO);
		
		if(result.size() < 1) {
			
			result = new ArrayList<UserDTO>();
			return result;
		}
		
		return result;
	}

	/**
	 * author yh.ki, (25.1.1)
	 * 직영점 이동 리스트 조회
	 */
	public List<UserDTO> storeTransferList
		(PagingDTO pagingDTO) {

		List<UserDTO> result = userDao.storeTransferList(pagingDTO);
		
		if(result.size() < 1) {
			
			result = new ArrayList<UserDTO>();
			return result;
		}
		
		return result;
	}

	/**
	 * author yh.ki, (25.1.1)
	 * 미발령 직원 리스트 조회
	 */
	public List<UserDTO> notTransferList
		(PagingDTO pagingDTO) {
		
		List<UserDTO> result = userDao.notTransferList(pagingDTO);
		
		
		
		if(result.size() < 1) {
			
			result = new ArrayList<UserDTO>();
			return result;
		}
		
		return result;
	}

	/**
	 * author yh.ki, (25.1.2)
	 * 인사이동 부서 및 직급 리스트 조회
	 */
	public List<UserDTO> userTransferInfo(String username) {
		
		List<UserDTO> result = userDao.userTransferInfo(username);
	    
		if(result.size() < 1) {
			
			result = new ArrayList<UserDTO>();
			return result;
		}
		
		return result;
	}

	/**
	 * author yh.kim, (25.01.02)
	 * 직원 인사이동 
	 */
	public boolean setEmployeeTransfer(List<UserDTO> userDto) {


		int insertRow = userDao.setEmployeeTransfer(userDto);
		logger.info("인사이동 로우 => " + insertRow);
		
		if(insertRow < 1) {
			logger.warn("인사이동 입력이 미완료되었습니다");
			return false;
		}
		
		logger.info(CommonUtil.toString(userDto));
		// 직원 데이블 정보 변경
		for (UserDTO user : userDto) {

			// 사번 생성 메서드
			user.setParent(user.getNew_department());
			String num = generateCompanyNumber(user);
			user.setPerson_num(num);

			logger.info("생성된 사번 => " + user.getPerson_num());

	        int result = userDao.updateEmployeeUser(user);
			logger.info("직원 테이블 업데이트 => " + result);
	        if (result < 1) {
	            logger.warn("업데이트 실패: " + user.getUsername());
	            return false;
	        }
	    }
		
		
		return true;
	}

	/**
	 * author yh.kim, (25.01.02)
	 * 직영점 인사이동
	 */
	public boolean setStoreTransfer(List<UserDTO> userDto) {

		logger.info("서비스 => " + CommonUtil.toString(userDto));
		
		// 직영점 인사이동
		int insertRow = userDao.setStoreTransfer(userDto);
		logger.info("직영점 이동 로우 => " + insertRow);
		
		if(insertRow < 1) {
			logger.warn("직영점 이동 입력이 미완료되었습니다");
			return false;
		}

		// 기존 직영점 주 인사이동
		int storeInsertRow = userDao.setExistingStoreTransfer(userDto);

		logger.info("기존 직염점 주 변경 인사 로우 => " + storeInsertRow);

		if(storeInsertRow < 1) {
			logger.warn("기존 직영점주의 직영점 이동 입력이 미완료되었습니다");
			return false;
		}

		boolean insertResult = false;

		// 1차 update - 직원 테이블
		for (UserDTO user : userDto) {
			int result = userDao.updateStoreUser(user);
			logger.info("직원 테이블 업데이트 => " + result);
			if (result < 1) {
				logger.warn("업데이트 실패: " + user.getUsername());
				return false;
			}

			//int temporaryRow = userDao.temporaryStoreId(user);
			int storeResult = userDao.updateStoreOwner(user);
			logger.info("직영점 테이블 업데이트 => " + storeResult);

			if (storeResult < 1) {
				logger.warn("업데이트 실패: " + user.getUsername());
				return false;
			}

			insertResult = true;
		}

		if(insertResult){
			for (UserDTO user : userDto) {
				user.setUsername(user.getOwner());
				user.setNew_position(user.getOld_position());

				// 직원 테이블 업데이트
				int result = userDao.updateStoreUser(user);
				logger.info("2차 직원 테이블 업데이트 => " + result);
				if (result < 1) {
					logger.warn("2차 업데이트 실패: " + user.getUsername());
					return false;
				}

				// 직영점 테이블 업데이트
				int storeResult = userDao.updateStoreOwner(user);
				logger.info("2차 직영점 테이블 업데이트 => " + storeResult);
				if (storeResult < 1) {
					logger.warn("2차 업데이트 실패: " + user.getUsername());
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * author yh.kim, (25.01.02)
	 * 직원 퇴사 처리 
	 */
	public boolean usrQuitDelete(List<UserDTO> userDto) {

		
		for (UserDTO user : userDto) {
	        int result = userDao.usrQuitDelete(user);
	        logger.info("직원 퇴사 로우 =>" + result);
	        if (result < 1) {
	            logger.warn("업데이트 실패: " + user.getUsername());
	            return false;
	        }
	    }
		
		return true;
		
	}

	/**
	 * author yh.kim, (25.01.02)
	 * 직영점 이동 시 직영점 name 조회
	 */
	public List<DeptDTO> storeTransferInfo(String username) {
		
		List<DeptDTO> result = userDao.storeTransferInfo(username);
	    
		if(result.size() < 1) {
			
			result = new ArrayList<DeptDTO>();
			return result;
		}
		
		return result;
	}


	/**
	 * author yh.kim, (25.01.11)
	 * 직원 등록 시 직영점 조회
	 */
	public List<DeptDTO> storeIdList(String parent) {

		List<DeptDTO> deptDTO = userDao.storeIdList(parent);
		DeptDTO dto = new DeptDTO();

		if(deptDTO.size() < 1) {
			dto.setStatus(502);
			dto.setMessage("선택가능한 직영점이 없습니다.");

			deptDTO.add(dto);
			return deptDTO;
		}

		dto.setStatus(202);
		dto.setMessage("직영점 리스트를 불러왔습니다.");

		deptDTO.add(dto);

		return deptDTO;
	}

	/**
	 * author yh.kim, (25.01.11)
	 * 퇴사 직원 조회 및 처리
	 * 매일 00:03 실행
	 */
	public void resignationProcessing() {

		int resignationRow = userDao.resignationProcessing();
		logger.info("퇴사 처리 로우 => " + resignationRow);

	}
}
