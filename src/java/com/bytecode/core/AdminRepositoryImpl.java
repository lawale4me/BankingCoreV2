/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytecode.core;

import com.bytecode.entities.Account;
import com.bytecode.entities.Appuser;
import com.bytecode.entities.Auditreport;
import com.bytecode.entities.Loan;
import com.bytecode.entities.Loanapllication;
import com.bytecode.entities.Paccount;
import com.bytecode.entities.Product;
import com.bytecode.entities.Transactions;
import com.bytecode.util.AdminRepository;
import com.bytecode.util.RepositoryBase;
import com.bytecode.util.RepositoryManager;
import com.bytecode.util.ResponseCode;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Ahmed
 */
public class AdminRepositoryImpl extends RepositoryBase implements AdminRepository {

    public AdminRepositoryImpl() {
    }

    @Override
    public void registerAccount(Account account) {
        EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        manager.persist(account);
        manager.getTransaction().commit();
    }

    @Override
    public void registerPAccount(Paccount pacct) {
        EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        manager.persist(pacct);
        manager.getTransaction().commit();
    }

    @Override
    public Product getProduct(String prodcode) {
        if (session != null && session.isActive()) {
            List<Product> prod = manager.createNamedQuery("Product.findByProductcode", Product.class).setParameter("productcode", prodcode).getResultList();
            return prod.isEmpty() ? null : prod.get(0);
        } else {
            EntityManager manage = RepositoryManager.getManager();
            List<Product> prod = manager.createNamedQuery("Product.findByProductcode", Product.class).setParameter("productcode", prodcode).getResultList();
            manage.close();
            return prod.isEmpty() ? null : prod.get(0);
        }
    }

    @Override
    public Paccount getPaccount(String accountNo) {
        if (session != null && session.isActive()) {
            List<Paccount> account = manager.createNamedQuery("Paccount.findByAccountno", Paccount.class).setParameter("accountno", accountNo).getResultList();
            return account.isEmpty() ? null : account.get(0);
        } else {
            EntityManager manage = RepositoryManager.getManager();
            List<Paccount> account = manage.createNamedQuery("Paccount.findByAccountno", Paccount.class).setParameter("accountno", accountNo).getResultList();
            manage.close();
            return account.isEmpty() ? null : account.get(0);
        }
    }

     public Product updateProduct(Product prod) {
       EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        prod= manager.merge(prod);
        manager.getTransaction().commit();
        return prod;
    }

    public Paccount updatePaccount(Paccount pacct) {
        EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        pacct= manager.merge(pacct);
        manager.getTransaction().commit();
        return pacct;
    }

    public Transactions updateTrxn(Transactions trxn) {
        EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        trxn= manager.merge(trxn);
        manager.getTransaction().commit();
        return trxn;
    }

    @Override
    public void applyForLoan(Loan loan) {
       EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        manager.persist(loan);
        manager.getTransaction().commit(); 
    }

   public boolean checkEmailInApplictaion(String email) {
         if (session != null && session.isActive()) {
            List<Loanapllication> loan = manager.createNamedQuery("Loanapllication.findByEmail", Loanapllication.class).setParameter("email", email).getResultList();
            return loan.isEmpty() ? false : true;
        } else {
            EntityManager manage = RepositoryManager.getManager();
            List<Loanapllication> loan = manager.createNamedQuery("Loanapllication.findByEmail", Loanapllication.class).setParameter("email", email).getResultList();
            manage.close();
            return loan.isEmpty() ? false : true;
        }
    }

    public boolean checkEmailInAccount(String email) {
         if (session != null && session.isActive()) {
            List<Account> account = manager.createNamedQuery("Account.findByAccountemail", Account.class).setParameter("accountemail", email).getResultList();
            return account.isEmpty() ? false : true;
        } else {
            EntityManager manage = RepositoryManager.getManager();
            List<Account> account = manager.createNamedQuery("Account.findByAccountemail", Account.class).setParameter("accountemail", email).getResultList();
            manage.close();
            return account.isEmpty() ? false : true;
        }
    }

    public void loanApplictaion(Loanapllication loanApplication) {
        EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        manager.persist(loanApplication);
        manager.getTransaction().commit();
    }

    public Appuser getUser(String userID) 
    {
        if(session!=null&&session.isActive())
        {
        List<Appuser> user = manager.createNamedQuery("Appuser.findByUsername", Appuser.class).setParameter("username", userID).getResultList();
        return user.isEmpty() ? null : user.get(0); 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Appuser> user = manager.createNamedQuery("Appuser.findByUsername", Appuser.class).setParameter("username", userID).getResultList();
        manager.close();
        return user.isEmpty() ? null : user.get(0); 
        }         
    
    }
    
    
    public Appuser validate(String userid, String password) {
        EntityManager manager = RepositoryManager.getManager();
        String query="SELECT u FROM Appuser u WHERE u.username = :username and u.password = :password";
        List<Appuser> adminUser =    manager.createQuery(query,Appuser.class).setParameter("username", userid).setParameter("password", password).getResultList();
        return adminUser.isEmpty() ? null : adminUser.get(0);
    }

