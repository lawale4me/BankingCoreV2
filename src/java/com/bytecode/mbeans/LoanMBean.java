/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bytecode.mbeans;


import com.bytecode.core.AdminRepositoryImpl;
import com.bytecode.core.ConManager;
import com.bytecode.entities.Appuser;
import com.bytecode.entities.Loanapllication;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ahmed
 */
@ManagedBean
@RequestScoped
public class LoanMBean {

    private List<Loanapllication> loans;
    private List<Loanapllication> filteredLoans;
    private Loanapllication loan;
    private Loanapllication selectedLoan;
    String username;
    String approvedAmount="";
    String comment="";
    Appuser user;
    ConManager appManager = new ConManager(new AdminRepositoryImpl());
    
    /**
     * Creates a new instance of ActivityMBean
     */
    public LoanMBean() {
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        username=(String) httpSession.getAttribute("username");  
        user=appManager.getUser(username);                        
        loans = appManager.getPendingLoans();
    }

    public List<Loanapllication> getLoans() {
        return loans;
    }

    public void setLoans(List<Loanapllication> loans) {
        this.loans = loans;
    }

    public List<Loanapllication> getFilteredLoans() {
        return filteredLoans;
    }

    public void setFilteredLoans(List<Loanapllication> filteredLoans) {
        this.filteredLoans = filteredLoans;
    }

    public Loanapllication getLoan() {
        return loan;
    }

    public void setLoan(Loanapllication loan) {
        this.loan = loan;
    }

    public Loanapllication getSelectedLoan() {
        return selectedLoan;
    }

    public void setSelectedLoan(Loanapllication selectedLoan) {
        this.selectedLoan = selectedLoan;
    }

    
     public void updateLoan(int status)
    {
        System.out.println("updateLoan"+status);  
        System.out.println("Comment"+comment);
       // selectedLoan.setStatus(status);
        //appManager.updateLoan(selectedLoan);            
    }               

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
                  
    
     
     
}
