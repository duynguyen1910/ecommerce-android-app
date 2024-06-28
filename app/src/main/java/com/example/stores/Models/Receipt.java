package com.example.stores.Models;

import java.io.Serializable;

public class Receipt implements Serializable {
    private int receiptID;
    private String creator;
    private String fullname;
    private String phoneNumber;
    private String address;
    private String startDay;
    private String endDay;


    private String note;
    private int memberID;
    private int status;  // 0: chưa trả , 1: đã trả


    public Receipt() {
    }

    // Constructor này dùng để lấy header của History
    public Receipt(int receiptID, String creator, String fullname, String phoneNumber, String address, String startDay, String endDay, String note, int status) {
        this.receiptID = receiptID;
        this.creator = creator;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.startDay = startDay;
        this.endDay = endDay;
        this.note = note;
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    // Constructor này dùng để lấy phiếu mượn trả về từ database qua phương thức, ReceiptDAO.getReceiptByMemberID(int memberID)
    public Receipt(int receiptID, String startDay, String endDay, String note, int memberID, int status) {
        this.receiptID = receiptID;
        this.startDay = startDay;
        this.endDay = endDay;
        this.note = note;
        this.memberID = memberID;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Constructor này để tạo phiếu mượn, insert vào database, sử dụng phương thức,  ReceiptDAO.addReceipt(Receipt receipt)
    public Receipt(String startDay, String endDay, String note, int memberID, int status) {
        this.startDay = startDay;
        this.endDay = endDay;
        this.note = note;
        this.memberID = memberID;
        this.status = status;
    }

    public Receipt(String creator, String startDay, String endDay, String note, int memberID, int status) {
        this.creator = creator;
        this.startDay = startDay;
        this.endDay = endDay;
        this.note = note;
        this.memberID = memberID;
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
