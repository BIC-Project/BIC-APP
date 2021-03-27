package com.bic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthJwtUtil {

    @Value("${app.secret}")
    private String SECRET;

    public String AUTHORITIES_KEY = "roles";

    public String extractUsername(String token) throws Exception {
	return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws Exception {
	return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
	final Claims claims = extractAllClaims(token);
	return claimsResolver.apply(claims);
    }

    public Collection<SimpleGrantedAuthority> getAuthorities(final String token) {

	final JwtParser jwtParser = Jwts.parser().setSigningKey(SECRET);

	final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
	final Claims claims = claimsJws.getBody();
	// System.out.println(claims);
	Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority((String) claims.get(AUTHORITIES_KEY)));

	return authorities;
    }

    private Claims extractAllClaims(String token) throws Exception {
	return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) throws Exception {
	return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) throws Exception {

	Map<String, Object> claims = new HashMap<>();
	String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
		.collect(Collectors.joining(","));
	System.out.println(authorities);
	claims.put(AUTHORITIES_KEY, authorities);
	return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) throws Exception {

	return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
		.signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws Exception {
	final String username = extractUsername(token);

	return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
