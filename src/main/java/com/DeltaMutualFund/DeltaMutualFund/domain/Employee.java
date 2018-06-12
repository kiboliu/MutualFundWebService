package com.DeltaMutualFund.DeltaMutualFund.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Employee implements UserDetails{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @NotEmpty(message = "Employee name can not be empty!")
    @Size(min=1, max=20, message ="Employee name must between 1 and 20 characters!")
    @Column(nullable = false, length = 20)
    private String username;

    @NotEmpty(message = "Employee password can not be empty!")
    @Size(min=3, max=100, message= "Employee name must between 3 and 20 characters!")
    @Column(nullable = false, length = 100)
    private String password;

    @NotEmpty(message = "Employee first name can not be empty!")
    @Size(min=1, max=100, message ="Employee first name must between 1 and 100 characters!")
    @Column(nullable = false, length = 100)
    private String firstname;

    @NotEmpty(message = "Employee last name can not be empty!")
    @Size(min=1, max=100, message ="Employee first name must between 1 and 100 characters!")
    @Column(nullable = false, length = 100)
    private String lastname;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "employee_authority", joinColumns = @JoinColumn(name = "employee_name", referencedColumnName = "username"),
        inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

  //default constructor
    protected Employee() {
    }

    //constructor with parameter
    public Employee(String username, String firstname, String lastname, String password) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
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



    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
        for(GrantedAuthority authority : this.authorities){
            simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return simpleAuthorities;
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

}
