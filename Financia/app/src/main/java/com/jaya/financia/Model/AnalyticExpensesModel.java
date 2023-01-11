package com.jaya.financia.Model;

public class AnalyticExpensesModel {
    private String category;
    private String total;

    public AnalyticExpensesModel(String category, String total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
