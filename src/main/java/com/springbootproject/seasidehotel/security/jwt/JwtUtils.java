package com.springbootproject.seasidehotel.security.jwt;


import com.springbootproject.seasidehotel.security.user.HotelUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * @author Tedi kondakçiu
 */

@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int jwtExpirationMs;

    public String generateJwtTokenForUser(Authentication authentication){
        HotelUserDetails userPrincipal = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        }catch (MalformedJwtException ex){
            log.error("Invalid jwt token : {} ", ex.getMessage());
        }catch (ExpiredJwtException ex){
            log.error("Expired token : {}", ex.getMessage());
        }catch (UnsupportedJwtException ex){
            log.error("This token is not supported : {}", ex.getMessage());
        }catch (IllegalArgumentException e){
            log.error("No claims found : {} ", e.getMessage());
        }
        return false;
    }

}
