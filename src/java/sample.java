/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileStore;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*; 
import java.util.*;
import java.net.*;

/**
 *
 * @author SudipBhandari
 */
public class sample extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
       
        
        String uuid = null;  
        PrintWriter pw = response.getWriter();
        String from = "johnlennon.post@gmail.com";
          String to = null;
        int flag = 0;
         String temp = null;
        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("username");
        
      try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/aindra","root","12345");
      Statement st = con.createStatement();
       
       
          ResultSet rs = st.executeQuery("select * from list");
          
         
          while (rs.next())
          {
          temp = rs.getString("name");
          if (name.equalsIgnoreCase(temp))
          {
          to = rs.getString("email");
          flag=1;
          break;
          }
          }
      }
      catch (ClassNotFoundException | SQLException e)
      {
          e.printStackTrace();
      }
      
      if (flag==0) {pw.print("No such user exists");}
      else 
      {
      //send an email
      
      String host = "smtp.gmail.com";
      String username = "johnlennon.post";
      String password = "*****";
      
      
      Properties properties = System.getProperties();
      
      properties.put("mail.smtp.com",host);
      properties.put("mail.smtp.starttls.enable", true); 
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.put("mail.smtp.user", username);
      properties.put("mail.smpt.password", password);
      properties.put("mail.smtp.port", 25);
      //properties.put("mail.smtp.auth", true);
      
      Session session = Session.getInstance(properties);
      
      
       try{  
         MimeMessage message = new MimeMessage(session);  
         message.setFrom(new InternetAddress(from));  
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
         message.setSubject("Get your code");  
         
         uuid = UUID.randomUUID().toString();
         
         
         //store the code in table
         Class.forName("com.mysql.jdbc.Driver");
         Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/aindra","root","12345");
         Statement st = con.createStatement();
         
         
         
        //message.setText(uuid);  
        URL domain = new URL("http://localhost:8080/testing/");
        URL url = new URL(domain+"?name="+name+"?uuid="+uuid);
        
        
        
        String resetlink = "http://localhost:8080/testing/linkverifier?name="+name+"&token="+uuid;
                
                
        message.setText(resetlink);
         
        
         
         Transport.send(message,username,password);
         pw.print("Password reset URL has been sent, please check your inbox");
         //response.sendRedirect("entercode.html");
         st.execute("insert into reset_links values('"+to+"','"+name+"','"+uuid+"');");
  
      }catch (MessagingException mex) {
           
           mex.printStackTrace(pw);
      }  
       catch (ClassNotFoundException  | SQLException e)
       {
           e.printStackTrace(pw);
       }
      
      
      
      }
    
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
