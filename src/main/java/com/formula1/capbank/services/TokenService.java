/*package com.formula1.capbank.services;

import com.formula1.capbank.entities.Utilisateur;
import com.formula1.capbank.exceptions.InvalidTokenException;
import com.formula1.capbank.repositories.UtilisateurRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.security.core.userdetails.User.withUsername;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class TokenService implements ITokenService {

    private final UtilisateurRepository utilisateurRepository;

    private final Long expiration;
    private final String secret;

    public TokenService(UtilisateurRepository utilisateurRepository,
                        @Value("${jwt.expiration}") Long expiration,
                        @Value("${jwt.secret}") String secret) {
        this.utilisateurRepository = utilisateurRepository;
        this.expiration = expiration;
        this.secret = secret;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return doGenerateToken(userDetails, new Date(System.currentTimeMillis() + expiration));
    }

    @Override
    public String getUsernameFromToken(String token) throws InvalidTokenException {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token) throws InvalidTokenException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws InvalidTokenException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws InvalidTokenException {

        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("Token non supporté");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Token est malformé");
        } catch (SignatureException e) {
            throw new InvalidTokenException("Token signature est invalide");
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("Token est vide");
        }
    }

    @Override
    public Boolean isTokenExpired(String token) throws InvalidTokenException {
        return getExpirationDateFromToken(token).before(new Date());
    }

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) throws InvalidTokenException {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec numéro de compte: " + email));
        return withUsername(email).password(utilisateur.getPassword()).build();
    }

    private String doGenerateToken(UserDetails userDetails, Date expire) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expire)
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}*/
