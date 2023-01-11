package com.jaya.financia.Model;

public class AnalyticIncomesModel {
    private String month;
    private String total_incomes;

    public AnalyticIncomesModel(String month, String total_incomes) {
        this.month = month;
        this.total_incomes = total_incomes;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotal_incomes() {
        return total_incomes;
    }

    public void setTotal_incomes(String total_incomes) {
        this.total_incomes = total_incomes;
    }
}
