/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bytecode.makeshift;

import com.bytecode.core.AdminRepositoryImpl;
import com.bytecode.core.ConManager;
import com.bytecode.entities.Paccount;
import com.bytecode.response.BalanceResponse;
import com.bytecode.util.ResponseCode;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Ahmed
 */
@WebServlet(name="WebPortal_CheckBalance", urlPatterns={"/WebPortal/CheckBalance"})
public class CheckBalance extends HttpServlet {
   
    ConManager manager ;
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        BalanceResponse balRes = new BalanceResponse();

        String accountNo = null,pin = null,channel=null;
        
        try{
             accountNo = request.getParameter("accountNo").trim();
             pin = request.getParameter("pin").trim();
             channel = request.getParameter("channel").trim();
        }
        catch(java.lang.NullPointerException e){
             balRes.setResponseCode(ResponseCode.FAILED);
            balRes.setDescription(ResponseCode.Invalid_request);
        }
         if (accountNo != null && pin != null) {
            try {
                Paccount acct = manager.getPaccount(accountNo);

                if (acct != null) {
                    if (acct.getAccountid().getPin().equalsIgnoreCase(pin)) {
                        balRes.setBalance(acct.getBalance().toString());
                        balRes.setLedgerBalance(acct.getLedgerbalance().toString());
                        balRes.setResponseCode(ResponseCode.OK);
                        balRes.setDescription(ResponseCode.BALANCE_SUCCESSFUL);
                    } else {
                        balRes.setResponseCode(ResponseCode.FAILED);
                        balRes.setDescription(ResponseCode.INVALID_PIN);
                    }
                } else {
                    balRes.setResponseCode(ResponseCode.FAILED);
                    balRes.setDescription(ResponseCode.INVALID_ACCOUNTNO);
                }

            } catch (Exception e) {
                System.err.println("Exception:" + e);
                balRes.setResponseCode(ResponseCode.FAILED);
                balRes.setDescription(ResponseCode.Internal_Error_Occurred);
            }
        } else {
            balRes.setResponseCode(ResponseCode.FAILED);
            balRes.setDescription(ResponseCode.Invalid_request);
        }

        Gson gson=new Gson();
        out.println(gson.toJson(balRes));
        out.flush();
    
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
        BalanceResponse balRes = new BalanceResponse();
        try{
       balRes.setResponseCode(ResponseCode.METHODNOTALLOWED);
       balRes.setDescription(ResponseCode.METHOD_NOT_ALLOWED);
       
       out.println(balRes);   
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
        return "Short description";
    }// </editor-fold>

}
