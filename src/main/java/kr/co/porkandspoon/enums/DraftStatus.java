package kr.co.porkandspoon.enums;

public enum DraftStatus {
    SAVED("sv", "임시저장"),
    SUBMITTED("sd", "상신"),
    COMPLETED("co", "결재 완료"),
    REJECTED("re", "반려"),
    RECALLED("ca", "회수"),
    DELELTED("de", "삭제");


    private final String code;
    private final String desc;

    DraftStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

}
