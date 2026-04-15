/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Admin
 */
package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;

/*
@WebFilter("/*")
public class AuthSecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        
        
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        res.setHeader("Pragma", "no-cache"); // HTTP 1.0
        res.setDateHeader("Expires", 0); // Proxies

        boolean isLoginPage = requestURI.contains("/login.xhtml");
        boolean isResource = requestURI.contains("jakarta.faces.resource")
                                     || requestURI.contains("javax.faces.resource")
                                     || requestURI.contains("/resources/");
        
        boolean isPublicPage = isLoginPage 
                || requestURI.endsWith("index.xhtml")
                || requestURI.contains("/auth/")
                || requestURI.equals(contextPath + "/")
                || requestURI.equals(contextPath + "/faces/");
        
        boolean isCustomerPath = requestURI.contains("/customer/");

       if (isPublicPage || isResource || isCustomerPath) {
            chain.doFilter(request, response);
            return;
        } 
        
        HttpSession session = req.getSession(false);
        User staff = (session != null) ? (User) session.getAttribute("staff") : null;
        if (staff == null) {
            res.sendRedirect(contextPath + "/faces/views/auth/login.xhtml");
            return;
        }

        if (requestURI.contains("/admin/") && !"ADMIN".equals(staff.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (requestURI.contains("/manager/") && !"MANAGER".equals(staff.getRole()) && !"ADMIN".equals(staff.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (requestURI.contains("/employee/") && !"EMPLOYEE".equals(staff.getRole()) && !"MANAGER".equals(staff.getRole()) && !"ADMIN".equals(staff.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request, response);
    }
}
*/