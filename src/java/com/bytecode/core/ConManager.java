/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bytecode.core;

import com.bytecode.dto.AccountDTO;
import com.bytecode.dto.AuditReportDTO;
import com.bytecode.dto.LoanDTO;
import com.bytecode.dto.LoanStatus;
import com.bytecode.dto.Response;
import com.bytecode.entities.Account;
import com.bytecode.entities.Appuser;
import com.bytecode.entities.Auditreport;
import com.bytecode.entities.Loanapllication;
import com.bytecode.entities.Paccount;
import com.bytecode.entities.Product;
import com.bytecode.entities.Transactions;
import com.bytecode.response.LoanApplicationResponse;
import com.bytecode.util.GenerateNumbers;
import com.bytecode.util.ResponseCode;
import com.bytecode.util.UnitOfWorkSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Ahmed
 */
public class ConManager {

    AdminRepositoryImpl adminrepo;

    public ConManager(AdminRepositoryImpl adminRepositoryImpl) {
        this.adminrepo = adminRepositoryImpl;
    }

    public String registerAccount(AccountDTO accountdto) throws RegisterException {
        UnitOfWorkSession ses = adminrepo.begin();
        Long acctno = null;

        Product prod = adminrepo.getProduct(accountdto.getProdcode());
        if (prod != null) {
            /**
             * ***ACCOUNT DETAILS******
             */
            Account acct = new Account();
            acct.setAccountname(accountdto.getAccountname());
            acct.setAccountphone(accountdto.getAccountphone());
            acct.setAccountemail(accountdto.getAccountemail());
            acct.setAddress(accountdto.getAddress());
            acct.setDateopened(new Date());
            acct.setDob(accountdto.getDob());
            acct.setStatus(ResponseCode.ACTIVE);
            acct.setAddress(accountdto.getAddress());
            acct.setPin(accountdto.getPin()==null?String.valueOf(GenerateNumbers.generateRandom(4)):accountdto.getPin().toString());
            adminrepo.registerAccount(acct);
            //CRITICAL SECTION
            synchronized (this) {
//                acctno = prod.getProductlastno() + prod.getProductnextvalue();
//                prod.setProductlastno(acctno);
                adminrepo.updateProduct(prod);
            }
                //END OF CRITICAL SECTION

            /**
             * ***PRODUCT DETAILS******
             */
            Paccount pacct = new Paccount();
            pacct.setAccountid(acct);
            pacct.setAccountno(acctno.toString());//DYNAMIC ACCOUNTNUMBER
            pacct.setBalance(ResponseCode.EMPTY_BALANCE);
            pacct.setLedgerbalance(ResponseCode.EMPTY_BALANCE);
            pacct.setDateopened(new Date());
            pacct.setProdtype(prod);
            adminrepo.registerPAccount(pacct);
            System.out.println("Generated Accountnumber:"+acctno);
        }
        else{
          throw new RegisterException("Invalid Product");    
        }
        ses.commit();
        
        return acctno.toString();
    }

    public void transferFunds(Transactions trxn) throws TransferException {
        UnitOfWorkSession ses = adminrepo.begin();
        if (trxn.getSourceaccount().equalsIgnoreCase(trxn.getDestaccount())) {
            throw new TransferException(ResponseCode.SAME_ACCOUNT_TRANSFER_NOT_POSSIBLE);
        }
        synchronized (this) {
            Paccount pacct = getPaccount(trxn.getSourceaccount());
            if (pacct != null) {
                Paccount destPacct = adminrepo.getPaccount(trxn.getDestaccount());
                if (destPacct != null) {
                    if (pacct.getBalance() >= trxn.getAmount()) {
                        pacct.setBalance(pacct.getBalance() - trxn.getAmount());
                        destPacct.setBalance(destPacct.getBalance() + trxn.getAmount());
                        trxn.setStatus(ResponseCode.OK);
                        trxn.setDescription(ResponseCode.SUCCESSFUL);
                        adminrepo.updatePaccount(pacct);
                        adminrepo.updatePaccount(destPacct);
                        adminrepo.updateTrxn(trxn);
                    } else {
                        trxn.setStatus(ResponseCode.FAILED);
                        trxn.setDescription(ResponseCode.SFAILED);
                        adminrepo.updateTrxn(trxn);
                        throw new TransferException(ResponseCode.INSUFFICIENT_BALANCE);
                    }
                } else {
                    trxn.setStatus(ResponseCode.FAILED);
                    trxn.setDescription(ResponseCode.SFAILED);
                    adminrepo.updateTrxn(trxn);
                    throw new TransferException(ResponseCode.DEST_ACCOUNT_DOES_NOT_EXIST);
                }

            }
        }
        ses.commit();
    }

