package com.DeltaMutualFund.DeltaMutualFund.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class Positionidentity implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    private Long cusId;

    public Long getCusId() {
        return cusId;
    }

    public void setCusId(Long cusId) {
        this.cusId = cusId;
    }

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    @NotNull
    private Long fundId;

    public Positionidentity() {

    }

    public Positionidentity(Long cusId, Long fundId) {
        this.cusId = cusId;
        this.fundId = fundId;
    }

}
