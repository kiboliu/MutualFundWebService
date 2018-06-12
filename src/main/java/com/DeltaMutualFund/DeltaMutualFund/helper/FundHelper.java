package com.DeltaMutualFund.DeltaMutualFund.helper;

public class FundHelper {
    public FundHelper(String name, String symbol, String price, double lastprice) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.lastprice = lastprice;
    }

    public FundHelper(){}
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String symbol;
    private String price;

    public double getLastprice() {
        return lastprice;
    }

    public void setLastprice(double lastprice) {
        this.lastprice = lastprice;
    }

    private double lastprice;

}
