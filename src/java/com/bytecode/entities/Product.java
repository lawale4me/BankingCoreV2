/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytecode.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ahmed
 */
@Entity
@Table(name = "product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findByProductid", query = "SELECT p FROM Product p WHERE p.productid = :productid"),
    @NamedQuery(name = "Product.findByProductcode", query = "SELECT p FROM Product p WHERE p.productcode = :productcode"),
    @NamedQuery(name = "Product.findByProductlastno", query = "SELECT p FROM Product p WHERE p.productlastno = :productlastno"),
    @NamedQuery(name = "Product.findByProductname", query = "SELECT p FROM Product p WHERE p.productname = :productname"),
    @NamedQuery(name = "Product.findByProductnextvalue", query = "SELECT p FROM Product p WHERE p.productnextvalue = :productnextvalue"),
    @NamedQuery(name = "Product.findByProducttype", query = "SELECT p FROM Product p WHERE p.producttype = :producttype")})
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "productid")
    private Integer productid;
    @Size(max = 255)
    @Column(name = "productcode")
    private String productcode;
    @Column(name = "productlastno")
    private BigInteger productlastno;
    @Size(max = 255)
    @Column(name = "productname")
    private String productname;
    @Column(name = "productnextvalue")
    private Integer productnextvalue;
    @Size(max = 255)
    @Column(name = "producttype")
    private String producttype;
    @OneToMany(mappedBy = "prodtype")
    private Collection<Paccount> paccountCollection;

    public Product() {
    }

    public Product(Integer productid) {
        this.productid = productid;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public BigInteger getProductlastno() {
        return productlastno;
    }

    public void setProductlastno(BigInteger productlastno) {
        this.productlastno = productlastno;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Integer getProductnextvalue() {
        return productnextvalue;
    }

    public void setProductnextvalue(Integer productnextvalue) {
        this.productnextvalue = productnextvalue;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    @XmlTransient
    public Collection<Paccount> getPaccountCollection() {
        return paccountCollection;
    }

    public void setPaccountCollection(Collection<Paccount> paccountCollection) {
        this.paccountCollection = paccountCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productid != null ? productid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.productid == null && other.productid != null) || (this.productid != null && !this.productid.equals(other.productid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bytecode.entities.Product[ productid=" + productid + " ]";
    }
    
}
