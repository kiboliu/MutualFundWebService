package com.DeltaMutualFund.DeltaMutualFund.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Funddetails {

    @EmbeddedId
    private Fundidentity fundIdentity;
    
    private double fundprice;
    
    public Funddetails() {
        
    }

    public Funddetails(Fundidentity fundidentity, double fundprice) {
        this.fundIdentity = fundidentity;
        this.fundprice = fundprice;
    }

    public Fundidentity getFundIdentity() {
        return fundIdentity;
    }

    public void setFundIdentity(Fundidentity fundIdentity) {
        this.fundIdentity = fundIdentity;
    }

    public double getFundprice() {
        return fundprice;
    }

    public void setFundprice(double fundprice) {
        this.fundprice = fundprice;
    }
    
    

}
