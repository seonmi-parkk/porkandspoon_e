package kr.co.porkandspoon.enums;

public enum DraftTargetType {
    BRAND("df001", "브랜드"),
    DIRECT_STORE("df002", "직영점");

    private final String code;
    private final String desc;

    DraftTargetType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

}
