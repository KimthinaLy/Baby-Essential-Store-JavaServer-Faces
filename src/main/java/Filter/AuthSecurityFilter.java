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

@WebFilter("/*")
public class AuthSecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String loginURL = req.getContextPath() + "/views/auth/login.xhtml";

        User user = (session != null) ? (User) session.getAttribute("user") : null;
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();

        boolean isPublicPage = requestURI.endsWith("index.xhtml")
                || requestURI.contains("/auth/")
                || requestURI.contains("product.xhtml")
                || requestURI.contains("cart.xhtml")
                || requestURI.equals(contextPath + "/");

        boolean isResource = requestURI.contains("jakarta.faces.resource") || requestURI.contains("javax.faces.resource");

        if (isPublicPage || isResource) {
            chain.doFilter(request, response);
            return;
        }

        if (user == null) {
            res.sendRedirect(loginURL);
            return;
        }

        if (requestURI.contains("/admin/") && !"ADMIN".equals(user.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (requestURI.contains("/manager/") && !"MANAGER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (requestURI.contains("/employee/") && !"EMPLOYEE".equals(user.getRole()) && !"MANAGER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}
