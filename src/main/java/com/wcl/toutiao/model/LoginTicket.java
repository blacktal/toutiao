package com.wcl.toutiao.model;

import java.util.Date;

/**
 * @ClassName: LoginTicket
 * @Description: TODO
 * @author Lulu
 * @date 2017年11月26日 下午7:59:11
 */
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private Date expired;
    private int status;// 0有效，1无效

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
