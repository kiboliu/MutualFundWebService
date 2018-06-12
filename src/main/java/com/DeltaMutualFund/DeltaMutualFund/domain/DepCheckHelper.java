package com.DeltaMutualFund.DeltaMutualFund.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DepCheckHelper {
    
    @Id
    private String username;
    
    private String amount;
    
    //constructor with parameter
    public DepCheckHelper(String username, String amount) {
        this.username = username;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    
    
}
