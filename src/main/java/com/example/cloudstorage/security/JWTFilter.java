package com.example.cloudstorage.security;

import com.example.cloudstorage.config.AuthenticationConstant;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component

public class JWTFilter extends GenericFilterBean {
    private final JWTToken jwtToken;

    public JWTFilter(JWTToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null && jwtToken.validateAccessToken(token)) {
            Claims claims = jwtToken.getAccessClaims(token);

            JWTAuthentication jwtInfoToken = new JWTAuthentication();
            jwtInfoToken.setRoles(getRoles(claims));
            jwtInfoToken.setUsername(claims.getSubject());
            jwtInfoToken.setAuthenticated(true);

            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AuthenticationConstant.AUTH_TOKEN);
        if (StringUtils.hasText(bearer) && bearer.startsWith(AuthenticationConstant.TOKEN_PREFIX)) {
            return bearer.substring(7);
        }
        return null;
    }

    private static Set<Role> getRoles(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }

}
