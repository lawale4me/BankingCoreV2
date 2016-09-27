/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bytecode.makeshift;

import com.bytecode.core.AdminRepositoryImpl;
import com.bytecode.core.ConManager;
import com.bytecode.core.LoanException;
import com.bytecode.dto.LoanDTO;
import com.bytecode.response.LoanApplicationResponse;
import com.bytecode.util.Email;
import com.bytecode.util.ResponseCode;
import com.bytecode.util.SendEmail;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Ahmed
 */
@WebServlet(name="WebPortal_LoanApplication", urlPatterns={"/WebPortal/LoanApplication"})
public class LoanApplication extends HttpServlet {
   
    ConManager manager ;
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        LoanApplicationResponse loanResponse = new LoanApplicationResponse();

        String surname = null,tenor = null,title=null,
               totalExistingLoan = null,signature = null,purpose=null,prodCode = null,phone = null,
               payDay=null ,otherNames = null,officeEmail = null,loanAmount=null,
               lengthOfService = null,kinWork = null,kinType=null,
                kinPhone=null ,kinName = null,jobDesc = null,idCard=null,
                grade=null ,employerPhone = null,employerAddress = null,employer=null,
                empNo=null ,email =null, dob = null,dept=null,
                channel=null ,bankStatement = null,bankName = null,bankAccountNo=null,
                annualSalary=null ,address = null,installment=null,loanType=null;
        try{
            
            
             phone = request.getParameter("phone");
             surname = request.getParameter("surname");
             tenor = request.getParameter("tenor");
             title = request.getParameter("title");
             totalExistingLoan = request.getParameter("totalExistingLoan");
             signature = request.getParameter("signature");
             purpose = request.getParameter("purpose");
             prodCode = request.getParameter("prodCode");             
             payDay = request.getParameter("payDay");
             otherNames = request.getParameter("otherNames");
             officeEmail = request.getParameter("officeEmail");
             loanAmount = request.getParameter("loanAmount");
             lengthOfService = request.getParameter("lengthOfService");
             kinWork = request.getParameter("kinWork");
             kinType = request.getParameter("kinType");
             kinPhone = request.getParameter("kinPhone");
             kinName = request.getParameter("kinName");
             jobDesc = request.getParameter("jobDesc");
             idCard = request.getParameter("idCard");
             grade = request.getParameter("grade");
             employerPhone = request.getParameter("employerPhone");
             employerAddress = request.getParameter("employerAddress");
             employer = request.getParameter("employer");
             empNo = request.getParameter("empNo");
             email = request.getParameter("email");
             dob = request.getParameter("dob");
             dept = request.getParameter("dept");
             bankStatement = request.getParameter("bankStatement");
             bankName = request.getParameter("bankName");
             bankAccountNo = request.getParameter("bankAccountNo");
             annualSalary = request.getParameter("annualSalary");
             address = request.getParameter("address");             
             channel = request.getParameter("channel");
             loanType = request.getParameter("loanType");
             installment = request.getParameter("installment");
        }
        catch(java.lang.NullPointerException e){
             loanResponse.setResponseCode(String.valueOf(ResponseCode.FAILED));
            loanResponse.setDescription(ResponseCode.Invalid_request);
        }
         
        //CHECK IF EMAIL EXISTS To avoid duplicate  records            
        System.out.println("Email:"+email);
         if (!manager.checkEmail(email)) 
        {                        
            try {                        
               
                
                LoanDTO loan = new LoanDTO();
                loan.setSurname(surname);
                loan.setTenor(tenor);
                loan.setTitle(title);
                loan.setTotalExistingLoan(totalExistingLoan);
                loan.setSignature(signature);
                loan.setPurpose(purpose);
                loan.setProdCode(prodCode);
                loan.setPhone(phone);
                loan.setPayDay(payDay);
                loan.setOtherNames(otherNames);
                loan.setOfficeEmail(officeEmail);
                loan.setLoanAmount(loanAmount);
                loan.setLengthOfService(lengthOfService);
                loan.setKinWork(kinWork);
                loan.setKinType(kinType);
                loan.setKinPhone(kinPhone);
                loan.setKinName(kinName);
                loan.setJobDesc(jobDesc);
                loan.setIdCard(idCard);
                loan.setGrade(grade);
                loan.setEmployerPhone(employerPhone);
                loan.setEmployerAddress(employerAddress);
                loan.setEmployer(employer);
                loan.setEmpNo(empNo);
                loan.setEmail(email);
                loan.setDob(dob);
                loan.setDept(dept);
                loan.setChannel(channel);
                loan.setBankStatement(bankStatement);
                loan.setBankName(bankName);
                loan.setBankAccountNo(bankAccountNo);
                loan.setAnnualSalary(annualSalary);
                loan.setAddress(address);
                loan.setLoanType(Integer.parseInt(loanType));
                loan.setLoanDate(new Date());                
                
                
                                
                LoanApplicationResponse res =  manager.LoanApplication(loan);
                
                loanResponse.setResponseCode(ResponseCode.sOK);
                loanResponse.setDescription(ResponseCode.LOAN_APPLICATION_SUCCESSFUL);
                loanResponse.setReferenceNo(res.getReferenceNo());
                
//                SendEmail sendObject=new SendEmail();
//                Email mail=new Email();
//                mail.setEmailAddress(loan.getEmail());
//                mail.setMessage(sendObject.constructLoanEmail(loan));
//                mail.setSubject(ResponseCode.LOAN_APPLICATION_SUBMITTED);
//                sendObject.sendSimpleMail(mail);
                
            } catch (LoanException e) {                
                loanResponse.setResponseCode(String.valueOf(ResponseCode.FAILED));
                loanResponse.setDescription(e.getMessage());
            }

        } else {
            loanResponse.setResponseCode(String.valueOf(ResponseCode.FAILED));
            loanResponse.setDescription("User Details exist");
        }
        

        Gson gson=new Gson();
       out.println(gson.toJson(loanResponse));
        out.close();
    
    }
    
   @Override
   public void init(){
   manager= new ConManager(new AdminRepositoryImpl());
   }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        LoanApplicationResponse loanResponse = new LoanApplicationResponse();
        try
       {
            loanResponse.setResponseCode(String.valueOf(ResponseCode.METHODNOTALLOWED));
            loanResponse.setDescription(ResponseCode.METHOD_NOT_ALLOWED);
       
             Gson gson=new Gson();
             out.println(gson.toJson(loanResponse));
        }
       finally {
            out.close();
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Loan Application Service";
    }// </editor-fold>

}
