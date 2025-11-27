package com.schnofiticationbe.security;

import com.schnofiticationbe.security.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
            String path = request.getRequestURI();

            if (!path.startsWith("/api/admin") || path.equals("/api/admin/join") || path.equals("/api/admin/login")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = resolveToken(request);
            if (token == null || !jwtProvider.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            String userId = jwtProvider.getUserId(token);
            Authentication auth = new UsernamePasswordAuthenticationToken(userId, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
