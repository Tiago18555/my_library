package com.TiagoSoftware.MyLibrary.Component;

import java.io.IOException;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.TiagoSoftware.MyLibrary.Services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthFilterComponent extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtUtilService;

    @Autowired
    //private AccessService accessService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String jwtToken = jwtUtil.extractJwtFromRequest(request);

            if (StringUtils.hasText(jwtToken) && !jwtUtilService.validateToken(jwtToken).isError()) {
                UserDetails userDetails = new User(
                        jwtUtilService.getEmailFromToken(jwtToken), ""
                );

                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                       userDetails, null
                );

                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        } catch(ExpiredJwtException ex) {
            request.setAttribute("exception", ex);
        } catch(BadCredentialsException ex) {
            request.setAttribute("exception", ex);
        }

        if ((StringUtils.hasText(request.getHeader("Authorization")) && request.getHeader("Authorization").startsWith("Bearer ")) ||
                StringUtils.hasText(request.getHeader("DeviceInfo")) && request.getHeader("DeviceInfo").startsWith("DeviceInfo "))
            accessService.saveUserAccess(request);

        chain.doFilter(request, response);
    }
}
