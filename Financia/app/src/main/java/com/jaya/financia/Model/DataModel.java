package com.jaya.financia.Model;

public class DataModel {
    private int id;
    private String type;
    private String category;
    private String note;
    private String amount;
    private String date;

    public DataModel(int id, String type, String category, String note, String amount, String date) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.note = note;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
