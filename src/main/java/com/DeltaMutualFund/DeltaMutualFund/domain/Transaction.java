package com.DeltaMutualFund.DeltaMutualFund.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.DeltaMutualFund.DeltaMutualFund.repository.FunddetailsRepository;
import com.DeltaMutualFund.DeltaMutualFund.repository.UserRepository;

import java.sql.Date;

@Entity
public class Transaction {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private Long cusid;
    private Long fund_id;
    private String fund_name;
    private Date exe_date;
    private double shares;
    private String transactiontype;
    private double amount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCus_id() {
        return cusid;
    }
    public void setCus_id(Long cus_id) {
        this.cusid = cus_id;
    }
    public Long getFund_id() {
        return fund_id;
    }
    public void setFund_id(Long fund_id) {
        this.fund_id = fund_id;
    }
    public Date getExe_date() {
        return exe_date;
    }
    public void setExe_date(Date exe_date) {
        this.exe_date = exe_date;
    }
    public double getShares() {
        return shares;
    }
    public void setShares(double shares) {
        this.shares = shares;
    }
    public String getTransactiontype() {
        return transactiontype;
    }
    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

}
