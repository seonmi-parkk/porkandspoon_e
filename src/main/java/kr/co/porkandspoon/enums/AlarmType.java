package kr.co.porkandspoon.enums;

public enum AlarmType {
    CONFERENCE("ml001", "회의실 예약"),
    MAIL("ml002", "메일"),
    REPLY("ml003", "댓글"),
    RE_REPLY("ml004", "대댓글"),
    EDU_REGISTER("ml005", "교육 등록"),
    EDU_DEADLINE("ml006", "교육 기한 안내"),
    APPROVAL_REQUEST("ml007", "결재 요청"),
    APPROVAL_COMPLETED("ml008", "결재 승인"),
    APPROVAL_REFUSAL("ml009", "결재 반려"),
    CHAT("ml010", "채팅");

    private final String code;
    private final String desc;

    AlarmType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

}