    public List<Loanapllication> getAllLoans() {
        if(session!=null&&session.isActive())
        {
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByLatest", Loanapllication.class).getResultList();
        return loans.isEmpty() ? null : loans; 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByLatest", Loanapllication.class).getResultList();
        manager.close();
        return loans.isEmpty() ? null : loans; 
        } 
    }

    public List<Loanapllication> getPendingLoans() {
        if(session!=null&&session.isActive())
        {
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByStatus", Loanapllication.class).setParameter("status", 0).getResultList();
        return loans.isEmpty() ? null : loans; 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByStatus", Loanapllication.class).setParameter("status", 0).getResultList();
        manager.close();
        return loans.isEmpty() ? null : loans; 
        } 
    }
    
    public List<Loanapllication> getApprovedLoans() {
        if(session!=null&&session.isActive())
        {
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByStatus", Loanapllication.class).setParameter("status", 1).getResultList();
        return loans.isEmpty() ? null : loans; 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByStatus", Loanapllication.class).setParameter("status", 1).getResultList();
        manager.close();
        return loans.isEmpty() ? null : loans; 
        } 
    }
    
    
    public List<Appuser> getAllUsers() {
         if(session!=null&&session.isActive())
        {
        List<Appuser> users = manager.createNamedQuery("Appuser.findAll", Appuser.class).getResultList();
        return users.isEmpty() ? null : users; 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Appuser> users = manager.createNamedQuery("Appuser.findAll", Appuser.class).getResultList();
        manager.close();
        return users.isEmpty() ? null : users; 
        }
    }

    public void addUser(Appuser user) 
    {
        if(session!=null&&session.isActive())
        {
         manager.persist(user);
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        manager.persist(user);
        manager.getTransaction().commit();
        }
    }

    public void audit(Auditreport ar) {
        if(session!=null&&session.isActive())
        {
         manager.persist(ar);
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        manager.persist(ar);
        manager.getTransaction().commit();
        }
    }

    public List<Auditreport> getAllAuditReports() {
          if(session!=null&&session.isActive())
        {
        List<Auditreport> report = manager.createNamedQuery("Auditreport.findAll", Auditreport.class).getResultList();
        return report.isEmpty() ? null : report; 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Auditreport> report = manager.createNamedQuery("AuditReport.findAll", Auditreport.class).getResultList();
        manager.close();
        return report.isEmpty() ? null : report; 
        } 
    }

    public Appuser getUser(Integer userID) {
         if(session!=null&&session.isActive())
        {
        List<Appuser> user = manager.createNamedQuery("Appuser.findById", Appuser.class).setParameter("id", userID).getResultList();
        return user.isEmpty() ? null : user.get(0); 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Appuser> user = manager.createNamedQuery("Appuser.findById", Appuser.class).setParameter("id", userID).getResultList();
        manager.close();
        return user.isEmpty() ? null : user.get(0); 
        }        
    }

    public ResponseDetails createUser(Appuser adminUser) {
          ResponseDetails resp=new ResponseDetails();              
        try{ 
        if(session!=null&&session.isActive())
        {                  
                manager.persist(adminUser);                
        }
        else
        {
            EntityManager manager = RepositoryManager.getManager();
            manager.persist(adminUser);        
            manager.close();                             
         }
                resp.setErrorCode(ResponseCode.OK);
                resp.setErrorDescription("user created");         
                return resp;
         } catch (NoResultException e) {
             resp.setErrorCode(ResponseCode.USER_NOT_FOUND);            
             resp.setErrorDescription("invalid username or pass");     
             return resp;
            }
    }

    public List<Loanapllication> getAuthorizedLoans() {
        if(session!=null&&session.isActive())
        {
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByStatus", Loanapllication.class).setParameter("status", 2).getResultList();
        return loans.isEmpty() ? null : loans; 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByStatus", Loanapllication.class).setParameter("status", 2).getResultList();
        manager.close();
        return loans.isEmpty() ? null : loans; 
        }
    }
    
    public List<Loanapllication> getDisbursedLoans() {
        if(session!=null&&session.isActive())
        {
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByStatus", Loanapllication.class).setParameter("status", 3).getResultList();
        return loans.isEmpty() ? null : loans; 
        }
        else
        {
        EntityManager manager = RepositoryManager.getManager();
        List<Loanapllication> loans = manager.createNamedQuery("Loanapllication.findByStatus", Loanapllication.class).setParameter("status", 3).getResultList();
        manager.close();
        return loans.isEmpty() ? null : loans; 
        }
    }

    public void updateLoan(Loanapllication selectedLoan) {
           if (session!=null&&session.isActive()) {
            manager.merge(selectedLoan);
        }else{
        EntityManager manager = RepositoryManager.getManager();
        manager.getTransaction().begin();
        manager.merge(selectedLoan);
        manager.getTransaction().commit();
        }
    }

}
