package kr.co.porkandspoon.dto;

public class MainDto {
    private String name;
    private UserDTO userInfo;
    private int unreadMail;
    private int haveToApprove;
    private int reservationCount;

    public MainDto() {}
    public MainDto(String name, UserDTO userInfo, int unreadMail, int haveToApprove, int reservationCount) {
        this.name = name;
        this.userInfo = userInfo;
        this.unreadMail = unreadMail;
        this.haveToApprove = haveToApprove;
        this.reservationCount = reservationCount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserDTO userInfo) {
        this.userInfo = userInfo;
    }

    public int getUnreadMail() {
        return unreadMail;
    }

    public void setUnreadMail(int unreadMail) {
        this.unreadMail = unreadMail;
    }

    public int getHaveToApprove() {
        return haveToApprove;
    }

    public void setHaveToApprove(int haveToApprove) {
        this.haveToApprove = haveToApprove;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }
}
