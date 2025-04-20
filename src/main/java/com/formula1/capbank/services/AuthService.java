/*
package com.formula1.capbank.services;

import com.formula1.capbank.dtos.Register.RegisterRequest;
import com.formula1.capbank.dtos.Register.RegisterResponse;
import com.formula1.capbank.entities.Utilisateur;
import com.formula1.capbank.repositories.AuthRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(AuthRepository authRepository, ModelMapper modelMapper) {
        this.authRepository = authRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        Utilisateur utilisateur = modelMapper.map(registerRequest, Utilisateur.class);
        utilisateur.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Utilisateur savedUser =  authRepository.save(utilisateur);
        return modelMapper.map(savedUser, RegisterResponse.class);
    }
}
*/
