package com.jaya.financia.Model;

import java.util.List;

public class ResponseAnalyticExpenses {
    private int kode;
    private String pesan;
    private List<AnalyticExpensesModel> data;

    public ResponseAnalyticExpenses(int kode, String pesan, List<AnalyticExpensesModel> data) {
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

    public List<AnalyticExpensesModel> getData() {
        return data;
    }

    public void setData(List<AnalyticExpensesModel> data) {
        this.data = data;
    }
}
