package com.DeltaMutualFund.DeltaMutualFund.helper;

import java.util.List;

public class PriceList {
    public List<FundHelper> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<FundHelper> priceList) {
        this.priceList = priceList;
    }
    public PriceList(){}
    private List<FundHelper> priceList;

    public String validate() {
        String error = "";
        if (priceList == null || priceList.size() == 0) {
            error = "Prices are null.";
            return error;
        }
        try {
            for (int i = 0; i < priceList.size(); i++) {
                double d = Double.parseDouble(priceList.get(i).getPrice().replace(",", ""));
                if (d < 0.01 || d > 10000) {
                    error = "Prices are not satisfied with the number limitation.";
                    return error;
                }
//                if (prev > 0.009 && d < prev * 0.8) {
//                    error = "Please set new price NOT below 20% to the last price.";
//                    return error;
//                }
//                if (prev > 0.009 && d > prev * 1.2) {
//                    error = "Please set new price NOT exceed 20% to the last price.";
//                    return error;
//                }
            }
        } catch (NumberFormatException e) {
                error = "Invalid Price input.";
        }
        return error;
    }
}
