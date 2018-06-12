package com.DeltaMutualFund.DeltaMutualFund.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Position {
    
    @EmbeddedId
    private Positionidentity positionIdentity;
    
    private double fundshares;

    public Positionidentity getPositionIdentity() {
        return positionIdentity;
    }

    public void setPositionIdentity(Positionidentity positionIdentity) {
        this.positionIdentity = positionIdentity;
    }

    public double getFundshares() {
        return fundshares;
    }

    public void setFundshares(double fundshares) {
        this.fundshares = fundshares;
    }
    
    public Position() {

    }

    public Position(Positionidentity positionidentity, double fundshares) {
        this.positionIdentity = positionidentity;
        this.fundshares = fundshares;
    }
}
