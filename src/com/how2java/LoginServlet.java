package com.how2java;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {  
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  
      throws ServletException, IOException {  
        String name = req.getParameter("name");  
        String password = req.getParameter("password");  
        Subject subject = SecurityUtils.getSubject();  
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);  
        try {  
            subject.login(token);
            Session session=subject.getSession();
            session.setAttribute("subject", subject);
            
            resp.sendRedirect("");
        } catch (AuthenticationException e) {  
            req.setAttribute("error", "验证失败");  
            req.getRequestDispatcher("login.jsp").forward(req, resp); 
        }  
    }  
}  