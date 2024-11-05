package com.sanmarcos.promecal.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class JwtService {
    public String getToken(UserDetails  user){
        return getToken(new HashMap<>(), user);
    }
    private String getToken(Map<String, Object> extraClaims, UserDetails user){
        //return Jwts.builder().setClaims(extraClaims).setSubject(user.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
         //       .setExpiration(new Date(System.currentTimeMillis()+1000*60*24)).signWith(getKey(),SignatureAlgorithm.HS256)
         //       .compact();
        return null;
    }
    //private Key getKey(){
    //    byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
    //    return keys.hmacS
    //}
}
