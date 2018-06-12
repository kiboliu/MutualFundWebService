package com.DeltaMutualFund.DeltaMutualFund.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "User name can not be empty!")
    @Size(min=1, max=20, message ="User name must between 1 and 20 characters!")
    @Column(nullable = false, length = 20)
    private String username;

    @NotEmpty(message = "Password can not be empty!")
    @Size(min=3, max=100, message ="User password must between 3 and 100 characters!")
    @Column(length = 100)
    private String password;

    @NotEmpty(message = "User first name can not be empty!")
    @Size(min=1, max=100, message ="User first name must between 1 and 100 characters!")
    @Column(length = 100)
    private String firstname;

    @NotEmpty(message = "User last name can not be empty!")
    @Size(min=1, max=100, message ="User last name must between 1 and 100 characters!")
    @Column(length = 100)
    private String lastname;

    @NotEmpty(message = "Address line 1 can not be empty!")
    @Size(min=1, max=100, message ="Address line 1 must between 1 and 100 characters!")
    @Column(length = 100)
    private String addr_line1;

    @Column(length = 100)
    private String addr_line2;

    @NotEmpty(message = "City can not be empty!")
    @Size(min=1, max=100, message ="City must between 1 and 100 characters!")
    @Column(length = 100)
    private String city;

    @NotEmpty(message = "State can not be empty!")
    @Size(min=2, max=2, message ="State must be 2 characters!")
    @Column(length = 2)
    private String state;

    @NotEmpty(message = "Zip code can not be empty!")
    @Size(min=5, max=5, message ="Zip code must be 5 characters!")
    @Column(length = 5)
    private String zip;

    @Min(0)
    @Max(1000000000)
    private double cash;

    @Min(0)
    @Max(1000000000)
    private double availablebalance;

    public double getAvailablebalance() {
        return availablebalance;
    }

    public void setAvailablebalance(double availablebalance) {
        this.availablebalance = availablebalance;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

    public User(){

    }

    public User(String username, String password, String firstname, String lastname, String addr_line1, String addr_line2, String city, String state, String zip, double cash, double availablebalance) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.addr_line1 = addr_line1;
        this.addr_line2 = addr_line2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.cash = cash;
        this.availablebalance = availablebalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddr_line1() {
        return addr_line1;
    }

    public void setAddr_line1(String addr_line1) {
        this.addr_line1 = addr_line1;
    }

    public String getAddr_line2() {
        return addr_line2;
    }

    public void setAddr_line2(String addr_line2) {
        this.addr_line2 = addr_line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d,username='%s',password='%s']", id, username, password);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (GrantedAuthority authority : this.authorities) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return simpleGrantedAuthorities;
    }

}
