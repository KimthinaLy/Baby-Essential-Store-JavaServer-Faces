/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filter;

import io.jsonwebtoken.Claims;
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
import util.jwtUtil;

/**
 *
 * @author Admin
 */
@WebFilter("/faces/views/customer/*")
public class CustomerJwtFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String token = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("customer_auth".equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }

           Claims claims = (token != null) ? jwtUtil.validateToken(token) : null;

        if (claims != null) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/faces/views/auth/login.xhtml");
        }
    }
}
