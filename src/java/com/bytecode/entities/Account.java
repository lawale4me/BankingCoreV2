/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytecode.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ahmed
 */
@Entity
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByAccountid", query = "SELECT a FROM Account a WHERE a.accountid = :accountid"),
    @NamedQuery(name = "Account.findByAccountemail", query = "SELECT a FROM Account a WHERE a.accountemail = :accountemail"),
    @NamedQuery(name = "Account.findByAccountname", query = "SELECT a FROM Account a WHERE a.accountname = :accountname"),
    @NamedQuery(name = "Account.findByAccountphone", query = "SELECT a FROM Account a WHERE a.accountphone = :accountphone"),
    @NamedQuery(name = "Account.findByAddress", query = "SELECT a FROM Account a WHERE a.address = :address"),
    @NamedQuery(name = "Account.findByDateopened", query = "SELECT a FROM Account a WHERE a.dateopened = :dateopened"),
    @NamedQuery(name = "Account.findByDob", query = "SELECT a FROM Account a WHERE a.dob = :dob"),
    @NamedQuery(name = "Account.findByGender", query = "SELECT a FROM Account a WHERE a.gender = :gender"),
    @NamedQuery(name = "Account.findByPin", query = "SELECT a FROM Account a WHERE a.pin = :pin"),
    @NamedQuery(name = "Account.findByStatus", query = "SELECT a FROM Account a WHERE a.status = :status"),
    @NamedQuery(name = "Account.findByUserid", query = "SELECT a FROM Account a WHERE a.userid = :userid")})
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "accountid")
    private Integer accountid;
    @Size(max = 255)
    @Column(name = "accountemail")
    private String accountemail;
    @Size(max = 255)
    @Column(name = "accountname")
    private String accountname;
    @Size(max = 255)
    @Column(name = "accountphone")
    private String accountphone;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @Column(name = "dateopened")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateopened;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Size(max = 255)
    @Column(name = "gender")
    private String gender;
    @Size(max = 255)
    @Column(name = "pin")
    private String pin;
    @Column(name = "status")
    private Integer status;
    @Size(max = 255)
    @Column(name = "userid")
    private String userid;
    @OneToMany(mappedBy = "accountid")
    private Collection<Loan> loanCollection;
    @OneToMany(mappedBy = "accountid")
    private Collection<Paccount> paccountCollection;
    @OneToMany(mappedBy = "accountid")
    private Collection<Transactions> transactionsCollection;

    public Account() {
    }

    public Account(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getAccountid() {
        return accountid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public String getAccountemail() {
        return accountemail;
    }

    public void setAccountemail(String accountemail) {
        this.accountemail = accountemail;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountphone() {
        return accountphone;
    }

    public void setAccountphone(String accountphone) {
        this.accountphone = accountphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateopened() {
        return dateopened;
    }

    public void setDateopened(Date dateopened) {
        this.dateopened = dateopened;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @XmlTransient
    public Collection<Loan> getLoanCollection() {
        return loanCollection;
    }

    public void setLoanCollection(Collection<Loan> loanCollection) {
        this.loanCollection = loanCollection;
    }

    @XmlTransient
    public Collection<Paccount> getPaccountCollection() {
        return paccountCollection;
    }

    public void setPaccountCollection(Collection<Paccount> paccountCollection) {
        this.paccountCollection = paccountCollection;
    }

    @XmlTransient
    public Collection<Transactions> getTransactionsCollection() {
        return transactionsCollection;
    }

    public void setTransactionsCollection(Collection<Transactions> transactionsCollection) {
        this.transactionsCollection = transactionsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountid != null ? accountid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.accountid == null && other.accountid != null) || (this.accountid != null && !this.accountid.equals(other.accountid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bytecode.entities.Account[ accountid=" + accountid + " ]";
    }
    
}
