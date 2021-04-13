package org.ospic.platform.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.ospic.platform.infrastructure.tenants.ThreadLocalStorage;
import org.ospic.platform.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private int refreshExpirationDateInMs;

    @Value("${mod.app.jwtSecret}")
    private String jwtSecret;

    @Value("${mod.app.jwtExpirationMs}")
    private int jwtExpirationInMs;

    @Value("${mod.app.jwtRefreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }
    private UserDetailsImpl userDetails;

    @Autowired
    AuthenticationManager authenticationManager;

    public String generateJwtToken(Authentication authentication) {

        userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return generateToken(userDetails);
    }

    // generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
        }
        claims.put("tenant", ThreadLocalStorage.getTenantName().get());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(jwtSecret))
                .compact();

    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512,  TextCodec.BASE64.decode(jwtSecret))
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey( TextCodec.BASE64.decode(jwtSecret)).parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey( TextCodec.BASE64.decode(jwtSecret)).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            // Jwt token has not been tampered with
            Jws<Claims> claims = Jwts.parser().setSigningKey(TextCodec.BASE64.decode(jwtSecret)).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException("Token has Expired");
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey( TextCodec.BASE64.decode(jwtSecret)).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String authToken) {
        UserDetailsImpl ud = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = ud.getAuthorities();
        Collection<? extends GrantedAuthority> roles = ud.getRoles();
        authorities.stream().map(authority -> {
            simpleGrantedAuthorities.add((SimpleGrantedAuthority) authority);
            return null;
        });
        return simpleGrantedAuthorities;
    }
}
