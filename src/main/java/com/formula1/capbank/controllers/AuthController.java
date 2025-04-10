package com.formula1.capbank.controllers;

import com.formula1.capbank.dtos.Login.LoginRequest;
import com.formula1.capbank.dtos.Login.LoginResponse;
import com.formula1.capbank.dtos.Register.RegisterRequest;
import com.formula1.capbank.services.AuthService;
import com.formula1.capbank.services.TokenService;
import com.formula1.capbank.util.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenService tokenService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthService authService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseHandler.generateResponse("Utilisateur registered successfully", HttpStatus.OK, authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationManager
                    .authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String tokenJwt = tokenService.generateToken(userDetails);

            LoginResponse loginResponse = LoginResponse
                    .builder()
                    .accessToken(tokenJwt)
                    .tokenType("Bearer")
                    .build();
            return ResponseHandler.generateResponse("Connexion réussie", HttpStatus.OK, loginResponse);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Identifiant invalides", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors la de connexion", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
