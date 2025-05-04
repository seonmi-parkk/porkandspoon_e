package kr.co.porkandspoon.enums;

public enum ApprovalStatus {
    UNCHECKED("ap001", "미확인"),
    IN_PROGRESS("ap002", "결재중"),
    REJECTED("ap003", "반려"),
    COMPLETED("ap004", "결재완료");

    private final String code;
    private final String desc;

    ApprovalStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

}
