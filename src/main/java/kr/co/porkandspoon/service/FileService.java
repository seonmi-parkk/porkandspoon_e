package kr.co.porkandspoon.service;

import kr.co.porkandspoon.dao.FileDAO;
import kr.co.porkandspoon.dto.FileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    @Value("${upload.path}") String paths;
    @Value("${uploadTem.path}") String tem_path;

    private final FileDAO fileDAO;

    public FileService(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    Logger logger = LoggerFactory.getLogger(getClass());

    // 파일 서버저장
    public FileDTO saveFileToServer(MultipartFile file) {
        String ori_filename = file.getOriginalFilename();
        String ext = ori_filename.substring(ori_filename.lastIndexOf("."));
        String new_filename = UUID.randomUUID() + ext;

        try {
            Path path = Paths.get(paths, new_filename);
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        return new FileDTO(ori_filename, new_filename, file.getContentType());
    }

    // 파일 정보 DB에 저장
    public void saveFileToDB(FileDTO fileDTO, String code_name, String pk_idx) throws IOException {
        fileDTO.setCode_name(code_name);
        fileDTO.setPk_idx(pk_idx);
        fileDAO.saveFile(fileDTO);
    }

//    // 서버 저장 + DB 저장 통합 (파일 1개)
//    public FileDTO saveSingleFile(MultipartFile file, String code_name, String pk_idx) throws IOException {
//        FileDTO fileDTO = saveFileToServer(file);
//        saveFileToDB(fileDTO, code_name, pk_idx);
//        return fileDTO;
//    }

    // 여러 개 파일 저장
    public void saveFiles(String pk_idx, String code_name, MultipartFile... files) {
        List<Path> savedPaths = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    FileDTO fileDTO = saveFileToServer(file);
                    saveFileToDB(fileDTO, code_name, pk_idx);
                    savedPaths.add(Paths.get(paths, fileDTO.getNew_filename()));
                } catch (IOException e) {
                    for (Path path : savedPaths) {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ex) {
                            logger.info("파일 삭제 실패: " + path.toString());
                        }
                    }

                    throw new RuntimeException("파일 저장 실패: ", e);
                }
            }
        }
    }

    // 파일 복사 저장 (임시저장 -> 저장 폴더)
    public void moveFiles(List<FileDTO> imgs) {
        if (imgs != null && !imgs.isEmpty()) {
            for (FileDTO img : imgs) {
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
        }
    }

    // 파일 삭제
//    @Transactional
//    public void deleteFiles(String draftIdx, String codeName, FileDTO... deleteFiles) {
//        for (FileDTO file : deleteFiles) {
//            if(file != null) {
//                String filePath = file.getNew_filename();
//                // 파일 삭제 (서버 폴더에서)
//                try {
//                    File fileToDelete = new File(paths + filePath);
//                    if (fileToDelete.exists()) {
//                        boolean deleted = fileToDelete.delete();  // 파일 삭제
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // 데이터베이스에서 파일 정보 삭제
//                file.setPk_idx(draftIdx);
//                file.setCode_name(codeName);
//                fileDAO.deleteFile(file);
//            }
//        }

//    }


    @Transactional
    public void deleteFiles(String draftIdx, String codeName, FileDTO... deleteFiles) {
        for (FileDTO file : deleteFiles) {
            if (file != null) {
                file.setPk_idx(draftIdx);
                file.setCode_name(codeName);

                deleteFileFromServer(file);
                deleteFileFromDB(file);
            }
        }
    }

    // 서버에서 파일 삭제
    public void deleteFileFromServer(FileDTO file) {
        String filePath = file.getNew_filename();
        try {
            File fileToDelete = new File(paths + filePath);
            if (fileToDelete.exists()) {
                boolean deleted = fileToDelete.delete();
            }
        } catch (Exception e) {
            logger.error("파일 삭제 실패: ", e);
        }
    }

    // DB에서 파일 삭제
    public void deleteFileFromDB(FileDTO file) {
        fileDAO.deleteFile(file);
    }






}
