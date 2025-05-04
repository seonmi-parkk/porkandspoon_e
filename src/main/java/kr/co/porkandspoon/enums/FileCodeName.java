package kr.co.porkandspoon.enums;

public enum FileCodeName {
    BRAND_LOGO("bl001", "브랜드 로고"),
    BRAND_DESC("bc001", "브랜드 설명"),
    DRAFT("df000", "기안문"),
    MAIL("ma001", "메일"),
    USER_PROFILE("up100", "직원 프로필"),
    USER_SIGN("us100", "직원 서명"),
    FREE_BOARD("fb001", "자유 게시판"),
    LIBRARY("lb001", "라이브러리");

    private final String code;
    private final String desc;

    FileCodeName(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

}
