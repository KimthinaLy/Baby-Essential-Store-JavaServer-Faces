/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filter;

import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import util.UtilJwt;

/**
 *
 * @author Admin
 */

@WebFilter("views/customer/*")
public class CustomerJwtFilter implements Filter {

    @Inject
    private UtilJwt uitlJwt;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();

        String token = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("customer_auth".equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }

       boolean isPublicStorePage = requestURI.contains("login.xhtml")
                || requestURI.contains("register.xhtml")
                || requestURI.contains("product.xhtml")
                || requestURI.contains("index.xhtml")
                || requestURI.contains("cart.xhtml");
                

        if (isPublicStorePage) {
            chain.doFilter(request, response);
            return;
        }

        Claims claims = (token != null) ? uitlJwt.validateToken(token) : null;

        if (claims != null) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "views/customer/login.xhtml");
        }
    }
}
