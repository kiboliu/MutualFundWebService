package com.DeltaMutualFund.DeltaMutualFund.helper;


public class PositionHelper {
    private Long fundId;

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String fundName;
    private double shares;
    private double price;
    public PositionHelper(){}
    public PositionHelper(Long fundId, String fundName, double shares, double price) {
        this.fundId = fundId;
        this.fundName = fundName;
        this.shares = shares;
        this.price = price;
    }

}
