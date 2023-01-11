package com.jaya.financia.Model;

import java.util.List;

public class ResponseAnalyticIncomes {
    private int kode;
    private String pesan;
    private List<AnalyticIncomesModel> data;

    public ResponseAnalyticIncomes(int kode, String pesan, List<AnalyticIncomesModel> data) {
        this.kode = kode;
        this.pesan = pesan;
        this.data = data;
    }

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

    public List<AnalyticIncomesModel> getData() {
        return data;
    }

    public void setData(List<AnalyticIncomesModel> data) {
        this.data = data;
    }
}
