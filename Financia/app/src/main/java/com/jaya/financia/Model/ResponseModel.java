package com.jaya.financia.Model;

import java.util.List;

public class ResponseModel {
    private int kode;
    private String pesan;
    private List<DataModel> data;
    private int total;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

    public int getTotal() {return total;}

    public void setTotal(int total) {this.total = total;}
}
