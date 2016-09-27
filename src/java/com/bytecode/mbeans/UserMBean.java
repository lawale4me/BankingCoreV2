/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bytecode.mbeans;


import com.bytecode.core.AdminRepositoryImpl;
import com.bytecode.core.ConManager;
import com.bytecode.core.UserException;
import com.bytecode.dto.UserType;
import com.bytecode.entities.Appuser;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
public class UserMBean {

    
    String username,email,phone,name,passwd;
    UserType role;
    String adminUsername;
    Appuser adminUser;
    List<String> branchnames;
    ConManager appManager = new ConManager(new AdminRepositoryImpl());
    
    
    /**
     * Creates a new instance of UserMBean
     */
    public UserMBean() {
    }

    @PostConstruct
    public void init() {        
        
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        adminUsername=(String) httpSession.getAttribute("username");  
        adminUser=appManager.getUser(adminUsername);        
       // branches = (List<Branch>) appManager.getBranches();
    }
    
    

   

    public UserType getRole() {
        return role;
    }

    public void setRole(UserType role) {
        this.role = role;
    }      
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
    public void create() throws UserException
    {
               
               
        Appuser user =new Appuser();
        user.setEmail(email);
        user.setUsername(username);
        user.setName(name);
        user.setRole(UserType.Admin.ordinal());
        user.setPassword(passwd);        
        
        
        System.out.println("About to create user");
        appManager.addUser(user);
        System.out.println("User created");
        //appManager.addActivity(new Activities("You Created a new user:"+user.getUsername(), adminUser, null));
        role=null;
        email="";
        //branch=null;            
        username="";
//        
//         Email email1=new Email();
//         Email email2=new Email();
//         email1.setEmailAddress(adminUser.getEmail());
//         email1.setSubject("Your have create a new user :" +user.getUsername());
//         email1.setMessage("User :" +user.getUsername()+"\n"+user.getEmail()+"\n"+Log1.APPURL);
//         
//         email2.setEmailAddress(user.getEmail());
//         email2.setSubject("Your Banking Core account has been created:" );
//         email2.setMessage("User :" +user.getUsername()+"\n"+user.getEmail()+"\n"+Log1.APPURL);
//         
//         new SendEmail().sendSimpleMail(email1);
//         new SendEmail().sendSimpleMail(email2);
        
         appManager.audit(user.getUsername(), "New User created  "+user.getUsername(),"IPADDRESS");
        FacesMessage message = new FacesMessage("Succesful", user.getUsername()+" user has been Created.");
        FacesContext.getCurrentInstance().addMessage(null, message);     
    }

  

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    
    


}