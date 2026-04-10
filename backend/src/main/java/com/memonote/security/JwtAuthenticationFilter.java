package com.memonote.security;

import com.memonote.config.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Value("${jwt.header}")
    private String header;
    
    @Value("${jwt.prefix}")
    private String prefix;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader(header);
        
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(prefix)) {
            String token = authHeader.substring(prefix.length() + 1);
            
            if (jwtTokenUtil.validateToken(token)) {
                Long userId = jwtTokenUtil.getUserId(token);
                String username = jwtTokenUtil.getUsername(token);
                Integer role = jwtTokenUtil.getRole(token);
                
                String roleName = role == 1 ? "ROLE_ADMIN" : "ROLE_USER";
                
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, Collections.singletonList(new SimpleGrantedAuthority(roleName))
                );
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
