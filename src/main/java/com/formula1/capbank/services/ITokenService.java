package com.formula1.capbank.services;

import com.formula1.capbank.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;
import java.util.function.Function;

public interface ITokenService extends UserDetailsService {
    public String generateToken(UserDetails userDetails);

    public String getUsernameFromToken(String token) throws InvalidTokenException;

    public Date getExpirationDateFromToken(String token) throws InvalidTokenException;

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
            throws InvalidTokenException;

    public Boolean isTokenExpired(String token) throws InvalidTokenException;

    public Boolean isTokenValid(String token, UserDetails userDetails) throws InvalidTokenException;

}
