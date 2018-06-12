package com.DeltaMutualFund.DeltaMutualFund.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;


@Embeddable
public class Fundidentity implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    @NotNull
    private Long fundId;

    @NotNull
    private Date priceDate;

    public Fundidentity() {

    }

    public Fundidentity(Long fundId, Date priceDate) {
        this.fundId = fundId;
        this.priceDate = priceDate;
    }

}
