package com.TiagoSoftware.MyLibrary.Component;

import com.TiagoSoftware.MyLibrary.Repositories.AuthRepository;
import com.TiagoSoftware.MyLibrary.Services.JwtService;
import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtTokenUtil;
    @Autowired
    private final AuthRepository userRepo;

    public JwtTokenFilter(JwtService jwtTokenUtil,
                          AuthRepository userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate


        String token = extractJwtFromRequest(request);

        List<SimpleGrantedAuthority> roles = null;

        roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // Get jwt token and validate

        try {
            if (StringUtils.hasText(token) && jwtTokenUtil.validate(token)) {
                // Get user identity and set it on the spring security context
                // boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
                UserDetails userDetails = new User(
                        jwtTokenUtil.getUserFromToken(token), "",
                        roles
                );

                UsernamePasswordAuthenticationToken
                        authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities()
                );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch(ExpiredJwtException ex) {
            request.setAttribute("exception", ex);
        } catch(BadCredentialsException ex) {
            request.setAttribute("exception", ex);
        }

        chain.doFilter(request, response);
    }

    private static String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }

}