package kr.co.porkandspoon.dto;

public class DraftPermissionResultDTO {
    private boolean permitted;
    private String message;
    private boolean isDraftSender;
    private String approverStatus;
    private String approverOrder;
    private boolean approverTurn;

    public DraftPermissionResultDTO(boolean permitted, String message, boolean isDraftSender, String approverStatus, String approverOrder, boolean approverTurn) {
        this.permitted = permitted;
        this.message = message;
        this.isDraftSender = isDraftSender;
        this.approverStatus = approverStatus;
        this.approverOrder = approverOrder;
        this.approverTurn = approverTurn;
    }

    public boolean isPermitted() {
        return permitted;
    }

    public String getMessage() {
        return message;
    }

    public boolean isDraftSender() {
        return isDraftSender;
    }

    public String getApproverStatus() {
        return approverStatus;
    }

    public String getApproverOrder() {
        return approverOrder;
    }

    public boolean isApproverTurn() {
        return approverTurn;
    }
}
