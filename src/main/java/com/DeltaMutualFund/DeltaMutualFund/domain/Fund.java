package com.DeltaMutualFund.DeltaMutualFund.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Fund {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Fund Name can not be empty!")
    @Column(unique = true)
    private String name;
    @NotEmpty(message = "Fund symbol can not be empty!")
    @Size(min=1, max=5, message ="Fund symbol must be a one to five long string!")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Fund ticker only allows alphabets")
    @Column(unique = true)
    private String symbol;

    //default constructor
    protected Fund() {
    }

    //constructor with parameter
    public Fund(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

}

