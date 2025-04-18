package kr.co.porkandspoon.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * author sm.park (24.12.13)
     * json 문자열을 List로 반환
     *
     * @param json : 변환할 json 문자열
     * @param clazz : 변환하고 싶은 타입 클래스
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (MismatchedInputException e){
            return new ArrayList<>();
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("jsonToList 파싱 실패", e);
        }
    }

}
