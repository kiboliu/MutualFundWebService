package com.DeltaMutualFund.DeltaMutualFund.helper;

public class SellHelper {
    private String fundname;

    public String getFundname() {
        return fundname;
    }

    public void setFundname(String fundname) {
        this.fundname = fundname;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    private double shares;
    public SellHelper() {}
    public SellHelper(String fundname, double shares) {
        this.fundname = fundname;
        this.shares = shares;
    }
}