    public Paccount getPaccount(String accountNo) {
        UnitOfWorkSession ses = adminrepo.begin();
        Paccount acct = adminrepo.getPaccount(accountNo);
        ses.commit();
        return acct;
    }

    public Double getBalance(String accountNo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updatePaccount(Paccount acct) {
        UnitOfWorkSession ses = adminrepo.begin();
        adminrepo.updatePaccount(acct);
        ses.commit();
    }

    public void fundWithrawal(Transactions trxn) throws WithdrawalException {
        UnitOfWorkSession ses = adminrepo.begin();
        Paccount pacct = getPaccount(trxn.getSourceaccount());
        if (pacct != null) {
            System.out.println("GOT HERE");
            if (pacct.getBalance() >= trxn.getAmount()) {
                System.out.println("Balance:"+pacct.getBalance());
                pacct.setBalance(pacct.getBalance() - trxn.getAmount());
                System.out.println("Balance:"+pacct.getBalance());
                trxn.setStatus(ResponseCode.OK);
                trxn.setDescription(ResponseCode.SUCCESSFUL);
                adminrepo.updatePaccount(pacct);
                adminrepo.updateTrxn(trxn);
                System.out.println("Balance:"+pacct.getBalance());
            } else {
                System.out.println("GOT HERE ELSE");
                trxn.setStatus(ResponseCode.FAILED);
                trxn.setDescription(ResponseCode.SFAILED);
                adminrepo.updateTrxn(trxn);
                throw new WithdrawalException(ResponseCode.INSUFFICIENT_BALANCE);
            }
        }else{
            throw new WithdrawalException(ResponseCode.INVALID_ACCOUNTNO);
        }
        ses.commit();
    }
    
     public void fundDeposit(Transactions trxn) throws WithdrawalException {
        UnitOfWorkSession ses = adminrepo.begin();
        Paccount pacct = getPaccount(trxn.getSourceaccount());
        if (pacct != null) {
            if (true) //check product max deposit
            {
                pacct.setBalance(pacct.getBalance() + trxn.getAmount());
                trxn.setStatus(ResponseCode.OK);
                trxn.setDescription(ResponseCode.SUCCESSFUL);
                adminrepo.updatePaccount(pacct);
                adminrepo.updateTrxn(trxn);
            } else {
                trxn.setStatus(ResponseCode.FAILED);
                trxn.setDescription(ResponseCode.SFAILED);
                adminrepo.updateTrxn(trxn);
                throw new WithdrawalException(ResponseCode.INSUFFICIENT_BALANCE);
            }
        }
        ses.commit();
    }

    public LoanApplicationResponse LoanApplication(LoanDTO loan) throws LoanException {
        UnitOfWorkSession ses = adminrepo.begin();        
        LoanApplicationResponse lres=new LoanApplicationResponse();

        
            Loanapllication loanApplication = new Loanapllication();
            loanApplication.setSurname(loan.getSurname());
            loanApplication.setPhone(loan.getPhone());
            loanApplication.setEmail(loan.getEmail());
            loanApplication.setAddress(loan.getAddress());
            loanApplication.setRequestdate(new Date());                               
            loanApplication.setStatus(LoanStatus.PENDING.ordinal());                        
            
            
            String refNum = String.valueOf(System.currentTimeMillis() + (new java.security.SecureRandom().nextInt(999) + 1));
            loanApplication.setReferenceno(refNum);                        
            loanApplication.setAnnualSalary(loan.getAnnualSalary());
            loanApplication.setBankAccountNo(loan.getBankAccountNo());
            loanApplication.setBankName(loan.getBankName());
            loanApplication.setBankStatement(loan.getBankStatement());
            loanApplication.setChannel(loan.getChannel());
            loanApplication.setDept(loan.getDept());
            loanApplication.setDob(loan.getDob());
            loanApplication.setEmpNo(loan.getEmpNo());
            loanApplication.setEmployer(loan.getEmployer());
            loanApplication.setEmployerAddress(loan.getEmployerAddress());
            loanApplication.setEmployerPhone(loan.getEmployerPhone());
            loanApplication.setGrade(loan.getGrade());
            loanApplication.setIdCard(loan.getIdCard());
            loanApplication.setJobDesc(loan.getJobDesc());
            loanApplication.setKinName(loan.getKinName());
            loanApplication.setKinPhone(loan.getKinPhone());
            loanApplication.setKinType(loan.getKinType());
            loanApplication.setKinWork(loan.getKinWork());
            loanApplication.setLengthOfService(loan.getLengthOfService());
            loanApplication.setLoanAmount(loan.getLoanAmount());
            loanApplication.setOfficeEmail(loan.getOfficeEmail());
            loanApplication.setOtherNames(loan.getOtherNames());
            loanApplication.setPayDay(loan.getPayDay());
            loanApplication.setPurpose(loan.getPurpose());
            loanApplication.setSignature(loan.getSignature());
            loanApplication.setTenor(loan.getTenor());
            loanApplication.setTitle(loan.getTitle());
            loanApplication.setTotalExistingLoan(loan.getTotalExistingLoan());
            loanApplication.setProdCode(loan.getProdCode());
            
            try{
            adminrepo.loanApplictaion(loanApplication);
            lres.setReferenceNo(refNum);
            }catch(Exception le){
                le.printStackTrace();
                throw new LoanException("LOAN APPLICATION Error");
            }
        ses.commit();
        return lres;
    }

    public boolean checkEmail(String email) {
        boolean status=false;
        UnitOfWorkSession ses = adminrepo.begin(); 
        status=adminrepo.checkEmailInApplictaion(email);        
        if(!status){
        status=adminrepo.checkEmailInAccount(email);
        }
        System.out.println("Email "+email+" Status:"+status);
        ses.commit();
        return status;
    }

     public Response login(String userid, String password) throws Exception {
      System.out.println("Attempt to logon to the application "+userid);
      Response authres=new Response();
        UnitOfWorkSession ses = adminrepo.begin();
        try {
          //  password = Util.hash(password);
        } catch (Exception ex) {            
            System.out.println(ex.getMessage());
            System.out.println("Password hashing issue "+userid);
            throw new Exception("Password hashing issue");
        }
        
        
        
        Appuser adminUser = adminrepo.validate(userid, password);
        if(adminUser!=null)
        {            
            if(true)
            {
            if(adminUser.getStatus()==1)
            {
            authres.setStatus(true);
            authres.setStatuscode(ResponseCode.ACTIVE);
            authres.setDescription("User exists");
            }
            else
            {
            authres.setStatuscode(ResponseCode.USER_DISABLED);
            authres.setDescription("User is disabled");
            }
            }
            else{
                authres.setStatuscode(ResponseCode.USER_EXPIRED);
                authres.setDescription("User password has expired");
            }
        } 
        else
        {             
             authres.setStatuscode(ResponseCode.USER_NOT_FOUND);            
             authres.setDescription("Invalid username or password");     
        }                
        
        ses.commit();
        return authres;  
    }

   public Appuser getUser(String userID) {
         UnitOfWorkSession ses = adminrepo.begin(); 
         Appuser mprofile=adminrepo.getUser(userID);
         ses.commit();
        return mprofile;
    }
   
   public Appuser getUser(Integer userID) {
         UnitOfWorkSession ses = adminrepo.begin(); 
         Appuser mprofile=adminrepo.getUser(userID);
         ses.commit();
        return mprofile;
    }

    public List<Loanapllication> getLoans() {        
        UnitOfWorkSession ses = adminrepo.begin(); 
        List<Loanapllication> loans=adminrepo.getAllLoans();
        ses.commit();
        return loans;
    }
    
    public List<Loanapllication> getPendingLoans() {        
        UnitOfWorkSession ses = adminrepo.begin(); 
        List<Loanapllication> loans=adminrepo.getPendingLoans();
        ses.commit();
        return loans;
    }
    
    public List<Loanapllication> getApprovedLoans() {        
        UnitOfWorkSession ses = adminrepo.begin(); 
        List<Loanapllication> loans=adminrepo.getApprovedLoans();
        ses.commit();
        return loans;
    }

    public List<Appuser> getAllAppusers() {
        UnitOfWorkSession ses = adminrepo.begin(); 
        List<Appuser> users=adminrepo.getAllUsers();
        ses.commit();
        return users;
    }

    public void addUser(Appuser user)  {
        UnitOfWorkSession ses = adminrepo.begin(); 
         adminrepo.addUser(user);
         ses.commit();
    }

    public void audit(String username, String action, String ipaddress) {
          UnitOfWorkSession ses = adminrepo.begin(); 
        Auditreport ar = new  Auditreport();
        ar.setAction(action);
        ar.setActiondate(new Date());
        ar.setIpaddress(ipaddress);
        ar.setUsername(username);
        adminrepo.audit(ar);
         ses.commit();
    }

    public List<AuditReportDTO> getAllAuditReports() {
            List<AuditReportDTO> ardtos=new ArrayList<AuditReportDTO>();
        UnitOfWorkSession ses = adminrepo.begin(); 
        List<Auditreport> ar=adminrepo.getAllAuditReports();
        if(ar!=null){
        for(Auditreport a:ar){
            AuditReportDTO ardto=new AuditReportDTO(a);            
            ardtos.add(ardto);
        }
        }
        ses.commit();
        return ardtos;
    }
    

    public ResponseDetails createUser(Appuser user) throws ProcessingException {
        UnitOfWorkSession ses = adminrepo.begin();        
        try 
        {
        String pass  = genPass();                 
        String actualpass= pass;
        pass = hash(pass);
        
        
        
        Appuser exist = adminrepo.getUser(user.getUsername());
        
        if(exist!=null){
            System.out.println("Admin user already exists "+user.getUsername());
            throw new ProcessingException("Admin User Already exists");
        }         
        
        //String username, String firstname, String lastname, String password, String email, Staff staffId        
//        adminUser.setPwdExpired(true);
        ResponseDetails authres = adminrepo.createUser(user);
//        OutMessages sms =new OutMessages();
//        sms.setDestination(phone);
//        sms.setSource(Log.OTPHEADER);
//        sms.setMessage(Log.ADMINUSERMESSAGE+actualpass);        
//        tokenRepo.insertSMS(sms);
        ses.commit();
        
        return authres;
        } catch (ProcessingException ex) {            
           ses.rollback();         
           System.out.println(ex.getMessage());
           throw new ProcessingException(ex.getMessage());
        }
       
//        return null;
    }

    private String hash(String password) throws ProcessingException {
        try {
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            byte[] passwordBytes = password.getBytes();
            byte[] digest = sha512.digest(passwordBytes);
            StringBuilder stringBuilder = new StringBuilder();
            String s;
            for (byte b : digest) {
                s = Integer.toHexString((int) b & 0xff).toUpperCase();
                if (s.length() == 1) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ConManager.class.getName()).log(Level.SEVERE, null, ex);            
            ex.printStackTrace();
            throw new ProcessingException("NoSuchAlgorithmException");
        }
    }

    
    
     private String genPass()  {
      final String sChar = "*+&$%&*+&&$%";
      String alpha =  randomString(5);//RandomStringUtils.randomAlphanumeric(6);
      String lower =  randomString(2).toLowerCase();//RandomStringUtils.randomAlphabetic(1).toLowerCase();
      int spot = Integer.parseInt(RandomStringUtils.randomNumeric(1));
      char special = sChar.charAt(spot);
      return alpha+lower+spot+special;
    }
     
      String randomString( int len ) 
    {
        final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ) 
        sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public List<Loanapllication> getAuthorizedLoans() {
        UnitOfWorkSession ses = adminrepo.begin(); 
        List<Loanapllication> loans=adminrepo.getAuthorizedLoans();
        ses.commit();
        return loans;
  
    }

    public List<Loanapllication> getDisbursedLoans() {
          UnitOfWorkSession ses = adminrepo.begin(); 
        List<Loanapllication> loans=adminrepo.getDisbursedLoans();
        ses.commit();
        return loans;
    }

    public void updateLoan(Loanapllication selectedLoan) {
         UnitOfWorkSession ses = adminrepo.begin(); 
        adminrepo.updateLoan(selectedLoan);
        ses.commit();
    }

 
    

    
}
