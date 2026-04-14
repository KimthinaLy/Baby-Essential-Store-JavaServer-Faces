/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.faces.context.FacesContext;
import model.User;
import util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;

/**
 *
 * @author Admin
 */
@Named
@RequestScoped
public class UserContext {
    @Inject
     private JwtUtil jwtUtil;

    public Integer getCurrentUserId() {

        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            return null;
        }

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        // 1. Check JWT Cookie First
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("customer_auth".equals(c.getName())) {
                    Claims claims = jwtUtil.validateToken(c.getValue());
                    if (claims != null) {
                        return (Integer) claims.get("userId");
                    }
                }
            }
        }

        // 2. Fallback to Session
        User staff = (User) context.getExternalContext().getSessionMap().get("staff");
        return (staff != null) ? staff.getUserId() : null;
    }
}
