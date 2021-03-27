package com.bic.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bic.service.CustomUserDetailsService;
import com.bic.util.AuthJwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private AuthJwtUtil authJwtUtil;

    @Autowired
    private CustomUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
	    FilterChain filterChain) throws ServletException, IOException {

	String authorizationHeader = httpServletRequest.getHeader("Authorization");

	String token = null;
	String userName = null;
	try {
	    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		token = authorizationHeader.substring(7);
		userName = authJwtUtil.extractUsername(token);
	    }
	    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		UserDetails userDetails = service.loadUserByUsername(userName);
		if (authJwtUtil.validateToken(token, userDetails)) {
		    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
			    userDetails, null, userDetails.getAuthorities());
		    usernamePasswordAuthenticationToken
			    .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
		    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
	    }
	} catch (Exception e) {
	} finally {
	    filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
    }

}
